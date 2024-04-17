<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>Liste</title>
		<link rel="stylesheet" href="/assets/bootstrap/css/bootstrap.min.css">
	</head>
	<body>
	 <section class="py-5">
		<div class="container py-5">
		<div class="row mb-5">
				<div class="col-md-8 col-xl-6 text-center mx-auto">
			<h1>Liste person</h1>
	        <a href="/person/add"><button class="btn btn-primary">Retour</button></a>
	        <br/><br/>
			</div>
			</div>
		</div>
		
		<div id="content">
			<table border="1" class="table">
				<tr class="text-muted w-lg-50">
					<th scope="col">id</th>
					<th scope="col">firstname</th>
					<th scope="col">lastname</th>
					<th scope="col">cin</th>
					<th scope="col">bithday</th>

					<th scope="row"> action </th>
				</tr>
				<#list persons as person>
					<tr>
				     	<td>${person.id}</td>
				     	<td>${person.firstname}</td>
				     	<td>${person.lastname}</td>
				     	<td>${person.cin}</td>
				     	<td>${person.bithday}</td>

						<td scope="row">                 
							<div>
								<a href="${'/person/detail/' + person.getId() }"><button class="btn btn-primary">Détail</button></a> |
								<a href="${'/person/update/' + person.getId() }"><button class="btn btn-warning">Modifier</button></a> |
								<a href="${'/person/delete/' + person.getId() }"><button class="btn btn-danger">Supprimer</button></a>
							</div>
						</td>
					</tr>
				</#list>
			</table>
	        <div>
			<nav aria-label="Page navigation example">
	            <nobr>
				<ul class="pagination">
	                <li class="page-item"><#if hasPrev><a class="page-link" href="${'list?page=' + prev}">Prev</a>&nbsp;&nbsp;&nbsp;</#if></li>
	                <li class="page-item"><#if hasNext><a class="page-link" href="${'list?page=' + next}">Next</a></#if></li>
	            </nobr>
				</ul>
				</nav>
	        </div>
		</div>
			</section>
	</body>
</html>
