<%@ page language="java" contentType="text/html;charset=utf-8"%>
<html>
<head>
    <%@ include file="../../inc/listheadinc.jsp" %>

    <fmt:setBundle basename="i18n.example.Example1Action" var="example1"/>

    <title><fmt:message key="titleList"  bundle="${example1}"/></title>

</head>

<body>
<div class="content">
    <div class="row-fluid">
        <form id="formList" name="formList" action="<%=contextPath %>/example/example1/example1Action/list.do" class="form-search">
            <div class="row-fluid force-margin">
                <%//下面的控件给Action提供数据，用来分页%>
                <form:hidden path="param._orderby"/>
                <form:hidden path="param._desc"/>
                <form:hidden path="param._pageno"/>
                <form:hidden path="param._pagesize"/>
                <input type="hidden" name="_rowcount" value="${dp.rowCount}"/>

                <div class="box">
                    <div class="box-head">
                        <h3><fmt:message key="list"  bundle="${example1}"/></h3>
                        <div class="actions">
                            <ul>
                                <li>
                                    <a href="javascript:void(0);" onclick="doNew('/example/example1/example1Action/add.do')" class="btn btn-square">
                                        <fmt:message key='button_new'/></a>
                                </li>
                                <li>
                                    <a href="javascript:void(0);" onclick="doDelete('/example/example1/example1Action/delete.do')" class="btn btn-square">
                                        <fmt:message key='button_delete'/></a>
                                </li>
                                <li>
                                    <a href="javascript:void(0);" onclick="doQuery();" name="btnQuery" class="btn btn-square"><fmt:message key='button_search'/></a>
                                </li>
                            </ul>
                        </div>
                    </div>

                    <%@ include file="../../inc/alertinc.jsp" %>

                    <div class="box-qrytext">
                        <div class="row-fluid force-margin">
                            <div class="span5">
                                <label class="control-label"><fmt:message key="companyname" bundle="${example1}"/>:</label>
                                <form:input path="param._sk_companyname" />
                            </div>
                            <div class="span5">
                                <label class="control-label"><fmt:message key="shortname" bundle="${example1}"/>:</label>
                                <form:input path="param._sk_shortname" />
                            </div>
                        </div>
                    </div>

                    <div class="box-content box-nomargin">
                          <table class='table table-striped table-bordered'>
                            <thead>
                                <tr>
                                    <th class='table-checkbox'><input type="checkbox" class='sel_all'></th>
                                    <th>id</th>
                                    <th><fmt:message key="companyname" bundle="${example1}"/></th>
                                    <th><fmt:message key="companyname" bundle="${example1}"/>(使用code2Name组件转换)</th>
                                    <th><fmt:message key="shortname" bundle="${example1}"/></th>
                                    <th><fmt:message key="address" bundle="${example1}"/></th>
                                    <th><fmt:message key="account" bundle="${example1}"/></th>
                                    <th><fmt:message key="state" bundle="${example1}"/></th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${dp.datas}" var="data">
                                    <tr>
                                        <td class='table-checkbox'><input type="checkbox" class='selectable-checkbox'name="_selectitem" value="${data.id}" ></td>
                                        <td>${data.id}</td>
                                        <td><a href="<%=contextPath %>/example/example1/example1Action/edit.do?_pk=${data.id}">${data.companyname}</a></td>
                                        <td><j:code2Name definition="#COMPANY" code="${data.id}" /></td>
                                        <td>${data.shortname}</td>
                                        <td>${data.address}</td>
                                        <td>${data.account}</td>
                                        <td><j:code2Name definition="COMPANY_STATE" code="${data.state}" /></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                          </table>

                        <%@ include file="../../inc/pagenav.jsp"%>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>
