// Código para manejar el login y almacenar el token en localStorage
document.addEventListener('DOMContentLoaded', function () {
    var loginForm = document.getElementById('login-form');
    if (loginForm) {
        loginForm.addEventListener('submit', function (e) {
            e.preventDefault();

            var username = document.getElementById('user').value;
            var password = document.getElementById('password').value;

            fetch('http://localhost:8081/sisadi/auth/signin', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    usuario: username,
                    contrasena: password
                }),
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Error al conectar con el servidor');
                    }
                    return response.json();
                })
                .then(data => {
                    console.log('Success:', data);
                    if (data.status === "OK" && data.data && data.data.token) {
                        localStorage.setItem('authToken', data.data.token); // Almacenar el token JWT
                        window.location.href = './default.html'; // Redirigir a la página principal
                    } else {
                        alert('Usuario o contraseña incorrectos');
                    }
                })
                .catch((error) => {
                    console.error('Error:', error);
                    alert('Usuario o contraseña incorrectos');
                });
        });
    } else {
        console.error('Error inesperado');
    }
});
