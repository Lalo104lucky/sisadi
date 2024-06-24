// Funciones para obtener datos del usuario y colectivo
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

async function obtenerTransferEntrada() {
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

            // Mostrar datos del colectivo
            const folio = localStorage.getItem('folio') || 'No definido';
            const unidad = localStorage.getItem('unidad') || 'No definido';
            const tipoMovimiento = localStorage.getItem('tipoMovimiento') || 'No definido';
            const fecha = localStorage.getItem('fecha') || 'No definida';

            // Mostrar los datos en los elementos HTML correspondientes
            document.getElementById('nombre-unidad').textContent = unidad;
            document.getElementById('nombre-folio').textContent = folio;
            document.getElementById('nombre-tipo-movimiento').textContent = tipoMovimiento;
            document.getElementById('nombre-fecha').textContent = fecha;
        } else {
            mostrarNombrePersona('Usuario no encontrado');
        }

    } catch (error) {
        console.error('Hubo un problema con la solicitud:', error);
        mostrarNombrePersona('Error al cargar datos del usuario');
    }
}

function parseJwt(token) {
    try {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(atob(base64).split('').map(function (c) {
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

async function obtenerInsumos() {
    try {
        const authToken = localStorage.getItem('authToken');
        if (!authToken) {
            throw new Error('No se encontró el token de autenticación');
        }

        const response = await fetch(`http://localhost:8081/sisadi/insumo/`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${authToken}`
            }
        });

        if (!response.ok) {
            throw new Error('Error al obtener los insumos');
        }

        const data = await response.json();
        return data.data; // Devuelve los datos de los insumos
    } catch (error) {
        console.error('Hubo un problema al obtener los insumos:', error);
        return [];
    }
}


function filtrarInsumos(insumos, clave, descripcion) {
    if (clave) {
        return insumos.filter(insumo => insumo.clave.toString() === clave.toString());
    } else if (descripcion) {
        return insumos.filter(insumo => insumo.descripcion.toLowerCase().includes(descripcion.toLowerCase()));
    }
    return insumos;
}

document.addEventListener('DOMContentLoaded', async () => {
    obtenerTransferEntrada();

    const insumos = await obtenerInsumos();
    llenarDescripciones(insumos);

    // Event listener para buscar insumo
    document.getElementById('agregarInsumo').addEventListener('click', async () => {
        const clave = document.getElementById('folioInput').value;
        const descripcion = document.getElementById('unitSelect').value;
        const buscarPorClave = document.getElementById('customCheck').checked;

        let insumoFiltrado;
        if (buscarPorClave && clave) {
            insumoFiltrado = filtrarInsumos(insumos, clave, null);
        } else if (!buscarPorClave && descripcion) {
            insumoFiltrado = filtrarInsumos(insumos, null, descripcion);
        }

        if (insumoFiltrado && insumoFiltrado.length > 0) {
            const insumoData = insumoFiltrado[0]; // Considerar el primer insumo de la respuesta
            agregarInsumoATabla(insumoData);
        } else {
            console.error('No se encontró el insumo');
        }
    });
});

function llenarDescripciones(insumos) {
    const unitSelect = document.getElementById('unitSelect');
    insumos.forEach(insumo => {
        const option = document.createElement('option');
        option.value = insumo.descripcion;
        option.textContent = insumo.descripcion;
        unitSelect.appendChild(option);
    });
}

// Variables globales para los totales
let totalClaves = 0;
let totalInsumos = 0;
let totalGeneral = 0;

// Función para agregar insumo a la tabla
function agregarInsumoATabla(insumo) {
    const { clave, descripcion, precio, existencia } = insumo;
    const cantidad = parseFloat(document.getElementById('cantidadInput').value);

    const tableBody = document.getElementById('insumos-body');
    const row = document.createElement('tr');

    const total = cantidad * precio;
    row.innerHTML = `
        <td>${clave}</td>
        <td>${descripcion}</td>
        <td>${cantidad}</td>
        <td>$ ${precio.toFixed(2)}</td>
        <td>$ ${total.toFixed(2)}</td>
        <td><button class="btn btn-danger btn-sm" onclick="eliminarFila(this)">Eliminar</button></td>
    `;

    tableBody.appendChild(row);

    // Actualizar totales globales
    totalClaves++;
    totalInsumos += cantidad;
    totalGeneral += total;

    // Actualizar totales mostrados en la tabla
    actualizarTotales();

    // Mostrar detalles del insumo seleccionado
    mostrarDetallesInsumo(insumo);
}

function mostrarDetallesInsumo(insumo) {
    document.getElementById('precioInsumo').textContent = insumo.precio.toFixed(2);
    // Asumiendo que `existencia` es un campo del insumo, actualizar su valor
    //document.getElementById('existenciaInsumo').textContent = insumo.existencia || 'N/A';
}


// Función para actualizar los totales en la tabla HTML
function actualizarTotales() {
    // Seleccionar los th de totales por sus IDs
    const totalClavesTh = document.getElementById('total-claves-cell');
    const totalInsumosTh = document.getElementById('total-insumos-cell');
    const totalGeneralTh = document.getElementById('total-general-cell');

    // Actualizar el contenido de los th con los totales actuales
    totalClavesTh.textContent = totalClaves;
    totalInsumosTh.textContent = totalInsumos; // Mostrar cantidad de insumos con dos decimales
    totalGeneralTh.textContent = totalGeneral.toFixed(2); // Mostrar total general con dos decimales
}


function eliminarFila(button) {
    const row = button.closest('tr');
    const cantidad = parseFloat(row.children[2].textContent); // Obtener la cantidad de la fila eliminada

    // Restar los valores de la fila eliminada de los totales globales
    totalClaves--;
    totalInsumos -= cantidad;
    totalGeneral -= parseFloat(row.children[4].textContent); // Restar el total de la fila eliminada

    // Eliminar la fila del DOM
    row.remove();

    // Actualizar totales mostrados en la tabla
    actualizarTotales();
}

