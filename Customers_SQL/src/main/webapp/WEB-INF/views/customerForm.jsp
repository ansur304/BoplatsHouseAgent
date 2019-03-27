<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Customer Registration Form</title>
</head>
<body bgcolor="">
	<h1 align="center">Customer Registration Form</h1>
	<form:form method="post" action="/customers/submit">
	<h2>${msg}</h2><br>
		<h3>Please provide below information to register</h3>
		<table>
			<tr>
				<td>Name :</td>
				<td><input type="text" name="name" /></td>
			</tr>
			<tr>
				<td>Mobile Number :</td>
				<td><input type="text" name="mobileNumber" /></td>
			</tr>
			<tr>
				<td>Email Id :</td>
				<td><input type="text" name="email" /></td>
			</tr>

			<tr>
				<td>Personal Number :</td>
				<td><input type="text" name="personalNumber" /></td>
			</tr>

			<tr>
				<td>Password :</td>
				<td><input type="password" name="password" /></td>
			</tr>
			<tr></tr>

			<tr>
				<td><input type="submit" value="Register" name="create" /></td>
			</tr>

		</table>
		<br>
		<br>
		<h2>Registered Customers</h2>
		<table border="1">
			<thead>
				<tr align="center">
					<th>Name</th>
					<th>Mobile Number</th>
					<th>Email Id</th>
					<th>Personal Number</th>
					<th>Action</th>
				</tr>
			</thead>
			<tbody>
				<c:choose>
					<c:when test="${empty customerDb}">
						<i></i>
						<br />
					</c:when>
					<c:otherwise>
						<c:forEach items="${customerDb}" var="customerDb">
							<tr align="center">
								<td>${customerDb.name}</td>
								<td>${customerDb.mobileNumber}</td>
								<td>${customerDb.email}</td>
								<td>${customerDb.personalNumber}</td>
								<td><input type="submit" value="Start Job" name="runJob" />
									<input type="hidden" name="selectedName"
									value="${customerDb.name}" /></td>

							</tr>
						</c:forEach>
						<br />
					</c:otherwise>
				</c:choose>

			</tbody>
		</table>
		<br>
		<p>
			<input type="submit" value="Delete All" name="deleteAll" />
		</p>
	</form:form>
</body>
</html>