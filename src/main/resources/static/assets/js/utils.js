function swal(text, icon) {
    const Toast = Swal.mixin({
        toast: true,
        position: 'top-end',
        iconColor: icon === 'success' ? '#a5dc86' : '#f27474',
        customClass: {
            popup: 'colored-toast'
        },
        showConfirmButton: false,
        timer: 3000,
        timerProgressBar: true
    });
    Toast.fire({
        icon: icon,
        title: text
    });
}

function loadingSwal(text) {
    const Toast = Swal.mixin({
        toast: true,
        position: 'top-end',
        iconColor: '#3fc3ee',
        customClass: {
          popup: 'colored-toast'
        },
        showConfirmButton: false,
        timer: 30000,
        timerProgressBar: true
     });
    Toast.fire({
        icon: 'info',
        title: text
    });
}
