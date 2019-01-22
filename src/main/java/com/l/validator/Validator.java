package com.l.validator;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * the validation tool from l.sl
 * <pre>
 * eg:
 * Validator
 *	.get(505,"data validation failed")//获取实例并设置异常编号为505,异常内容为data validation failed
 * 	.beTrue(xx==xx)
 * 	.beFalse(yy=yy)
 * 	.beInRange(12,30,23)
 * 	.beNumber(0.2E-23)
 * 	.beEqual(o1,o2)//到这里凡是未满足条件的都会抛出编号为505，内容为data validation failed的ValidationException
 * 	.restoreThrowInfo()//重置异常编号和信息为默认
 * 	.beNotEqual(o2,o1)
 * 	.rewriteThrowInfo(506,"data validation failed again!")//重新填入异常编号和异常信息
 * 	.disableKeepLastInform()//不记住上次异常信息
 * 	.beBlank(o,101,"101 error")//这里不满足条件则会抛出101和101 error,但是并不会保留这个异常编码和信息到下次异常的报出,因为之前已经disableKeepLastInform()
 * 	.beNotBlank(o)//这里抛出异常还是会报出506,data validation failed again
 * 
 * </pre>
 */
public class Validator{
	private final static String DEX="数据校验失败!",UNKNOWN_TYPE_EX="给定类型未知";
	private final static int DCO=0x0,UNKOWN_TYPE_CO=-0x1;
	private String ex=DEX;
	private Integer co=DCO;
	private boolean keeplastinform=false;
	
	
	/**
	 * 功能描述：启用上次异常信息保留
	 */
	public Validator enableKeepLastInform(){
		this.keeplastinform=true;
		return this;
	}
	
	/**
	 * 功能描述：禁用上次异常信息保留
	 */
	public Validator disableKeepLastInform(){
		this.keeplastinform=false;
		return this;
	}
	
	/*   constructor part   */
	
	private Validator(int co,String ex){
		this.co=co;
		this.ex=ex;
		enableKeepLastInform();
	}
	
	private Validator(int co){
		this.co=co;
		enableKeepLastInform();
	}
	
	private Validator(String ex){
		this.ex=ex;
		enableKeepLastInform();
	}
	
	private Validator(){
	}
	
	/*   the get part   */
	
	/**
	 * 功能描述：获取默认错误编号、默认错误信息的校验器
	 */
	public final static Validator get(){
		return new Validator();
	}
	
	/**
	 * 功能描述：获取指定错误编号、指定错误信息的校验器，如果后续校验条件中有设置,则会被后续指定的值覆盖
	 */
	public final static Validator get(int co,String ex){
		return new Validator(co,ex);
	}
	
	/**
	 * 功能描述：获取指定错误编号的校验器，如果后续校验条件中有设置,则会被后续指定的值覆盖
	 */
	public final static Validator get(int co){
		return new Validator(co);
	}
	
	/**
	 * 功能描述：获取指定错误信息的校验器，如果后续校验条件中有设置,则会被后续指定的值覆盖
	 */
	public final static Validator get(String ex){
		return new Validator(ex);
	}
	
	
	/*   be true part   */
	public Validator beTrue(boolean exp,Integer co,String ex){
		rewriteThrowInfo(co, ex);
		if(!exp){
			doThrow();
		}
		return this;
	}
	public Validator beTrue(boolean exp){
		return beTrue(exp,null,null);
	}
	public Validator beTrue(boolean exp,Integer co){
		return beTrue(exp, co,null);
	}
	public Validator beTrue(boolean exp,String ex){
		return beTrue(exp, null,ex);
	}
	
	
	/*   be false part   */
	public Validator beFalse(boolean exp,Integer co,String ex){
		return beTrue(!exp,co,ex);
	}
	public Validator beFalse(boolean exp){
		return beTrue(!exp);
	}
	public Validator beFalse(boolean exp,String ex){
		return beTrue(!exp,ex);
	}
	public Validator beFalse(boolean exp,Integer co){
		return beTrue(!exp,co);
	}
	
	
	/*   be null part   */
	
