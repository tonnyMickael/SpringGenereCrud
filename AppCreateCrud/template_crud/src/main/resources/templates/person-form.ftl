<!DOCTYPE HTML>
<HTML>
    <head>
        <meta charset="UTF-8">
        <title>formulaire</title>
        <link rel="stylesheet" href="/assets/bootstrap/css/bootstrap.min.css">
    </head>
    <body>
    <section class="py-5">
        <div class="container py-5">
        <div class="row mb-5">
				<div class="col-md-8 col-xl-6 text-center mx-auto">
            <h1>formulaire</h1>
                </div> 
                    </div>
                    	<div class="row d-flex justify-content-center">
				            <div class="col-md-6 col-xl-4">
            <form name="person" action="add" method="post">
                <p>firstname: <input class="form-control" type = "text" name = "firstname"> </p>
				<p>lastname: <input class="form-control" type = "text" name = "lastname"> </p>
				<p>cin: <input class="form-control" type = "text" name = "cin"> </p>
				<p>bithday: <input class="form-control" type = "date" name = "bithday"> </p>
				

                <p> Region
                    <select name="region_id" class="form-control">
                        <#list listRegion as region>
                            <option value="${region.id}">${region.name}</option>
                        </#list>
                    </select>
                </p>

                <input type="submit" value="Valider" class="btn btn-primary">
            </form>
                        </div> 
                    </div>
        </div>
        </section>
    </body>
</HTML>
