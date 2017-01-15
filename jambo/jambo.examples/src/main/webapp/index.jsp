<%
String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>index</title>
<script type="" language="">
var para="width=1024,height=740,toolbar=no,top=0,left=0,status=yes,resizable=1,menubar=no,location=no,status=no";
window.open("<%=contextPath%>/system/loginAction/welcome.do","",para);
window.opener=null;
window.close();
</script>
</head>
<body>
</body>
</html>