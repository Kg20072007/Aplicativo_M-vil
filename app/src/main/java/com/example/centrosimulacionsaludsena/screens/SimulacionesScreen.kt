package com.example.centrosimulacionsaludsena.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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

data class EscenarioSimulacion(
    val id: Int,
    val nombre: String,
    val nivel: String,
    val icono: String,
    val descripcion: String,
    val duracion: String,
    val participantes: String,
    val ambiente: String,
    val estado: String,
    val objetivo: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimulacionesScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val senaGreen = Color(0xFF006837)
    val senaGreenDark = Color(0xFF066B2D)
    val bgLight = Color(0xFFF5F7F6)

    val escenarios = remember {
        listOf(
            EscenarioSimulacion(
                id = 1,
                nombre = "Atención de Urgencias",
                nivel = "Alta Fidelidad",
                icono = "🚑",
                descripcion = "Simulación de manejo de trauma y reanimación avanzada con simuladores de respuesta fisiológica real.",
                duracion = "45 min",
                participantes = "Hasta 8",
                ambiente = "Urgencias",
                estado = "Disponible",
                objetivo = "Fortalecer las competencias para la atención inicial de pacientes en situaciones de urgencia."
            ),
            EscenarioSimulacion(
                id = 2,
                nombre = "Cuidados Intensivos (UCI)",
                nivel = "Alta Fidelidad",
                icono = "❤️",
                descripcion = "Monitoreo invasivo y manejo de ventilación mecánica en pacientes en estado crítico.",
                duracion = "60 min",
                participantes = "Hasta 6",
                ambiente = "UCI",
                estado = "Disponible",
                objetivo = "Desarrollar habilidades para el cuidado y monitoreo de pacientes en estado crítico."
            ),
            EscenarioSimulacion(
                id = 3,
                nombre = "Ginecobstetricia",
                nivel = "Media Fidelidad",
                icono = "👶",
                descripcion = "Prácticas de atención de parto normal y manejo de complicaciones neonatales inmediatas.",
                duracion = "50 min",
                participantes = "Hasta 8",
                ambiente = "Ginecobstétrico",
                estado = "Disponible",
                objetivo = "Fortalecer las habilidades de atención materno-infantil y respuesta ante situaciones obstétricas."
            )
        )
    }

    var simulacionSeleccionada by remember { mutableStateOf<EscenarioSimulacion?>(null) }
    var busquedaTexto by remember { mutableStateOf("") }
    var nivelFiltro by remember { mutableStateOf("Todos") }
    var dropdownExpanded by remember { mutableStateOf(false) }

    val totalEscenarios = escenarios.size
    val altaFidelidadCount = escenarios.count { it.nivel == "Alta Fidelidad" }
    val mediaFidelidadCount = escenarios.count { it.nivel == "Media Fidelidad" }

    val escenariosFiltrados = escenarios.filter { escenario ->
        val coincideTexto = busquedaTexto.isEmpty() ||
                escenario.nombre.lowercase().contains(busquedaTexto.lowercase().trim()) ||
                escenario.descripcion.lowercase().contains(busquedaTexto.lowercase().trim())

        val coincideNivel = nivelFiltro == "Todos" || escenario.nivel == nivelFiltro
        coincideTexto && coincideNivel
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Simulaciones Clínicas", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Fortalecimiento de competencias en entornos de alta fidelidad.", fontSize = 12.sp, color = Color.White.copy(alpha = 0.9f))
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
            Text("FORMACIÓN", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = senaGreen)
            Text("Escenarios de Simulación", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color(0xFF172B24))
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ResumenSimulacionCard("🏥", "Total", "$totalEscenarios", Color(0xFFDFF4E8), Modifier.weight(1f))
                ResumenSimulacionCard("⭐", "Alta fidelidad", "$altaFidelidadCount", Color(0xFFE2EFF0), Modifier.weight(1f))
                ResumenSimulacionCard("👥", "Media fidelidad", "$mediaFidelidadCount", Color(0xFFEEE5FF), Modifier.weight(1f))
                ResumenSimulacionCard("⏱️", "Promedio", "50 min", Color(0xFFDFF5F1), Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("📖 Escenarios disponibles", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF172B24))
                    Text("Selecciona un escenario para ver su información detallada.", fontSize = 13.sp, color = Color(0xFF718078))

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = busquedaTexto,
                            onValueChange = { busquedaTexto = it },
                            placeholder = { Text("🔎 Buscar escenario...") },
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
                                Text(if (nivelFiltro == "Todos") "Todos los niveles" else nivelFiltro, fontSize = 13.sp)
                            }
                            DropdownMenu(
                                expanded = dropdownExpanded,
                                onDismissRequest = { dropdownExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Todos los niveles") },
                                    onClick = { nivelFiltro = "Todos"; dropdownExpanded = false }
                                )
                                DropdownMenuItem(
                                    text = { Text("Alta Fidelidad") },
                                    onClick = { nivelFiltro = "Alta Fidelidad"; dropdownExpanded = false }
                                )
                                DropdownMenuItem(
                                    text = { Text("Media Fidelidad") },
                                    onClick = { nivelFiltro = "Media Fidelidad"; dropdownExpanded = false }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    if (escenariosFiltrados.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(30.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No se encontraron escenarios con esos criterios.", color = Color.Gray)
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            escenariosFiltrados.forEach { escenario ->
                                EscenarioCardItem(
                                    escenario = escenario,
                                    senaGreen = senaGreen,
                                    onVerInfoClick = { simulacionSeleccionada = escenario }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    simulacionSeleccionada?.let { sim ->
        Dialog(onDismissRequest = { simulacionSeleccionada = null }) {
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
                        Surface(
                            color = Color(0xFFE5F5EB),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(
                                text = sim.nivel,
                                color = senaGreen,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }

                        IconButton(onClick = { simulacionSeleccionada = null }) {
                            Icon(Icons.Default.Close, contentDescription = "Cerrar")
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("${sim.icono} ${sim.nombre}", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF172B24))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(sim.descripcion, color = Color(0xFF66766F), fontSize = 14.sp)

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Objetivo de la simulación", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF172B24))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(sim.objetivo, color = Color(0xFF66766F), fontSize = 13.sp)

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        InfoBadge("⏱️ ${sim.duracion}", senaGreen, Modifier.weight(1f))
                        InfoBadge("👥 ${sim.participantes}", senaGreen, Modifier.weight(1f))
                        InfoBadge("🏥 ${sim.ambiente}", senaGreen, Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            Toast.makeText(context, "Protocolo de ${sim.nombre}", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = senaGreen),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("📄 Ver protocolo", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun ResumenSimulacionCard(icon: String, label: String, count: String, iconBg: Color, modifier: Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(iconBg, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(icon, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(label, fontSize = 11.sp, color = Color(0xFF63736C), maxLines = 1)
                Text(count, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF172B24))
            }
        }
    }
}

@Composable
fun EscenarioCardItem(
    escenario: EscenarioSimulacion,
    senaGreen: Color,
    onVerInfoClick: () -> Unit
) {
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(escenario.icono, fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(escenario.nombre, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF172B24))
                }

                Surface(
                    color = Color(0xFFA8EF70),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "● ${escenario.estado}",
                        color = Color(0xFF155B2D),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(escenario.descripcion, fontSize = 13.sp, color = Color(0xFF6B7B74))

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DatoMin("⏱️", "Duración", escenario.duracion)
                DatoMin("👥", "Participantes", escenario.participantes)
                DatoMin("🏥", "Ambiente", escenario.ambiente)
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onVerInfoClick,
                colors = ButtonDefaults.buttonColors(containerColor = senaGreen),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver información →", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun DatoMin(icon: String, label: String, value: String) {
    Column {
        Text(icon, fontSize = 14.sp)
        Text(label, fontSize = 10.sp, color = Color(0xFF75827D))
        Text(value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF263832))
    }
}

@Composable
fun InfoBadge(text: String, color: Color, modifier: Modifier) {
    Box(
        modifier = modifier
            .background(Color(0xFFEAF6EF), RoundedCornerShape(8.dp))
            .padding(vertical = 8.dp, horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}