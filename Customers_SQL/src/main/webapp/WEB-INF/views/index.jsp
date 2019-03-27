<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Home Page</title>
</head>
<body bgcolor="">
	<h1>Welcome !</h1>
	<form:form method="get" action="/customers/register">
		<h2>${msg}</h2>
		<a href="/customerForm">Click here to register</a>
	</form:form>
</body>
</html>