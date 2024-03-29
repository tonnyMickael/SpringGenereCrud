<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>Formulaire</title>
	</head>
	<body>
		<div id="header">
		  <h1>Formulaire d'ajout</h1>
		</div>
		
		<div id="content">
		    <form name="produit" action="add" method="post">
		      Designation : <input type="text" name="designation" /><br/>
		      prix: <input type="number" name="prix" /><br/>
		    <select name="tag_id">
		        <#list listTag as tag>
		            <option value="${tag.id}">${tag.name}</option>
		        </#list>
		    </select>
		      <input type="submit" value="Ajouter" />
		    </form>
		</div>
	</body>
</html>