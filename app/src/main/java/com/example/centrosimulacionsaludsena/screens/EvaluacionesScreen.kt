package com.example.centrosimulacionsaludsena.screens

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

data class PracticaItem(
    val id: Int,
    val nombre: String,
    val icono: String,
    val categoria: String,
    val resumen: String,
    val ruta: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvaluacionesScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val senaGreen = Color(0xFF006837)
    val senaGreenDark = Color(0xFF066B2D)
    val bgLight = Color(0xFFF5F7F6)

    val practicas = remember {
        listOf(
            PracticaItem(1, "Desinfección", "🧼", "Bioseguridad", "Practica los principios de limpieza y desinfección.", "/practica/desinfeccion"),
            PracticaItem(2, "Equipos biomédicos", "🏥", "Equipos", "Reconoce los equipos y sus funciones principales.", "/practica/biomedicos"),
            PracticaItem(3, "Farmacia", "💊", "Medicamentos", "Refuerza los conocimientos sobre medicamentos.", "/practica/farmacia"),
            PracticaItem(4, "Geriatría", "👴", "Cuidado", "Practica conceptos de cuidado del adulto mayor.", "/practica/geriatria"),
            PracticaItem(5, "Laboratorio", "🔬", "Procedimientos", "Repasa el manejo de muestras y la bioseguridad.", "/practica/laboratorio"),
            PracticaItem(6, "Sistema de monitoreo", "📊", "Monitoreo", "Refuerza el reconocimiento y seguimiento de parámetros.", "/practica/monitoreo"),
            PracticaItem(7, "Atención prehospitalaria", "🚑", "Emergencias", "Repasa la atención inicial en situaciones de emergencia.", "/practica/prehospitalaria"),
            PracticaItem(8, "Reanimación cardiopulmonar (RCP)", "❤️", "Emergencias", "Refuerza los conceptos de respuesta ante una emergencia.", "/practica/rcp"),
            PracticaItem(9, "Registro y retroalimentación", "📝", "Evaluación", "Revisa los resultados y las oportunidades de mejora.", "/practica/retroalimentacion"),
            PracticaItem(10, "Signos vitales", "🌡️", "Valoración", "Practica el registro de los signos vitales.", "/practica/signos-vitales")
        )
    }

    var busqueda by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("Todas") }
    var dropdownExpanded by remember { mutableStateOf(false) }

    val categorias = listOf("Todas") + practicas.map { it.categoria }.distinct()

    val filtradas = remember(busqueda, categoria) {
        val texto = busqueda.trim().lowercase()
        practicas.filter { practica ->
            val coincideTexto = texto.isEmpty() ||
                    practica.nombre.lowercase().contains(texto) ||
                    practica.resumen.lowercase().contains(texto)
            val coincideCategoria = categoria == "Todas" || practica.categoria == categoria
            coincideTexto && coincideCategoria
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Prácticas y Evaluaciones", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
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
                Text("🎓 APRENDIZAJE PRÁCTICO", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = senaGreen)
                Text("Prácticas y evaluaciones 🩺", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF172B24))
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Selecciona una práctica para fortalecer tus competencias y poner a prueba tus conocimientos.",
                    fontSize = 13.sp,
                    color = Color(0xFF65756E),
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Resumen superior
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ResumenEvalCard("${practicas.size}", "Prácticas registradas", Modifier.weight(1f))
                ResumenEvalCard("🧠", "Aprendizaje práctico", Modifier.weight(1f))
                ResumenEvalCard("📋", "Refuerzo de competencias", Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Filtros y Búsqueda
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = busqueda,
                    onValueChange = { busqueda = it },
                    placeholder = { Text("🔎 Buscar una práctica...") },
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

            // Listado de Prácticas
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
                        Text("No encontramos esa práctica", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF172B24))
                        Text("Prueba otro término o selecciona otra categoría.", fontSize = 13.sp, color = Color(0xFF65756E))
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
                    filtradas.forEach { practica ->
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
                                    Text(practica.icono, fontSize = 28.sp)
                                    Surface(
                                        color = Color(0xFFE5F5EB),
                                        shape = RoundedCornerShape(16.dp)
                                    ) {
                                        Text(
                                            text = practica.categoria,
                                            color = senaGreen,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Text(practica.nombre, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF172B24))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(practica.resumen, fontSize = 13.sp, color = Color(0xFF6B7B74))

                                Spacer(modifier = Modifier.height(14.dp))

                                Button(
                                    onClick = {
                                        Toast.makeText(context, "Iniciando: ${practica.nombre}", Toast.LENGTH_SHORT).show()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = senaGreen),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Iniciar práctica →", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ResumenEvalCard(valor: String, label: String, modifier: Modifier) {
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