<#--
<input type="selector"/>
-->
<#macro selector
list value="" multiple=false  listKey="" listValue="" listDeep=""  default="请选择"
label="" validation=""    colon=":" hasColon="true" readonly="" def="" condition=""
id="" name="" class="" style="" size="" title="" disabled=false
labelStyle="" onchange=""
>
<div class="form-group">
    <#include "comm/labelinc.ftl"/><#rt/>
    <#--<div style="height:auto">-->
        <select<#rt/>
            <#if id!=""> id="${id}"</#if><#rt/>
            <#if multiple>multiple="multiple"</#if><#rt/>
            <#if readonly!=""> readonly="${readonly}"</#if><#rt/>
            <#if onchange!=""> onchange="${onchange}"</#if><#rt/>
            <#include "comm/attributeinc.ftl"/><#rt/>
                ><#rt/>
            <#if value?string=="">
                <option value="">${default}</option>
            </#if>
            <#if list?is_hash>
                <#list list?keys as key>
                    <option value="${key}" <#if key?string==value?string> selected="selected"</#if>>${list[key]}</option><#rt/>
                </#list>
            <#elseif list?is_sequence || list?eval?is_sequence>
                <#assign tempList=[]/>
                <#if list?is_string>
                    <#assign tempList=list?eval/>
                </#if>
                <#if listKey!="" && listValue!="">
                    <#if listDeep!="" && tempList?size gt 0><#local origDeep=tempList[0][listDeep]+1/></#if>
                    <#list tempList as item>
                        <option value="${item[listKey]}"<#if item[listKey]?string==value?string> selected="selected"</#if>><#if listDeep!="" && item[listDeep] gte origDeep><#list origDeep..item[listDeep] as i>&nbsp;&nbsp;</#list>></#if>${item[listValue]!}</option><#rt/>
                    </#list>
                <#else>
                    <#list list as item>
                        <option value="${item}"<#if item==value> selected="selected"</#if>>${item}</option><#rt/>
                    </#list>
                </#if>
            </#if>
        </select>
    <#--</div>-->
</div>
<script>
    $(function(){
        var ${name}DBValue ="${value}";
        var ${name}Def = encodeURIComponent("${def}")
        var ${name}requrl = "${contextPath}/code2name/selector/qrymap.do?def=" + ${name}Def + "&condition=${condition}";
        //页面加载列表
        $.ajax({
            type: "post",
            dataType: "json",
            url: ${name}requrl,
            success: function(msg){
                $("#${name}").html("");
                $("#${name}").append("<option value=''>请选择</option>");
                for (var key in msg) {
                    if(key == ${name}DBValue) {
                        $("#${name}").append("<option value='" + key + "' selected>"+msg[key]+"</option>")
                    }else{
                        $("#${name}").append("<option value='" + key + "'>"+msg[key]+"</option>")
                    }
                }
            }
        });
    });
</script>
</#macro>

<#--
<input type="picker"/>
-->
<#macro picker  id=""    name=""    value=""	label=""	title="" def="" condition=""
 	class="form-control" 	style=""	readonly=""	disabled=false	maxlength=""	minlength=""	
 	validation="" size=""	 placeholder="" groupClass="form-group" onclick="" onchange="" labelStyle="">
<div class="${groupClass}">
	<#--用来存放KEY-->
    <input type="hidden" <#rt/>
		<#if id!=""> id="${id}"</#if><#rt/>
		<#if name!=""> name="${name}"</#if><#rt/>
		<#if value?? && value?string!=""> value="${value?html}"</#if><#rt/>
        /><#rt/>

    <input type="hidden" <#rt/>
		<#if id!=""> id="${id}_condition"</#if><#rt/>
		<#if name!=""> name="${name}_condition"</#if><#rt/>
		<#if value?? && value?string!=""> value="${condition?html}"</#if><#rt/>
        /><#rt/>

	<#--用来显示NAME-->
	<#include "comm/labelinc.ftl"/><#rt/>
		<input type="text" <#rt/>
		<#--<#include "common-attributeinc2.ftl" /><#rt/>-->
		<#if id!=""> id="${id}_text"</#if><#rt/>
		<#if name!=""> name="${name}_text"</#if><#rt/>
		<#if maxlength!=""> maxlength="${maxlength}"</#if><#rt/>
		readonly="true"<#rt/>
		<#if value?? && value?string!=""> value="${value?html}"</#if><#rt/>
		<#if  onchange?? && onchange!=""> onchange="${onchange}"</#if><#rt/>
		<#if class!=""> class="${class}"</#if><#rt/>
		<#if style!=""> style="${style}"</#if><#rt/>
		<#if  onclick?? && onclick!=""> onclick="${onclick}"</#if><#rt/>
		/><#rt/>

        <#--//选择按钮-->
    <button type="button"<#rt/>
		<#if id!=""> id="${id}_button"</#if><#rt/>
		<#if name!=""> name="${name}_button"</#if><#rt/>
		<#if readonly!=""> disabled="${readonly}"</#if><#rt/>

		<#if btnStyle?? && btnStyle!=""> style="${btnStyle?default('')}"<#rt/>
		<#else>style="width: 34px;height: 33px;"<#rt/></#if>
		<#if btnClass?? && btnClass!=""> class="${btnClass?default('')}"<#rt/>
		<#else>class="btn btn-default btn-sm"<#rt/></#if>
		onclick="pickFor${name}('${name}')"<#rt/>
        ><#rt/>
        <span class="glyphicon glyphicon-align-justify"></span><#rt/>
	</button><#rt/>
