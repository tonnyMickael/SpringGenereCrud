<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>Liste</title>
	</head>
	<body>
		<div id="header">
			<h1>Liste person</h1>
		</div>
		
		<div id="content">
			<table border="1">
				<tr>
					<th>id</th>
					<th>firstname</th>
					<th>lastname</th>
					<th>birthday</th>
					<th>cin</th>

					<th> action </th>
				</tr>
				<#list persons as person>
					<tr>
				     	<td>${person.getId()}</td>
				     	<td>${person.getFirstname()}</td>
				     	<td>${person.getLastname()}</td>
				     	<td>${person.getBirthday()}</td>
				     	<td>${person.getCin()}</td>

						<td>                 
							<div>
								<a href="${'/person/detail/' + person.getId() }">Détail</a> |
								<a href="${'/person/update/' + person.getId() }">Modifier</a> |
								<a href="${'/person/delete/' + person.getId() }">Supprimer</a>
							</div>
						</td>
					</tr>
				</#list>
			</table>
	        <div>
	            <nobr>
	                <#if hasPrev><a href="${'list?page=' + prev}">Prev</a>&nbsp;&nbsp;&nbsp;</#if>
	                <#if hasNext><a href="${'list?page=' + next}">Next</a></#if>
	            </nobr>
	        </div>
		</div>
	</body>
</html>
