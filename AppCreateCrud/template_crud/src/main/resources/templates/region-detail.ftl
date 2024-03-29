<!DOCTYPE HTML>
<html>
    <head>
        <meta charset="UTF-8" />
        <title>Détail</title>
    </head>
    <body>
        <h1>Détail: </h1>
        <a href="/region/list">Retour</a>
        <br/><br/>
        <#if region??>
            <table border="0">
				<tr>
					<td>id</td>
					<td>:</td>
					<td>${region.getId()}</td>
				</tr>
				<tr>
					<td>name</td>
					<td>:</td>
					<td>${region.getName()}</td>
				</tr>




            </table>
            <br/><br/>
        <#else>
            <p>Aucun résultat...</p>
        </#if>
    </body>
</html>
