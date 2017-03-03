<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Login</title>
</head>
<body>
	<%@ include file="header.jsp"%>
	<form action="" method="post">
		<label>Login with your Student ID (MSSV)</label> <br /> <input
			type="text" name="userId" /> <br />
		<p style="color: red;">${errorMessage}</p>
		<br />
		<button type="submit">Login</button>
	</form>
</body>
</html>