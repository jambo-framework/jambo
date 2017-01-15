<#if form??>
    <input type="hidden" name="id" value="${form.id?default('')}"/>
</#if>
<#if cmd??>
    <input type="hidden" name="cmd" value="${cmd}"/>
</#if>

<#if param??>
    <@jf.hidden id="_orderby" name="_orderby" value="${param._orderby?default('')}"/>
    <@jf.hidden id="_desc" name="_desc" value="${param._desc?default('')}"/>
    <@jf.hidden id="_pageno" name="_pageno" value="${param._pageno?default('')}"/>
    <@jf.hidden id="_pagesize" name="_pagesize" value="${param._pagesize?default('')}"/>
</#if>

<#if dp??>
    <@jf.hidden id="_rowcount" name="_rowcount" value="${dp.rowCount?c?default(0)}"/>
</#if>
