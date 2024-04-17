<!DOCTYPE HTML>
<HTML>
    <head>
        <meta charset="UTF-8">
        <title>Formulaire region dans person </title>
         <link rel="stylesheet" href="/assets/bootstrap/css/bootstrap.min.css">
    </head>
    <body>
     <section class="py-5">
         <div class="container py-5">
            <div class="row mb-5">
				<div class="col-md-8 col-xl-6 text-center mx-auto">
                <p class="fw-bold text-success mb-2">region</p>
            <h1>Formulaire region dans person</h1>
            <a href="${'/person/detail/'+person.id}"><button class="btn btn-primary"> Retour</button></a>
            <br/><br/>
                       </div>
                            </div>
            <#if person??>
                <table border="0" class="table">
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

					<tr class="text-muted w-lg-50">
						<td>region</td>
						<td>:</td>
						<td>
                            <form name="region" action="/person/detail/region/add" method="post">
                                <input class="form-control" name="person_id" type="hidden" value="${person.getId()}"/>
                                <select name="id" class="form-control">
                                    <#list listRegion as region>
                                        <option value="${region.id}">${region.name}</option>
                                    </#list>
                                </select>

                                <input class="btn btn-primary" type="submit" value="Valider">
                            </form>
						</td>
					</tr>
                </table>
                <br/><br/>
            <#else>
                <p>Aucun résultat...</p>
            </#if>
                     </div>
             </div>
        </div>
        	</section>
    </body>
</HTML>
        
