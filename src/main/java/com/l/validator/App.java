package com.l.validator;

/**
 * Hello world!
 *
 */
public class App{
    public static void main( String[] args ){
    	long begin=System.currentTimeMillis();
		try{
			Validator
				.get(505,"手机号验证失败!")
				.beMobilePhoneNumber("18600951679")
				.bePhoneNumber("023-66793381")
				.beNumbers("0x0001")
				.beContains("123", "123");
			;
			System.out.println("success!!");
		}catch(ValidationException e){
			System.out.println("co:"+e.getCo()+"\tex:"+e.getEx());
		}finally{
			System.out.println("cost:"+(System.currentTimeMillis()-begin));
		}
    }
}
