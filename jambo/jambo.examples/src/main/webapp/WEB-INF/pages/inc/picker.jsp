<%@ page language="java" contentType="text/html;charset=utf-8" %>
<%@ include file="../inc/listheadinc.jsp" %>
<html>
<head>
    <title>selector</title>
</head>

<body style="padding: 1px 1px 1px 1px;">
<div class="content" style="padding: 1px 1px 1px 1px;">
    <div class="row-fluid">
        <form id="formList" name="formList" action="pickerAction/list.do" class="form-search" style="margin-bottom: 5px">
            <div class="row-fluid force-margin">
                    <%//下面的控件给Action提供数据，用来分页%>
                <form:hidden path="param._orderby"/>
                <form:hidden path="param._desc"/>
                <form:hidden path="param._pageno"/>
                <form:hidden path="param._pagesize"/>
                <form:hidden path="param.btnid"/>
                <input type="hidden" name="_rowcount" value="${dp.rowCount}"/>

                <input type="hidden" name="definition" value="${param.definition}"/>
                <!-- textfield name="condition" key="condition"/ -->
                <input type="hidden" name="dbFlag" value="${param.dbFlag}"/>

                <div class="box" style="margin-top: 10px">
                    <div class="box-head">
                        <h3><fmt:message key="title_selector"/></h3>

                        <div class="actions">
                            <ul>
                                <li>
                                    <a href="javascript:void(0);" onclick="doQuery();"
                                       name="btnQuery" class="btn btn-square"><fmt:message key='button_search'/></a>
                                </li>
                            </ul>
                        </div>
                    </div>

                    <%@ include file="alertinc.jsp" %>

                    <div class="box-qrytext">
                        <div class="row-fluid force-margin">
                            <div class="form-group">
                                <label for="code" class="control-label"><fmt:message key="msgCode"/>:</label>
                                <form:input path="param.code" class="input-square"/>

                                <label for="name" class="control-label"><fmt:message key="msgName"/>:</label>
                                <form:input path="param.name" class="input-square"/>
                            </div>
                        </div>
                    </div>

                    <div class="box-content box-nomargin">
                        <table class='table table-striped table-bordered'>
                            <thead>
                                <tr>
                                    <th><fmt:message key="msgCode"/></th>
                                    <th><fmt:message key="msgName"/></th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr align="center">
                                    <td><a href="javascript:selectCode('${param.btnid}', '' ,'');"><fmt:message key="msgNull"/></a></td>
                                    <td>&nbsp;</td>
                                </tr>
                                <c:forEach items="${dp.datas}" var="data">
                                <tr>
                                    <td><a href="javascript:selectCode('${param.btnid}', '${data.code}','${data.name}');">${data.code}</a></td>
                                    <td>${data.name}</td>
                                </tr>
                                </c:forEach>
                            </tbody>
                        </table>

                        <%@ include file="pagenav.jsp"%>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>
