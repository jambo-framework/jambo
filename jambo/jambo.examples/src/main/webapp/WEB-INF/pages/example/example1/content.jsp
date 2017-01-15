<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="../../inc/contentheadinc.jsp" %>
<html>
<head>
    <fmt:setBundle basename="i18n.example.Example1Action" var="example1"/>
    <title><fmt:message key="titleUpdate"  bundle="${example1}"/></title>
    <script type="text/javascript">
        //进入页面时控制
        $(document).ready(function(){
            //控制页面元素是否可编辑
            $("#companyname").disable = ($("#cmd").val()!="NEW")
        });
    </script>
</head>
<body>
<div class="content">
    <div class="box">
        <div class="box-head">
            <h3><fmt:message key="content"  bundle="${example1}"/></h3>
        </div>
        <div class="box-content">

            <%@ include file="../../inc/alertinc.jsp" %>

            <%--class中包含validate，则会自动jquery --%>
            <form id="formItem" name="formItem" action="<%=contextPath %>/example/example1/example1Action/save.do" class="form-horizontal validate"  method="post" >

                <form:hidden path="form.id"/>
                <input type="hidden" name="cmd" value="${cmd}"/>

                <form:hidden path="param._orderby"/>
                <form:hidden path="param._desc"/>
                <form:hidden path="param._pageno"/>
                <form:hidden path="param._pagesize"/>
                <form:hidden path="param._sk_companyname"/>
                <form:hidden path="param._sk_shortname"/>

                <div class="row-fluid">

                    <div class="control-group">
                        <label for="companyname" class="control-label"><fmt:message key="companyname"  bundle="${example1}"/>:</label>
                        <div class="controls">
                            <form:input class="input-square required" path="form.companyname" maxlength="24"/>
                            <span style="color:red">*</span>
                        </div>
                    </div>

                    <div class="control-group">
                        <label for="shortname" class="control-label"><fmt:message key="shortname"  bundle="${example1}"/>:</label>
                        <div class="controls">
                            <form:input class="input-square" path="form.shortname" maxlength="24"/>
                        </div>
                    </div>

                    <div class="control-group">
                        <label for="address" class="control-label"><fmt:message key="address"  bundle="${example1}"/>:</label>
                        <div class="controls">
                            <form:input class="input-square" path="form.address" maxlength="24"/>
                        </div>
                    </div>

                    <div class="control-group">
                        <label for="account" class="control-label"><fmt:message key="account"  bundle="${example1}"/>:</label>
                        <div class="controls">
                            <form:input class="input-square" path="form.account" maxlength="24"/>
                        </div>
                    </div>

                    <div class="control-group">
                        <label for="state" class="control-label"><fmt:message key="state"  bundle="${example1}"/>:</label>
                        <div class="controls">
                            <j:selector name="state" definition="COMPANY_STATE" value="${form.state}"/>
                        </div>
                    </div>
                </div>

                <div class="form-actions">
                    <button class="btn btn-primary" type="submit" onclick="doSave('/example/example1/example1Action/save.do')">
                        <fmt:message key='button_save'/></button>
                    <input type="reset" class='btn btn-danger' value="<fmt:message key='button_reset'/>">
                    <a href="javascript:void(0);" onclick="doReturn('/example/example1/example1Action/list.do')" class="btn btn-info">
                        <fmt:message key='button_return'/></a>
                </div>
            </form>

        </div>
    </div>
</div>
</body>
</html>
