<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>My Bookings</title>
    <style>
        table { border-collapse: collapse; width: 100%; }
        th, td { padding: 8px; text-align: left; border: 1px solid #ddd; }
        .success { color: green; }
        .error { color: red; }
    </style>
</head>
<body>
    <h1>My Bookings</h1>

    <c:if test="${not empty success}">
        <div class="success">${success}</div>
    </c:if>

    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>

    <table>
        <tr>
            <th>Workspace</th>
            <th>Start Time</th>
            <th>End Time</th>
            <th>Actions</th>
        </tr>
        <c:forEach items="${bookings}" var="booking">
            <tr>
                <td>${booking.workspace.name}</td>
                <td>${booking.startTime}</td>
                <td>${booking.endTime}</td>
                <td>
                    <form action="/bookings/cancel/${booking.id}" method="post">
                        <input type="hidden" name="userId" value="${booking.user.id}">
                        <button type="submit">Cancel</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>