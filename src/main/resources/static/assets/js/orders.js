function deleteOrder(id) {
    Swal.fire({
          title: "Möchten Sie wirklich " + id + " löschen?",
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
    fetch(window.location.origin + '/api/orders/' + id, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + getAuthorizationCookie()
        },
    })
    .then(async res => {
        if (res.status === 204) {
            swal("Erfolgreich gelöscht!", 'success');
            window.open((url = '/dashboard/orders'), (target = '_self'));
        } else {
            swal("Beim Erreichen des Servers ist ein Problem aufgetreten! Bitte wenden Sie sich an den Support.", 'error');
        }
    })
    .catch(err => {
        swal("Beim Erreichen des Servers ist ein Problem aufgetreten! Bitte wenden Sie sich an den Support.", 'error');
    });
}

function openUpdate(vehicleType, yearlyDrive, zipcode) {
    document.getElementById('yearlyDrive').value = yearlyDrive.replaceAll('.', '');
    document.getElementById('zipcode').value = zipcode;

    const select = document.getElementById('vehicleType');
    for (const option of select.options) {
        if (option.text.trim() === vehicleType.trim()) {
            select.value = option.value;
            break;
        }
    }

    document.getElementById('orderUpdate').style.display="block";
}

function closeUpdate() {
    document.getElementById('orderUpdate').style.display="none";
}

function update(id) {
    const orderVM = new Object();
    orderVM.id = id;
    orderVM.vehicleType = document.getElementById('vehicleType').value;
    orderVM.yearlyDrive = document.getElementById('yearlyDrive').value;
    orderVM.zipcode = document.getElementById('zipcode').value;
    if (orderVM.vehicleType === '') {
        swal("Bitte geben Sie Ihren Fahrzeugtyp ein!", 'error');
    } else if (orderVM.yearlyDrive === '') {
        swal("Bitte geben Sie die jährliche Kilometerleistung Ihres Wagens ein!", 'error');
    } else if (orderVM.zipcode === '') {
        swal("Bitte geben Sie Ihre Postleitzahl ein!", 'error');
    } else {
        loadingSwal('Berechnung läuft!');
        fetch(window.location.origin + '/api/orders/' + id, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + getAuthorizationCookie()
            },
            body: JSON.stringify(orderVM)
        })
        .then(async res => {
            if (res.status === 200) {
                swal("Bestellung ist aktualisiert!", 'success');
                window.location.reload();
            } else if (res.status === 404) {
                swal("Die Bestellung ist nicht gefunden!", 'error');
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
