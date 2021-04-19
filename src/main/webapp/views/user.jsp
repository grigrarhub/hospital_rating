<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE HTML>
<html>
<head>
    <title>Users</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" type="text/css" href="${contextPath}/resources/css/user.css">
    <script src="${contextPath}/resources/js/script.js"></script>
</head>
<body>
<center>
<h1>Колличество людей с неверными данными: ${count}</h1>
</center>
<table class="table_sort">
    <thead>
        <tr>
            <th>ID</th>
            <th>Full Name</th>
            <th>Email</th>
            <th>Full director name</th>
            <th>Hospital name</th>
            <th>Birthday</th>
            <th>Discharge day</th>
        </tr>
    </thead>
    <tbody>
    <c:forEach items="${users}" var="user">
        <tr>
            <td>${user.id}</td>
            <td>${user.fullName}</td>
            <td>${user.email}</td>
            <td>${user.fullDirectorName}</td>
            <td>${user.hospitalName}</td>
            <td>${user.birthday}</td>
            <td>${user.dischargeDate}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>