<#-- 分割线-->
<#macro panelNav empty=
false>
<div class="ms-content-body-panel-nav"  <#if empty>style="  padding: 0;"</#if>>
    <#nested/>
</div>
</#macro>

<#macro contentPanel style="">
<div class="ms-content-body-panel" style="${style}">
    <#nested/>
</div>
</#macro>

<#--模态框按钮区域-->
<#macro modalButtonarea>
<div class="modal-footer">
    <#nested/>
</div>
</#macro>

<#--警告的模态框-->
<#macro warn  modalName>
<div id="warn${modalName}Dialog" class="modal fade  ${modalName}" tabindex="-1" data-focus-on="input:first">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title"  id="warn${modalName}Title">警告！</h4>
            </div>
            <div class="modal-body"  id="warn${modalName}Body">
                <#nested/>
            </div>
            <div class="modal-footer">
                <button type="button" data-dismiss="modal" class="btn btn-default">关闭</button>
                <button type="button"  id="warn${modalName}Ok" class="btn btn-primary right" >确定</button>
            </div>
        </div>
    </div>
</div>
</#macro>

<#--模块框-->
<#macro modal modalName title="标题" style="" resetFrom=true>
<div class="modal fade ${modalName}" id="${modalName}Dialog" >
    <div class="modal-dialog"  style="${style}">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="${modalName}Title">
                ${title}
                </h4>
            </div>
            <#nested/>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<script>

        <#if resetFrom>
        $(function(){
            $('#${modalName}Dialog').on('hide.bs.modal', function (event) {
                if ($("#${modalName}Dialog form").length>0) {
                    $("#${modalName}Dialog form").data('bootstrapValidator').resetForm(true);
                }
            })
        });
        </#if>

</script>
</#macro>

<#--模态框内容-->
<#macro modalBody>
<div class="modal-body">
    <#nested/>
</div>
</#macro>

<#--模态框按钮区域-->
<#macro modalButton>
<div class="modal-footer">
    <#nested/>
</div>
</#macro>