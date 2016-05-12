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
		public final static long MAX_TIME_LONG = 20000000000000L;

		/**
		 * ttl value = 2 key不存在
		 */
		public final static long TTL_VALUE_KEY_NOT_EXIST = -2;

		/**
		 * ttl value = -1 key永不过期
		 */
		public final static long TTL_VALUE__NEVER_EXPIRES_KEY = -1;

		/**
		 * 关闭防雪崩
		 */
		public final static long AVALANCHE_CLOSED = -1;

	}

}
