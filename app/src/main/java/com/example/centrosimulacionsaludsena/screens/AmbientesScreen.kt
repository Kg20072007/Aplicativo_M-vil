package com.example.centrosimulacionsaludsena.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

data class AmbienteItem(
    val id: Int,
    val nombre: String,
    val capacidad: Int,
    val estado: String,
    val ubicacion: String,
    val imagen: String,
    val descripcion: String,
    val equipos: List<String>
)

data class ReservaAmbiente(
    val id: String,
    val instructor: String,
    val correo: String,
    val programa: String,
    val ficha: String,
    val ambiente: String,
    val estudiantes: Int,
    val fecha: String,
    val hora: String,
    val horaFin: String,
    val observaciones: String,
    var estado: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmbientesScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val senaGreen = Color(0xFF006837)
    val senaGreenDark = Color(0xFF066B2D)
    val bgLight = Color(0xFFF5F7F6)

    val ambientesLista = remember {
        listOf(
            AmbienteItem(1, "Sala de Monitoreo Clínico", 10, "Disponible", "Bloque de simulación · Sala 101", "https://images.unsplash.com/photo-1579684385127-1ef15d508118?auto=format&fit=crop&w=900&q=80", "Espacio para practicar la valoración del paciente y el seguimiento de signos vitales.", listOf("Monitor de signos vitales", "Camilla clínica", "Tensiómetro")),
            AmbienteItem(2, "Sala de Procedimientos", 12, "Disponible", "Bloque de simulación · Sala 102", "https://images.unsplash.com/photo-1584982751601-97dcc096659c?auto=format&fit=crop&w=900&q=80", "Ambiente para practicar procedimientos básicos y técnicas de enfermería.", listOf("Camilla", "Carro de curaciones", "Instrumental clínico")),
            AmbienteItem(3, "Laboratorio de Enfermería", 15, "En uso", "Bloque de simulación · Sala 103", "https://images.unsplash.com/photo-1581056771107-24ca5f033842?auto=format&fit=crop&w=900&q=80", "Espacio de aprendizaje práctico para el desarrollo de competencias clínicas.", listOf("Maniquí de práctica", "Camillas", "Material de enfermería")),
            AmbienteItem(4, "Sala de RCP", 8, "Disponible", "Bloque de simulación · Sala 104", "https://images.unsplash.com/photo-1511174511562-5f7f18b874f8?auto=format&fit=crop&w=900&q=80", "Ambiente para entrenamiento en reanimación cardiopulmonar y atención de emergencias.", listOf("Maniquí RCP", "Desfibrilador de entrenamiento", "Botiquín"))
        )
    }

    val sharedPreferences = remember {
        context.getSharedPreferences("sena_cronograma_prefs", Context.MODE_PRIVATE)
    }

    fun leerReservas(): MutableList<ReservaAmbiente> {
        val jsonString = sharedPreferences.getString("reservasAmbiente", "[]") ?: "[]"
        val lista = mutableListOf<ReservaAmbiente>()
        try {
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                lista.add(
                    ReservaAmbiente(
                        id = obj.optString("id", "${System.currentTimeMillis()}-$i"),
                        instructor = obj.optString("instructor"),
                        correo = obj.optString("correo"),
                        programa = obj.optString("programa"),
                        ficha = obj.optString("ficha"),
                        ambiente = obj.optString("ambiente"),
                        estudiantes = obj.optInt("estudiantes", 1),
                        fecha = obj.optString("fecha"),
                        hora = obj.optString("hora"),
                        horaFin = obj.optString("horaFin"),
                        observaciones = obj.optString("observaciones"),
                        estado = obj.optString("estado", "Reservado")
                    )
                )
            }
        } catch (_: Exception) {}
        return lista
    }

    fun guardarReservas(lista: List<ReservaAmbiente>) {
        val jsonArray = JSONArray()
        for (r in lista) {
            val obj = JSONObject().apply {
                put("id", r.id)
                put("instructor", r.instructor)
                put("correo", r.correo)
                put("programa", r.programa)
                put("ficha", r.ficha)
                put("ambiente", r.ambiente)
                put("estudiantes", r.estudiantes)
                put("fecha", r.fecha)
                put("hora", r.hora)
                put("horaFin", r.horaFin)
                put("observaciones", r.observaciones)
                put("estado", r.estado)
            }
            jsonArray.put(obj)
        }
        sharedPreferences.edit().putString("reservasAmbiente", jsonArray.toString()).apply()
    }

    var reservas by remember { mutableStateOf(leerReservas()) }
    var ambienteSeleccionado by remember { mutableStateOf(ambientesLista[0]) }
    var busqueda by remember { mutableStateOf("") }
    var filtroEstado by remember { mutableStateOf("Todos") }

    // Formulario de reserva
    var instructor by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var programa by remember { mutableStateOf("") }
    var ficha by remember { mutableStateOf("") }
    var ambienteSeleccionadoForm by remember { mutableStateOf(ambientesLista[0].nombre) }
    var estudiantes by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var horaInicio by remember { mutableStateOf("") }
    var horaFin by remember { mutableStateOf("") }
    var observaciones by remember { mutableStateOf("") }

    var dropdownExpanded by remember { mutableStateOf(false) }

    val hoyStr = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()) }

    val ambientesFiltrados = remember(busqueda, filtroEstado) {
        ambientesLista.filter { amb ->
            val coincideNombre = amb.nombre.lowercase().contains(busqueda.lowercase().trim())
            val coincideFiltro = filtroEstado == "Todos" || amb.estado == filtroEstado
            coincideNombre && coincideFiltro
        }
    }

    val reservasFuturas = remember(reservas) {
        reservas.filter { r ->
            r.fecha >= hoyStr && r.estado != "Cancelado" && r.estado != "Cancelada"
        }.sortedBy { "${it.fecha} ${it.hora}" }.take(5)
    }

    val disponiblesCount = ambientesLista.count { it.estado == "Disponible" }

    fun solicitarReserva() {
        val ambObj = ambientesLista.find { it.nombre == ambienteSeleccionadoForm }
        if (ambObj == null || ambObj.estado != "Disponible") {
            Toast.makeText(context, "Selecciona un ambiente válido y disponible.", Toast.LENGTH_SHORT).show()
            return
        }
        if (instructor.isBlank() || correo.isBlank() || programa.isBlank() || ficha.isBlank() || estudiantes.isBlank() || fecha.isBlank() || horaInicio.isBlank() || horaFin.isBlank()) {
            Toast.makeText(context, "Completa todos los campos obligatorios (*).", Toast.LENGTH_SHORT).show()
            return
        }
        val cant = estudiantes.toIntOrNull() ?: 0
        if (cant < 1 || cant > ambObj.capacidad) {
            Toast.makeText(context, "Capacidad máxima para este ambiente es de ${ambObj.capacidad} personas.", Toast.LENGTH_LONG).show()
            return
        }

        val nueva = ReservaAmbiente(
            id = "${System.currentTimeMillis()}",
            instructor = instructor,
            correo = correo,
            programa = programa,
            ficha = ficha,
            ambiente = ambienteSeleccionadoForm,
            estudiantes = cant,
            fecha = fecha,
            hora = horaInicio,
            horaFin = horaFin,
            observaciones = observaciones,
            estado = "Reservado"
        )

        val listaActualizada = reservas.toMutableList()
        listaActualizada.add(nueva)
        reservas = listaActualizada
        guardarReservas(reservas)

        Toast.makeText(context, "¡Solicitud registrada correctamente!", Toast.LENGTH_SHORT).show()
        instructor = ""
        correo = ""
        programa = ""
        ficha = ""
        estudiantes = ""
        fecha = ""
        horaInicio = ""
        horaFin = ""
        observaciones = ""
    }

    fun cancelarReserva(id: String) {
        val listaActualizada = reservas.map { r ->
            if (r.id == id) r.copy(estado = "Cancelado") else r
        }.toMutableList()
        reservas = listaActualizada
        guardarReservas(reservas)
        Toast.makeText(context, "Reserva cancelada.", Toast.LENGTH_SHORT).show()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Ambientes de Simulación", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Gestión de espacios y reservas del SENA", fontSize = 12.sp, color = Color.White.copy(alpha = 0.9f))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = senaGreenDark)
            )
        },
        containerColor = bgLight
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            // Métrica de Resumen
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AmbienteMetricaCard("🏥", "${ambientesLista.size}", "Registrados", Modifier.weight(1f))
                AmbienteMetricaCard("✓", "$disponiblesCount", "Disponibles", Modifier.weight(1f))
                AmbienteMetricaCard("📅", "${reservasFuturas.size}", "Próximas", Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Explorar Ambientes
            Text("Explorar ambientes", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF172B24))
            Text("Selecciona un espacio para consultar sus características.", fontSize = 12.sp, color = Color(0xFF718078))

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = busqueda,
                    onValueChange = { busqueda = it },
                    placeholder = { Text("Buscar ambiente...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                Box {
                    OutlinedButton(
                        onClick = { dropdownExpanded = true },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(if (filtroEstado == "Todos") "Todos los estados" else filtroEstado, fontSize = 12.sp)
                    }
                    DropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false }
                    ) {
                        DropdownMenuItem(text = { Text("Todos los estados") }, onClick = { filtroEstado = "Todos"; dropdownExpanded = false })
                        DropdownMenuItem(text = { Text("Disponible") }, onClick = { filtroEstado = "Disponible"; dropdownExpanded = false })
                        DropdownMenuItem(text = { Text("En uso") }, onClick = { filtroEstado = "En uso"; dropdownExpanded = false })
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (ambientesFiltrados.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().padding(30.dp), contentAlignment = Alignment.Center) {
                    Text("No se encontraron ambientes con esos criterios.", color = Color.Gray)
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    ambientesFiltrados.forEach { ambiente ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (ambienteSeleccionado.id == ambiente.id) Color(0xFFF2F9F4) else Color.White
                            ),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Surface(
                                    color = if (ambiente.estado == "Disponible") Color(0xFFE5F5EB) else Color(0xFFFFF3DC),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Text(
                                        text = "● ${ambiente.estado}",
                                        color = if (ambiente.estado == "Disponible") senaGreen else Color(0xFF94601A),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Text(ambiente.nombre, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF172B24))
                                Text("👥 Capacidad: ${ambiente.capacidad} personas", fontSize = 12.sp, color = Color(0xFF718078))

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = { ambienteSeleccionado = ambiente },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Ver detalles", fontSize = 12.sp)
                                    }

                                    Button(
                                        onClick = {
                                            ambienteSeleccionado = ambiente
                                            ambienteSeleccionadoForm = ambiente.nombre
                                        },
                                        enabled = ambiente.estado == "Disponible",
                                        colors = ButtonDefaults.buttonColors(containerColor = senaGreen),
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Reservar", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Detalle del ambiente seleccionado
            Text("Detalle del ambiente", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF172B24))
            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Surface(
                        color = if (ambienteSeleccionado.estado == "Disponible") Color(0xFFE5F5EB) else Color(0xFFFFF3DC),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = ambienteSeleccionado.estado,
                            color = if (ambienteSeleccionado.estado == "Disponible") senaGreen else Color(0xFF94601A),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(ambienteSeleccionado.nombre, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF172B24))
                    Text("📍 ${ambienteSeleccionado.ubicacion}", fontSize = 13.sp, color = Color(0xFF718078))

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(ambienteSeleccionado.descripcion, fontSize = 13.sp, color = Color(0xFF4A5568))

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Equipos disponibles", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = senaGreen)
                    Spacer(modifier = Modifier.height(6.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        ambienteSeleccionado.equipos.forEach { eq ->
                            Text("✓ $eq", fontSize = 12.sp, color = Color(0xFF4A5568))
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text("Capacidad máxima: ${ambienteSeleccionado.capacidad} personas.", fontSize = 12.sp, color = Color(0xFF718078))

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { ambienteSeleccionadoForm = ambienteSeleccionado.nombre },
                        enabled = ambienteSeleccionado.estado == "Disponible",
                        colors = ButtonDefaults.buttonColors(containerColor = senaGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Reservar este ambiente", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Próximas reservas
            Text("Próximas reservas", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF172B24))
            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (reservasFuturas.isEmpty()) {
                        Box(modifier = Modifier.fillMaxWidth().padding(20.dp), contentAlignment = Alignment.Center) {
                            Text("Todavía no hay próximas reservas registradas.", color = Color.Gray, fontSize = 13.sp)
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            reservasFuturas.forEach { res ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(res.ambiente, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF172B24))
                                        Text("${res.instructor} · ${res.fecha} · ${res.hora} - ${res.horaFin}", fontSize = 11.sp, color = Color(0xFF718078))
                                    }
                                    Button(
                                        onClick = { cancelarReserva(res.id) },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC3545)),
                                        shape = RoundedCornerShape(6.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text("Cancelar", fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Formulario de Solicitud de Reserva
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Solicitar una reserva", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = senaGreen)
                    Text("Completa los datos para registrar la solicitud del ambiente.", fontSize = 12.sp, color = Color(0xFF718078))

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = instructor,
                            onValueChange = { instructor = it },
                            label = { Text("Nombre del instructor *") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )

                        OutlinedTextField(
                            value = correo,
                            onValueChange = { correo = it },
                            label = { Text("Correo electrónico *") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )

                        OutlinedTextField(
                            value = programa,
                            onValueChange = { programa = it },
                            label = { Text("Programa de formación *") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )

                        OutlinedTextField(
                            value = ficha,
                            onValueChange = { ficha = it },
                            label = { Text("Número de ficha *") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )

                        // Selector Ambiente Form
                        var ambDropdown by remember { mutableStateOf(false) }
                        Box {
                            OutlinedButton(
                                onClick = { ambDropdown = true },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Ambiente: $ambienteSeleccionadoForm")
                            }
                            DropdownMenu(
                                expanded = ambDropdown,
                                onDismissRequest = { ambDropdown = false }
                            ) {
                                ambientesLista.forEach { amb ->
                                    DropdownMenuItem(
                                        text = { Text("${amb.nombre} (${amb.estado})") },
                                        enabled = amb.estado == "Disponible",
                                        onClick = {
                                            ambienteSeleccionadoForm = amb.nombre
                                            ambDropdown = false
                                        }
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = estudiantes,
                            onValueChange = { estudiantes = it },
                            label = { Text("Cantidad de estudiantes *") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )

                        OutlinedTextField(
                            value = fecha,
                            onValueChange = { fecha = it },
                            label = { Text("Fecha (YYYY-MM-DD) *") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = horaInicio,
                                onValueChange = { horaInicio = it },
                                label = { Text("Hora inicio *") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                shape = RoundedCornerShape(8.dp)
                            )
                            OutlinedTextField(
                                value = horaFin,
                                onValueChange = { horaFin = it },
                                label = { Text("Hora fin *") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                shape = RoundedCornerShape(8.dp)
                            )
                        }

                        OutlinedTextField(
                            value = observaciones,
                            onValueChange = { observaciones = it },
                            label = { Text("Observaciones") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            maxLines = 3
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = ::solicitarReserva,
                        colors = ButtonDefaults.buttonColors(containerColor = senaGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Registrar reserva", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun AmbienteMetricaCard(icon: String, value: String, label: String, modifier: Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(icon, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF006837))
            Text(label, fontSize = 10.sp, color = Color(0xFF718078), maxLines = 1)
        }
    }
}