<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>
	<form action="/helloweb/join" method="POST" >
		이메일 : <input type="text" name="email" value=""/>
		<br/><br/>
		패스워드 : <input type="text" name="password" value=""/>
		<br/><br/>
		생년 : 	
		<select name="birth-year">
			<option value="1993">1993년</option>
			<option value="1994">1994년</option>
			<option value="1995">1995년</option>
			<option value="1996">1996년</option>
			<option value="1997">1997년</option>
			<option value="1998">1998년</option>
		</select>		
		<br/><br/>		
		성별 : 
		여<input type="radio" name="gender" value="female">
		남<input type="radio" name="gender" value="male" checked="checked">
		<br/><br/>		
		
		취미:
		코딩<input type="checkbox" name="hobbies" value="coding">
		게임<input type="checkbox" name="hobbies" value="gaming">
		낚시<input type="checkbox" name="hobbies" value="fishing">
		<br/><br/>
		자기소개 :
		<textarea name="description" rows="" cols=""></textarea>
		<br/><br/>		
		
		<input type="submit" value="가입"/>
	</form>

</body>
</html>