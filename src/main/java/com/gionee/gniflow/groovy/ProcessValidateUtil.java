package com.gionee.gniflow.groovy;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.IDN;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

public class ProcessValidateUtil {
	private static final Log log = LoggerFactory.make();
	private static Logger logger = Logger.getLogger(ProcessValidateUtil.class);
	public static final String IS_VALID = "isValid";
	public static final String MESSAGE = "message";
	/**
	 * 字段必须为空的验证
	 * @param validValue
	 * @return
	 */
	private static Map<String,Object> nullValidate(Object object){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		Boolean flag = false;
		if(object == null){
			flag = true;
		}else{
			flag = false;
		}
		String message = "其值必须为 true";
		resultMap.put(MESSAGE, message);
		resultMap.put(IS_VALID, flag);
		return resultMap;
	}
	/**
	 * 被注释的元素必须不为 null 
	 * @param validValue
	 * @return
	 */
	private static Map<String,Object> notNullValidate(Object object){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		Boolean flag = false;
		if(object != null){
			flag = true;
		}else{
			flag = false;
		}
		resultMap.put(MESSAGE, "其值必须为空");
		resultMap.put(IS_VALID, flag);
		return resultMap;
	}
	/**
	 * 被注释的元素必须为 true  
	 * @param validValue
	 * @return
	 */
	private static  Map<String,Object> assertTrueValidate(Boolean bool){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		Boolean flag = false;
		if(bool == null || bool){
			flag = true;
		}
		resultMap.put(MESSAGE, "其值必须为 true");
		resultMap.put(IS_VALID, flag);
		return resultMap;
	}
	/**
	 * 必须为 false  
	 * @param validValue
	 * @return
	 */
	private static Map<String,Object> assertFalseValidate(Boolean bool){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		Boolean flag = false;
		if(bool == null || !bool){
			flag = true;
		}
		resultMap.put(MESSAGE, "值必须为 false");
		resultMap.put(IS_VALID, flag);
		return resultMap;
	}
	/**
	 * 必须是一个数字，其值必须大于等于指定的最小值 
	 * @param validValue
	 * @param arg
	 * @return
	 */
	private static Map<String,Object> minValidate(Number value,long minValue){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		Boolean flag = false;
		if ( value == null ) {
			flag = true;
		}
		if ( value instanceof BigDecimal ) {
			flag = ( ( BigDecimal ) value ).compareTo( BigDecimal.valueOf( minValue ) ) != -1;
		}
		else if ( value instanceof BigInteger ) {
			flag = ( ( BigInteger ) value ).compareTo( BigInteger.valueOf( minValue ) ) != -1;
		}
		else {
			long longValue = value.longValue();
			flag = longValue >= minValue;
		}
		resultMap.put(MESSAGE, "其值必须大于指定的最小值："+minValue);
		resultMap.put(IS_VALID, flag);
		return resultMap;
	}
	/**
	 * 必须是一个数字，其值必须小于等于指定的最大值 
	 * @param validValue
	 * @param arg
	 * @return
	 */
	private static Map<String,Object> maxValidate(Number value,long maxValue){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		Boolean flag = false;
		if ( value == null ) {
			flag = true;
		}
		if ( value instanceof BigDecimal ) {
			flag = ( ( BigDecimal ) value ).compareTo( BigDecimal.valueOf( maxValue ) ) != 1;
		}
		else if ( value instanceof BigInteger ) {
			flag = ( ( BigInteger ) value ).compareTo( BigInteger.valueOf( maxValue ) ) != 1;
		}
		else {
			long longValue = value.longValue();
			flag = longValue <= maxValue;
		}
		resultMap.put(MESSAGE, "其值必须小于指定的最小值："+maxValue);
		resultMap.put(IS_VALID, flag);
		return resultMap;
	}
	/**
	 * 必须是一个数字，其值必须大于等于指定的最小值 
	 * @param validValue
	 * @param arg
	 * @return
	 */
	private static Map<String,Object> decimalMinValidate(Number value,BigDecimal maxValue){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		Boolean flag = false;
		if ( value == null ) {
			flag = true;
		}

		int comparisonResult;
		if ( value instanceof BigDecimal ) {
			comparisonResult = ( (BigDecimal) value ).compareTo( maxValue );
		}
		else if ( value instanceof BigInteger ) {
			comparisonResult = ( new BigDecimal( (BigInteger) value ) ).compareTo( maxValue );
		}
		else if ( value instanceof Long ) {
			comparisonResult = ( BigDecimal.valueOf( value.longValue() ).compareTo( maxValue ) );
		}
		else {
			comparisonResult = ( BigDecimal.valueOf( value.doubleValue() ).compareTo( maxValue ) );
		}
		flag = comparisonResult  >= 0;
		resultMap.put(MESSAGE, "其值必须大于等于指定的最小值 ："+maxValue);
		resultMap.put(IS_VALID, flag);
		return resultMap;
	}
	/**
	 * 必须是一个数字，其值必须小于等于指定的最大值 
	 * @param validValue
	 * @param arg
	 * @return
	 */
	private static Map<String,Object> decimalMaxValidate(Number value,BigDecimal maxValue){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		Boolean flag = false;
		if ( value == null ) {
			flag = true;
		}

		int comparisonResult;
		if ( value instanceof BigDecimal ) {
			comparisonResult = ( (BigDecimal) value ).compareTo( maxValue );
		}
		else if ( value instanceof BigInteger ) {
			comparisonResult = ( new BigDecimal( (BigInteger) value ) ).compareTo( maxValue );
		}
		else if ( value instanceof Long ) {
			comparisonResult = ( BigDecimal.valueOf( value.longValue() ).compareTo( maxValue ) );
		}
		else {
			comparisonResult = ( BigDecimal.valueOf( value.doubleValue() ).compareTo( maxValue ) );
		}
		flag = comparisonResult <= 0;
		resultMap.put(MESSAGE, "其值必须小于等于指定的最大值  ："+maxValue);
		resultMap.put(IS_VALID, flag);
		return resultMap;
	}
	/**
	 * 大小必须在指定的范围内   
	 * @param validValue
	 * @param args
	 * @return
	 */
	private static Map<String,Object> sizeValidate(Collection<Object> collection,String[] args){
		int min  = Integer.parseInt(args[0]);
		int max  = Integer.parseInt(args[1]);
		Map<String,Object> resultMap = new HashMap<String,Object>();
		Boolean flag = false;
		if ( min < 0 ) {
			flag = false;
			resultMap.put(MESSAGE, "其最小值不能小于0");
			resultMap.put(IS_VALID, flag);
			return resultMap;
		}
		if ( max < 0 ) {
			flag = false;
			resultMap.put(MESSAGE, "其最大值不能小于0");
			resultMap.put(IS_VALID, flag);
			return resultMap;
		}
		if ( max < min ) {
			flag = false;
			resultMap.put(MESSAGE, "其最大值不能小于最小值");
			resultMap.put(IS_VALID, flag);
			return resultMap;
		}
		if ( collection == null ) {
			flag = true;
		}
		int length = collection.size();
		flag = length >= min && length <= max;
		resultMap.put(MESSAGE, "其集合大小必须在指定的范围：["+min+","+max+"]内  ");
		resultMap.put(IS_VALID, flag);
		return resultMap;
	}
	/**
	 * 必须是一个数字，其值必须在可接受的范围内
	 * @param validValue
	 * @return
	 */
	private static Map<String,Object> digitsValidate(Number num,String[] args){
		int maxIntegerLength  = Integer.parseInt(args[0]);
		int maxFractionLength  = Integer.parseInt(args[1]);
		Map<String,Object> resultMap = new HashMap<String,Object>();
		Boolean flag = false;
		if ( maxIntegerLength < 0 ) {
			flag = false;
			resultMap.put(MESSAGE, "其值的整数数位长度不能小于0");
			resultMap.put(IS_VALID, flag);
			return resultMap;
		}
		if ( maxFractionLength < 0 ) {
			flag = false;
			resultMap.put(MESSAGE, "其值的小数位长度不能小于0");
			resultMap.put(IS_VALID, flag);
			return resultMap;
		}
		if ( num == null ) {
			flag = true;
		}

		BigDecimal bigNum;
		if ( num instanceof BigDecimal ) {
			bigNum = ( BigDecimal ) num;
		}
		else {
			bigNum = new BigDecimal( num.toString() ).stripTrailingZeros();
		}

		int integerPartLength = bigNum.precision() - bigNum.scale();
		int fractionPartLength = bigNum.scale() < 0 ? 0 : bigNum.scale();

		flag = ( maxIntegerLength >= integerPartLength && maxFractionLength >= fractionPartLength );
		resultMap.put(MESSAGE, "其值的整数位长度不能大于："+maxIntegerLength+",小数长度不能大于："+maxFractionLength);
		resultMap.put(IS_VALID, flag);
		return resultMap;
	}
	/**
	 * 必须是一个过去的日期    
	 * @param validValue
	 * @return
	 */
	private static Map<String,Object> pastValidate(Date date){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		Boolean flag = false;
		if ( date == null ) {
			flag = true;
		}
		flag = date.before( new Date() );
		resultMap.put(MESSAGE, "其值必须是一个过去的日期  ");
		resultMap.put(IS_VALID, flag);
		return resultMap;
	}
	/**
	 * 必须是一个将来的日期    
	 * @param validValue
	 * @return
	 */
	private static Map<String,Object> futureValidate(Date date){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		Boolean flag = false;
		//null values are valid
		if ( date == null ) {
			flag = true;
		}
		flag = date.after( new Date() );
		resultMap.put(MESSAGE, "其值必须是一个将来的日期 ");
		resultMap.put(IS_VALID, flag);
		return resultMap;
	}
	/**
	 * 必须符合指定的正则表达式    
	 * @param validValue
	 * @return
	 */
	private static Map<String,Object> patternValidate(String value,String parameters){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		Boolean flag = false;
		java.util.regex.Pattern pattern = null;
		try {
			pattern = java.util.regex.Pattern.compile( parameters, java.util.regex.Pattern.CASE_INSENSITIVE );
		}
		catch ( PatternSyntaxException e ) {
			throw log.getInvalidRegularExpressionException( e );
		}
		if ( value == null ) {
			flag = true;
		}
		Matcher m = pattern.matcher( value );
		flag = m.matches();
		resultMap.put(MESSAGE, "其值必须符合指定的正则表达式   :"+parameters);
		resultMap.put(IS_VALID, flag);
		return resultMap;
	}
	/**
	 * 验证字符串非null，且长度必须大于0    
	 * @param validValue
	 * @return
	 */
	private static Map<String,Object> notblankValidate(String validValue){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		Boolean flag = false;
		if ( validValue == null ) {
			flag = false;
		}

		flag = validValue.trim().length() > 0;
		resultMap.put(MESSAGE, "其值不能为空");
		resultMap.put(IS_VALID, flag);
		return resultMap;
	}
	/**
	 * 必须是电子邮箱地址    
	 * @param validValue
	 * @return
	 */
	private static Map<String,Object> emailValidate(String value){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put(MESSAGE, "必须是电子邮箱地址 ");
		Boolean flag = false;
		if ( value == null || value.length() == 0 ) {
			flag =  true;
		}
		String[] emailParts = value.toString().split( "@" );
		try{
			if ( emailParts.length != 2 ) {
				flag = false;
				resultMap.put(IS_VALID, flag);
				return resultMap;
			}
			if ( emailParts[0].endsWith( "." ) || emailParts[1].endsWith( "." ) ) {
				flag = false;
				resultMap.put(IS_VALID, flag);
				return resultMap;
			}
		}catch(Exception e){
			resultMap.put(IS_VALID, flag);
			return resultMap;
		}
		String ATOM = "[a-z0-9!#$%&'*+/=?^_`{|}~-]";
		String DOMAIN = ATOM + "+(\\." + ATOM + "+)*";
		String IP_DOMAIN = "\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\]";
		java.util.regex.Pattern localPattern = java.util.regex.Pattern.compile( ATOM + "+(\\." + ATOM + "+)*", java.util.regex.Pattern.CASE_INSENSITIVE );
		java.util.regex.Pattern domainPattern = java.util.regex.Pattern.compile( DOMAIN + "|" + IP_DOMAIN, java.util.regex.Pattern.CASE_INSENSITIVE );
		if ( matchPart( emailParts[0], localPattern ) ) {
			flag = true;
		}

		flag = matchPart( emailParts[1], domainPattern );
		resultMap.put(IS_VALID, flag);
		return resultMap;
	}
	
