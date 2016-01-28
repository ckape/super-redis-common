package cn.friday.base.service.global.redis.registry;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.List;

/**
 * 注册中心，
 * 每个使用的key都会注册到这里
 * @author BravoZu
 *
 */
public class RegistryService {

	/**
	 * key注册中心，检查对应key在项目中是否存在
	 */
	private final static List<String> registerCenter = new ArrayList<String>();
	
	/**
	 * 注册key
	 * @param key
	 * @return
	 *@author BravoZu
	 */
	public static synchronized boolean registry(String key){
		if( registerCenter.contains(key) ){
			 checkArgument(  false , "存在重复的key，请重新命名，对应key:"+key);
		}else{
			registerCenter.add(key);
		}
		return true;
	}
	
}
