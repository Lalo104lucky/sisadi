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

async function obtenerProveedor() {
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

async function guardarNuevoProveedor() {
    try {
        const nombre = document.getElementById('nombre').value;
        const rfc = document.getElementById('rfc').value;

        const authToken = localStorage.getItem('authToken');
        if (!authToken) {
            throw new Error('No se encontró el token de autenticación');
        }

        // Decodificar el authToken para obtener usuario y id_usuario
        const { usuario, id_usuario } = parseJwt(authToken);

        const nuevoProveedor = {
            nombre: nombre,
            rfc: rfc,
            usuario_id: id_usuario,
        };

        const response = await fetch('http://localhost:8081/sisadi/proveedor/', {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${authToken}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(nuevoProveedor),
        });

        if (!response.ok) {
            throw new Error('Error al agregar al proveedor');
        }

        // Limpiar el modal después de agregar correctamente
        limpiarModalNuevoProveedor();
        $('#modalNuevoProveedor').modal('hide');

        // Actualizar la tabla después de agregar
        await obtenerProveedor();
    } catch (error) {
        console.error('Hubo un problema al intentar agregar al proveedor:', error);
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

async function editarProveedor(id_proveedor) {
    try {
        const authToken = localStorage.getItem('authToken');
        if (!authToken) {
            throw new Error('No se encontró el token de autenticación');
        }

        const response = await fetch(`http://localhost:8081/sisadi/proveedor/${id_proveedor}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${authToken}`
            }
        });

        if (!response.ok) {
            throw new Error('Error al obtener al proveedor');
        }

        const proveedor = await response.json();

        if (!proveedor || !proveedor.data) {
            throw new Error('Datos de proveedor no válidos');
        }

        const proveedorData = proveedor.data;

        // Rellenar el formulario del modal con los datos del insumo
        document.getElementById('editarNombre').value = proveedorData.nombre;
        document.getElementById('editarRfc').value = proveedorData.rfc;
        document.getElementById('editarIdProveedor').value = id_proveedor;

        // Mostrar el modal
        $('#modalEditarProveedor').modal('show');
    } catch (error) {
        console.error('Hubo un problema al intentar obtener el insumo:', error);
    }
}

async function guardarEdicionProveedor() {
    try {
        const id_proveedor = document.getElementById('editarIdProveedor').value;
        const nombre = document.getElementById('editarNombre').value;
        const rfc = document.getElementById('editarRfc').value;

        const authToken = localStorage.getItem('authToken');
        if (!authToken) {
            throw new Error('No se encontró el token de autenticación');
        }

        // Decodificar el authToken para obtener usuario y id_usuario
        const { id_usuario } = parseJwt(authToken);

        const proveedorEditado = {
            id_proveedor: parseInt(id_proveedor, 10),
            nombre: nombre,
            rfc: rfc,
            usuario_id: id_usuario,
        };

        const response = await fetch(`http://localhost:8081/sisadi/proveedor/`, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${authToken}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(proveedorEditado),
        });

        if (!response.ok) {
            throw new Error('Error al editar al proveedor');
        }

        // Cerrar el modal después de editar correctamente
        $('#modalEditarProveedor').modal('hide');

        // Actualizar la tabla después de editar
        await obtenerProveedor();
        console.log(`Proveedor editado correctamente`);
    } catch (error) {
        console.error('Hubo un problema al intentar editar al proveedor:', error);
    }
}

async function eliminarProveedor(id_proveedor) {
    try {
        // Obtener el token almacenado en localStorage
        const authToken = localStorage.getItem('authToken');
        if (!authToken) {
            throw new Error('No se encontró el token de autenticación');
        }

        const response = await fetch(`http://localhost:8081/sisadi/proveedor/${id_proveedor}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${authToken}`
            }
        });

        if (!response.ok) {
            throw new Error('Error al eliminar al proveedor');
        }

        // Actualizar la tabla después de eliminar
        await obtenerProveedor();
        alert(`Proveedor eliminado correctamente`);
    } catch (error) {
        console.error('Hubo un problema al intentar eliminar al proveedor:', error);
        // Aquí podrías mostrar un mensaje de error al usuario si falla la eliminación
    }
}

function mostrarNombrePersona(nombre) {
    const nombreElemento = document.getElementById('nombre-usuario');
    if (nombreElemento) {
        nombreElemento.textContent = nombre;
    }
}


function limpiarModalNuevoProveedor() {
    document.getElementById('nombre').value = '';
    document.getElementById('rfc').value = '';
}

function llenarTabla(proveedor) {
    const tbody = document.getElementById('proveedor-body');
    tbody.innerHTML = '';  // Limpiar la tabla antes de llenarla

    proveedor.forEach(proveedor => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${proveedor.nombre}</td>
            <td>${proveedor.rfc}</td>
            <td>
                <button class="btn btn-warning btn-sm me-3" onclick="editarProveedor(${proveedor.id_proveedor})"><i class="bi bi-pencil"></i> Editar</button>
                <button class="btn btn-danger btn-sm" onclick="eliminarProveedor(${proveedor.id_proveedor})"><i class="bi bi-trash"></i> Eliminar</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

// Llamar a la función para obtener los datos cuando se carga la página
document.addEventListener('DOMContentLoaded', obtenerProveedor);