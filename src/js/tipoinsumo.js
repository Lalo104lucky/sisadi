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

async function obtenerTipoInsumo() {
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

async function buscarInsumoPorClave(clave) {
    try {
        const authToken = localStorage.getItem('authToken');
        if (!authToken) {
            throw new Error('No se encontró el token de autenticación');
        }

        const response = await fetch(`http://localhost:8081/sisadi/insumo/clave/${clave}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${authToken}`
            }
        });

        if (!response.ok) {
            throw new Error('Error al buscar el insumo por clave');
        }

        const data = await response.json();
        return data.data; // Devuelve los datos del insumo
    } catch (error) {
        console.error('Hubo un problema al buscar el insumo por clave:', error);
        return null;
    }
}

async function guardarNuevoTipoInsumo() {
    const claveInput = document.getElementById('clave');
    const clave = claveInput.value;

    if (!clave) {
        alert('Por favor, ingrese una clave.');
        return;
    }

    const insumos = await buscarInsumoPorClave(clave);

    if (insumos && insumos.length > 0) {
        insumos.forEach(insumo => agregarInsumoATabla(insumo));
        $('#modalAgregarInsumo').modal('hide');
        claveInput.value = ''; // Limpiar el campo de entrada
    } else {
        alert('No se encontró un insumo con la clave proporcionada.');
    }
}

async function guardarTipoInsumo() {
    const nombreInput = document.getElementById('nombreInput');
    const partidaInput = document.getElementById('partidaInput');

    const nombre = nombreInput.value;
    const partida = partidaInput.value;

    if (!nombre ) {
        alert('Por favor, ingrese el nombre.');
        return;
    }

    // Obtener los IDs de los insumos de la tabla
    const insumoIds = Array.from(document.querySelectorAll('#insumos-body tr'))
        .map(tr => tr.getAttribute('data-insumo-id'));

    if (insumoIds.length === 0) {
        alert('No hay insumos para guardar.');
        return;
    }

    const payload = {
        nombre: nombre,
        partida: partida,
        insumo_id: insumoIds.map(id => parseInt(id, 10))
    };

    try {
        const authToken = localStorage.getItem('authToken');
        if (!authToken) {
            throw new Error('No se encontró el token de autenticación');
        }

        const response = await fetch('http://localhost:8081/sisadi/tipoinsumo/', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${authToken}`
            },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            throw new Error('Error al guardar el tipo de insumo');
        }

        const data = await response.json();
        alert('Tipo de insumo guardado exitosamente');
        // Limpiar los campos y la tabla
        nombreInput.value = '';
        partidaInput.value = '';
        document.getElementById('insumos-body').innerHTML = '';
    } catch (error) {
        console.error('Hubo un problema con la solicitud:', error);
        alert('Hubo un problema al guardar el tipo de insumo.');
    }
}

function agregarInsumoATabla(insumo) {
    const tbody = document.getElementById('insumos-body');

    const tr = document.createElement('tr');
    tr.setAttribute('data-insumo-id', insumo.id_insumo); // Añadir data attribute con el id del insumo
    tr.innerHTML = `
        <td>${insumo.clave}</td>
        <td>${insumo.descripcion}</td>
        <td>
            <button class="btn btn-danger btn-sm" onclick="eliminarInsumo(this)"><i class="bi bi-trash"></i> Eliminar</button>
        </td>
    `;
    tbody.appendChild(tr);
}

function eliminarInsumo(button) {
    const tr = button.parentNode.parentNode;
    tr.parentNode.removeChild(tr);
}

document.addEventListener('DOMContentLoaded', obtenerTipoInsumo);
