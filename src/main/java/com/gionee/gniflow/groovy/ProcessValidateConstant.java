package com.gionee.gniflow.groovy;

/**
 * 验证类型常量类，在P
 * @author zhangp
 *
 */
public class ProcessValidateConstant {
	public static final String Required= "required";//被注释的元素必须不为 null 
	public static final String Null= "null";//被注释的元素必须为 null   
	public static final String NotNull= "notnull";//被注释的元素必须不为 null 
	public static final String AssertTrue= "asserttrue";//被注释的元素必须为 true  
	public static final String AssertFalse= "assertfalse";//被注释的元素必须为 false   
	public static final String Min= "min";//(value) 被注释的元素必须是一个数字，其值必须大于等于指定的最小值    
	public static final String Max= "max";//(value) 被注释的元素必须是一个数字，其值必须小于等于指定的最大值    
	public static final String DecimalMin= "decimalmin";//(value) 被注释的元素必须是一个数字，其值必须大于等于指定的最小值    
	public static final String DecimalMax= "decimalmax";//(value) 被注释的元素必须是一个数字，其值必须小于等于指定的最大值    
	public static final String Size= "size";//(max=, min=)被注释的元素的大小必须在指定的范围内   
	public static final String Digits= "digits";// (integer, fraction)被注释的元素必须是一个数字，其值必须在可接受的范围内    
	public static final String Past= "past";//被注释的元素必须是一个过去的日期    
	public static final String Future= "future";// 被注释的元素必须是一个将来的日期    
	public static final String Pattern= "pattern";//(regex=,flag=) 被注释的元素必须符合指定的正则表达式    
	public static final String NotBlank= "notblank";//验证字符串非null，且长度必须大于0    
	public static final String Email= "email";//被注释的元素必须是电子邮箱地址    
	public static final String Length= "length";//(min=,max=) 被注释的字符串的大小必须在指定的范围内    
	public static final String NotEmpty= "notempty";// 被注释的字符串的必须非空    
	public static final String Range= "range";//(min=,max=,message=) 被注释的元素必须在合适的范围内
}
