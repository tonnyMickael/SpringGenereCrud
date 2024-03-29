<!DOCTYPE HTML>
<HTML>
    <head>
        <meta charset="UTF-8">
        <title>formulaire</title>
    </head>
    <body>
        <div>
            <h1>formulaire</h1>
            <form name="region" action="add" method="post">
                <label>name: <input type = "text" name = "name"> </label>


                <select name="person_id">
                    <#list listPerson as person>
                        <option value="${person.id}">${person.name}</option>
                    </#list>
                </select>

                <input type="submit" value="valided">
            </form>
        </div>
    </body>
</HTML>
