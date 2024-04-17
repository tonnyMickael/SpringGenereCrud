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
    <p class="fw-bold text-success mb-2">person</p>
        <h1>Détail: </h1>
                </div>
            </div>
        <a href="/person/list"><button class="btn btn-primary">Retour</button></a>
        <br/><br/>
        <#if person??>
            <table border="1" class="table">
				<tr>
					<td>id</td>
					<td>:</td>
					<td>${person.id}</td>
				</tr>
				<tr>
					<td>firstname</td>
					<td>:</td>
					<td>${person.firstname}</td>
				</tr>
				<tr>
					<td>lastname</td>
					<td>:</td>
					<td>${person.lastname}</td>
				</tr>
				<tr>
					<td>cin</td>
					<td>:</td>
					<td>${person.cin}</td>
				</tr>
				<tr>
					<td>bithday</td>
					<td>:</td>
					<td>${person.bithday}</td>
				</tr>


				<#if person.listRegion ??>
					<tr class="text-muted w-lg-50">
						<td>region</td>
						<td>:</td>
						<td>
							<table table border="1" class="table">
							<thead>
							<tr>
								<th>id</th>
								<th>name</th>
							</tr>
							</thead>
							<tbody>
							<#list person.listRegion as region>
								<tr>
									<td>${region.id}</td>
									<td>${region.name}</td>
									<td>
										<div>
											<a href="${'/person/detail/region/update/'+ person.getId()+'/'+region.getId() }"><button class="btn btn-warning">Modifier</button></a> | 
											<a href="${'/person/detail/region/delete/'+ person.getId()+'/'+ region.getId() }"><button class="btn btn-danger">Supprimer</button></a>
										</div>
									</td>
								</tr>
							</#list>
							</tbody>
							</table>
						</td>
					</tr>
				</#if>
            </table>
            <br/><br/>
            <a href="${'/person/detail/'+person.id+'/region/add' }"><button class="btn btn-primary">Ajouter region</button></a>
        <#else>
            <p>Aucun résultat...</p>
        </#if>
        </div>
        </section>
    </body>
</html>
