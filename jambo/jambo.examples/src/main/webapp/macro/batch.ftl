<!--导入对话框-->
<@ms.modal modalName="importModal" title="批量数据导入!">
    <@ms.modalBody>
        <@ms.form name="importForm" isvalidation=true class="form-inline">
            <@ms.row>
                <@ms.col size="3" style="line-height: 30px;text-align: right;">
                <b>文件上传:</b>
                </@ms.col>

                <@ms.col size="9">
                    <@uploadFile path="upload/batchUploadFile/" inputName="appFile" size="1" filetype="csv" msg="只能上传1M以下的文件."  maxSize="1" callBack="setUploadFileUrl" isRename="true"/>
                </@ms.col>
            </@ms.row>
            <@ms.row>
                <@ms.col size="3" style="line-height: 30px;text-align: right;">
                <b>文件名称:</b>
                </@ms.col>
                <@ms.col size="9">
                    <@ms.text name="uploadFilename" id="uploadFilename" readonly="true" style="width:100%"/>
                </@ms.col>
            </@ms.row>
            <@ms.row>
                <@ms.col size="3" style="line-height: 30px;text-align: right;">
                <b>处理日志:</b>
                </@ms.col>
                <@ms.col size="9" >
                <div id="lognameTag">
                    <a id="importLogname" href="#">点击查看处理日志或右键另存</a>
                </div>
                </@ms.col>
            </@ms.row>
            <@hidden name="processorClass" value="instBiz" />
        </@ms.form>
    </@ms.modalBody>
    <@ms.modalButton>
        <@ms.button class="btn btn-primary" id="importOKButton" value="导入"/>
        <@ms.button class="btn btn-primary" id="importCloseButton" value="关闭"/>
    </@ms.modalButton>
</@ms.modal>

<script>
    $(function(){
        //点击导入对话框中的导入按钮
        $("#importOKButton").click(function() {
            $("#importOKButton").addClass("disabled");
            var formData = $("#importForm").serialize();
            $.ajax({
                type: "post",
                dataType: "json",
                url:  "${base}/manager/batch/import.do",
                data: formData,
                success: function(msg){
                    if(msg.processStatus == "0") {
                        alert("导入成功！");
                    } else alert("导入时可能出现错误，请查看日志文件.");
                    $("#lognameTag").show();
                    $("#importLogname").attr("href", msg.processResult);
                }
            });
        });

        //点击导入对话框中的关闭按钮，关闭模态框
        $("#importCloseButton").click(function() {
            $(".importModal").modal("hide");
        })
    });

    function setUploadFileUrl(e){
        if (e<0) {
        } else {
            var fileUrl = e;
            $("#uploadFilename").attr("value", fileUrl);
            $("#importOKButton").removeClass("disabled");
        }
    }

    //点击导出按钮,保存导出的数据
    function exportToFile(btn, clsname, searchform){
        btn.addClass("disabled");
        var formData = searchform.serialize();
        $.ajax({
            type: "post",
            dataType: "json",
            url:  "${base}/manager/batch/export.do?processorClass=" + clsname,
            data: formData,
            success: function(msg){
                if(msg.processStatus == "0") {
                    alert("导出成功！");
                    window.open (msg.processResult);
                } else alert("导出时出现错误.");
            }
        });
        btn.removeClass("disabled");
    };

    //点击导入按钮,打开导入对话框
    function showImportModal(clsname){
        $("#processorClass").attr("value", clsname)
        $("#lognameTag").hide();
        $("#importOKButton").addClass("disabled");
        $("#uploadFilename").attr("value", "");
        $(".importModal").modal();
    };

</script>