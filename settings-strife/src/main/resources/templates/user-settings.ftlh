<!DOCTYPE html>
<html>
<head>
    <title>
        Strife
    </title>
</head>
<body>
<h1>Strife</h1>
<h3>Info about ${user.name}</h3>
<b>Phone number: </b> ${user.phoneNumber}<br>
<b>Email: </b> ${user.email}<br>

<h4>Edit User Settings</h4>
<form action="/settings/user/${user.id}" method="POST">
    <label for="username">Username:</label><br>
    <input type="text" id="username" name="username" value="${user.name}"><br>

    <label for="phoneNumber">Phone Number:</label><br>
    <input type="text" id="phoneNumber" name="phoneNumber" value="${user.phoneNumber}"><br>

    <label for="email">Email:</label><br>
    <input type="email" id="email" name="email" value="${user.email}"><br>

    <input type="submit" value="Update Settings">
</form>

<#if errorMessage??>
    <h2 style="color: red">${errorMessage}</h2>
</#if>

</body>
</html>
