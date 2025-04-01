<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <style>
        .error { color: red; }
        form { max-width: 400px; margin: 0 auto; }
    </style>
</head>
<body>
    <h1>Login</h1>

    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>

    <form method="post" action="/auth/login">
        <input type="hidden" name="redirect" value="${param.redirect}">

        <div>
            <label>Username:
                <input type="text" name="username" required>
            </label>
        </div>

        <div>
            <label>Password:
                <input type="password" name="password" required>
            </label>
        </div>

        <button type="submit">Login</button>
    </form>

    <p>Don't have an account? <a href="/auth/register">Register</a></p>
</body>
</html>