	/**
	 * 功能描述：判断给定对象是否为null,如果不为null则抛出异常
	 * @param o
	 * @param co
	 * @param ex
	 */
	public Validator beNull(Object o,Integer co,String ex){
		return beTrue(o==null, co, ex);
	}
	public Validator beNull(Object o,Integer co){
		return beNull(o,co,null);
	}
	public Validator beNull(Object o,String ex){
		return beNull(o, null, ex);
	}
	public Validator beNull(Object o){
		return beNull(o, null, null);
	}
	
	
	/*   be not null part   */
	/**
	 * 功能描述：判断给定对象是否不为null,如果为null则抛出异常
	 * @param o
	 * @param co
	 * @param ex
	 * 2018年3月27日 下午4:40:17
	 */
	public Validator beNotNull(Object o,Integer co,String ex){
		return beTrue(o!=null);
	}
	public Validator beNotNull(Object o,Integer co){
		return beNotNull(o,co,null);
	}
	public Validator beNotNull(Object o,String ex){
		return beNotNull(o, null, ex);
	}
	public Validator beNotNull(Object o){
		return beNotNull(o, null, null);
	}
	

	/*   equal part   */
	/**
	 * 功能描述：判断是否相等,如果不相等则抛出异常,都为null也会抛出异常,如果需要null做对比,可以使用{@link #beEqualNullable(Object, Object, Integer, String)}
	 */
	public Validator beEqual(Object o1,Object o2,Integer co,String ex){
		return beTrue(o1!=null&&o2!=null&&o1.equals(o2),co,ex);
	}
	public Validator beEqual(Object o1,Object o2,Integer co){
		return beEqual(o1, o2, co, null);
	}
	public Validator beEqual(Object o1,Object o2,String ex){
		return beEqual(o1, o2, null,ex);
	}
	public Validator beEqual(Object o1,Object o2){
		return beEqual(o1, o2, null,null);
	}
	
	/*   be not equal part    */
	/**
	 * 功能描述：判断是否相等,如果相等则抛出异常,都为null也会抛出异常,其中任意一个为null也会抛出异常,如果需要null做对比,可以使用{@link #beNotEqualNullable(Object, Object, Integer, String)}
	 */
	public Validator beNotEqual(Object o1,Object o2,Integer co,String ex){
		return beTrue(o1==null?false:o2==null?false:!o1.equals(o2));
	}
	public Validator beNotEqual(Object o1,Object o2,Integer co){
		return beNotEqual(o1, o2, co, null);
	}
	public Validator beNotEqual(Object o1,Object o2,String ex){
		return beNotEqual(o1, o2, null,ex);
	}
	public Validator beNotEqual(Object o1,Object o2){
		return beNotEqual(o1, o2, null,null);
	}
	
	
	/*   equal-nullable part   */
	/**
	 * 功能描述：判断是否相等,如果不相等则抛出异常,可以使用null做操作
	 */
	public Validator beEqualNullable(Object o1,Object o2,Integer co,String ex){
		return beTrue(o1==null?o2==null?true:false:o2==null?false:o1.equals(o2));
	}
	public Validator beEqualNullable(Object o1,Object o2,Integer co){
		return beEqualNullable(o1, o2, co, null);
	}
	public Validator beEqualNullable(Object o1,Object o2,String ex){
		return beEqualNullable(o1, o2, null,ex);
	}
	public Validator beEqualNullable(Object o1,Object o2){
		return beEqualNullable(o1, o2, null,null);
	}
	
	/*   be not equal-nullable part    */
	/**
	 * 功能描述：判断是否相等,如果相等则抛出异常,可以使用null做对比 
	 */
	public Validator beNotEqualNullable(Object o1,Object o2,Integer co,String ex){
		return beTrue(o1==null?o2==null?false:!o2.equals(o1):!o1.equals(o2));
	}
	public Validator beNotEqualNullable(Object o1,Object o2,Integer co){
		return beNotEqualNullable(o1, o2, co, null);
	}
	public Validator beNotEqualNullable(Object o1,Object o2,String ex){
		return beNotEqualNullable(o1, o2, null,ex);
	}
	public Validator beNotEqualNullable(Object o1,Object o2){
		return beNotEqualNullable(o1, o2, null,null);
	}
	
