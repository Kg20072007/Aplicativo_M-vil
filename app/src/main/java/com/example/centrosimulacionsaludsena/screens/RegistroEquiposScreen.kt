package com.example.centrosimulacionsaludsena.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

data class EquipoItem(
    val id: String,
    val nombre: String,
    val placa: String,
    val precio: Double
)

data class PrestamoEquipo(
    val id: Long,
    val municipio: String,
    val instructor: String,
    val ficha: String,
    val programa: String,
    val fechaSalida: String,
    val fechaDevolucion: String,
    val diasPrestamo: Int,
    var equipos: List<EquipoPrestado>,
    var estado: String
)

data class EquipoPrestado(
    val nombre: String,
    val placa: String,
    val precio: Double,
    var estado: String,
    var fechaRealDevolucion: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroEquiposScreen(
    onBackClick: () -> Unit = {},
    onNavigateCalendario: () -> Unit = {}
) {
    val context = LocalContext.current
    val senaGreen = Color(0xFF006837)
    val senaGreenDark = Color(0xFF066B2D)
    val bgLight = Color(0xFFF5F7F6)

    val municipiosCauca = listOf(
        "Almaguer", "Argelia", "Balboa", "Bolívar", "Buenos Aires", "Cajibío",
        "Caldono", "Caloto", "Corinto", "El Tambo", "Florencia", "Guachené",
        "Guapi", "Inzá", "Jambaló", "La Sierra", "La Vega", "López de Micay",
        "Mercaderes", "Miranda", "Morales", "Padilla", "Páez", "Patía",
        "Piamonte", "Piendamó - Tunía", "Popayán", "Puerto Tejada", "Puracé",
        "Rosas", "San Sebastián", "Santa Rosa", "Santander de Quilichao",
        "Silvia", "Sotará", "Suárez", "Sucre", "Timbío", "Timbiquí",
        "Toribío", "Totoró", "Villa Rica"
    )

    val tiposEquipos = listOf(
        "Camilla",
        "Monitor multiparámetro",
        "Desfibrilador",
        "Bomba de infusión",
        "Aspirador de secreciones",
        "Mesa instrumental"
    )

    val sharedPreferences = remember {
        context.getSharedPreferences("sena_equipos_prefs", Context.MODE_PRIVATE)
    }

    fun cargarPrestamos(): MutableList<PrestamoEquipo> {
        val jsonString = sharedPreferences.getString("usosEquipos", "[]") ?: "[]"
        val lista = mutableListOf<PrestamoEquipo>()
        try {
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val eqArray = obj.optJSONArray("equipos") ?: JSONArray()
                val listEq = mutableListOf<EquipoPrestado>()
                for (j in 0 until eqArray.length()) {
                    val eqObj = eqArray.getJSONObject(j)
                    listEq.add(
                        EquipoPrestado(
                            nombre = eqObj.optString("nombre"),
                            placa = eqObj.optString("placa"),
                            precio = eqObj.optDouble("precio", 0.0),
                            estado = eqObj.optString("estado", "Prestado"),
                            fechaRealDevolucion = eqObj.optString("fechaRealDevolucion", "")
                        )
                    )
                }
                lista.add(
                    PrestamoEquipo(
                        id = obj.optLong("id", System.currentTimeMillis()),
                        municipio = obj.optString("municipio"),
                        instructor = obj.optString("instructor"),
                        ficha = obj.optString("ficha"),
                        programa = obj.optString("programa"),
                        fechaSalida = obj.optString("fechaSalida"),
                        fechaDevolucion = obj.optString("fechaDevolucion"),
                        diasPrestamo = obj.optInt("diasPrestamo", 1),
                        equipos = listEq,
                        estado = obj.optString("estado", "Prestado")
                    )
                )
            }
        } catch (_: Exception) {}
        return lista
    }

    fun guardarPrestamos(lista: List<PrestamoEquipo>) {
        val jsonArray = JSONArray()
        for (p in lista) {
            val obj = JSONObject().apply {
                put("id", p.id)
                put("municipio", p.municipio)
                put("instructor", p.instructor)
                put("ficha", p.ficha)
                put("programa", p.programa)
                put("fechaSalida", p.fechaSalida)
                put("fechaDevolucion", p.fechaDevolucion)
                put("diasPrestamo", p.diasPrestamo)
                put("estado", p.estado)

                val eqArray = JSONArray()
                for (e in p.equipos) {
                    eqArray.put(JSONObject().apply {
                        put("nombre", e.nombre)
                        put("placa", e.placa)
                        put("precio", e.precio)
                        put("estado", e.estado)
                        put("fechaRealDevolucion", e.fechaRealDevolucion)
                    })
                }
                put("equipos", eqArray)
            }
            jsonArray.put(obj)
        }
        sharedPreferences.edit().putString("usosEquipos", jsonArray.toString()).apply()
    }

    var prestamos by remember { mutableStateOf(cargarPrestamos()) }

    var municipio by remember { mutableStateOf("Popayán") }
    var instructor by remember { mutableStateOf("") }
    var ficha by remember { mutableStateOf("") }
    var programa by remember { mutableStateOf("") }
    var fechaSalida by remember { mutableStateOf("") }
    var fechaDevolucion by remember { mutableStateOf("") }

    var equipoSeleccionado by remember { mutableStateOf(tiposEquipos[0]) }
    var placa by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var equiposSeleccionados by remember { mutableStateOf(listOf<EquipoItem>()) }

    var municipioExpanded by remember { mutableStateOf(false) }
    var tipoEquipoExpanded by remember { mutableStateOf(false) }

    val formatoCOP: (Double) -> String = { valor ->
        val format = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CO"))
        format.maximumFractionDigits = 0
        format.format(valor)
    }

    val totalSeleccionado = equiposSeleccionados.sumOf { it.precio }

    fun agregarEquipoLocal() {
        val placaLimpia = placa.trim().uppercase()
        if (placaLimpia.isEmpty()) {
            Toast.makeText(context, "Escribe la placa del equipo.", Toast.LENGTH_SHORT).show()
            return
        }
        val precioVal = precio.toDoubleOrNull()
        if (precioVal == null || precioVal < 0) {
            Toast.makeText(context, "Ingresa un precio unitario válido.", Toast.LENGTH_SHORT).show()
            return
        }
        if (equiposSeleccionados.any { it.placa == placaLimpia }) {
            Toast.makeText(context, "Ese equipo ya está agregado al préstamo.", Toast.LENGTH_SHORT).show()
            return
        }

        val yaPrestado = prestamos.any { p ->
            p.equipos.any { e -> e.placa.uppercase() == placaLimpia && e.estado == "Prestado" }
        }
        if (yaPrestado) {
            Toast.makeText(context, "El equipo con placa $placaLimpia ya se encuentra prestado.", Toast.LENGTH_LONG).show()
            return
        }

        val nuevo = EquipoItem(
            id = "${System.currentTimeMillis()}-${Math.random()}",
            nombre = equipoSeleccionado,
            placa = placaLimpia,
            precio = precioVal
        )
        equiposSeleccionados = equiposSeleccionados + nuevo
        placa = ""
        precio = ""
    }

    fun registrarPrestamo() {
        if (instructor.isBlank() || ficha.isBlank() || programa.isBlank() || fechaSalida.isBlank() || fechaDevolucion.isBlank()) {
            Toast.makeText(context, "Por favor completa todos los datos del préstamo.", Toast.LENGTH_SHORT).show()
            return
        }
        if (equiposSeleccionados.isEmpty()) {
            Toast.makeText(context, "Debes agregar por lo menos un equipo.", Toast.LENGTH_SHORT).show()
            return
        }
        if (fechaDevolucion < fechaSalida) {
            Toast.makeText(context, "La fecha de devolución no puede ser anterior a la fecha de salida.", Toast.LENGTH_LONG).show()
            return
        }

        val inicio = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(fechaSalida)?.time ?: 0L
        val fin = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(fechaDevolucion)?.time ?: 0L
        val dias = maxOf(1, ((fin - inicio) / (1000 * 60 * 60 * 24)).toInt())

        val nuevoPrestamo = PrestamoEquipo(
            id = System.currentTimeMillis(),
            municipio = municipio,
            instructor = instructor.trim(),
            ficha = ficha.trim(),
            programa = programa.trim(),
            fechaSalida = fechaSalida,
            fechaDevolucion = fechaDevolucion,
            diasPrestamo = dias,
            equipos = equiposSeleccionados.map {
                EquipoPrestado(
                    nombre = it.nombre,
                    placa = it.placa,
                    precio = it.precio,
                    estado = "Prestado"
                )
            },
            estado = "Prestado"
        )

        val actualizadas = prestamos.toMutableList()
        actualizadas.add(nuevoPrestamo)
        prestamos = actualizadas
        guardarPrestamos(prestamos)

        Toast.makeText(context, "¡Préstamo registrado correctamente!", Toast.LENGTH_SHORT).show()

        instructor = ""
        ficha = ""
        programa = ""
        fechaSalida = ""
        fechaDevolucion = ""
        equiposSeleccionados = emptyList()
    }

    fun devolverEquipo(idPrestamo: Long, placaEquipo: String) {
        val actualizadas = prestamos.map { p ->
            if (p.id != idPrestamo) p
            else {
                val listaEqs = p.equipos.map { e ->
                    if (e.placa.equals(placaEquipo, ignoreCase = true)) {
                        e.copy(estado = "Disponible", fechaRealDevolucion = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()))
                    } else e
                }
                val quedanPrestados = listaEqs.any { it.estado == "Prestado" }
                p.copy(equipos = listaEqs, estado = if (quedanPrestados) "Prestado" else "Finalizado")
            }
        }.toMutableList()
        prestamos = actualizadas
        guardarPrestamos(prestamos)
        Toast.makeText(context, "El equipo $placaEquipo fue registrado como devuelto.", Toast.LENGTH_SHORT).show()
    }

    val equiposPrestadosGlobal = prestamos.filter { p ->
        p.equipos.any { it.estado == "Prestado" }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Control de equipos", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Registro y seguimiento de equipos de simulación", fontSize = 12.sp, color = Color.White.copy(alpha = 0.9f))
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
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Información del préstamo", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = senaGreen)
                    Text("Completa los datos antes de agregar los equipos.", fontSize = 12.sp, color = Color(0xFF718078))

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Box {
                            OutlinedButton(
                                onClick = { municipioExpanded = true },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Municipio: $municipio", color = Color(0xFF24332A))
                            }
                            DropdownMenu(
                                expanded = municipioExpanded,
                                onDismissRequest = { municipioExpanded = false }
                            ) {
                                municipiosCauca.forEach { mun ->
                                    DropdownMenuItem(
                                        text = { Text(mun) },
                                        onClick = {
                                            municipio = mun
                                            municipioExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = instructor,
                            onValueChange = { instructor = it },
                            label = { Text("Instructor responsable *") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )

                        OutlinedTextField(
                            value = ficha,
                            onValueChange = { ficha = it },
                            label = { Text("Ficha de formación *") },
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

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = fechaSalida,
                                onValueChange = { fechaSalida = it },
                                label = { Text("Fecha salida (YYYY-MM-DD)") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                shape = RoundedCornerShape(8.dp)
                            )
                            OutlinedTextField(
                                value = fechaDevolucion,
                                onValueChange = { fechaDevolucion = it },
                                label = { Text("Fecha devolución") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                shape = RoundedCornerShape(8.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Seleccionar equipos", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = senaGreen)
                    Text("Ingresa la placa y el precio real de cada equipo.", fontSize = 12.sp, color = Color(0xFF718078))

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Box {
                            OutlinedButton(
                                onClick = { tipoEquipoExpanded = true },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Tipo: $equipoSeleccionado", color = Color(0xFF24332A))
                            }
                            DropdownMenu(
                                expanded = tipoEquipoExpanded,
                                onDismissRequest = { tipoEquipoExpanded = false }
                            ) {
                                tiposEquipos.forEach { tipo ->
                                    DropdownMenuItem(
                                        text = { Text(tipo) },
                                        onClick = {
                                            equipoSeleccionado = tipo
                                            tipoEquipoExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = placa,
                            onValueChange = { placa = it },
                            label = { Text("Placa (Ej. EQ-024)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )

                        OutlinedTextField(
                            value = precio,
                            onValueChange = { precio = it },
                            label = { Text("Precio unitario (COP)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )

                        Button(
                            onClick = ::agregarEquipoLocal,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE6F3EA), contentColor = senaGreen),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("+ Agregar equipo", fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Equipos seleccionados (${equiposSeleccionados.size})", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = senaGreen)
                    Spacer(modifier = Modifier.height(8.dp))

                    if (equiposSeleccionados.isEmpty()) {
                        Box(modifier = Modifier.fillMaxWidth().padding(20.dp), contentAlignment = Alignment.Center) {
                            Text("Todavía no has agregado equipos.", color = Color.Gray, fontSize = 13.sp)
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            equiposSeleccionados.forEach { eq ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(eq.nombre, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = senaGreen)
                                        Text("Placa: ${eq.placa} · Precio: ${formatoCOP(eq.precio)}", fontSize = 11.sp, color = Color(0xFF666666))
                                    }
                                    Button(
                                        onClick = { equiposSeleccionados = equiposSeleccionados.filter { it.id != eq.id } },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFDEAEA), contentColor = Color(0xFFC62828)),
                                        shape = RoundedCornerShape(6.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Text("🗑️", fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total del préstamo:", fontWeight = FontWeight.Bold, color = senaGreen)
                        Text(formatoCOP(totalSeleccionado), fontWeight = FontWeight.Bold, color = senaGreen)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = onNavigateCalendario,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("📅 Ver calendario", fontSize = 12.sp)
                        }

                        Button(
                            onClick = ::registrarPrestamo,
                            colors = ButtonDefaults.buttonColors(containerColor = senaGreen),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("✓ Registrar y PDF", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Equipos actualmente prestados", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF172B24))
            Spacer(modifier = Modifier.height(10.dp))

            if (equiposPrestadosGlobal.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Box(modifier = Modifier.fillMaxWidth().padding(30.dp), contentAlignment = Alignment.Center) {
                        Text("No hay equipos prestados actualmente.", color = Color.Gray, fontSize = 13.sp)
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    equiposPrestadosGlobal.forEach { prestamo ->
                        val activos = prestamo.equipos.filter { it.estado == "Prestado" }
                        if (activos.isNotEmpty()) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(2.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(prestamo.instructor, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = senaGreen)
                                    Text("Ficha ${prestamo.ficha} · ${prestamo.programa}", fontSize = 12.sp, color = Color(0xFF666666))
                                    Text("Municipio: ${prestamo.municipio}", fontSize = 12.sp, color = Color(0xFF666666))

                                    Spacer(modifier = Modifier.height(8.dp))
                                    HorizontalDivider(color = Color(0xFFE1E8E3))
                                    Spacer(modifier = Modifier.height(8.dp))

                                    activos.forEach { eq ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(eq.nombre, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                                Text("Placa: ${eq.placa} · ${formatoCOP(eq.precio)}", fontSize = 11.sp, color = Color(0xFF666666))
                                            }
                                            Button(
                                                onClick = { devolverEquipo(prestamo.id, eq.placa) },
                                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE6F3EA), contentColor = senaGreen),
                                                shape = RoundedCornerShape(6.dp),
                                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                            ) {
                                                Text("↩ Devolver", fontSize = 11.sp)
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}