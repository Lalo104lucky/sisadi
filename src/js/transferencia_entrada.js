async function obtenerDatosUsuario(id_usuario) {
    try {
        const authToken = localStorage.getItem('authToken');
        if (!authToken) {
            throw new Error('No se encontró el token de autenticación');
        }

        const response = await fetch(`http://localhost:8081/sisadi/usuario/${id_usuario}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${authToken}`
            }
        });

        if (!response.ok) {
            throw new Error('Error al obtener los datos del usuario');
        }

        const data = await response.json();
        return data.data; // Devuelve los datos del usuario
    } catch (error) {
        console.error('Hubo un problema al intentar obtener los datos del usuario:', error);
        return null;
    }
}

async function obtenerTransEntrada() {
    try {
        const authToken = localStorage.getItem('authToken');
        if (!authToken) {
            throw new Error('No se encontró el token de autenticación');
        }

        const { id_usuario } = parseJwt(authToken);

        // Obtener los datos del usuario
        const datosUsuario = await obtenerDatosUsuario(id_usuario);
        if (datosUsuario) {
            const nombrePersona = `${datosUsuario.persona.nombre} ${datosUsuario.persona.apellido_p} ${datosUsuario.persona.apellido_m}`;
            // Mostrar el nombre de la persona en el HTML
            mostrarNombrePersona(nombrePersona);
        } else {
            mostrarNombrePersona('Usuario no encontrado');
        }

    } catch (error) {
        console.error('Hubo un problema con la solicitud:', error);
    }
}

function parseJwt(token) {
    try {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));

        const tokenPayload = JSON.parse(jsonPayload);

        // Asegurarse de obtener id_usuario como número
        const id_usuario = typeof tokenPayload.id_usuario === 'number' ? tokenPayload.id_usuario : parseInt(tokenPayload.id_usuario, 10);

        return {
            usuario: tokenPayload.sub, // O tokenPayload.usuario si es necesario
            id_usuario: id_usuario,
        };
    } catch (error) {
        console.error('Error al decodificar el token JWT:', error);
        return {};
    }
}

function mostrarNombrePersona(nombre) {
    const nombreElemento = document.getElementById('nombre-usuario');
    if (nombreElemento) {
        nombreElemento.textContent = nombre;
    }
}

document.addEventListener('DOMContentLoaded', () => {
    obtenerTransEntrada();

    // Enfocar automáticamente en el campo de entrada cuando la página se carga
    const folioInput = document.getElementById('folioInput');
    if (folioInput) {
        folioInput.focus();
    }

    // Funciones para iniciar y detener el escaneo
    function iniciarLector() {
        if (folioInput) {
            folioInput.focus();
        }
    }

    function detenerLector() {
        console.log("Lector detenido.");
    }

    // Capturar el valor del folioInput y redirigir
    function redirigir() {
        if (folioInput) {
            const folio = folioInput.value;
            if (folio) {
                localStorage.setItem('folio', folio);
                localStorage.setItem('unidad', 'HAE 139 "CENTENARIO DE LA REVOLUCIÓN MEXICANA"');
                localStorage.setItem('tipoMovimiento', 'TRANSFERENCIA DE ENTRADA');
                localStorage.setItem('nombre', "ENTRADA");
                localStorage.setItem('proveedor_id', null);
                localStorage.setItem('fecha', new Date().toLocaleDateString('es-MX', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' }));
            } else {
                alert('Folio no puede estar vacío');
            }
        }
    }

    const btnCrear = document.querySelector('.btn-crear');
    const btnCancel = document.querySelector('.btn-cancel');

    if (btnCrear && btnCancel) {
        btnCrear.addEventListener('click', redirigir);
        btnCancel.addEventListener('click', detenerLector);
    } else {
        console.error('No se encontraron los botones para agregar los event listeners');
    }

    document.getElementById('crearButton').addEventListener('click', async function() {
        if (folioInput) {
            const folio = folioInput.value;
            if (folio) {
                const operacionData = {
                    fecha: new Date().toISOString(),
                    folio: folio,
                    nombre: "ENTRADA",
                    tipo_movimiento: "TRANSFERENCIA DE ENTRADA",
                    unidad: "HAE 139",
                    proveedor_id: null
                };
    
                try {
                    const response = await fetch('http://localhost:8081/sisadi/operacion/', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': `Bearer ${localStorage.getItem('authToken')}`
                        },
                        body: JSON.stringify(operacionData)
                    });
    
                    if (!response.ok) {
                        throw new Error('Error al crear la operación');
                    }
    
                    const responseData = await response.json();
                    console.log('Operación creada con éxito:', responseData);
    
                    const operacionId = responseData.data.id_operacion;
                    localStorage.setItem('operacionId', operacionId);
    
                    window.location.href = 'entradas_transferencia.html';
                } catch (error) {
                    console.error('Hubo un problema con la solicitud:', error);
                }
            } else {
                alert('Folio no puede estar vacío');
            }
        }
    });    
});