	/*   be not blank part    */
	/**
	 * 功能描述：判断给定对象是否不为空,可判定对象为<br/>
	 * 			字符串 !=null&&.length()>0<br/>
	 * 			集合类 !=null&&.size()>0<br/>
	 * 			map类  !=null&&.size()>0<br/>
	 * 			迭代器 !=null&&.hasNext()<br/>
	 * 			数字类型!=0<br/>
	 * 			<b style="color:red">不在上述类型中则直接抛异常</b>
	 */
	@SuppressWarnings("rawtypes")
	public Validator beNotBlank(Object o,Integer co,String ex){
		beTrue(o!=null, co, ex);
		if(o instanceof String){
			beTrue(((String) o).length()>0, co, ex);
		}else if(o instanceof Collection){
			beTrue(((Collection)o).size()>0, co, ex);
		}else if(o instanceof Map){
			beTrue(((Map) o).size()>0, co, ex);
		}else if(o instanceof Iterator){
			beTrue(((Iterator) o).hasNext(), co, ex);
		}else if(o instanceof Number){
			beTrue(((Number) o).doubleValue()>0, co, ex);
		}else{
			beTrue(false,UNKOWN_TYPE_CO,UNKNOWN_TYPE_EX);
		}
		return this;
	}
	public Validator beNotBlank(Object o,Integer co){
		return beNotBlank(o,co,null);
	}
	public Validator beNotBlank(Object o,String ex){
		return beNotBlank(o,null,ex);
	}
	public Validator beNotBlank(Object o){
		return beNotBlank(o,null,null);
	}
	
	/*   be blank part    */
	/**
	 * 功能描述：判断给定对象是否为空,可判定对象为<br/>
	 * 			字符串 !=null&&.length()>0<br/>
	 * 			集合类 !=null&&.size()>0<br/>
	 * 			map类  !=null&&.size()>0<br/>
	 * 			迭代器 !=null&&.hasNext()<br/>
	 * 			数字类型==0<br/>
	 * 			<b style="color:red">不在上述类型中则直接抛异常</b>
	 */
	@SuppressWarnings("rawtypes")
	public Validator beBlank(Object o,Integer co,String ex){
		beTrue(o!=null, co, ex);
		if(o instanceof String){
			beTrue(((String) o).length()==0, co, ex);
		}else if(o instanceof Collection){
			beTrue(((Collection)o).size()==0, co, ex);
		}else if(o instanceof Map){
			beTrue(((Map) o).size()==0, co, ex);
		}else if(o instanceof Iterator){
			beTrue(!((Iterator) o).hasNext(), co, ex);
		}else if(o instanceof Number){
			beTrue(((Number) o).doubleValue()==0, co, ex);
		}else{
			beTrue(false,UNKOWN_TYPE_CO,UNKNOWN_TYPE_EX);
		}
		return this;
	}
	public Validator beBlank(Object o,Integer co){
		return beBlank(o,co,null);
	}
	public Validator beBlank(Object o,String ex){
		return beBlank(o,null,ex);
	}
	public Validator beBlank(Object o){
		return beBlank(o,null,null);
	}

