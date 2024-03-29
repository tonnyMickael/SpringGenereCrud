<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>Modifier</title>
	</head>
	<body>
		<div id="header">
			<h1>modification region</h1>
		</div>
		
		<div id="content">
			<form name="region" action="${'/region/update/'+region.getId()}" method="post">
            		name:<input name="name" type="text" value="${region.getName()}"/><br>

			<input type="submit" value="Modifier">
            </form>
		</div>
	</body>
</html>
