package cn.friday.base.service.global.redis.util;

import org.apache.commons.beanutils.Converter;

public class BooleanRedisConvert implements Converter {

	@Override
	public  Object convert(Class type, Object value) {
		if("true".equals(value.toString())){
			return true;
		}else if("false".equals(value.toString())){
			return false;
		}
		
		return null;
	}

}