	/*   be in range part   */
	/**
	 * 功能描述：判断所给对象是否在指定范围内<br/>
	 * 			字符串 !=null&&.length() in range<br/>
	 * 			集合类 !=null&&.size() in range<br/>
	 * 			map类  !=null&&.size() in range<br/>
	 * 			迭代器 !=null&&iterator times in range<br/>
	 * 			数字类型!=null&&in range<br/>
	 * 			<b style="color:red">不在上述类型中则直接抛异常</b>
	 */
	@SuppressWarnings("rawtypes")
	public Validator beInRange(Number begin,Number end,Object o,boolean beginInclude,boolean endInclude,Integer co,String ex){
		beTrue(o!=null&&begin!=null&&end!=null, co, ex);
		double be=begin.doubleValue(),en=end.doubleValue();
		double len=-1;
		if(o instanceof String){
			len=((String) o).length();
		}else if(o instanceof Collection){
			len=((Collection) o).size();
		}else if(o instanceof Map){
			len=((Map) o).size();
		}else if(o instanceof Iterator){
			Iterator i=(Iterator)o;
			double j=1;
			while(i.hasNext()){
				i.next();
				j++;
				//大于最大长度则直接break
				if(j>end.doubleValue()+1){
					break;
				}
			}
			len=len+j;
		}else if(o instanceof Number){
			len=((Number) o).doubleValue();
		}else{
			beTrue(false,UNKOWN_TYPE_CO,UNKNOWN_TYPE_EX);
		}
		beTrue((beginInclude?be<=len:be<len)&&(endInclude?len<=en:len<en), co, ex);
		return this;
	}
	public Validator beInRange(Number begin,Number end,Object o,boolean beginIncluded,boolean endIncluded){
		return beInRange(begin,end,o,beginIncluded,endIncluded,null,null);
	}
	public Validator beInRange(Number begin,Number end,Object o,Integer co){
		return beInRange(begin,end,o,false,false,co,null);
	}
	public Validator beInRange(Number begin,Number end,Object o,String ex){
		return beInRange(begin,end,o,false,false,null,ex);
	}
	public Validator beInRange(Number begin,Number end,Object o){
		return beInRange(begin,end,o,false,false,null,null);
	}
	
	
	/*   be not in range part   */
	/**
	 * 功能描述：判断所给对象是否不在指定范围内<br/>
	 * 			字符串 !=null&&.length() not in range<br/>
	 * 			集合类 !=null&&.size() not in range<br/>
	 * 			map类  !=null&&.size() not in range<br/>
	 * 			迭代器 !=null&&iterator times not in range<br/>
	 * 			数字类型!=null&&not in range<br/>
	 * 			<b style="color:red">不在上述类型中则直接抛异常</b>
	 */
	@SuppressWarnings("rawtypes")
	public Validator beNotInRange(Number begin,Number end,Object o,boolean beginInclude,boolean endInclude,Integer co,String ex){
		beTrue(o!=null&&begin!=null&&end!=null, co, ex);
		double be=begin.doubleValue(),en=end.doubleValue();
		double len=-1;
		if(o instanceof String){
			len=((String) o).length();
		}else if(o instanceof Collection){
			len=((Collection) o).size();
		}else if(o instanceof Map){
			len=((Map) o).size();
		}else if(o instanceof Iterator){
			Iterator i=(Iterator)o;
			double j=1;
			while(i.hasNext()){
				i.next();
				j++;
			}
			len=len+j;
		}else if(o instanceof Number){
			len=((Number) o).doubleValue();
		}else{
			beTrue(false,UNKOWN_TYPE_CO,UNKNOWN_TYPE_EX);
		}
		beTrue((beginInclude?len<=be:len<be)||(endInclude?len>=en:len>en), co, ex);
		return this;
	}
	public Validator beNotInRange(Number begin,Number end,Object o,boolean beginIncluded,boolean endIncluded){
		return beNotInRange(begin,end,o,beginIncluded,endIncluded,null,null);
	}
	public Validator beNotInRange(Number begin,Number end,Object o,Integer co){
		return beNotInRange(begin,end,o,false,false,co,null);
	}
	public Validator beNotInRange(Number begin,Number end,Object o,String ex){
		return beNotInRange(begin,end,o,false,false,null,ex);
	}
	public Validator beNotInRange(Number begin,Number end,Object o){
		return beNotInRange(begin,end,o,false,false,null,null);
	}
	
	/*   be less than part   */
	/**
	 * 功能描述：判断所给数据是否小于指定数据
	 */
	public Validator beLessThan(Number data,Number target,boolean targetInclude,Integer co,String ex){
		beTrue(data!=null&&target!=null,co,ex);
		double ddata=data.doubleValue(),tdata=target.doubleValue();
		return beTrue(targetInclude?ddata<=tdata:ddata<tdata, co, ex);
	}
	public Validator beLessThan(Number data,Number target,boolean targetInclude){
		return beLessThan(data,target,targetInclude,null,null);
	}
	public Validator beLessThan(Number data,Number target,boolean targetInclude,Integer co){
		return beLessThan(data,target,targetInclude,co,null);
	}
	public Validator beLessThan(Number data,Number target,boolean targetInclude,String ex){
		return beLessThan(data,target,targetInclude,null,ex);
	}
	public Validator beLessThan(Number data,Number target){
		return beLessThan(data,target,false,null,null);
	}
	
