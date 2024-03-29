<!DOCTYPE HTML>
<html>
    <head>
        <meta charset="UTF-8" />
        <title>Détail</title>
        <link rel="stylesheet" type="text/css" href="/css/style.css"/>
    </head>
    <body>
        <h1>Détail: </h1>
        <a href="/produit/list">Retour</a>
        <br/><br/>
        <#if produit??>
            <table border="0">
                <tr>
                    <td>ID</td>
                    <td>:</td>
                    <td>${produit.getId()}</td>          
                </tr>
                <tr>
                    <td>Title</td>
                    <td>:</td>
                    <td>${produit.getDesignation()}</td>             
                </tr>
                <tr>
                    <td>Prix</td>
                    <td>:</td>
                    <td>${produit.getPrix()}</td>              
                </tr>
                <tr>
                    <td>Catégorie</td>
                    <td>:</td>
                    <td>
		                <table table border="1">
		                	<tr>
		                		<th></th>
		                		<th>Catégorie</th>
		                	</tr>
		                	<tr>
		                		<td>${produit.getTag().getId()}</td>
		                		<td>${produit.getTag().getName()}</td>
		                	</tr>
		                </table>
                    </td>              
                </tr>
            </table>
            <br/><br/>
        </#if>
        <#if errorMessage?has_content>
            <div class="error">${errorMessage}</div>
        </#if>
    </body>
</html>