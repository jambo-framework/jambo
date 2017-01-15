<#include "/inc/headinc.ftl"/>
<html>
<head>
    <title>Jambo Framework</title>
    <link rel="stylesheet" href="${contextPath}/css/login.css">
</head>
<body class='login_body'>
    <div class="wrap">
        <h2>Please Login</h2>
        <h4>Jambo Framework</h4>
        <form action="${contextPath}/system/loginAction/login.do"  autocomplete="off" method="post" class="validate">
            <#if ActionInfo??>
                <div class="alert alert-error">
                    <strong>Error!</strong>${ActionInfo.message?default('')}
                </div>
            </#if>
            <div class="login">
                <#--<@jf.text label="${message('userName')}:" name="oprcode" id="oprcode" groupClass="email" class="user-input" title="请输入${message('userName')}"  placeholder="请输入${message('userName')}" value="${oprcode?default('')}"   />-->
                <#--<@jf.text label="${message('password')}:" name="password" id="password" groupClass="pw" class="user-input" title="请输入${message('password')}"  placeholder="请输入${message('password')}" value="${password?default('')}"   />-->

                <div class="email">
                    <label for="oprcode"><@message key="userName"/>：</label>
                    <div class="user-input controls">
                        <div class="input-group control-group">
                            <span class="input-group-addon glyphicon glyphicon-user"></span>
                            <input type="text" id="oprcode" name="oprcode" required>
                        </div>
                    </div>
                </div>

                <div class="pw">
                    <label for="password"><@message key="password"/>：</label>
                    <div class="pw-input controls">
                        <div class="input-group control-group">
                            <span class="input-group-addon glyphicon glyphicon-lock"></span>
                            <input type="password" id="password" name="password" required>
                        </div>
                    </div>
                </div>
            </div>

            <div class="submit">
                <a href="#">重置密码?</a>
                <button class="btn btn-red5"><@message key="button_login"/></button>
            </div>
        </form>
    </div>
</body>
</html>