	/*   be greater than part   */
	/**
	 * 功能描述：判断所给数据是否小于指定数据
	 */
	public Validator beGreaterThan(Number data,Number target,boolean targetInclude,Integer co,String ex){
		beTrue(data!=null&&target!=null,co,ex);
		double ddata=data.doubleValue(),tdata=target.doubleValue();
		return beTrue(targetInclude?ddata>=tdata:ddata>tdata, co, ex);
	}
	public Validator beGreaterThan(Number data,Number target,boolean targetInclude){
		return beGreaterThan(data,target,targetInclude,null,null);
	}
	public Validator beGreaterThan(Number data,Number target,boolean targetInclude,Integer co){
		return beGreaterThan(data,target,targetInclude,co,null);
	}
	public Validator beGreaterThan(Number data,Number target,boolean targetInclude,String ex){
		return beGreaterThan(data,target,targetInclude,null,ex);
	}
	public Validator beGreaterThan(Number data,Number target){
		return beGreaterThan(data,target,false,null,null);
	}
	
	/*   be in part   */
	/**
	 * 功能描述：判断所给数据(第一个参数)，是否在后续参数中出现过,如果没有则抛出异常
	 */
	public Validator beIn(Object o,Object ... target){
		beTrue(o!=null&&target.length>0,null,null);
		boolean flag=false;
		for(Object tmpo : target){
			if(o.equals(tmpo)){
				flag=true;
				break;
			}
		}
		return beTrue(flag,null,null);
	}
	
	/*   be not in part   */
	/**
	 * 功能描述：判断所给数据(第一个参数)，是否未在后续参数中出现过,如果有，则抛出异常
	 */
	public Validator beNotIn(Object o,Object ... target){
		beTrue(o!=null,null,null);
		if(target.length==0){
			return this;
		}
		boolean flag=true;
		for(Object tmpo : target){
			if(o.equals(tmpo)){
				flag=false;
				break;
			}
		}
		return beTrue(flag,null,null);
	}
	
	
	/*   be contains part    */
	/**
	 * 功能描述：判断给定对象是否在第二个对象中包含,可判定对象为<br/>
	 * 			字符串 !=null&&.contains()>0<br/>
	 * 			集合类 !=null&&.contains()>0<br/>
	 * 			map类  !=null&&.containsKey()>0<br/>
	 * 			<b style="color:red">不在上述类型中则直接抛异常</b>
	 */
	@SuppressWarnings("rawtypes")
	public Validator beContains(Object o1,Object o2,Integer co,String ex){
		beTrue(o1!=null&&o2!=null, co, ex);
		if(o2 instanceof String){
			beTrue(((String) o2).contains(o1.toString()), co, ex);
		}else if(o2 instanceof Collection){
			beTrue(((Collection) o2).contains(o1), co, ex);
		}else if(o2 instanceof Map){
			beTrue(((Map) o2).containsKey(o1), co, ex);
		}else{
			beTrue(false,UNKOWN_TYPE_CO,UNKNOWN_TYPE_EX);
		}
		return this;
	}
	public Validator beContains(Object o1,Object o2,Integer co){
		return beContains(o1,o2,co,null);
	}
	public Validator beContains(Object o1,Object o2,String ex){
		return beContains(o1,o2,null,ex);
	}
	public Validator beContains(Object o1,Object o2){
		return beContains(o1,o2,null,null);
	}
	
	/*   be not contains part    */
	/**
	 * 功能描述：判断给定对象是否在第二个对象中包含,可判定对象为<br/>
	 * 			字符串 !=null&&.contains()>0<br/>
	 * 			集合类 !=null&&.contains()>0<br/>
	 * 			map类  !=null&&.containsKey()>0<br/>
	 * 			<b style="color:red">不在上述类型中则直接抛异常</b>
	 */
	@SuppressWarnings("rawtypes")
	public Validator beNotContains(Object o1,Object o2,Integer co,String ex){
		beTrue(o1!=null&&o2!=null, co, ex);
		if(o2 instanceof String){
			beTrue(!((String) o2).contains(o1.toString()), co, ex);
		}else if(o2 instanceof Collection){
			beTrue(!((Collection) o2).contains(o1), co, ex);
		}else if(o2 instanceof Map){
			beTrue(!((Map) o2).containsKey(o1), co, ex);
		}else{
			beTrue(false,UNKOWN_TYPE_CO,UNKNOWN_TYPE_EX);
		}
		return this;
	}
	public Validator beNotContains(Object o1,Object o2,Integer co){
		return beNotContains(o1,o2,co,null);
	}
	public Validator beNotContains(Object o1,Object o2,String ex){
		return beNotContains(o1,o2,null,ex);
	}
	public Validator beNotContains(Object o1,Object o2){
		return beNotContains(o1,o2,null,null);
	}
	
