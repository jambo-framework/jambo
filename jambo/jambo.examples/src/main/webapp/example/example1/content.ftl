<#include "/inc/contentheadinc.ftl"/>
<html>
<head>
    <#assign example1="example.Example1Action">
    <title><@message key="titleUpdate"  bundle="${example1}"/></title>
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
            <h3><b><@message key="content"  bundle="${example1}"/></b></h3>
        </div>
        <div class="box-content">

            <#include "/inc/alertinc.ftl"/>

            <#--class中包含validate，则会自动jquery -->
            <form id="formItem" name="formItem" action="${contextPath}/example/example1/example1Action/save.do" class="form-horizontal validate"  method="post" >

                <#--<form:hidden path="form.id"/>-->
                <#include "/inc/pageinc.ftl"/>

                <#--<form:hidden path="param._orderby"/>-->
                <#--<form:hidden path="param._desc"/>-->
                <#--<form:hidden path="param._pageno"/>-->
                <#--<form:hidden path="param._pagesize"/>-->
                <#--<form:hidden path="param._sk_companyname"/>-->
                <#--<form:hidden path="param._sk_shortname"/>-->

                <div class="row-fluid">

                    <@jf.text labelStyle="width: 10%;"  label="${message('companyname', '${example1}')}:" name="companyname" id="companyname" class="user-input" value="${form.companyname?default('')}"/>
                    <#--<div class="control-group">-->
                        <#--<label for="companyname" class="control-label"><@message key="companyname"  bundle="${example1}"/>:</label>-->
                        <#--<div class="controls">-->
                            <#--<form:input class="input-square required" path="form.companyname" maxlength="24"/>-->
                            <#--<span style="color:red">*</span>-->
                        <#--</div>-->
                    <#--</div>-->

                    <@jf.text labelStyle="width: 10%;"  label="${message('shortname', '${example1}')}:" name="shortname" id="shortname" class="user-input" value="${form.shortname?default('')}"/>
                    <#--<div class="control-group">-->
                        <#--<label for="shortname" class="control-label"><@message key="shortname"  bundle="${example1}"/>:</label>-->
                        <#--<div class="controls">-->
                            <#--<form:input class="input-square" path="form.shortname" maxlength="24"/>-->
                        <#--</div>-->
                    <#--</div>-->

                    <@jf.text labelStyle="width: 10%;"  label="${message('address', '${example1}')}:" name="address" id="address" class="user-input" value="${form.address?default('')}"/>
                    <#--<div class="control-group">-->
                        <#--<label for="address" class="control-label"><@message key="address"  bundle="${example1}"/>:</label>-->
                        <#--<div class="controls">-->
                            <#--<form:input class="input-square" path="form.address" maxlength="24"/>-->
                        <#--</div>-->
                    <#--</div>-->

                    <@jf.text labelStyle="width: 10%;"   label="${message('account', '${example1}')}:" name="account" id="account" class="user-input" value="${form.account?default('')}"/>
                    <#--<div class="control-group">-->
                        <#--<label for="account" class="control-label"><@message key="account"  bundle="${example1}"/>:</label>-->
                        <#--<div class="controls">-->
                            <#--<form:input class="input-square" path="form.account" maxlength="24"/>-->
                        <#--</div>-->
                    <#--</div>-->

                    <#--<@jf.text labelStyle="width: 10%;" label="${message('state', '${example1}')}:" name="state" id="state" class="user-input" value="${form.state?default('')}"/>-->
                    <#--<@selector labelStyle="width: 10%;" label="${message('state', '${example1}')}:" name="state" id="state" def="COMPANY_STATE" style="width: 60%;" list=["请选择"] value="${form.state?default('无')}"/>-->

                    <@jf.selector labelStyle="width: 10%;" label="${message('state', '${example1}')}:" name="state" id="state" def="COMPANY_STATE" style="width: 20%;" list=["请选择"] value="${form.state?default('无')}"/>

                    <#--<div class="control-group">-->
                        <#--<label for="state" class="control-label"><@message key="state"  bundle="${example1}"/>:</label>-->
                        <#--<div class="controls">-->
                            <#--<j:selector name="state" definition="COMPANY_STATE" value="${form.state}"/>-->
                        <#--</div>-->
                    <#--</div>-->
                </div>

                <div class="form-actions">
                    <button class="btn btn-primary" type="submit" onclick="doSave('/example/example1/example1Action/save.do')">
                        <@message key='button_save'/></button>
                    <input type="reset" class='btn btn-danger' value="<@message key='button_reset'/>">
                    <a href="javascript:void(0);" onclick="doReturn('/example/example1/example1Action/list.do')" class="btn btn-info">
                        <@message key='button_return'/></a>
                </div>
            </form>

        </div>
    </div>
</div>
</body>
</html>
