<#--
<input type="checkbox"/>
-->
<#macro checkboxlist
	list listKey="" listValue="" valueList=[]
	label=""     colon=":" hasColon="true"
	id="" name="" class="" style="" size="" title="" disabled=false
	validation="" direction=true
	labelStyle="" allCheck=""
	>
<div class="form-group ms-form-group">	
	<#include "../comm/labelinc.ftl"/><#rt/>
	<div class="ms-form-control">
		<#if list?is_sequence>
			<#if listKey!="" && listValue!="">
				<#if allCheck != "">
					<label><input type="checkbox" id="allCheck" value="全选" data-original-title="选择所有" data-toggle="tooltip"/>全选</label>
				</#if>
				<#list list as item>
					<#local rkey=item[listKey]>
					<#local rvalue=item[listValue]>
					<#local index=item_index>
					<#local hasNext=item_has_next>
					<#include "checkboxlist-item.ftl"><#t/>
				</#list>
			<#else>
				<#if allCheck != "">
					<label><input type="checkbox" id="allCheck" value="全选" data-original-title="选择所有" data-toggle="tooltip"/>全选</label>
				</#if>
				<#list list as item>
					<#local rkey=item>
					<#local rvalue=item>
					<#local index=item_index>
					<#local hasNext=item_has_next>
					<#include "checkboxlist-item.ftl"><#t/>
				</#list>
			</#if>
		<#else>
			<#if allCheck != "">
				<label><input type="checkbox" id="allCheck" value="全选" data-original-title="选择所有" data-toggle="tooltip"/>全选</label>
			</#if>
			<#list list?keys as key>
				<#local rkey=key/>
				<#local rvalue=list[key]/>
				<#local index=key_index>
				<#local hasNext=key_has_next>
				<#include "checkboxlist-item.ftl"><#t/>
			</#list>
		</#if>
	</div>
</div>	
</#macro>