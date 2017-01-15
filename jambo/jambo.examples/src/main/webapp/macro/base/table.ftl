<#--提示没有数据-->
<#macro nodata content="暂无数据" style="text-align:center">
<p class="alert alert-info" role="alert" style="${style}">
${content}
</p>
</#macro>


<#--表格列表-->
<#macro table head="" list="" id="" style="">
<table class="table table-hover" style="${style}">
    <!--表格栏目属性 开始-->
    <thead>
    <tr>
        <#list head as h>
            <th class="text-center">${h}</th>
        </#list>
    </tr>
    </thead>
    <!--表格栏目属性 结束-->
    <tbody id=${id}>
        <#nested/>
    </tbody>
</table>
</#macro>