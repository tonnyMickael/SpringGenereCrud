<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>Formulaire</title>
	</head>
	<body>
		<div id="header">
		  <h1>Formulaire de modification</h1>
		</div>

		<div id="content">
		    <form name="produit" action="${'/produit/update/' + produit.getId()}" method="post">
		      Designation : <input type="text" name="designation" value="${(produit.getDesignation())!''}" /><br/>
		      prix: <input type="number" name="prix" value="${(produit.getPrix()?string["0.###"]?replace(",", "."))!}"/><br/>
		      <select name="tag_id">
				<#list listTag as tag>
        			<option value="${tag.getId()}" <#if produit.getTag().getId() == tag.getId()>selected</#if>>${tag.getName()}</option>
				</#list>
		      </select>		      
		      <input type="submit" value="Modifier" />
		    </form>
		</div>
	</body>
</html>


