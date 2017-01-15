<#include "/inc/listheadinc.ftl"/>
<html>
<head>
    <#assign example1="example.Example1Action">
    <title><@message key="titleList" bundle="${example1}"/></title>

    <#--另外一种使用多国语言的写法-->
    <#--<title>${message('titleList','example.Example1Action')}</title>-->
</head>

<body>
<div class="content">
    <div class="row-fluid">
        <form id="formList" name="formList" action="${contextPath}/example/example1/example1Action/list.do" class="form-search">
            <div class="row-fluid force-margin">
                <#--下面的控件给Action提供数据，用来分页-->
                <#include "/inc/pageinc.ftl"/>

                <#--<form:hidden path="param._orderby"/>-->
                <#--<form:hidden path="param._desc"/>-->
                <#--<form:hidden path="param._pageno"/>-->
                <#--<form:hidden path="param._pagesize"/>-->
                <#--<input type="hidden" name="_rowcount" value="${dp.rowCount}"/>-->

                <div class="box">
                    <div class="box-head">
                        <h3><b><@message key="list" bundle="${example1}"/></b></h3>
                        <div class="actions">
                            <ul>
                                <li>
                                    <a href="javascript:void(0);" onclick="doNew('/example/example1/example1Action/add.do')" class="btn btn-default">
                                        <@message key='button_new'/></a>
                                </li>
                                <li>
                                    <a href="javascript:void(0);" onclick="doDelete('/example/example1/example1Action/delete.do')" class="btn btn-default">
                                        <@message key='button_delete'/></a>
                                </li>
                                <li>
                                    <a href="javascript:void(0);" onclick="doQuery();" name="btnQuery" class="btn btn-default"><@message key='button_search'/></a>
                                </li>
                            </ul>
                        </div>
                    </div>

                    <#include "/inc/alertinc.ftl"/>

                    <div class="box-qrytext">
                        <@jf.row>
                            <@jf.col size="6">
                                <@jf.text label="${message('companyname', '${example1}')}:" name="_sk_companyname" id="_sk_companyname" class="user-input" value="${param._sk_companyname?default('')}"/>
                            </@jf.col>
                            <@jf.col size="6">
                                <@jf.text label="${message('shortname', '${example1}')}:" name="_sk_shortname" id="_sk_shortname" class="user-input" value="${param._sk_shortname?default('')}"/>
                            </@jf.col>
                        </@jf.row>
                        <div class="row-fluid force-margin">
                         <#--<div class="span5">-->
                                <#--<label class="control-label"><@message key="companyname" bundle="${example1}"/>:</label>-->
                                <#--<form:input path="param._sk_companyname" />-->
                            <#--</div>-->
                            <#--<div class="span5">-->
                                <#--<label class="control-label"><@message key="shortname" bundle="${example1}"/>:</label>-->
                                <#--<form:input path="param._sk_shortname" />-->
                            <#--</div>-->
                        </div>
                    </div>

                    <@jf.table head=["<input type='checkbox' class='sel_all'>","id",
                        "${message('companyname','${example1}')}",
                        "${message('companyname','${example1}')}(使用code2Name组件转换)",
                        "${message('shortname','${example1}')}",
                        "${message('address','${example1}')}",
                        "${message('account','${example1}')}",
                        "${message('state','${example1}')}"
                    ]>
                        <#if dp?has_content>
                            <#list dp.datas as data>
                                <tr>
                                    <td class='table-checkbox'><input type="checkbox" class='selectable-checkbox'name="_selectitem" value="${data.id?c?default(0)}" ></td>
                                    <td class="text-center">${data.id?c?default(0)}</td>
                                    <td class="text-center"><a href="${contextPath}/example/example1/example1Action/edit.do?_pk=${data.id?c?default(0)}">${data.companyname?default("")}</a></td>
                                    <td class="text-center"><@code2Name definition="#COMPANY" code="${data.id?c?default(0)}"/></td>
                                    <td class="text-center">${data.shortname?default("")}</td>
                                    <td class="text-center">${data.address?default("")}</td>
                                    <td class="text-center">${data.account?default("")}</td>
                                    <td class="text-center"><@code2Name definition="COMPANY_STATE" code="${data.state?default('')}"/></td>
                                </tr>
                            </#list>
                        <#else>
                            <tr>
                                <td colspan="100%" class="text-center">
                                    <@jf.nodata content="没有查询到数据！" />
                                </td>
                            </tr>
                        </#if>
                    </@jf.table>
                    <#include "/inc/pagenav.ftl"/>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>
