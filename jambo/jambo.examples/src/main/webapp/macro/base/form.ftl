<#--
<form></form>
name:表单名称
id:表单id
action:提交地址
method:提交方式
-->
<#macro form  name id="" target=""
	 action="" method="post"  enctype=""  
	 class="form-horizontal" style=""  
	 isvalidation=false
	>
<form<#rt/>
 role="form"<#rt/>
 method="${method}"<#rt/>
 action="${action}"<#rt/>
<#if target!="">target="${target}" <#else> target="_self"</#if><#rt/>

<#if id!=""> id="${id}"<#else> id="${name}"</#if><#rt/>
<#if enctype!=""> enctype="${enctype}"</#if><#rt/>
<#include "../comm/attributeinc.ftl"/><#rt/>
>
<#nested/><#rt/>
<#if isvalidation>
<script>
	$(function() {
				var id = "${name}";
				<#if id?? && id!="">id="${id}"</#if>
				$('#'+id).bootstrapValidator({
						feedbackIcons : {
							valid : 'glyphicon glyphicon-ok',
							invalid : 'glyphicon glyphicon-remove',
							validating : 'glyphicon glyphicon-refresh',
							autoFocus : true
						}
				});	
	})
</script>
</#if>
</form>
</#macro>

<#--整行-->
<#macro row>
<div class="row">
	<#nested/>
</div>
</#macro>

<#--整列-->
<#macro col size="1" style="">
<div class="col-md-${size} col-sm-${size}" style="${style}">
	<#nested/>
</div>
</#macro>

<#--提示没有数据-->
<#macro nodata content="none" style="text-align:center">
<p class="alert alert-info" role="alert" style="${style}">
${content}
</p>
</#macro>

