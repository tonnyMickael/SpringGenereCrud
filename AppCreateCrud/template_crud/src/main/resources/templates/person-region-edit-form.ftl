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
                        <h1>Formulaire region dans person</h1>
            <a href="${'/person/detail/'+person.id}"><button class="btn btn-primary">Retour</button></a>
            <br/><br/>
                </div>
                    </div>
            <#if person??>
                <table border="1" class="table">
                					<tr class="text-muted w-lg-50">
						<td>id</td>
						<td>:</td>
						<td>${person.id}</td>
					</tr>
					<tr class="text-muted w-lg-50">
						<td>firstname</td>
						<td>:</td>
						<td>${person.firstname}</td>
					</tr>
					<tr class="text-muted w-lg-50">
						<td>lastname</td>
						<td>:</td>
						<td>${person.lastname}</td>
					</tr>
					<tr class="text-muted w-lg-50">
						<td>cin</td>
						<td>:</td>
						<td>${person.cin}</td>
					</tr>
					<tr class="text-muted w-lg-50">
						<td>bithday</td>
						<td>:</td>
						<td>${person.bithday}</td>
					</tr>

					<tr class="text-muted w-lg-50">
						<td>region</td>
						<td>:</td>
						<td>
                        <div class="row d-flex justify-content-center">
				<div class="col-md-6 col-xl-4">
					<div id="content">
                            <form name="region" action="/person/detail/region/update" method="post">
                                <input class="form-control" name="person_id" type="hidden" value="${person.getId()}"/>
                                <input class="form-control" name="region_id" type="hidden" value="${region.getId()}">
                                <select name="id" class="form-control">
                                    <#list listRegion as regionIndex>
                                        <option value="${regionIndex.id}"<#if regionIndex.getId() == region.getId()> selected</#if>>${regionIndex.name}</option>
                                    </#list>
                                </select>

                                <input type="submit" value="Valider" class="btn btn-primary">
                            </form>
                              </div>  
                                </div>  
                                    </div>
						</td>
					</tr>
                </table>
                <br/><br/>
            <#else>
                <p>Aucun résultat...</p>
            </#if>


        </div>
        </section>
    </body>
</HTML>
        
