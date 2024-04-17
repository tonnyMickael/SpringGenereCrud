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
					<p class="fw-bold text-success mb-2">region</p>
			<h1>modification region</h1>
				</div>
			</div>	
	        <a href="/region/list"><button class="btn btn-primary">Retour</button></a>
	        <br/><br/>
		</div>
		
		<div class="row d-flex justify-content-center">
				<div class="col-md-6 col-xl-4">
			<form name="region" action="${'/region/update/'+region.getId()}" method="post">
            		<p>name:<input class="form-control"name="name" type="text" value="${region.getName()}"/><br></p>

			
			<input class="btn btn-primary" type="submit" value="Modifier">
            </form>
				</div>
			</div>	
		</div>
		</section>
	</body>
</html>
