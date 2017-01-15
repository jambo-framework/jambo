<#--<base target="mainWindow"/>-->
<#setting url_escaping_charset='utf-8'>
<#assign bootstrap="bootstrap-3.3.6">
<#assign contextPath=request.getContextPath()>

<meta content="IE=edge" http-equiv="X-UA-Compatible"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0"/>
<#--<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>-->
<meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
<meta name="author" content="www.jambo-framework.com" />

<#--<script type="text/javascript" src="${contextPath}/js/jquery-1.10.2.min.js"></script>-->
<script type="text/javascript" src="${contextPath}/js/jquery-2.2.3.min.js"></script>

<link rel="stylesheet" type="text/css" href="${contextPath}/${bootstrap}/css/bootstrap.min.css" media="all"/>
<script type="text/javascript" src="${contextPath}/${bootstrap}/js/bootstrap.min.js"></script>

<script type="text/javascript" src="${contextPath}/js/bootstrap-switch.min.js"></script>
<link rel="stylesheet" type="text/css" href="${contextPath}/css/bootstrap-switch.css" media="all"/>

<link rel="stylesheet" type="text/css" href="${contextPath}/css/bootstrap-notify.css" media="all"/>

<#--jquery动画效果-->
<link rel="stylesheet" type="text/css" href="${contextPath}/css/animate.css" media="all"/>

<#--输入校验插件-->
<#--<link rel="stylesheet" type="text/css" href="${contextPath}/css/bootstrapValidator.css" media="all"/>-->
<#--<script type="text/javascript" src="${contextPath}/js/bootstrapValidator.js"></script>-->
<script type="text/javascript" src="${contextPath}/js/jquery.validate.min.js"></script>
<#--通过切换语言文件实现校验提示的多国语言-->
<script src="${contextPath}/js/i18n/messages_zh.js"></script>

<#--基于Bootstrap的事件通知jQuery插件-->
<#--<script type="text/javascript" src="${contextPath}/js/bootstrap-notify.js"></script>-->
<#--用js拼装html的模板插件-->
<#--<script type="text/javascript" src="${contextPath}/js/jquery.tmpl.min.js"></script>-->
<#--JavaScript 日期处理类库-->
<script type="text/javascript" src="${contextPath}/js/moment.js"></script>

<link rel="stylesheet" href="${contextPath}/css/jfstyle.css">
<script src="${contextPath}/js/jambo/jframe.js"></script>

<!--时间插件-->
<#--<link href="${contextPath}/css/daterangepicker.css" rel="stylesheet">-->
<#--<script type="text/javascript" src="${contextPath}/js/daterangepicker.js"></script>-->

<link rel="stylesheet" type="text/css" href="${contextPath}/css/bootstrap-datetimepicker.min.css" media="all"/>
<script type="text/javascript" src="${contextPath}/js/bootstrap-datetimepicker/bootstrap-datetimepicker.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="${contextPath}/js/bootstrap-datetimepicker/bootstrap-datetimepicker.fr.js" charset="UTF-8"></script>

<!----上传控件--->
<#--<script type="text/javascript" src="${contextPath}/js/swfupload/swfupload.js"></script>-->
<#--<script type="text/javascript" src="${contextPath}/js/swfupload/jquery.swfupload.js"></script>-->
<#--<script type="text/javascript" src="${contextPath}/js/swfupload/fileprogress.js"></script>-->

<!--建议手动加在语言，避免在ie下有时因为加载语言失败导致编辑器加载失败-->
<!--这里加载的语言文件会覆盖你在配置项目里添加的语言类型，比如你在配置项目里配置的是英文，这里加载的中文，那最后就是中文-->
<#--<script type="text/javascript" charset="utf-8" src="${contextPath}/ueditor1_3_6/lang/zh-cn/zh-cn.js"></script>-->
<#--<script type="text/javascript" charset="utf-8" src="${contextPath}/ueditor1_3_6/ueditor.config.msg.js"></script>-->
<#--<script type="text/javascript" charset="utf-8" src="${contextPath}/ueditor1_3_6/ueditor.all.js"></script>-->
<#--<script type="text/javascript" charset="utf-8" src="${contextPath}/ueditor1_3_6/ueditor.parse.js"></script>-->
<#--树形控件-->
<#--<link rel="stylesheet" type="text/css" href="${contextPath}/css/ztree.css" media="all"/>-->
<#--<script type="text/javascript" src="${contextPath}/js/jquery.ztree.all.js"></script>-->

<script>
    $(function() {
        //启用工具提示
        $("[data-toggle='tooltip']").tooltip();
        $("[data-toggle='popover']").popover({html: true});
    });

    var contextPath = "${contextPath}";
    <#--加载这些语言变量，是为了方便js实现多国语言-->
    var msgInputCorrectEmail = "<@message key="msgInputCorrectEmail"/>";
    var msgInput = "<@message key="msgInput"/>";
    var msgInputNotNull ="<@message key="msgInputNotNull"/>";
    var msgInvalidDate = "<@message key="msgInvalidDate"/>";
    var msgInvalidDateYyyymmdd = "<@message key="msgInvalidDateYyyymmdd"/>";
    var msgInvalidNumberFormat = "<@message key="msgInvalidNumberFormat"/>";
    var msgInvalidTime = "<@message key="msgInvalidTime"/>";
    var msgValidate_url = "<@message key='msgValidate_url'/>";
    var msgValidate_creditcard = "${message('msgValidate_creditcard')}";
    var msgValidate_equalTo = "${message('msgValidate_equalTo')}";
    var msgValidate_accept = "${message('msgValidate_accept')}";
    var msgValidate_maxlength = "${message('msgValidate_maxlength')}";
    var msgValidate_minlength = "${message('msgValidate_minlength')}";
    var msgValidate_rangelength = "${message('msgValidate_rangelength')}";
    var msgValidate_range = "${message('msgValidate_range')}";
    var msgValidate_max = "${message('msgValidate_max')}";
    var msgValidate_min = "${message('msgValidate_min')}";
</script>