	/*   be Number part    */
	/**
	 * 功能描述：判断给定对象是否为数字
	 */
	public Validator beNumbers(Object o,Integer co,String ex){
		if(o instanceof Number) return this;
		String so=o.toString();
		if(so.startsWith("0x")){
			beFalse(so.contains(".")||so.contains("_"));
			so=so.substring(2);
		}
		try{
			new BigDecimal(so);
			return this;
		}catch(NumberFormatException e){
			return beTrue(false);
		}
	}
	public Validator beNumbers(Object o,Integer co){
		return beNumbers(o,co,null);
	}
	public Validator beNumbers(Object o,String ex){
		return beNumbers(o,null,ex);
	}
	public Validator beNumbers(Object o){
		return beNumbers(o,null,null);
	}
	
	
	
	/*   be mobile phone number part    */
	/**
	 * 功能描述：判断给定对象是否为手机号
	 */
	public Validator beMobilePhoneNumber(String number,Integer co,String ex){
		return beTrueRegExp(number, "^[1][3,4,5,7,8,9][0-9]{9}$",co,ex);
	}
	public Validator beMobilePhoneNumber(String number,Integer co){
		return beMobilePhoneNumber(number,co,null);
	}
	public Validator beMobilePhoneNumber(String number,String ex){
		return beMobilePhoneNumber(number,null,ex);
	}
	public Validator beMobilePhoneNumber(String number){
		return beMobilePhoneNumber(number,null,null);
	}
	
	/*    be true RegExp part    */
	/**
	 * 功能描述：判断给定字符串是否满足正则表达式
	 */
	public Validator beTrueRegExp(String content,String regExp,Integer co,String ex){
		beTrue(content!=null&&regExp!=null);
		Pattern p = Pattern.compile(regExp);  
		return beTrue(p.matcher(content).matches(),co,ex); 
	}
	public Validator beTrueRegExp(String content,String regExp,Integer co){
		return beTrueRegExp(content,regExp,co,null);
	}
	public Validator beTrueRegExp(String content,String regExp,String ex){
		return beTrueRegExp(content,regExp,null,ex);
	}
	public Validator beTrueRegExp(String content,String regExp){
		return beTrueRegExp(content,regExp,null,null);
	}
	
	/*   be phone number part    */
	/**
	 * 功能描述：判断给定对象是否为座机号码
	 */
	public Validator bePhoneNumber(String number,Integer co,String ex){
		beNotNull(number,co,ex);
		//未包含区号
		if(number.indexOf("-")==-1){
			return beTrueRegExp(number, "^[1-9]{1}[0-9]{5,8}$");
		}else{//有区号
			return beTrueRegExp(number, "^[0][1-9]{2,3}-[0-9]{5,10}$");
		}
	}
	public Validator bePhoneNumber(String number,Integer co){
		return bePhoneNumber(number,co,null);
	}
	public Validator bePhoneNumber(String number,String ex){
		return bePhoneNumber(number,null,ex);
	}
	public Validator bePhoneNumber(String number){
		return bePhoneNumber(number,null,null);
	}
	
	/*  do throw part  */
	private void doThrow(){
		throw new ValidationException(this.co,this.ex);
	}
	
	/*   restore throw info   */
	/**
	 * 功能描述：重置异常编码和消息为默认值
	 */
	public Validator restoreThrowInfo(){
		this.co=DCO;
		this.ex=DEX;
		return this;
	}
	
	/*   rewrite throw info   */
	/**
	 * 功能描述：重新设置异常编码和异常消息
	 */
	public Validator rewriteThrowInfo(Integer co,String ex){
		this.co=co==null?keeplastinform?this.co:DCO:co;
		this.ex=ex==null?keeplastinform?this.ex:DEX:ex;
		return this;
	}
}