	private static boolean matchPart(String part, java.util.regex.Pattern pattern) {
		try {
			part = IDN.toASCII( part );
		}
		catch ( IllegalArgumentException e ) {
			return false;
		}
		Matcher matcher = pattern.matcher( part );
		return matcher.matches();
	}
	/**
	 * 字符串的大小必须在指定的范围内    
	 * @param validValue
	 * @param args
	 * @return
	 */
	private static Map<String,Object> lengthValidate(String validValue,String[] args){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		Boolean flag = false;
		int min  = Integer.parseInt(args[0]);
		int max  = Integer.parseInt(args[1]);
		if ( min < 0 ) {
			flag = false;
			resultMap.put(MESSAGE, "最小长度不能小于0");
			resultMap.put(IS_VALID, flag);
			return resultMap;
		}
		if ( max < 0 ) {
			flag = false;
			resultMap.put(MESSAGE, "最大长度不能小于0");
			resultMap.put(IS_VALID, flag);
			return resultMap;
		}
		if ( max < min ) {
			flag = false;
			resultMap.put(MESSAGE, "最小长度不能大于最大长度");
			resultMap.put(IS_VALID, flag);
			return resultMap;
		}
		if ( validValue == null ) {
			flag = true;
		}
		int length = validValue.length();
		if(length >= min && length <= max){
			flag = true;
		}
		resultMap.put(MESSAGE, "其值必须在["+min+","+max+"]范围内 ");
		resultMap.put(IS_VALID, flag);
		return resultMap;
	}
	/**
	 * 不存在需要验证的类型时返回信息
	 * @param validType
	 * @return
	 */
	private static Map<String,Object> noneValidate(String validType){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put(IS_VALID, true);
		String message = "当前环境暂无"+validType+"验证规则";
		logger.debug(message);
		resultMap.put(MESSAGE, message);
		return resultMap;
	}
	
