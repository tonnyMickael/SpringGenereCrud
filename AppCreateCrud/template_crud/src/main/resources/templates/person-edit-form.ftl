<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>Modifier</title>
		<link rel="stylesheet" href="/assets/bootstrap/css/bootstrap.min.css">
	</head>
	<body>
		<section class="py-5">
			<div class="container py-5">
				<div class="row mb-5">
					<div class="col-md-8 col-xl-6 text-center mx-auto">
					<p class="fw-bold text-success mb-2">person</p>
			<h1>modification person</h1>
				</div>
			</div>	
	        <a href="/person/list"><button class="btn btn-primary">Retour</button></a>
	        <br/><br/>
		</div>
		
		<div class="row d-flex justify-content-center">
				<div class="col-md-6 col-xl-4">
			<form name="person" action="${'/person/update/'+person.getId()}" method="post">
            		<p>firstname:<input class="form-control"name="firstname" type="text" value="${person.getFirstname()}"/><br></p>
		<p>lastname:<input class="form-control"name="lastname" type="text" value="${person.getLastname()}"/><br></p>
		<p>cin:<input class="form-control"name="cin" type="text" value="${person.getCin()}"/><br></p>
		<p>bithday:<input class="form-control"name="bithday" type="date" value="${person.getBithday()}"/><br></p>

			<p>region:<select name="region_id" class="form-control ">
				<#list listRegion as region>
					<option value="${region.getId()}">${region.getName()}</option>
				</#list>
			</select></p>
			<input class="btn btn-primary" type="submit" value="Modifier">
            </form>
				</div>
			</div>	
		</div>
		</section>
	</body>
</html>
