package com.l.validator;

import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App{
    public static void main( String[] args ){
    	long begin=System.currentTimeMillis();
		try{
			List<String> a=new ArrayList<>();
			a.add("a");
			a.add("b");
			a.add("c");
			Validator
				.get(500,"验证非包含!")
				.beNotContains("a", a)
			;
			System.out.println("success!!");
		}catch(ValidationException e){
			System.out.println("co:"+e.getCo()+"\tex:"+e.getEx());
		}finally{
			System.out.println("cost:"+(System.currentTimeMillis()-begin));
		}
    }
}
