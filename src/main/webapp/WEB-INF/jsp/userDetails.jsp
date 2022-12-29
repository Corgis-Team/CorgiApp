<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:useBean id="user" scope="request" type="com.andersen.corgiapp.entity.User"/>

<html>
<head>
    <title>User's details</title>
</head>
<body>
<p>Id: ${user.id}</p>
<p>Name: ${user.name}></p>
<p>Surname: ${user.surname}</p>
<p>Age: ${user.age}</p>
<p><a href="/users/search">Use search</a></p>
<p><a href="/users/">Show all users</a></p>
</body>
</html>