package cn.friday.base.service.global.redis.util;

import java.sql.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.Converter;

public class TimestampRedisConvert implements Converter {

	private static final String rexp = "[0-9]*";
	
	private static Pattern pattern(){
		Pattern pat = Pattern.compile(rexp);  
		return pat;
	}
	
	@Override
	public Object convert(Class type, Object value) {
		
		if(checkTimestamp(value.toString())){
			return new Date(Long.parseLong(value.toString()));
		}
		
		return null;
	}
	
	/**
	 * 检查是否为时间戳
	 * @param str
	 * @return
	 */
	public boolean checkTimestamp(String str){
		Matcher mat = pattern().matcher(str);
		
		if(mat.matches() && str.startsWith("14")){
			return true;
		}
		
		return false;
	}

}
