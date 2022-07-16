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
    <script>
        $(function () {
            $("#DownloadBtn").click(function () {

                window.location.href="testDownload.do";

            })
        })
    </script>
</head>
<body>
    <input type="button" id="DownloadBtn" value="下载">
</body>
</html>