	/**
	 * 根据验证规则，参数，传入值返回验证结果
	 * @param validType
	 * @param validValue
	 * @param args
	 * @return
	 * @throws ParseException 
	 */
	public  static Map<String,Object> validate(String validType, Object validValue,String ...args) throws ParseException {
		// TODO Auto-generated method stub
		Map<String,Object> result = new HashMap<String,Object>();
		switch(validType){
			case ProcessValidateConstant.Required :result = notblankValidate((String)validValue);break;
			case ProcessValidateConstant.Null :result = nullValidate(validValue);break;
			case ProcessValidateConstant.NotNull :result = notNullValidate(validValue);break;
			case ProcessValidateConstant.AssertTrue :result = assertTrueValidate((Boolean)validValue);break;
			case ProcessValidateConstant.AssertFalse :result = assertFalseValidate((Boolean)validValue);break;
			case ProcessValidateConstant.Min :result = minValidate(NumberUtils.createNumber((String)validValue),NumberUtils.toLong(args[0]));break;
			case ProcessValidateConstant.Max :result = maxValidate(NumberUtils.createNumber((String)validValue),NumberUtils.toLong(args[0]));break;
			case ProcessValidateConstant.DecimalMin :result = decimalMinValidate(NumberUtils.createNumber((String)validValue),NumberUtils.createBigDecimal(args[0]));break;
			case ProcessValidateConstant.DecimalMax :result = decimalMaxValidate(NumberUtils.createNumber((String)validValue),NumberUtils.createBigDecimal(args[0]));break;
			case ProcessValidateConstant.Size :result = sizeValidate((Collection)validValue,args);break;
			case ProcessValidateConstant.Digits :result = digitsValidate(NumberUtils.createNumber((String)validValue),args);break;
			case ProcessValidateConstant.Past :result = pastValidate(DateUtils.parseDate((String)validValue, new String[]{"yyyy-MM-dd"}));break;
			case ProcessValidateConstant.Future :result = futureValidate(DateUtils.parseDate((String)validValue, new String[]{"yyyy-MM-dd"}));break;
			case ProcessValidateConstant.Pattern :result = patternValidate((String)validValue,args[0]);break;
			case ProcessValidateConstant.NotBlank :result = notblankValidate((String)validValue);break;
			case ProcessValidateConstant.Email :result = emailValidate((String)validValue);break;
			case ProcessValidateConstant.Length :result = lengthValidate((String)validValue,args);break;
			default:result = noneValidate(validType);
			
		}
		return result;
		
	}
}
