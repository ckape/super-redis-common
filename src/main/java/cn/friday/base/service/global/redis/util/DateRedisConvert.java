package cn.friday.base.service.global.redis.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.Converter;

import cn.friday.common.service.global.utils.DateUtil;


public class DateRedisConvert implements Converter {
	
	private static final String rexp = "^\\d{4}-\\d{1,2}-\\d{1,2}\\s+\\d{1,2}:\\d{1,2}:\\d{1,2}$";
	
	private static Pattern pattern(){
		Pattern pat = Pattern.compile(rexp);  
		return pat;
	}

	@Override
	public Object convert(Class type, Object value) {
		if(checkDateType(value.toString())){
			return DateUtil.changStringToDate(value.toString());
		}
		return null;
	}
	
	
	
	public boolean checkDateType(String str){
		Matcher mat = pattern().matcher(str);
		return mat.matches();
	}
	

}
