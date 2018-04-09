# LValidator
Server end's form data validation tool

<pre>
 <b>example:</b>
 Validator
	.get(505,"data validation failed")//获取实例并设置异常编号为505,异常内容为data validation failed
 	.beTrue(xx==xx)
 	.beFalse(yy=yy)
 	.beInRange(12,30,23)
 	.beNumber(0.2E-23)
 	.beEqual(o1,o2)//到这里凡是未满足条件的都会抛出编号为505，内容为data validation failed的ValidationException
 	.restoreThrowInfo()//重置异常编号和信息为默认
 	.beNotEqual(o2,o1)
 	.rewriteThrowInfo(506,"data validation failed again!")//重新填入异常编号和异常信息
 	.disableKeepLastInform()//不记住上次异常信息
 	.beBlank(o,101,"101 error")//这里不满足条件则会抛出101和101 error,但是并不会保留这个异常编码和信息到下次异常的报出,因为之前已经disableKeepLastInform()
 	.beNotBlank(o)//这里抛出异常还是会报出506,data validation failed again
  
  <b>that's it,enjoy</b>
</pre>
