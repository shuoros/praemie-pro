function login() {
    const loginVm = new Object();
    loginVm.email = document.getElementById('loginEmail').value;
    loginVm.password = document.getElementById('loginPassword').value;
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

function register() {
    const registerVm = new Object();
    registerVm.firstName = document.getElementById('registerFirstName').value;
    registerVm.lastName = document.getElementById('registerLastName').value;
    registerVm.email = document.getElementById('registerEmail').value;
    registerVm.password = document.getElementById('registerPassword').value;
    registerVm.rememberMe = true;
    if (registerVm.firstName === '') {
        swal("Bitte geben Sie Ihren Vorname ein!", 'error');
    } else if (registerVm.firstName === '') {
        swal("Bitte geben Sie Ihren Nachname ein!", 'error');
    } else if (registerVm.email === '') {
        swal("Bitte geben Sie Ihre E-Mail-Adresse ein!", 'error');
    } else if (registerVm.password === '') {
        swal("Bitte geben Sie Ihr Passwort ein!", 'error');
    } else {
        loadingSwal('Anmelden läuft!');
        fetch(window.location.origin + '/api/users/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(registerVm)
        })
        .then(async res => {
            if (res.status === 201) {
                const data = await res.json();
                swal("Sie sind erflogreich registeriert!", 'success');
            } else if (res.status === 409) {
                swal("Die eingegebene E-Mail-Adresse ist schon registeriert!", 'error');
            } else if (res.status === 400) {
                swal("Bitte überprüfen Sie Ihre Eingaben!", 'error');
            } else {
                swal("Beim Erreichen des Servers ist ein Problem aufgetreten! Bitte wenden Sie sich an den Support.", 'error');
            }
        })
        .catch(err => {
            swal("Beim Erreichen des Servers ist ein Problem aufgetreten! Bitte wenden Sie sich an den Support.", 'error');
        });
    }
}
