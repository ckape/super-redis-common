package cn.friday.base.service.global.redis.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import com.google.common.collect.Maps;

/**
 * 
 * <b><code>ReflectUtil</code></b>
 * <p>
 * java 反射的帮助类
 * </p>
 * <b>Creation Time:</b> 2016年5月4日 下午2:42:34
 * @author jiangnan.zjn@alibaba-inc.com
 */
public class ReflectUtil {

	public static Map<String, Class<?>> getOrderedFieldAndType(Object o) {
		Map<String, Class<?>> map = Maps.newLinkedHashMap();
		Field[] fields = o.getClass().getDeclaredFields();
		if (fields != null && fields.length > 0) {
			for (Field field : fields) {
				map.put(field.getName(), field.getType());
			}
		}
		return map;
	}

	/**
	 * 执行getter方法，获取属性对应的值
	 * 
	 * @param owner
	 * @param methodName
	 * @param type
	 * @return
	 * @throws Exception 下午2:47:43
	 * 2016年5月4日
	 * @author jiangnan.zjn@alibaba-inc.com
	 */
	public static Object invokeGetterMethod(Object owner, String methodName, Class<?> type) throws Exception {
		Method method = null;
		try {
			if (type == boolean.class) {
				method = owner.getClass().getMethod(isBoolean(methodName));
			} else {
				method = owner.getClass().getMethod(getter(methodName));
			}
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
			return null;
		}
		return method.invoke(owner);
	}

	private static String getter(String field) {
		return "get" + transName(field);
	}

	private static String isBoolean(String name) {
		if (name.startsWith("is")) {
			return name;
		}
		return "is" + transName(name);
	}

	private static String transName(String name) {
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}

}
