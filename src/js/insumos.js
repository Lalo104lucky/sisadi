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


async function obtenerInsumos() {
    try {
        const authToken = localStorage.getItem('authToken');
        if (!authToken) {
            throw new Error('No se encontró el token de autenticación');
        }

        // Decodificar el authToken para obtener usuario y id_usuario
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

        const response = await fetch('http://localhost:8081/sisadi/insumo/');
        if (!response.ok) {
            throw new Error('El servidor no está respondiendo ' + response.statusText);
        }
        const data = await response.json();

        // Filtrar los insumos para mostrar solo los del usuario actual
        const insumosUsuario = data.data.filter(insumo => insumo.usuario.id_usuario === id_usuario);

        // Llenar la tabla de insumos
        llenarTabla(insumosUsuario);
    } catch (error) {
        console.error('Hubo un problema con la solicitud:', error);
    }
}


async function guardarNuevoInsumo() {
    try {
        const clave = document.getElementById('clave').value;
        const descripcion = document.getElementById('descripcion').value;
        const unidad = document.getElementById('unidad').value;

        const authToken = localStorage.getItem('authToken');
        if (!authToken) {
            throw new Error('No se encontró el token de autenticación');
        }

        // Decodificar el authToken para obtener usuario y id_usuario
        const { usuario, id_usuario } = parseJwt(authToken);

        const nuevoInsumo = {
            clave: clave,
            descripcion: descripcion,
            unidad: unidad,
            usuario_id: id_usuario,
        };

        const response = await fetch('http://localhost:8081/sisadi/insumo/', {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${authToken}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(nuevoInsumo),
        });

        if (!response.ok) {
            throw new Error('Error al agregar el insumo');
        }

        // Limpiar el modal después de agregar correctamente
        limpiarModalNuevoInsumo();
        $('#modalNuevoInsumo').modal('hide');

        // Actualizar la tabla después de agregar
        await obtenerInsumos();
    } catch (error) {
        console.error('Hubo un problema al intentar agregar el insumo:', error);
        // Aquí podrías mostrar un mensaje de error al usuario si falla la creación
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


async function editarInsumo(id_insumo) {
    try {
        const authToken = localStorage.getItem('authToken');
        if (!authToken) {
            throw new Error('No se encontró el token de autenticación');
        }

        const response = await fetch(`http://localhost:8081/sisadi/insumo/${id_insumo}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${authToken}`
            }
        });

        if (!response.ok) {
            throw new Error('Error al obtener el insumo');
        }

        const insumo = await response.json();

        // Verificar que el insumo tiene datos válidos
        if (!insumo || !insumo.data) {
            throw new Error('Datos de insumo no válidos');
        }

        const insumoData = insumo.data;

        // Rellenar el formulario del modal con los datos del insumo
        document.getElementById('editarClave').value = insumoData.clave;
        document.getElementById('editarDescripcion').value = insumoData.descripcion;
        document.getElementById('editarUnidad').value = insumoData.unidad;
        document.getElementById('editarIdInsumo').value = id_insumo;

        // Mostrar el modal
        $('#modalEditarInsumo').modal('show');
    } catch (error) {
        console.error('Hubo un problema al intentar obtener el insumo:', error);
    }
}

async function guardarEdicionInsumo() {
    try {
        const id_insumo = document.getElementById('editarIdInsumo').value;
        const clave = document.getElementById('editarClave').value;
        const descripcion = document.getElementById('editarDescripcion').value;
        const unidad = document.getElementById('editarUnidad').value;

        const authToken = localStorage.getItem('authToken');
        if (!authToken) {
            throw new Error('No se encontró el token de autenticación');
        }

        // Decodificar el authToken para obtener usuario y id_usuario
        const { id_usuario } = parseJwt(authToken);

        const insumoEditado = {
            id_insumo: parseInt(id_insumo, 10),
            clave: clave,
            descripcion: descripcion,
            unidad: unidad,
            usuario_id: id_usuario,
        };

        const response = await fetch(`http://localhost:8081/sisadi/insumo/`, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${authToken}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(insumoEditado),
        });

        if (!response.ok) {
            throw new Error('Error al editar el insumo');
        }

        // Cerrar el modal después de editar correctamente
        $('#modalEditarInsumo').modal('hide');

        // Actualizar la tabla después de editar
        await obtenerInsumos();
        console.log(`Insumo editado correctamente`);
    } catch (error) {
        console.error('Hubo un problema al intentar editar el insumo:', error);
    }
}

async function eliminarInsumo(id_insumo) {
    try {
        // Obtener el token almacenado en localStorage
        const authToken = localStorage.getItem('authToken');
        if (!authToken) {
            throw new Error('No se encontró el token de autenticación');
        }

        const response = await fetch(`http://localhost:8081/sisadi/insumo/${id_insumo}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${authToken}`
            }
        });

        if (!response.ok) {
            throw new Error('Error al eliminar el insumo');
        }

        // Actualizar la tabla después de eliminar
        await obtenerInsumos();
        alert(`Insumo eliminado correctamente`);
    } catch (error) {
        console.error('Hubo un problema al intentar eliminar el insumo:', error);
        // Aquí podrías mostrar un mensaje de error al usuario si falla la eliminación
    }
}

function mostrarNombrePersona(nombre) {
    const nombreElemento = document.getElementById('nombre-usuario');
    if (nombreElemento) {
        nombreElemento.textContent = nombre;
    }
}


function limpiarModalNuevoInsumo() {
    document.getElementById('clave').value = '';
    document.getElementById('descripcion').value = '';
    document.getElementById('unidad').value = '';
}

function llenarTabla(insumos) {
    const tbody = document.getElementById('insumos-body');
    tbody.innerHTML = '';  // Limpiar la tabla antes de llenarla

    insumos.forEach(insumo => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${insumo.clave}</td>
            <td>${insumo.descripcion}</td>
            <td>
                <button class="btn btn-warning btn-sm me-3" onclick="editarInsumo(${insumo.id_insumo})"><i class="bi bi-pencil"></i> Editar</button>
                <button class="btn btn-danger btn-sm" onclick="eliminarInsumo(${insumo.id_insumo})"><i class="bi bi-trash"></i> Eliminar</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

// Llamar a la función para obtener los datos cuando se carga la página
document.addEventListener('DOMContentLoaded', obtenerInsumos);