<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="errorMessage" class="java.lang.String" scope="request"/>

<html>
<head>
    <title>Find user by id</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/users/details" method="get">
    <label for="id">User's ID</label>
    <input id="id" name="id" type="number" min="1">
    <br><br>

    <button type="submit">Find user</button>
    <a href="<c:url value="/users"/> ">
        <input type="button" value="Cancel"/>
    </a>
    <p><%=errorMessage%>
    </p>
</form>
</body>
</html>
