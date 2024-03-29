<!DOCTYPE HTML>
<html>
    <head>
        <meta charset="UTF-8" />
        <title>Détail</title>
    </head>
    <body>
        <h1>Détail: </h1>
        <a href="/person/list">Retour</a>
        <br/><br/>
        <#if person??>
            <table border="0">
				<tr>
					<td>id</td>
					<td>:</td>
					<td>${person.getId()}</td>
				</tr>
				<tr>
					<td>firstname</td>
					<td>:</td>
					<td>${person.getFirstname()}</td>
				</tr>
				<tr>
					<td>lastname</td>
					<td>:</td>
					<td>${person.getLastname()}</td>
				</tr>
				<tr>
					<td>birthday</td>
					<td>:</td>
					<td>${person.getBirthday()}</td>
				</tr>
				<tr>
					<td>cin</td>
					<td>:</td>
					<td>${person.getCin()}</td>
				</tr>


				<tr>
					<td>region</td>
					<td>:</td>
					<td>
						<table table border="1">
							<tr>
								<th>id</th>
								<th>name</th>
							</tr>
							<tr>
								<td>${person.getRegion().getId()}</td>
								<td>${person.getRegion().getName()}</td>
							</tr>
						</table>
					</td>
				</tr>

            </table>
            <br/><br/>
        <#else>
            <p>Aucun résultat...</p>
        </#if>
    </body>
</html>
