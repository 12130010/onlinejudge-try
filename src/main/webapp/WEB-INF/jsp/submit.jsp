<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Submit Problem</title>
<script type="text/javascript">
	var name = "${userName}";
	var shortName = "${shortName}";
</script>
<style type="text/css">
	#wrapper {
	  margin-left: 400px;
	  height: 768px
	}
	#content {
	  float: right;
	  width: 100%;
	}
	#sidebar {
	  float: left;
	  width: 400px;
	  margin-left: -400px;
	  background-color: #FFA;
	}
	#cleared {
	  clear: both;
	}
</style>
</head>
<body>
	<%@ include file="header.jsp" %>
	<%@ include file="chat.html" %>
	<h2>Submit problem: ${problemId}</h2>
	<a href="/problem">Back to List Problem</a>
	<div id="wrapper">
	  <div id="content"><embed src="${problemId}.pdf" type='application/pdf' width="100%" height="100%"></div>
	  <div id="sidebar">
	  	<p>---------------Result---------------</p>
		<p>${message}</p>
		<p style=" color: green;">${messageAccepted}</p>
		<p style=" color: red;">${messageWrong}</p>
		<p style=" color: red;">${messageError}</p>
		<p>---------------Result---------------</p>
		<form action="" method="post" enctype='multipart/form-data'>
			<label>Problem ID:</label>
			<input type="text" name="problemId" value="${problemId}" disabled="disabled"/>
			<br/>
			<label>File java:</label>
			<input type="file" name="fileSubmit"/>
			<br/>
			<button type="submit">Submit</button>
		</form>
	  </div>
	  <div id="cleared"></div>
	</div>
	<br/>
	<br/>
	<br/>
	<br/>
	<br/>
	<h3>Hoàng Nhược Quỳ &copy;</h3>
</body>
</html>