<%@ page import="com.jambo.jop.security.common.SecurityUtils" %>
<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="../../inc/contentheadinc.jsp" %>
<html>
<head>
    <title>crsf测试页</title>
</head>
<body>

<form action="/crsfAction/test.do" id="formItem" name="formItem" method="post" >
    <%
        //在第一次进入页面时,应该找个地方先生成一个token,比如在登录的Action里生成,不要象范例在这样的页面里生成
        String token = SecurityUtils.getSessionToken(request);
        if (token == null) {
            token = SecurityUtils.genSessionToken(request);
        }
    %>

    <input type="hidden" name="crsf_token" value="<%=token%>"/>

    <div class="table_div">
        <table class="top_table" border=0>
            <tr>
                <td width="500" class="AreaName" align="left" valign=middle>CRSF测试页,打开CRSF的TOKEN检查后,只有这个页面能用</td>
                <td>
                    <table width="100%" border="0" cellspacing="0" cellpadding="0" ID="Table3">
                        <tr>
                            <td align=right>
                            </td>
                        </tr>
                    </table></td>
            </tr>
        </table>
    </div>
    <div class="table_div">
        <table class="form_table">
            <tr>
                <td width="20%" align="right" ><div class="field-require">测试内容:</div></td>
                <td width="75%" align="left" class="form_table_left">
                    <%
                        String msg = request.getParameter("msg");
                    %>
                    <input cssStyle="form_input_1x" id="msg" name="msg" value="<%=msg%>"/>
                </td>
            </tr>
        </table>
    </div>

    <div class="table_div">
        <table class="table_button_list">
            <tr>
                <td width="100%" class="form_table_center">
                    <input type="submit"  name="btnSave" value="提交" class="submit"/>
                </td>
            </tr>
        </table>
    </div>

</form>
</body>
</html>