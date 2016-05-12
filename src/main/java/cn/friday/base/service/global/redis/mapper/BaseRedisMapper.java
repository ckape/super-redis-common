package cn.friday.base.service.global.redis.mapper;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.data.redis.hash.BeanUtilsHashMapper;

import cn.friday.base.service.global.redis.util.BooleanRedisConvert;
import cn.friday.base.service.global.redis.util.DateRedisConvert;
import cn.friday.base.service.global.redis.util.DateUtil;

public class BaseRedisMapper<T> extends BeanUtilsHashMapper<T> implements IBaseRedisMapper<T> {

	static {
		//注册转换帮助类
		ConvertUtils.register(new DateRedisConvert(), Date.class);
		ConvertUtils.register(new BooleanRedisConvert(), Boolean.class);
	}

	public BaseRedisMapper(Class<T> type) {
		super(type);
	}

	@Override
	public T fromObjectHash(Map<Object, Object> hash) {
		if (hash.size() < 2) {
			return null;
		}
		Map<String, String> map = new HashMap<String, String>();
		for (Object key : hash.keySet()) {
			String keyStr = key.toString();
			String valueStr = hash.get(key).toString();
			if (keyStr.startsWith("is") && (("true").equals(valueStr) || ("false").equals(valueStr))) {
				keyStr = keyStr.substring(2);
				keyStr = keyStr.substring(0, 1).toLowerCase() + keyStr.substring(1, keyStr.length());
			}

			//兼容以前的奇葩数据
			if ("isAnonymous".equals(keyStr)) {
				if ("1".contains(valueStr)) {
					valueStr = "true";
				} else if ("0".equals(valueStr)) {
					valueStr = "false";
				}
			}

			//兼容喜欢数据中的旧数据，存了时间戳
			if ("addTime".endsWith(keyStr) && checkTimestamp(valueStr)) {
				valueStr = DateUtil.changeLongToString(Long.parseLong(valueStr));
			}
			map.put(keyStr, valueStr);
		}
		return super.fromHash(map);
	}

	@Override
	public T fromHash(Map<String, String> hash) {

		if (hash.size() < 2) {
			return null;
		}

		Map<String, String> map = new HashMap<String, String>();
		for (String key : hash.keySet()) {
			String value = hash.get(key);
			if (key.startsWith("is") && (("true").equals(value) || ("false").equals(value))) {
				key = key.substring(2);
				key = key.substring(0, 1).toLowerCase() + key.substring(1, key.length());
			}
			map.put(key, value);
		}
		return super.fromHash(map);
	}

	@Override
	public Map<String, String> toHash(T object) {
		Map<String, String> map = super.toHash(object);
		Map<String, String> targetMap = new HashMap<String, String>();

		BeanUtilsBean beanUtilsBean = BeanUtilsBean.getInstance();
		PropertyDescriptor[] descriptors = beanUtilsBean.getPropertyUtils().getPropertyDescriptors(object);
		for (PropertyDescriptor pd : descriptors) {
			//返回值的类型
			String value = map.get(pd.getName());
			if (value != null) {
				Method method = beanUtilsBean.getPropertyUtils().getReadMethod(pd);
				String returnValueTypeName = method.getReturnType().getName();
				if ("java.util.Date".equals(returnValueTypeName)) {
					try {
						Object o = method.invoke(object);
						if (o instanceof Date) {
							value = DateUtil.formateDate((Date) o);
						}
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
				targetMap.put(pd.getName(), value);
			}
		}

		return targetMap;
	}

	private static final String rexp = "[0-9]*";

	private static Pattern pattern() {
		Pattern pat = Pattern.compile(rexp);
		return pat;
	}

	/**
	 * 检查是否为时间戳
	 * @param str
	 * @return
	 */
	public boolean checkTimestamp(String str) {
		Matcher mat = pattern().matcher(str);
		if (mat.matches() && str.startsWith("14") && str.length() == 13) {
			return true;
		}
		return false;
	}

}
