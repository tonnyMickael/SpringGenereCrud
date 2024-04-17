<!DOCTYPE HTML>
<html>

<head>
    <meta charset="UTF-8" />
    <title>Crudgenere</title>
    <link rel="stylesheet" href="/assets/bootstrap/css/bootstrap.min.css">
</head>

<body>
    <section class="py-5">
        <div class="container py-5">
            <div class="row mb-4 mb-lg-5">
                <div class="col-md-8 col-xl-6 text-center mx-auto">
                    <h1>Login</h1>
                </div>
            </div>
            <div class="row d-flex justify-content-center">
                <div class="col-md-6 col-xl-4">
                    <div class="card">
                        <div class="card-body text-center d-flex flex-column align-items-center">
                            <div class="bs-icon-xl bs-icon-circle bs-icon-primary shadow bs-icon my-4"><svg xmlns="http://www.w3.org/2000/svg" width="1em" height="1em" fill="currentColor" viewBox="0 0 16 16" class="bi bi-person">
                                    <path d="M8 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6zm2-3a2 2 0 1 1-4 0 2 2 0 0 1 4 0zm4 8c0 1-1 1-1 1H3s-1 0-1-1 1-4 6-4 6 3 6 4zm-1-.004c-.001-.246-.154-.986-.832-1.664C11.516 10.68 10.289 10 8 10c-2.29 0-3.516.68-4.168 1.332-.678.678-.83 1.418-.832 1.664h10z"></path>
                                </svg></div>
                            <form action="/login/auth" method="post">
                                <div class="mb-3">
                                    <p><input class="form-control" name="email" type="text" placeholder="email" /></p>
                                </div>
                                <div class="mb-3">
                                    <p><input class="form-control" name="password" type="password" placeholder="password" /></p>
                                </div>
                                <div class="mb-3">
                                    <p><input class="btn btn-primary" type="submit" value="se connecter" /></p>
                                </div>
                            </form>
                            <p><a href="${'/register/add'}"><span class="text-decoration-none">s'inscrire</span></a></p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</body>

</html>