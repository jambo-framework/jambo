<%@ include file="headinc.jsp" %>
<script type="text/javascript" src="<%=contextPath %>/js/jambo/list.js"></script>
<script type="text/javascript" language="javascript">
    var msgNoSelected="<fmt:message key="msgNoSelected"/>"
    var msgConfirmDelete="<fmt:message key="msgConfirmDelete"/>"
</script>
<%--配合分页标签调用ajax时用--%>
<%String ajaxurl="";%>
