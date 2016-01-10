package cn.friday.base.service.global.redis.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Set;

public class MemberUtil<T> {
	
	Class <T> entityClass;
	
	public MemberUtil( Class <T> entityClass ){
		this.entityClass = entityClass;
	}
	
	public String getMember(T t){
		
		if(checkBaseDataType(entityClass)){
			return String.valueOf(t);
		}else{
			try {
				StringBuilder builder = new StringBuilder();
				Field [] fields = t.getClass().getDeclaredFields();
				for(Field f:fields){
						builder.append(invokeGetterMethod(t, f.getName(), null,f.getType())).append("_");
				}
				return builder.substring(0,builder.length()-1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return "";
	}
	
	public T getObject(String member){
		if(checkBaseDataType(entityClass)){
			return (T)getBaseResult(member, entityClass);
		}else{
			try {
				T t = entityClass.newInstance();
				String [] arr = member.split("_");
				Field [] fields = entityClass.getDeclaredFields();
				for(int i=0; i<arr.length; i++){
					invokeSetterMethod(t, fields[i].getName(), new Object[]{ getValue(arr[i], fields[i].getType().getName()) }, fields[i].getType());
				}
				return t;
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
	
	public Set<T> getSet(Set<String> members ){
		Set<T> set = new LinkedHashSet<T>();
		for(String member:members){
			set.add(getObject(member));
		}
		return set;
	}
	
	private Object getValue(String member, String type ){
		
		if( "int".equals(type) || "java.lang.Integer".equals(type)){
			return Integer.parseInt(member);
		}else if("long".equals(type) || "java.lang.Long".equals(type)){
			return Long.parseLong(member);
		}else if( "boolean".equals(type) ||  "java.lang.Boolean".equals(type)){
			return Boolean .parseBoolean(member);
		}else if("java.lang.String".equals(type)){
			return member;
		}
		return member;
	}
	
	/**
	 * 检查基本数据类型
	 * @param c
	 * @return
	 */
	private boolean checkBaseDataType(Class<T> c){
		String className = c.getName();
		if("java.lang.Integer".equals(className) ||
				"java.lang.Long".equals(className) ||
				"java.lang.Boolean".equals(className) ||
				"java.lang.Character".equals(className) ||
				"java.lang.Byte".equals(className) ||
				"java.lang.Short".equals(className) ||
				"java.lang.Float".equals(className) ||
				"java.lang.Double".equals(className) ||
				"java.lang.String".equals(className) ){
			return true;
		}
		return false;
	}
	
	private Object getBaseResult(String member, Class<T> c ){
		String className = c.getName();
		if("java.lang.Integer".equals(className)){
			return Integer.parseInt(member);
		}else if("java.lang.Long".equals(className)){
			return Long.parseLong(member);
		}else if("java.lang.Boolean".equals(className)){
			return Boolean .parseBoolean(member);
		}else if("java.lang.String".equals(className)){
			return member;
		}
		return member;
	}
	
	private  Object invokeSetterMethod(Object owner,String methodName, Object[] args,Class<?> type) throws Exception {
		Method method = null;
		try {
			method = entityClass.getMethod(setter(methodName,type.getName()),type);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return method.invoke(owner,args);
	}
	
	private static String setter(String name,String type) {
		if(name.startsWith("is")){
			if("boolean".equals(type) || "java.lang.Boolean".equals(type)){
				name = name.substring(2);
			}
		}
		return setter(name);
	}
	
	private static String setter(String name) {
		return "set" + transName(name);
	}
	
	private static String transName(String name) {
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}
	
	/**
	 * 调用get方法
	 * @param owner
	 * @param methodName
	 * @param args
	 * @param type
	 * @return
	 * @throws Exception
	 */
	private  Object invokeGetterMethod(Object owner,String methodName, Object[] args,Class<?> type) throws Exception {
		
			Method method = null;
			try {
				if(type == boolean.class){
					method = entityClass.getMethod(isBoolean(methodName));
				}else{
					method = entityClass.getMethod(getter(methodName));
				}
			} catch (SecurityException e) {
			} catch (NoSuchMethodException e) {
				return " can't find 'get" + methodName + "' method";
			}
			return method.invoke(owner);
		}
	

	
	private static String isBoolean(String name) {
		if(name.startsWith("is")){
			return name;
		}
		return "is" + transName(name);
	}

	private static String getter(String name) {
		return "get" + transName(name);
	}

}