</div>
<script>
    $(function() {
        //使用key查询当前对应的name
        var ${name}code = encodeURIComponent("${value}");
        var ${name}def = encodeURIComponent("${def}");
		var ${name}comName=  "${name}_text";
        var ${name}requrl = "${contextPath}/code2name/selector/code2name.do?def=" + ${name}def + "&code="+${name}code;
        //页面加载管理员列表
        $.ajax({
            type: "post",
            dataType: "json",
            url: ${name}requrl,
            success: function(msg) {
                //避免显示初始值0
                if (msg.namevalue != null && msg.namevalue.length != 0 && msg.namevalue != 0) {
                    $("input[name='" + ${name}comName + "']").attr("value", msg.namevalue);
                } else {
                    $("input[name='" + ${name}comName + "']").attr("value", "");
                }
            }
        });
    });

    //点击查询
    $("#code2nameSearchBtn").click(function(){
        $("#code2nameSearchBtn").attr("disabled", true);
        refreshCode2nameList();
		$("#code2nameSearchBtn").attr("disabled", false);
    });

    function selectCode2nameValue(code,name){
        $("input[name='"+"${name}"+"']").attr("value", code);
        $("input[name='"+"${name}"+"_text']").attr("value", name);
        $(".pickerModal").modal('hide');
    }

    function refreshCode2nameList(){
        table = $("#code2nameZone");
		table.html("");
        var formData = $("#code2nameForm").serialize();
        $.ajax({
            type: "post",
            dataType: "json",
            url:  "${contextPath}/code2name/selector/qryList.do",
            data: formData,
            success: function(msg){
                if(msg.nodeLists) {
                    var shtml = "";
					for (i=0; i<msg.nodeLists.length; i++){
						node = msg.nodeLists[i];
                        shtml += "/<tr>";
                        shtml += "<td class='text-center' id='code'>" + node.code + "</td>";
                        shtml += "<td class='text-center' id='name'><a href=\"javascript:selectCode2nameValue(\'"+node.code+"\',\'"+node.name+"\');\">"+node.name+"</a></td>";
                        shtml += "<tr>";
                    }
                    table.append(shtml);
                }
            }
        });
	}

    function code2namePrevPage() {
        var pageno = parseInt($("input[name='code2namePageNo']").val());
		if (pageno > 1){
            $("input[name='code2namePageNo']").attr("value",pageno - 1);
            refreshCode2nameList();
		}
    }

    function code2nameNextPage() {
        var pageno = parseInt($("input[name='code2namePageNo']").val());
        $("input[name='code2namePageNo']").attr("value",pageno + 1);
		refreshCode2nameList();
    }

	function pickFor${name}(name) {
        $("input[name='code2nameQryGroup']").attr("value","${def}");
        var condition =   $("input[name='"+"${name}"+"_condition']").val();
        $("input[name='code2nameCondition']").attr("value", condition);
        refreshCode2nameList();
        $(".pickerModal").modal();//打开该模态框
    }
</script>
</#macro>

<!--数据字典弹出对话框-->
<@modal modalName="pickerModal" style="width:80%" title="选择对话框">
	<@modalBody >
		<@panelNav>
			<@form isvalidation=true name="code2nameForm" class="form-inline" action="">
				<@row>
					<@col size="5">
						<@text label="编号" name="code2nameQryCode" id="qryCode" title="请输入编号"  placeholder="请输入编号" value="${code2nameQryCode?default('')}"   />
					</@col>
					<@col size="5">
						<@text label="名称" name="code2nameQryName" id="qryName" title="请输入名称"  placeholder="请输入名称" value="${code2nameQryName?default('')}"   />
					</@col>
					<@button  id="code2nameSearchBtn" value="查询"/>
				</@row>
				<@hidden name="code2nameQryGroup" value="${def?default('')}" />
				<@hidden name="code2namePageNo" value="1" />
				<@hidden name="code2nameCondition" value="" />
			</@form>
		</@panelNav>
		<@contentPanel style="padding-bottom: 0px;">
			<@table head=['编号','名称']  id="code2nameZone" style="margin-bottom: 0px;">
                <tr>
                    <td colspan="100%" class="text-center">
						<@nodata content="没有数据" />
                    </td>
                </tr>
			</@table>

            <nav class="pageNav" style="height:44px;margin-bottom: 0px;">
                <ul class="pagination pull-right" style="margin-top: 5px;">
                    <li class="prev"><a href="javascript:code2namePrevPage()" target="_self"><i class="fa fa-angle-left">上一页</i></a></li>
                    <li class="next"><a href="javascript:code2nameNextPage()" target="_self"><i class="fa fa-angle-right">下一页</i></a></li>
                </ul>
            </nav>
		</@contentPanel>
	</@modalBody>
</@modal>

