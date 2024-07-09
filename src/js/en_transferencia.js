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
    await obtenerTransferEntrada();

    const insumos = await obtenerInsumos();
    llenarDescripciones(insumos);

    const operacionId = localStorage.getItem('operacionId');
    console.log('ID de la operación:', operacionId);

    document.querySelector('.btn-cerrar').addEventListener('click', () => {
        window.location.href = 'transferencia_entrada.html';
    });

    document.querySelector('.btn-cancel').addEventListener('click', async () => {
        try {
            await cancelarOperacion(operacionId);
            alert('Operación cancelada correctamente');
            window.location.href = 'transferencia_entrada.html';
        } catch (error) {
            console.error('Hubo un problema al cancelar la operación:', error);
            alert('Error al cancelar la operación. Por favor, inténtalo de nuevo.');
        }
    });

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
            const cantidad = parseFloat(document.getElementById('cantidadInput').value);
            const total = cantidad * insumoData.precio;

            const entradaData = {
                cantidad: cantidad.toString(),
                total: total.toFixed(2),
                operacion_id: operacionId,
                insumos_id: [insumoData.id_insumo]
            };

            try {
                const authToken = localStorage.getItem('authToken');
                const response = await fetch('http://localhost:8081/sisadi/entradas/', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${authToken}`
                    },
                    body: JSON.stringify(entradaData)
                });

                if (!response.ok) {
                    throw new Error('Error al crear la entrada');
                }

                const responseData = await response.json();
                console.log('Respuesta del servidor:', responseData);

                const idEntradas = responseData.data.id_entradas;
                agregarInsumoATabla(insumoData, idEntradas);
            } catch (error) {
                console.error('Hubo un problema al crear la entrada:', error);
            }

        } else {
            alert('No se encontró el insumo');
        }
    });
});

async function cancelarOperacion(operacionId) {
    try {
        const authToken = localStorage.getItem('authToken');
        if (!authToken) {
            throw new Error('No se encontró el token de autenticación');
        }

        const response = await fetch(`http://localhost:8081/sisadi/operacion/${operacionId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${authToken}`
            }
        });

        if (!response.ok) {
            throw new Error('Error al cancelar la operación');
        }

        console.log('Operación cancelada correctamente');
    } catch (error) {
        console.error('Hubo un problema al cancelar la operación:', error);
        throw error;
    }
}

function llenarDescripciones(insumos) {
    const unitSelect = document.getElementById('unitSelect');
    insumos.forEach(insumo => {
        const option = document.createElement('option');
        option.value = insumo.descripcion;
        option.textContent = insumo.descripcion;
        unitSelect.appendChild(option);
    });
}

let totalClaves = 0;
let totalInsumos = 0;
let totalGeneral = 0;

function agregarInsumoATabla(insumo, id_entradas) {
    const { clave, descripcion, precio } = insumo;
    const cantidad = parseFloat(document.getElementById('cantidadInput').value);

    if (isNaN(cantidad) || isNaN(precio)) {
        console.error('Cantidad o precio no son números válidos:', cantidad, precio);
        return;
    }

    const tableBody = document.getElementById('insumos-body');
    const row = document.createElement('tr');
    
    row.dataset.entradaId = id_entradas; 
    
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

    totalClaves++;
    totalInsumos += cantidad;
    totalGeneral += total;

    actualizarTotales();

    mostrarDetallesInsumo(insumo);
}

function mostrarDetallesInsumo(insumo) {
    document.getElementById('precioInsumo').textContent = insumo.precio.toFixed(2);
}


function actualizarTotales() {
    const totalClavesTh = document.getElementById('total-claves-cell');
    const totalInsumosTh = document.getElementById('total-insumos-cell');
    const totalGeneralTh = document.getElementById('total-general-cell');

    totalClavesTh.textContent = totalClaves;
    totalInsumosTh.textContent = totalInsumos.toFixed(2);
    totalGeneralTh.textContent = totalGeneral.toFixed(2);
}

function eliminarFila(button) {
    const row = button.closest('tr');
    const entradaId = row.dataset.entradaId; 

    eliminarEntrada(entradaId)
        .then(() => {
            const cantidad = parseFloat(row.children[2].textContent); 
            const total = parseFloat(row.children[4].textContent.replace('$', '').trim());

            if (isNaN(cantidad) || isNaN(total)) {
                console.error('Cantidad o total no son números válidos:', cantidad, total);
                return;
            }

            totalClaves--;
            totalInsumos -= cantidad;
            totalGeneral -= total;

            // Evitar que el totalGeneral sea negativo o -0.00
            if (totalGeneral < 0) {
                totalGeneral = 0;
            }

            row.remove();

            actualizarTotales();
        })
        .catch(error => {
            console.error('Hubo un problema al eliminar la entrada:', error);
            alert('Error al eliminar la entrada. Por favor, inténtalo de nuevo.');
        });
}

async function eliminarEntrada(entradaId) {
    try {
        const authToken = localStorage.getItem('authToken');
        if (!authToken) {
            throw new Error('No se encontró el token de autenticación');
        }

        const response = await fetch(`http://localhost:8081/sisadi/entradas/${entradaId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${authToken}`
            }
        });

        if (!response.ok) {
            throw new Error('Error al eliminar la entrada');
        }

        alert('Entrada eliminada correctamente');
    } catch (error) {
        console.error('Hubo un problema al eliminar la entrada:', error);
        throw error; 
    }
}
