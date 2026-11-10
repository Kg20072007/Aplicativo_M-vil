package com.example.centrosimulacionsaludsena.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
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

data class GuiaItem(
    val id: Int,
    val nombre: String,
    val icono: String,
    val categoria: String,
    val resumen: String,
    val objetivo: String,
    val temas: List<String>,
    val ruta: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuiasScreen(
    onBackClick: () -> Unit,
    onNavigateEvaluaciones: () -> Unit
) {
    val context = LocalContext.current
    val senaGreen = Color(0xFF006837)
    val senaGreenDark = Color(0xFF066B2D)
    val bgLight = Color(0xFFF5F7F6)

    val guias = remember {
        listOf(
            GuiaItem(1, "Signos vitales", "🩺", "Valoración", "Medición, monitoreo y registro de las funciones fisiológicas básicas.", "Reconocer los principales signos vitales y comprender su importancia en la valoración inicial.", listOf("Frecuencia cardíaca y valoración del pulso.", "Frecuencia respiratoria.", "Temperatura corporal.", "Presión arterial.", "Saturación de oxígeno."), "/guia/signos-vitales"),
            GuiaItem(2, "Equipos biomédicos", "🏥", "Equipos", "Reconocimiento y manejo básico de equipos biomédicos.", "Identificar la función de los equipos utilizados en el ambiente de simulación.", listOf("Monitor de signos vitales.", "Pulsioxímetro.", "Termómetro clínico.", "Tensiómetro.", "Inspección básica y reporte de fallas."), "/guia/biomedicos"),
            GuiaItem(3, "Desinfección", "🧼", "Bioseguridad", "Principios de limpieza y desinfección de instrumental y áreas clínicas.", "Diferenciar limpieza, desinfección y esterilización.", listOf("Higiene de manos.", "Limpieza de superficies.", "Elementos de protección personal.", "Uso seguro de productos de desinfección.", "Prevención de contaminación cruzada."), "/guia/desinfeccion"),
            GuiaItem(4, "Farmacia", "💊", "Medicamentos", "Custodia, control y distribución segura de medicamentos.", "Comprender las verificaciones esenciales relacionadas con los medicamentos.", listOf("Identificación del paciente.", "Verificación del medicamento, dosis, vía y horario.", "Alergias y contraindicaciones.", "Registro de la administración.", "Notificación de incidentes."), "/guia/farmacia"),
            GuiaItem(5, "Geriatría", "👴", "Cuidado", "Atención integral y segura del adulto mayor.", "Reconocer necesidades de cuidado y medidas para preservar la seguridad.", listOf("Trato digno y comunicación.", "Prevención de caídas.", "Movilización segura.", "Observación del estado general.", "Respeto por la autonomía."), "/guia/geriatria"),
            GuiaItem(6, "Laboratorio", "🔬", "Procedimientos", "Manejo de muestras biológicas y procedimientos de laboratorio.", "Identificar prácticas de bioseguridad y manejo adecuado de muestras.", listOf("Identificación de muestras.", "Uso de elementos de protección.", "Rotulado y trazabilidad.", "Manejo de residuos.", "Notificación de derrames y accidentes."), "/guia/laboratorio"),
            GuiaItem(7, "Atención prehospitalaria", "🚑", "Emergencias", "Principios de atención inicial y coordinación en emergencias.", "Comprender la seguridad de la escena y la activación del sistema de emergencias.", listOf("Seguridad de la escena.", "Solicitud de ayuda.", "Valoración inicial.", "Comunicación sobre el estado del paciente.", "Entrega estructurada de información."), "/guia/prehospitalaria"),
            GuiaItem(8, "Reanimación cardiopulmonar (RCP)", "🫁", "Emergencias", "Fundamentos de reconocimiento y respuesta ante una emergencia cardiorrespiratoria.", "Reconocer una posible emergencia y la necesidad de activar ayuda inmediata.", listOf("Reconocimiento de falta de respuesta.", "Activación del sistema de emergencias.", "Compresiones torácicas según la formación vigente.", "Uso del desfibrilador externo automático.", "Trabajo en equipo y seguridad."), "/guia/rcp"),
            GuiaItem(9, "Sistema de monitoreo", "📊", "Monitoreo", "Observación, registro y análisis de parámetros de monitoreo.", "Comprender la importancia del seguimiento y la identificación de anomalías.", listOf("Reconocimiento de parámetros del monitor.", "Seguimiento de cambios.", "Registro de información.", "Identificación y comunicación de alertas.", "Revisión básica del equipo."), "/guia/monitoreo"),
            GuiaItem(10, "Registro y retroalimentación", "📝", "Evaluación", "Registro de información y revisión de los resultados de las actividades.", "Comprender la importancia de documentar resultados e identificar oportunidades de mejora.", listOf("Registro de resultados.", "Documentación de incidentes.", "Revisión de las actividades.", "Identificación de oportunidades de mejora.", "Retroalimentación del aprendizaje."), "/guia/retroalimentacion")
        )
    }

    var busqueda by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("Todas") }
    var seleccionada by remember { mutableStateOf<GuiaItem?>(null) }
    var dropdownExpanded by remember { mutableStateOf(false) }

    val categorias = remember { listOf("Todas") + guias.map { it.categoria }.distinct() }

    val filtradas = remember(busqueda, categoria) {
        val texto = busqueda.trim().lowercase()
        guias.filter { guia ->
            val coincideTexto = texto.isEmpty() ||
                    guia.nombre.lowercase().contains(texto) ||
                    guia.resumen.lowercase().contains(texto) ||
                    guia.temas.any { it.lowercase().contains(texto) }

            val coincideCategoria = categoria == "Todas" || guia.categoria == categoria
            coincideTexto && coincideCategoria
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Guías de Aprendizaje", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Centro de Simulación · SENA", fontSize = 12.sp, color = Color.White.copy(alpha = 0.9f))
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
            // Cabecera / Hero
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("CENTRO DE SIMULACIÓN · SENA", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = senaGreen)
                Text("Guías de aprendizaje 📚", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF172B24))
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Explora los recursos y fortalece tus competencias para el cuidado seguro del paciente.",
                    fontSize = 13.sp,
                    color = Color(0xFF65756E),
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Resumen rápido
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ResumenGuiaCard("${guias.size}", "Guías disponibles", Modifier.weight(1f))
                ResumenGuiaCard("📖", "Aprendizaje autónomo", Modifier.weight(1f))
                ResumenGuiaCard("🧠", "Refuerzo de conocimientos", Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Buscador y Filtros
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = busqueda,
                    onValueChange = { busqueda = it },
                    placeholder = { Text("🔎 Buscar una guía...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = senaGreen,
                        unfocusedBorderColor = Color(0xFFD8E2DD)
                    )
                )

                Box {
                    OutlinedButton(
                        onClick = { dropdownExpanded = true },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF172B24))
                    ) {
                        Text(if (categoria == "Todas") "Todas las categorías" else categoria, fontSize = 12.sp)
                    }
                    DropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false }
                    ) {
                        categorias.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(if (cat == "Todas") "Todas las categorías" else cat) },
                                onClick = {
                                    categoria = cat
                                    dropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Listado de Tarjetas
            if (filtradas.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(30.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🔍", fontSize = 32.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No encontramos esa guía", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF172B24))
                        Text("Prueba con otro término o cambia la categoría.", fontSize = 13.sp, color = Color(0xFF65756E))
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = {
                                busqueda = ""
                                categoria = "Todas"
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = senaGreen),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Mostrar todas", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    filtradas.forEach { guia ->
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
                                    Text(guia.icono, fontSize = 28.sp)
                                    Surface(
                                        color = Color(0xFFE5F5EB),
                                        shape = RoundedCornerShape(16.dp)
                                    ) {
                                        Text(
                                            text = guia.categoria,
                                            color = senaGreen,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Text(guia.nombre, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF172B24))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(guia.resumen, fontSize = 13.sp, color = Color(0xFF6B7B74))

                                Spacer(modifier = Modifier.height(14.dp))

                                Button(
                                    onClick = { seleccionada = guia },
                                    colors = ButtonDefaults.buttonColors(containerColor = senaGreen),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Explorar guía →", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Footer con acceso a Evaluaciones
            Button(
                onClick = onNavigateEvaluaciones,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDFF4E8)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ir a prácticas y evaluaciones →", color = senaGreen, fontWeight = FontWeight.Bold)
            }
        }
    }

    // Modal de Detalle de Guía
    seleccionada?.let { guia ->
        Dialog(onDismissRequest = { seleccionada = null }) {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = Color.White,
                modifier = Modifier.fillMaxWidth().padding(10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(guia.icono, fontSize = 32.sp)
                        IconButton(onClick = { seleccionada = null }) {
                            Icon(Icons.Default.Close, contentDescription = "Cerrar")
                        }
                    }

                    Text("GUÍA DE APRENDIZAJE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = senaGreen)
                    Text(guia.nombre, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF172B24))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(guia.resumen, fontSize = 13.sp, color = Color(0xFF65756E))

                    Spacer(modifier = Modifier.height(14.dp))

                    // Objetivo
                    Surface(
                        color = Color(0xFFF2F7F4),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("🎯 Objetivo de aprendizaje", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = senaGreenDark)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(guia.objetivo, fontSize = 12.sp, color = Color(0xFF4A5568))
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text("📌 Temas para estudiar", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF172B24))
                    Spacer(modifier = Modifier.height(6.dp))

                    guia.temas.forEachIndexed { index, tema ->
                        Row(modifier = Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = Color(0xFFE5F5EB),
                                modifier = Modifier.size(20.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("${index + 1}", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = senaGreen)
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(tema, fontSize = 12.sp, color = Color(0xFF4A5568))
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Nota institucional
                    Surface(
                        color = Color(0xFFFFFBEB),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("💡", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Revisa los protocolos institucionales y las orientaciones de tu instructor antes de realizar procedimientos clínicos.", fontSize = 11.sp, color = Color(0xFF92400E))
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { seleccionada = null },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Cerrar")
                        }

                        Button(
                            onClick = {
                                val rutaDestino = guia.ruta
                                seleccionada = null
                                Toast.makeText(context, "Abriendo: $rutaDestino", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = senaGreen),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Abrir guía →", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ResumenGuiaCard(valor: String, label: String, modifier: Modifier) {
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
            Text(valor, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF172B24))
            Spacer(modifier = Modifier.height(2.dp))
            Text(label, fontSize = 10.sp, color = Color(0xFF63736C), maxLines = 1)
        }
    }
}