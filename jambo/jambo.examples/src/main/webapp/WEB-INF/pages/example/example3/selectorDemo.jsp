<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="j" uri="/jop-tags" %>
<html>
	<head>
		<title>index</title>
        <%@ include file="../../inc/listheadinc.jsp" %>
        <%
            String orderId = "A02341";
            pageContext.setAttribute("orderId",orderId);
        %>
    </head>
    <body style="margin:0px; padding:0px;">
        <p/>
        <p/>
        <h3>&nbsp;&nbsp;&nbsp; Selector&#26631;&#31614;Demo</h3>
        <br/>
        固定参数下拉框：DEPART_TYPE_CODE: 绑定name属性取值	<j:selector name="param._orderby" definition="TARGET_SYSTEM"  value="'CICS_DEMO_SYS'"/> <p/>

        固定参数下拉框：COMPANY_STATE: 绑定name属性取值	<j:selector name="param._pageno" definition="COMPANY_STATE"  value="0"/> <p/>

        picker1：#COMPANY: 绑定name属性取值	<j:selector   name="_pageno" btnClass="btn btn_picker" definition="#COMPANY" /> <p/>

        picker2：#COMPANY	绑定value属性取值 <j:selector name="selector2" definition="#COMPANY"  btnClass="btn btn_picker"  value="12"/> <p/>

        picker3：#COMPANY	禁用 <j:selector  definition="#EMPLOYEE"  name="orderId" btnClass="btn btn_picker"  readonly="true"/> <p/>

        picker4：#COMPANY	指定dbFlag <j:selector name="selector4" definition="#COMPANY"  btnClass="btn btn_picker"  value="12" dbFlag="COMMON"/> <p/>
    </body>
</html>