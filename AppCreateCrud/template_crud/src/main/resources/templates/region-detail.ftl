<!DOCTYPE HTML>
<html>
    <head>
        <meta charset="UTF-8" />
        <title>Détail</title>
        <link rel="stylesheet" href="/assets/bootstrap/css/bootstrap.min.css">
    </head>
    <body>
     <section class="py-5">
		<div class="container py-5">
		    <div class="row mb-5">
				<div class="col-md-8 col-xl-6 text-center mx-auto">
    <p class="fw-bold text-success mb-2">region</p>
        <h1>Détail: </h1>
                </div>
            </div>
        <a href="/region/list"><button class="btn btn-primary">Retour</button></a>
        <br/><br/>
        <#if region??>
            <table border="1" class="table">
				<tr>
					<td>id</td>
					<td>:</td>
					<td>${region.id}</td>
				</tr>
				<tr>
					<td>name</td>
					<td>:</td>
					<td>${region.name}</td>
				</tr>



            </table>
            <br/><br/>
            
        <#else>
            <p>Aucun résultat...</p>
        </#if>
        </div>
        </section>
    </body>
</html>
