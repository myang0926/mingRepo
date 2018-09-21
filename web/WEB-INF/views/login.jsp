<%@ taglib uri="http://www.springframework.org/tags/form" 	prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" 			prefix="c" %>
<c:set var="CTX_PATH" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<!--[if lt IE 7]> <html class="lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]> <html class="lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]> <html class="lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html lang="en"> <!--<![endif]-->
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>Login Form</title>
    <link rel="stylesheet" href="css/style.css">
    <!--[if lt IE 9]><script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script><![endif]-->
</head>
<body>
<section class="container">
    <div class="login">
        <h1>Login to Web App</h1>
        <form:form autocomplete="false" commandName="loginDto" method="post" action="${CTX_PATH}/login">
            <table cellpadding="3">
                <c:if test="${! empty loginFailed}">
                    <tr>
                        <td>
                            <span class="formError">${loginFailed}</span>
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td>
                        <form:label path="username">Email: </form:label>
                        <form:errors path="username" cssClass="formError" /><br />
                        <form:input path="username" style="width:300px;" />
                    </td>
                </tr>
                <tr>
                    <td>
                        <form:label path="password">Password: </form:label>
                        <form:errors path="password" cssClass="formError" /><br />
                        <form:password path="password" style="width:300px;" />
                    </td>
                </tr>
                <tr>
                    <td>
                        <a href="${CTX_PATH}/passwordreset" style="color:#016798;font-size:80%">Forgot Your Password?</a>
                        <input type="submit" name="login" value="Log in" style="float:right;" />
                    </td>
                </tr>
            </table>
        </form:form>
    </div>

    <div class="login-help">
        <p>Forgot your password? <a href="index.html">Click here to reset it</a>.</p>
    </div>
</section>

<section class="about">
    <p class="about-links">
        <a href="http://www.cssflow.com/snippets/login-form" target="_parent">View Article</a>
        <a href="http://www.cssflow.com/snippets/login-form.zip" target="_parent">Download</a>
    </p>
    <p class="about-author">
        &copy; 2012&ndash;2013 <a href="http://thibaut.me" target="_blank">Thibaut Courouble</a> -
        <a href="http://www.cssflow.com/mit-license" target="_blank">MIT License</a><br>
        Original PSD by <a href="http://www.premiumpixels.com/freebies/clean-simple-login-form-psd/" target="_blank">Orman Clark</a>
</section>
</body>
</html>