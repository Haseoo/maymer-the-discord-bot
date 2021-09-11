<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Mymes</title>
    <style>
        .container {
            max-width: 100vw;
            margin-left: 5%;
            margin-right: 5%;
            border: solid 1px gray;
            text-align: center;
        }

        img {
            max-width: 100%;
        }
    </style>
</head>
<body>
<c:forEach items="${memes}" var="entry">
    <div class="container">
        <h2>${entry.name} (${entry.count})</h2>
        <img src="${entry.url}" alt="${entry.name}"/>
    </div>
</c:forEach>
</body>
</html>
