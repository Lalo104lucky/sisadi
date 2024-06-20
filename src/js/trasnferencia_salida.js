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

// Aún le falta a este 
async function obtenerTransSalida() {
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

        const response = await fetch('http://localhost:8081/sisadi/proveedor/');
        if (!response.ok) {
            throw new Error('El servidor no está respondiendo ' + response.statusText);
        }
        const data = await response.json();

        const proveedorUsuario = data.data.filter(proveedor => proveedor.usuario.id_usuario === id_usuario);

        llenarTabla(proveedorUsuario);
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

document.addEventListener('DOMContentLoaded', obtenerTransSalida);