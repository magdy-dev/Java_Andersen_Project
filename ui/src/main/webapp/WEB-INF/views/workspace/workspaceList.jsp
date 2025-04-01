<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Workspace List</title>
    <style>
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
    </style>
</head>
<body>
    <h1>Available Workspaces</h1>
    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Type</th>
                <th>Capacity</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${workspaces}" var="workspace">
                <tr>
                    <td>${workspace.id}</td>
                    <td>${workspace.name}</td>
                    <td>${workspace.type}</td>
                    <td>${workspace.capacity}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>