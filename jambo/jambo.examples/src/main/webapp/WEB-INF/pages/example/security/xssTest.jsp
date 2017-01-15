<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="../../inc/contentheadinc.jsp" %>
<html>
<head>
    <title>xss测试页</title>
</head>
<body>

<form action="/redirect.do?url=example/security/xssTest" id="formItem" name="formItem" method="post" >
    <div class="table_div">
        <table class="top_table" border=0>
            <tr>
                <td width="210" class="AreaName" align="left" valign=middle>XSS测试页</td>
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
                测试脚本&quot;/&gt;&lt;script&gt;alert(20800)&lt;/script&gt;
            </tr>
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