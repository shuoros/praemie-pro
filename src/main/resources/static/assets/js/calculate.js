function calculate() {
    const calculateVM = new Object();
    calculateVM.vehicleType = document.getElementById('vehicleType').value;
    calculateVM.yearlyDrive = document.getElementById('yearlyDrive').value;
    calculateVM.zipcode = document.getElementById('zipcode').value;
    if (calculateVM.vehicleType === '') {
        swal("Bitte geben Sie Ihren Fahrzeugtyp ein!", 'error');
    } else if (calculateVM.yearlyDrive === '') {
        swal("Bitte geben Sie die jährliche Kilometerleistung Ihres Wagens ein!", 'error');
    } else if (calculateVM.zipcode === '') {
        swal("Bitte geben Sie Ihre Postleitzahl ein!", 'error');
    } else {
        loadingSwal('Berechnung läuft!');
        fetch(window.location.origin + '/public/insurance/calculate', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(calculateVM)
        })
        .then(async res => {
            if (res.status === 200) {
                swal("Berechnungen werden durchgeführt!", 'success');
                const data = await res.json();
                document.getElementById('yearlyPrice').innerHTML = data.yearlyPrice + " EUR";
                document.getElementById('monthlyPrice').innerHTML = data.monthlyPrice + " EUR";
                document.getElementById('result').style.display="block";
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