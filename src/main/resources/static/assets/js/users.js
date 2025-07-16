function deleteUser(id, name) {
    Swal.fire({
          title: "Möchten Sie wirklich " + name + " löschen?",
          text: "Diese Aktion ist irreversibel!",
          icon: "warning",
          showCancelButton: true,
          confirmButtonColor: "#3085d6",
          cancelButtonColor: "#d33",
          confirmButtonText: "Ja, löschen!",
          cancelButtonText: "Absagen"
    }).then((result) => {
        if (result.isConfirmed) {
            deleteRequest(id);
        }
    });
}

function deleteRequest(id) {
    loadingSwal('Löschen läuft!');
    fetch(window.location.origin + '/api/users/' + id, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + getAuthorizationCookie()
        },
    })
    .then(async res => {
        if (res.status === 204) {
            swal("Erfolgreich gelöscht!", 'success');
            window.open((url = '/dashboard/users'), (target = '_self'));
        } else {
            swal("Beim Erreichen des Servers ist ein Problem aufgetreten! Bitte wenden Sie sich an den Support.", 'error');
        }
    })
    .catch(err => {
        swal("Beim Erreichen des Servers ist ein Problem aufgetreten! Bitte wenden Sie sich an den Support.", 'error');
    });
}

function calculate(id) {
    const user = new Object();
    user.id = id;
    const orderVM = new Object();
    orderVM.vehicleType = document.getElementById('vehicleType').value;
    orderVM.yearlyDrive = document.getElementById('yearlyDrive').value;
    orderVM.zipcode = document.getElementById('zipcode').value;
    orderVM.user = user;
    if (orderVM.vehicleType === '') {
        swal("Bitte geben Sie Ihren Fahrzeugtyp ein!", 'error');
    } else if (orderVM.yearlyDrive === '') {
        swal("Bitte geben Sie die jährliche Kilometerleistung Ihres Wagens ein!", 'error');
    } else if (orderVM.zipcode === '') {
        swal("Bitte geben Sie Ihre Postleitzahl ein!", 'error');
    } else {
        loadingSwal('Berechnung läuft!');
        fetch(window.location.origin + '/api/orders', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + getAuthorizationCookie()
            },
            body: JSON.stringify(orderVM)
        })
        .then(async res => {
            if (res.status === 201) {
                swal("Berechnungen werden durchgeführt!", 'success');
                const order = await res.json();
                document.getElementById('noOrders').style.display="none";
                appendOrderRow(order);
            } else if (res.status === 406) {
                swal("Die Postleitzahl war ungültig!", 'error');
            } else if (res.status === 400) {
                swal("Einige Eingaben sind nicht richtig. Bitte überprüfen Sie es erneut!", 'error');
            } else {
                swal("Beim Erreichen des Servers ist ein Problem aufgetreten! Bitte wenden Sie sich an den Support.", 'error');
            }
        })
        .catch(err => {
            swal("Beim Erreichen des Servers ist ein Problem aufgetreten! Bitte wenden Sie sich an den Support.", 'error');
        });
    }
}
