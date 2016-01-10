package cn.friday.base.service.global.test;

import java.beans.PropertyDescriptor;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.MethodUtils;
import org.springframework.data.redis.hash.BeanUtilsHashMapper;

import cn.friday.base.service.global.redis.mapper.BaseRedisMapper;
import cn.friday.base.service.global.redis.util.MemberUtil;

public class Test {
	
	public static void main(String[] args) {
		BaseData baseData = new BaseData();
		
		baseData.setAge(1);
		baseData.setAny(true);
		baseData.setId(1);
		baseData.setMember("ddddd");
		baseData.setName("wwwww");
		
		BaseRedisMapper<BaseData> mapper = new BaseRedisMapper<BaseData>(BaseData.class);
		Map<String,String>  map = mapper.toHash(baseData);
		BaseData b = mapper.fromHash(map);
		System.out.println(b.getAge());
		
//		
//		BeanUtilsBean beanUtilsBean = BeanUtilsBean.getInstance();
//		PropertyDescriptor[] descriptors =
//				beanUtilsBean. getPropertyUtils().getPropertyDescriptors(baseData);
//		
//		for(PropertyDescriptor pd:descriptors){
//			System.out.println(pd.getName());
//			System.out.println(beanUtilsBean.getPropertyUtils().getReadMethod(pd).getReturnType().getName());
//		}
		
		
		
		
//		BeanUtilsHashMapper<BaseData> util = new BeanUtilsHashMapper<BaseData>(BaseData.class);
//		BaseData baseData = new BaseData();
//		baseData.setAge(17);
//		baseData.setId(021);
//		baseData.setMember("mmmmmm");
//		baseData.setName("&&&^^");
//		System.out.println(util.toHash(baseData));
		
//		MemberUtil<Data> memberUtil = new MemberUtil<Data>(Data.class);
//		Data data = new Data();
//		data.setId(101);
//		data.setMember("liyi");
//		System.out.println(memberUtil.getMember(data) );
//		System.out.println(memberUtil.getObject("101_liyi"));
		
		
		//----------------------------
//		MemberUtil<Integer> memberUtilInteger = new MemberUtil<Integer>();
//		memberUtilInteger.getMember(1);
//		memberUtilInteger.getObject("", Integer.class);
		
	}

}
