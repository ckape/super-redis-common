package cn.friday.base.service.global.redis.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * <b><code>DateUtil</code></b>
 * <p>
 * 时间帮助类
 * </p>
 * <b>Creation Time:</b> 2016年5月5日 下午6:12:48
 * @author jiangnan.zjn@alibaba-inc.com
 */
public class DateUtil {

	//默认的日期格式化
	private static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	//默认日期格式
	private static final SimpleDateFormat datetimeSdf = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);

	public static Date changStringToDate(String str) {
		try {
			return datetimeSdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String formateDate(Date date) {
		return datetimeSdf.format(date);
	}

	public static Date changeLongToDate(long date) {
		return new Date(date);
	}

	public static String changeLongToString(long date) {
		return datetimeSdf.format(new Date(date));
	}

}
