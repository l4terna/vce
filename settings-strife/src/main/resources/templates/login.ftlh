<!DOCTYPE html>
<html>
<head>
    <title>
        Strife
    </title>
</head>
<body>
<h1>Strife</h1>
<h4>Authorisation</h4>
<form action="/login" method="post">
    Email: <input type="email" name="username"><br><br>
    Password: <input type="password" name="password"><br><br>
    <input type="hidden" name="_csrf" value="${_csrf.token}">
    <input type="submit" value="Вход"/>
</form>
<a href="/registration">Registration</a>
</body>
</html>