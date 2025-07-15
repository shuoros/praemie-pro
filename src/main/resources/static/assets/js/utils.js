function signout() {
  deleteAuthorizationCookie();
  window.location.href = window.location.origin + '/authenticate';
}

function setAuthorizationCookie(value) {
  document.cookie = 'Authorization='+ value +'; Path=/; expires=;';
}

function deleteAuthorizationCookie() {
  document.cookie = 'Authorization=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}

function getCookie(name) {
  const value = `; ${document.cookie}`;
  const parts = value.split(`; ${name}=`);
  if (parts.length === 2) return parts.pop().split(';').shift();
}

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

function formatDate(date) {
    const d = new Date(date);
    const day = String(d.getDate()).padStart(2, '0');
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const year = d.getFullYear();
    return `${day}.${month}.${year}`;
}
