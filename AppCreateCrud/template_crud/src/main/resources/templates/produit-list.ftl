<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>Liste</title>
	</head>
	<body>
		<div id="header">
		  <h1>Liste</h1>
			<a href="/produit/add">Retour</a>
		</div>
		
		<div id="content">
		  <table border="1">
		    <tr>
		      <th>Id</th>
		      <th>Désignation</th>
		      <th>Prix</th>
		    </tr>
		    <#list produits as produit>
		      <tr>
		      	<td>${produit.getId()}</td>
		        <td>${produit.getDesignation()}</td>
		        <td>${produit.getPrix()}</td>
		      	<td>                 
		      		<div>
	                    <a href="${'/produit/update/' + produit.getId() }">Modifier</a> |
	                    <a href="${'/produit/delete/' + produit.getId() }">Supprimer</a> |
	                    <a href="${'/produit/detail/' + produit.getId() }">Détail</a>
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