<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>Modifier</title>
	</head>
	<body>
		<div id="header">
			<h1>modification person</h1>
		</div>
		
		<div id="content">
			<form name="person" action="${'/person/update/'+person.getId()}" method="post">
            		firstname:<input name="firstname" type="text" value="${person.getFirstname()}"/><br>
		lastname:<input name="lastname" type="text" value="${person.getLastname()}"/><br>
		birthday:<input name="birthday" type="date" value="${person.getBirthday()}"/><br>
		cin:<input name="cin" type="text" value="${person.getCin()}"/><br>

			<input type="submit" value="Modifier">
            </form>
		</div>
	</body>
</html>
