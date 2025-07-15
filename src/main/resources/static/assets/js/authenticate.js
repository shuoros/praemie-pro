function login() {
    const loginVm = new Object();
    loginVm.email = document.getElementById('email').value;
    loginVm.password = document.getElementById('password').value;
    loginVm.rememberMe = true;
    if (loginVm.email === '') {
        swal("Bitte geben Sie Ihre E-Mail-Adresse ein!", 'error');
    } else if (loginVm.password === '') {
        swal("Bitte geben Sie Ihr Passwort ein!", 'error');
    } else {
        loadingSwal('Anmelden läuft!');
        fetch(window.location.origin + '/api/authenticate', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(loginVm)
        })
        .then(async res => {
            if (res.status === 200) {
                const data = await res.json();
                swal("Sie sind angemeldet!", 'success');
                setAuthorizationCookie(data.token);
                window.open((url = '/dashboard'), (target = '_self'));
            } else if (res.status === 401) {
                swal("E-Mail oder Passwort war falsch!", 'error');
            } else {
                swal("Beim Erreichen des Servers ist ein Problem aufgetreten! Bitte wenden Sie sich an den Support.", 'error');
            }
        })
        .catch(err => {
            swal("Beim Erreichen des Servers ist ein Problem aufgetreten! Bitte wenden Sie sich an den Support.", 'error');
        });
  }
}