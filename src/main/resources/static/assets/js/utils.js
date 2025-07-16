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

function getAuthorizationCookie() {
  const value = `; ${document.cookie}`;
  const parts = value.split('; Authorization=');
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

function appendOrderRow(order) {
    const orders = document.querySelector("#orders tbody");

    const row = document.createElement("tr");

    row.innerHTML = `
        <td>${order.id}</td>
        <td>${order.vehicleType}</td>
        <td>${order.yearlyDrive} KM</td>
        <td>${order.zipcode}</td>
        <td>${order.yearlyPrice} EUR</td>
        <td>${order.date}</td>
        <td><a href="/dashboard/orders/${order.id}">Anzeigen</a></td>
    `;

    orders.appendChild(row);
}
