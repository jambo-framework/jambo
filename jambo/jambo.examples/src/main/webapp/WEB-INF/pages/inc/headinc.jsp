<%@ taglib prefix="j" uri="/jop-tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="s"%>

<%@ page  import="com.jambo.jop.common.spring.SpringContextManager"%>

<%
	String contextPath = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName()	+ ":" + request.getServerPort() + contextPath + "/";
%>
<base href="<%=basePath%>">

<meta charset="utf-8">
<meta name="viewport" content="width=device-width">

<meta name="author" content="www.jambo-framework.com" />
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content="no-cache"/> 
<meta http-equiv="Expires" content="0"/>

<fmt:setBundle basename="i18n.public" var="public"/>
<script type="text/javascript" language="javascript">
    var contextPath = "<%=contextPath%>";
    var msgInputCorrectEmail = "<fmt:message key="msgInputCorrectEmail"/>";
    var msgInput = "<fmt:message key="msgInput"/>";
    var msgInputNotNull ="<fmt:message key="msgInputNotNull"/>";
    var msgInvalidDate = "<fmt:message key="msgInvalidDate"/>";
    var msgInvalidDateYyyymmdd = "<fmt:message key="msgInvalidDateYyyymmdd"/>";
    var msgInvalidNumberFormat = "<fmt:message key="msgInvalidNumberFormat"/>";
    var msgInvalidTime = "<fmt:message key="msgInvalidTime"/>";
    <%--var msgIllegalFormat = "<fmt:message key="msgIllegalFormat"/>"--%>
    <%--var msgMonthOutRange = "<fmt:message key="msgMonthOutRange"/>"--%>
    <%--var msgNumberTooBig = "<fmt:message key="msgNumberTooBig"/>"--%>
    <%--var msgNumberTooSmall = "<fmt:message key="msgNumberTooSmall"/>"--%>
    <%--var msgTimeRangeError = "<fmt:message key="msgTimeRangeError"/>"--%>
    <%--var msgInvalidTimeHHMM = "<fmt:message key="msgInvalidTimeHHMM"/>"--%>
    <%--var msgStringOutRange = "<fmt:message key="msgStringOutRange"/>"--%>
    <%--var msgStringOutRangeInfo = "<fmt:message key="msgStringOutRangeInfo"/>"--%>
    <%--var msgIntegerTooLong = "<fmt:message key="msgIntegerTooLong"/>"--%>
    <%--var msgDecimalTooLong = "<fmt:message key="msgDecimalTooLong"/>"--%>
    <%--var msgIntTooLong = "<fmt:message key="msgIntTooLong"/>"--%>
    <%--var msIntMustBe = "<fmt:message key="msIntMustBe"/>"--%>
    <%--var msgPotinInfo = "<fmt:message key="msgPotinInfo"/>"--%>
    <%--var sameNewOldPwd="<fmt:message key="sameNewOldPwd"/>";--%>
    <%--var notSameNewPwd="<fmt:message key="notSameNewPwd"/>";--%>
</script>

<!-- css and javascript -->
<link rel="stylesheet" href="<%=contextPath %>/css/bootstrap.css">
<link rel="stylesheet" href="<%=contextPath %>/css/bootstrap-responsive.css">
<link rel="stylesheet" href="<%=contextPath %>/css/font-awesome.min.css" />
<link rel="stylesheet" href="<%=contextPath %>/css/jfstyle.css">

<script src="<%=contextPath %>/js/jquery.js"></script>
<%--<script src="<%=contextPath %>/js/less.js"></script>--%>
<script src="<%=contextPath %>/js/bootstrap.js"></script>
<script src="<%=contextPath %>/js/jquery.validate.min.js"></script>

<link rel="stylesheet" href="<%=contextPath %>/css/jfstyle.css">
<script src="<%=contextPath %>/js/jambo/jframe.js"></script>
<script src="<%=contextPath %>/js/jambo/messages.js"></script>

