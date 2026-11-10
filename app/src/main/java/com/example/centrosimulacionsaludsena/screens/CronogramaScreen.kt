package com.example.centrosimulacionsaludsena.screens

import android.content.Context
import android.widget.Toast
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
import androidx.compose.ui.window.Dialog
import org.json.JSONArray
import org.json.JSONObject

data class ReservaItem(
    var instructor: String,
    var programa: String,
    var ficha: String,
    var ambiente: String,
    var fecha: String,
    var hora: String,
    var observaciones: String,
    var estado: String = "Reservado"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CronogramaScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val senaGreen = Color(0xFF006837)
    val senaGreenDark = Color(0xFF066B2D)
    val bgLight = Color(0xFFF5F7F6)

    val sharedPreferences = remember {
        context.getSharedPreferences("sena_cronograma_prefs", Context.MODE_PRIVATE)
    }

    fun cargarReservasLocales(): MutableList<ReservaItem> {
        val jsonString = sharedPreferences.getString("reservasAmbiente", "[]") ?: "[]"
        val lista = mutableListOf<ReservaItem>()
        try {
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                lista.add(
                    ReservaItem(
                        instructor = obj.optString("instructor"),
                        programa = obj.optString("programa"),
                        ficha = obj.optString("ficha"),
                        ambiente = obj.optString("ambiente"),
                        fecha = obj.optString("fecha"),
                        hora = obj.optString("hora"),
                        observaciones = obj.optString("observaciones"),
                        estado = obj.optString("estado", "Reservado")
                    )
                )
            }
        } catch (_: Exception) {}
        return lista
    }

    fun guardarReservasLocales(lista: List<ReservaItem>) {
        val jsonArray = JSONArray()
        for (r in lista) {
            val obj = JSONObject().apply {
                put("instructor", r.instructor)
                put("programa", r.programa)
                put("ficha", r.ficha)
                put("ambiente", r.ambiente)
                put("fecha", r.fecha)
                put("hora", r.hora)
                put("observaciones", r.observaciones)
                put("estado", r.estado)
            }
            jsonArray.put(obj)
        }
        sharedPreferences.edit().putString("reservasAmbiente", jsonArray.toString()).apply()
    }

    var reservas by remember { mutableStateOf(cargarReservasLocales()) }
    var buscar by remember { mutableStateOf("") }
    var editandoIndex by remember { mutableStateOf<Int?>(null) }

    var instructor by remember { mutableStateOf("") }
    var programa by remember { mutableStateOf("") }
    var ficha by remember { mutableStateOf("") }
    var ambiente by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var observaciones by remember { mutableStateOf("") }

    var ambienteDropdownExpanded by remember { mutableStateOf(false) }
    val ambientesDisponibles = listOf(
        "Laboratorio de Simulación 1",
        "Laboratorio de Simulación 2",
        "Unidad de Urgencias",
        "UCI",
        "Sala de Hospitalización",
        "Sala Materno Infantil"
    )

    var reservaDetalle by remember { mutableStateOf<ReservaItem?>(null) }

    fun limpiarFormulario() {
        instructor = ""
        programa = ""
        ficha = ""
        ambiente = ""
        fecha = ""
        hora = ""
        observaciones = ""
        editandoIndex = null
    }

    fun registrarReserva() {
        if (instructor.isBlank() || programa.isBlank() || ficha.isBlank() || ambiente.isBlank() || fecha.isBlank() || hora.isBlank()) {
            Toast.makeText(context, "Complete todos los campos obligatorios.", Toast.LENGTH_SHORT).show()
            return
        }

        val nueva = ReservaItem(instructor, programa, ficha, ambiente, fecha, hora, observaciones, "Reservado")
        val listaActualizada = reservas.toMutableList()

        if (editandoIndex != null) {
            listaActualizada[editandoIndex!!] = nueva
            Toast.makeText(context, "Reserva actualizada correctamente.", Toast.LENGTH_SHORT).show()
        } else {
            listaActualizada.add(nueva)
            Toast.makeText(context, "Reserva registrada correctamente.", Toast.LENGTH_SHORT).show()
        }

        reservas = listaActualizada
        guardarReservasLocales(reservas)
        limpiarFormulario()
    }

    val reservasFiltradas = reservas.filter { r ->
        val q = buscar.lowercase().trim()
        q.isEmpty() ||
                r.instructor.lowercase().contains(q) ||
                r.programa.lowercase().contains(q) ||
                r.ambiente.lowercase().contains(q) ||
                r.ficha.lowercase().contains(q)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Cronograma de Ambientes", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Gestión y reservas del Centro de Simulación", fontSize = 12.sp, color = Color.White.copy(alpha = 0.9f))
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val totalStr = if (reservas.size < 10) "0${reservas.size}" else "${reservas.size}"
                CronogramaCard("📅", totalStr, "Reservas", Modifier.weight(1f))
                CronogramaCard("🏥", "06", "Ambientes", Modifier.weight(1f))
                CronogramaCard("👨‍🏫", "15", "Instructores", Modifier.weight(1f))
                CronogramaCard("🩺", "04", "Activas", Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        if (editandoIndex != null) "Editar Reserva" else "Registrar Reserva",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = senaGreen
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = instructor,
                            onValueChange = { instructor = it },
                            label = { Text("👨‍🏫 Instructor") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )

                        OutlinedTextField(
                            value = programa,
                            onValueChange = { programa = it },
                            label = { Text("📚 Programa (Ej. Enfermería)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )

                        OutlinedTextField(
                            value = ficha,
                            onValueChange = { ficha = it },
                            label = { Text("🎓 Número de Ficha") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )

                        Box {
                            OutlinedButton(
                                onClick = { ambienteDropdownExpanded = true },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF263238))
                            ) {
                                Text(if (ambiente.isEmpty()) "🏥 Seleccione un ambiente" else ambiente)
                            }
                            DropdownMenu(
                                expanded = ambienteDropdownExpanded,
                                onDismissRequest = { ambienteDropdownExpanded = false }
                            ) {
                                ambientesDisponibles.forEach { amb ->
                                    DropdownMenuItem(
                                        text = { Text(amb) },
                                        onClick = {
                                            ambiente = amb
                                            ambienteDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = fecha,
                                onValueChange = { fecha = it },
                                label = { Text("📅 Fecha (YYYY-MM-DD)") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                shape = RoundedCornerShape(8.dp)
                            )
                            OutlinedTextField(
                                value = hora,
                                onValueChange = { hora = it },
                                label = { Text("🕒 Hora (HH:MM)") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                shape = RoundedCornerShape(8.dp)
                            )
                        }

                        OutlinedTextField(
                            value = observaciones,
                            onValueChange = { observaciones = it },
                            label = { Text("📝 Observaciones adicionales") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            maxLines = 3
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        if (editandoIndex != null) {
                            OutlinedButton(
                                onClick = ::limpiarFormulario,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Cancelar")
                            }
                        }

                        Button(
                            onClick = ::registrarReserva,
                            colors = ButtonDefaults.buttonColors(containerColor = senaGreen),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(if (editandoIndex != null) "💾 Actualizar" else "💾 Registrar Reserva", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = buscar,
                onValueChange = { buscar = it },
                placeholder = { Text("🔍 Buscar por instructor, programa o ambiente...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(30.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text("Listado de Reservas", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF172B24))
            Spacer(modifier = Modifier.height(10.dp))

            if (reservasFiltradas.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Box(modifier = Modifier.fillMaxWidth().padding(30.dp), contentAlignment = Alignment.Center) {
                        Text("No se encontraron reservas registradas.", color = Color.Gray)
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    reservasFiltradas.forEach { r ->
                        val realIndex = reservas.indexOf(r)
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(r.ambiente, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = senaGreen)
                                    Surface(color = Color(0xFFE5F5EB), shape = RoundedCornerShape(12.dp)) {
                                        Text(r.estado, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = senaGreen, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))
                                Text("👨‍🏫 Instructor: ${r.instructor}", fontSize = 13.sp, color = Color(0xFF4A5568))
                                Text("📚 Programa: ${r.programa} (Ficha: ${r.ficha})", fontSize = 13.sp, color = Color(0xFF4A5568))
                                Text("📅 Fecha: ${r.fecha}  |  🕒 Hora: ${r.hora}", fontSize = 13.sp, color = Color(0xFF4A5568))

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
                                ) {
                                    Button(
                                        onClick = { reservaDetalle = r },
                                        colors = ButtonDefaults.buttonColors(containerColor = senaGreen),
                                        shape = RoundedCornerShape(6.dp),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text("Ver", fontSize = 12.sp)
                                    }

                                    Button(
                                        onClick = {
                                            instructor = r.instructor
                                            programa = r.programa
                                            ficha = r.ficha
                                            ambiente = r.ambiente
                                            fecha = r.fecha
                                            hora = r.hora
                                            observaciones = r.observaciones
                                            editandoIndex = realIndex
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                                        shape = RoundedCornerShape(6.dp),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text("Editar", fontSize = 12.sp)
                                    }

                                    Button(
                                        onClick = {
                                            val mutableList = reservas.toMutableList()
                                            mutableList.removeAt(realIndex)
                                            reservas = mutableList
                                            guardarReservasLocales(reservas)
                                            Toast.makeText(context, "Reserva eliminada", Toast.LENGTH_SHORT).show()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC3545)),
                                        shape = RoundedCornerShape(6.dp),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text("Eliminar", fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    reservaDetalle?.let { det ->
        Dialog(onDismissRequest = { reservaDetalle = null }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                modifier = Modifier.fillMaxWidth().padding(10.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Detalle de la Reserva", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = senaGreen)
                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Instructor: ${det.instructor}", fontSize = 14.sp)
                    Text("Programa: ${det.programa}", fontSize = 14.sp)
                    Text("Ficha: ${det.ficha}", fontSize = 14.sp)
                    Text("Ambiente: ${det.ambiente}", fontSize = 14.sp)
                    Text("Fecha: ${det.fecha}", fontSize = 14.sp)
                    Text("Hora: ${det.hora}", fontSize = 14.sp)
                    Text("Estado: ${det.estado}", fontSize = 14.sp)
                    Text("Observaciones: ${det.observaciones.ifEmpty { "Ninguna" }}", fontSize = 14.sp)

                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { reservaDetalle = null },
                        colors = ButtonDefaults.buttonColors(containerColor = senaGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Cerrar")
                    }
                }
            }
        }
    }
}

@Composable
fun CronogramaCard(icon: String, value: String, label: String, modifier: Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(icon, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0A8F3C))
            Text(label, fontSize = 10.sp, color = Color(0xFF555555), maxLines = 1)
        }
    }
}