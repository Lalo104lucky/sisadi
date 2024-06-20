document.addEventListener('DOMContentLoaded', function () {
    var logoutForm = document.getElementById('logout-form');
    if (logoutForm) {
        logoutForm.addEventListener('submit', function (e) {
            e.preventDefault();
            localStorage.removeItem('authToken');
            if (!localStorage.getItem('authToken')) {
                window.location.href = './index.html';
            }
        });
    } else {
        console.error(error.message);
    }
});
