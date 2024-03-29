<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>Liste</title>
	</head>
	<body>
		<div id="header">
			<h1>Liste region</h1>
		</div>
		
		<div id="content">
			<table border="1">
				<tr>
					<th>id</th>
					<th>name</th>

					<th> action </th>
				</tr>
				<#list regions as region>
					<tr>
				     	<td>${region.getId()}</td>
				     	<td>${region.getName()}</td>

						<td>                 
							<div>
								<a href="${'/region/detail/' + region.getId() }">Détail</a> |
								<a href="${'/region/update/' + region.getId() }">Modifier</a> |
								<a href="${'/region/delete/' + region.getId() }">Supprimer</a>
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
