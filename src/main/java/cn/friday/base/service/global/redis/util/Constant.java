package cn.friday.base.service.global.redis.util;

public class Constant {

	public static final class Redis {

		/**
		 * 每个redis存储的实体都要加这个后缀
		 */
		public final static String ENTITY_SUFFIX = "Redis";

		/**
		 * redis实体过期时间
		 */
		public final static int ENTITY_EXPIRE_TIME = 86400;

		/**
		* 一个很大的时间戳
		*/
		public final static long MAX_TIME_LONG = 20000000000000l;

	}

}
