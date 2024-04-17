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
			<h1>Liste region</h1>
	        <a href="/region/add"><button class="btn btn-primary">Retour</button></a>
	        <br/><br/>
			</div>
			</div>
		</div>
		
		<div id="content">
			<table border="1" class="table">
				<tr class="text-muted w-lg-50">
					<th scope="col">id</th>
					<th scope="col">name</th>

					<th scope="row"> action </th>
				</tr>
				<#list regions as region>
					<tr>
				     	<td>${region.id}</td>
				     	<td>${region.name}</td>

						<td scope="row">                 
							<div>
								<a href="${'/region/detail/' + region.getId() }"><button class="btn btn-primary">Détail</button></a> |
								<a href="${'/region/update/' + region.getId() }"><button class="btn btn-warning">Modifier</button></a> |
								<a href="${'/region/delete/' + region.getId() }"><button class="btn btn-danger">Supprimer</button></a>
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
