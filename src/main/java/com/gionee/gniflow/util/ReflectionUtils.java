package com.gionee.gniflow.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.util.FieldUtils;


public class ReflectionUtils {
	
	/**调用Getter方法
     * @param obj           对象
     * @param propertyName  属性名
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws  
	 * @throws IllegalArgumentException 
     * 
     */
    public static Object invokeGetterMethod(Object obj,String propertyName) throws IllegalArgumentException, InvocationTargetException, IllegalAccessException{
        if(obj == null){
        	return null;
        }
    	String getterMethodName = "get"+StringUtils.capitalize(propertyName);
        return invokeMethod(obj, getterMethodName, null, null);
    } 
    public static String invokeGetterMethodStr(Object obj,String propertyName) throws IllegalArgumentException, InvocationTargetException, IllegalAccessException{
    	if(obj == null){
    		return null;
    	}
    	String getterMethodName = "get"+StringUtils.capitalize(propertyName);
    	return invokeMethod(obj, getterMethodName, null, null)==null?"":invokeMethod(obj, getterMethodName, null, null).toString();
    } 
    /**
     * 调用Setter方法.
     * 
     * @param propertyType 用于查找Setter方法,为空时使用value的Class替代.
     * @throws IllegalAccessException 
     * @throws InvocationTargetException 
     */
    public static void invokeSetterMethod(Object target, String propertyName,
            Object value, Class<?> propertyType) throws IllegalArgumentException, InvocationTargetException, IllegalAccessException
    {
        Class<?> type = propertyType != null ? propertyType : value.getClass();
        String setterMethodName = "set" + StringUtils.capitalize(propertyName);
        invokeMethod(target, setterMethodName, propertyName,new Class[] { type },
                new Object[] { value });
    }
    
    /**调用Setter方法
     * @param obj           对象
     * @param propertyName  属性名
     * @param value
     * @throws IllegalAccessException 
     * @throws InvocationTargetException 
     */
  
    public static void invokeSetterMethod(Object target, String propertyName,
            Object value) throws IllegalArgumentException, InvocationTargetException, IllegalAccessException
    {
        invokeSetterMethod(target, propertyName, value, null);
    }
   
    /**直接调用对象方法，忽视private/protected修饰符
     * @param obj
     * @param methodName
     * @param parameterTypes
     * @param params
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     * 
     */
    public static String returnMst(String Mst){
    	switch (Mst) {
        case "class java.lang.Double":
            return "数字";
        case "class java.lang.Integer":
           return "数字";
        case "class java.lang.String":
            return "文本";
        default:
            return " ";
    }
    }
    public static Object invokeMethod (final Object obj,
            final String methodName,final String propertyName, final Class<?>[] parameterTypes,
            final Object[] args) throws IllegalArgumentException, InvocationTargetException, IllegalAccessException {
        Method method = obtainAccessibleMethod(obj, methodName, parameterTypes);
        if (method == null) {
            throw new IllegalArgumentException(
            	"传入的值类型是:"+returnMst(parameterTypes[0].toString())+",需要的类型是:"+returnMst(FieldUtils.getField(obj.getClass(), propertyName).getGenericType().toString())
               );
        }
            return method.invoke(obj, args);
       
    }
    public static Object invokeMethod (final Object obj,
    		final String methodName, final Class<?>[] parameterTypes,
    		final Object[] args) throws IllegalArgumentException, InvocationTargetException, IllegalAccessException {
    	Method method = obtainAccessibleMethod(obj, methodName, parameterTypes);
    	if (method == null) {
    		throw new IllegalArgumentException(
    				
    				);
    	}
    	return method.invoke(obj, args);
    	
    }
    
    /**循环向上转型，获取对象的DeclaredMethod,并强制设置为可访问
     * 如向上转型到Object仍无法找到，返回null
     * 
     * 用于方法需要被多次调用的情况，先使用本函数先取得Method,然后调用Method.invoke(Object obj,Object... args)
     * @param obj
     * @param methodName
     * @param parameterTypes
     * 
     */
    public static Method obtainAccessibleMethod(final Object obj,
            final String methodName, final Class<?>... parameterTypes) {
        Class<?> superClass = obj.getClass();
        Class<Object> objClass = Object.class;
        for (; superClass != objClass; superClass = superClass.getSuperclass()) {
            Method method = null;
            try {
            	
                method = superClass.getDeclaredMethod(methodName,
                            parameterTypes);
                method.setAccessible(true);
                return method;
            } catch (Exception e) {
                // Method不在当前类定义，继续向上转型
            }
        }
        return null;
    }
    
    
    
 public static Field[] getAllFields(Class clazz){
    	
    	return clazz.getDeclaredFields();
    }
     
}
