<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="s"%>
<!DOCTYPE html>
<html>
<head>
    <title>Jambo Framework</title>
    <meta name="description" content="">

    <%@ include file="inc/headinc.jsp" %>
    <link rel="stylesheet" href="<%=contextPath %>/css/login.css">
</head>
<body class='login_body'>
    <div class="wrap">
        <h2>Please Login</h2>
        <h4>Jambo Framework</h4>
        <form action="<%=contextPath %>/system/loginAction/login.do"  autocomplete="off" method="post" class="validate">
            <c:if test="${not empty ActionInfo.message}">
                <div class="alert alert-error">
                    <strong>Error!</strong>${ActionInfo.message}
                </div>
            </c:if>
            <div class="login">
                <div class="email">
                    <label for="oprcode"><fmt:message key="userName"/>：</label>
                    <div class="user-input">
                        <div class="control-group">
                            <div class="input-prepend"><span class="add-on"><i class="icon-user"></i></span>
                                <input type="text" id="oprcode" name="oprcode" class="{required:true}">
                            </div>
                        </div>
                    </div>
                </div>

                <div class="pw">
                    <label for="password"><fmt:message key="password"/>：</label>
                    <div class="pw-input">
                        <div class="control-group">
                            <div class="input-prepend"><span class="add-on"><i class="icon-lock"></i></span>
                                <input type="password" id="password" name="password" class='{required:true}'>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="submit">
                <a href="#">重置密码?</a>
                <button class="btn btn-red5"><fmt:message key="button_login"/></button>
            </div>
        </form>
    </div>
</body>
</html>