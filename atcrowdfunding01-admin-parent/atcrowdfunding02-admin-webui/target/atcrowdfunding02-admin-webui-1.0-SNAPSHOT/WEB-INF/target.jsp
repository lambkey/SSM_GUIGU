<%--
  Created by IntelliJ IDEA.
  User: lamp
  Date: 2022/3/13
  Time: 10:20
  To change this template use File | Settings | File Templates.
  此目录下面的文件是受到保护的，如果要访问，需要@Controller进行转发
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<! DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<h1>Success</h1>
<p>
    ${requestScope.adminList }
</p>
</body>
</html>
