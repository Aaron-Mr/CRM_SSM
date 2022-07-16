<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<head>
    <title>Title</title>

</head>
<body>
    <form action="workbench/activity/fileUpload.do" method="post" enctype="multipart/form-data">
        <br>
        <input type="file" name="myFile">
        <br><br><br>
        <input type="text" name="myName">
        <input type="submit" value="上传">
    </form>

</body>
</html>
