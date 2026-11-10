package com.example.centrosimulacionsaludsena.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class InsumoItem(
    val nombre: String,
    val categoria: String,
    val cantidad: Int,
    val unidad: String,
    val estadoStock: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    onBackClick: () -> Unit
) {
    val senaGreen = Color(0xFF006837) // Verde oscuro institucional
    val bgLight = Color(0xFFFFFFFF)     // Fondo blanco puro
    val cardBg = Color(0xFFF8FAFC)      // Fondo gris muy claro para tarjetas
    val textPrimary = Color(0xFF1E293B) // Texto oscuro principal
    val textSecondary = Color(0xFF64748B) // Texto secundario

    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedCategory by remember { mutableStateOf("Todos") }
    var searchQuery by remember { mutableStateOf("") }

    val categorias = listOf("Todos", "Guantes", "Jeringas", "Gasas", "Soluciones", "Catéteres", "Curación", "Ropa", "Equipos")

    val listaInsumos = remember {
        mutableStateListOf(
            InsumoItem("Guantes Talla M", "Guantes", 12, "Cajas", "Bajo stock"),
            InsumoItem("Jeringa 10ml", "Jeringas", 5, "Unidades", "Próximo a vencer"),
            InsumoItem("Sonda Vesical", "Catéteres", 0, "Unidades", "Agotado"),
            InsumoItem("Bata Quirúrgica", "Ropa", 45, "Unidades", "Disponible"),
            InsumoItem("Solución Salina", "Soluciones", 2, "Frascos", "Vencido")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Inventario de Insumos",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Control de existencias y entregas",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = senaGreen)
            )
        },
        containerColor = bgLight // Forzamos el fondo blanco en el Scaffold
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(bgLight)
        ) {
            // Pestañas superiores con diseño claro
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFFF1F5F9),
                contentColor = senaGreen
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Insumos", fontWeight = FontWeight.Bold, color = if(selectedTab == 0) senaGreen else textSecondary) },
                    icon = { Icon(Icons.Outlined.Inventory2, contentDescription = null, tint = if(selectedTab == 0) senaGreen else textSecondary) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Estado", fontWeight = FontWeight.Bold, color = if(selectedTab == 1) senaGreen else textSecondary) },
                    icon = { Icon(Icons.Outlined.Assessment, contentDescription = null, tint = if(selectedTab == 1) senaGreen else textSecondary) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Registrar", fontWeight = FontWeight.Bold, color = if(selectedTab == 2) senaGreen else textSecondary) },
                    icon = { Icon(Icons.Outlined.AddBox, contentDescription = null, tint = if(selectedTab == 2) senaGreen else textSecondary) }
                )
            }

            when (selectedTab) {
                0 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(bgLight)
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Buscar insumo o equipo...", fontSize = 13.sp, color = textSecondary) },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = senaGreen) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = senaGreen,
                                unfocusedBorderColor = Color(0xFFCBD5E1),
                                focusedContainerColor = cardBg,
                                unfocusedContainerColor = cardBg,
                                focusedTextColor = textPrimary,
                                unfocusedTextColor = textPrimary
                            )
                        )

                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            items(categorias) { cat ->
                                FilterChip(
                                    selected = cat == selectedCategory,
                                    onClick = { selectedCategory = cat },
                                    label = { Text(cat) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = senaGreen.copy(alpha = 0.15f),
                                        selectedLabelColor = senaGreen
                                    )
                                )
                            }
                        }

                        val filtrados = listaInsumos.filter { item ->
                            (selectedCategory == "Todos" || item.categoria == selectedCategory) &&
                                    item.nombre.contains(searchQuery, ignoreCase = true)
                        }

                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.background(bgLight)
                        ) {
                            items(filtrados) { insumo ->
                                InsumoCard(insumo, cardBg, textPrimary, textSecondary, senaGreen)
                            }
                        }
                    }
                }
                1 -> EstadoInventarioSection(listaInsumos, bgLight, cardBg, textPrimary, senaGreen)
                2 -> RegistrarInsumoSection(bgLight, textPrimary, senaGreen) { nuevo ->
                    listaInsumos.add(nuevo)
                    selectedTab = 0
                }
            }
        }
    }
}

@Composable
fun InsumoCard(insumo: InsumoItem, cardBg: Color, textPrimary: Color, textSecondary: Color, senaGreen: Color) {
    val (statusBg, statusText, badgeText) = when (insumo.estadoStock) {
        "Disponible" -> Triple(Color(0xFF22C55E).copy(alpha = 0.15f), Color(0xFF15803D), "🟢 Disponible")
        "Bajo stock" -> Triple(Color(0xFFEAB308).copy(alpha = 0.15f), Color(0xFFA16207), "🟡 Bajo stock")
        "Próximo a vencer" -> Triple(Color(0xFFF97316).copy(alpha = 0.15f), Color(0xFFC2410C), "🟠 Próximo a vencer")
        "Vencido" -> Triple(Color(0xFFEF4444).copy(alpha = 0.15f), Color(0xFFB91C1C), "🔴 Vencido")
        else -> Triple(Color(0xFF334155).copy(alpha = 0.15f), Color(0xFF0F172A), "⚫ Agotado")
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(42.dp).clip(CircleShape).background(senaGreen.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.MedicalServices, contentDescription = null, tint = senaGreen, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = insumo.nombre, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                Text(text = "Categoría: ${insumo.categoria}", fontSize = 12.sp, color = textSecondary)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(text = "${insumo.cantidad} ${insumo.unidad}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(statusBg).padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(text = badgeText, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = statusText)
                }
            }
        }
    }
}

@Composable
fun EstadoInventarioSection(insumos: List<InsumoItem>, bgLight: Color, cardBg: Color, textPrimary: Color, senaGreen: Color) {
    val disponibles = insumos.count { it.estadoStock == "Disponible" }
    val bajoStock = insumos.count { it.estadoStock == "Bajo stock" }
    val proximos = insumos.count { it.estadoStock == "Próximo a vencer" }
    val vencidos = insumos.count { it.estadoStock == "Vencido" }
    val agotados = insumos.count { it.estadoStock == "Agotado" }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.background(bgLight)
    ) {
        item { Text("Resumen General de Alertas", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textPrimary) }
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MetricaCard("Disponibles", disponibles.toString(), Color(0xFF22C55E), cardBg, textPrimary, Modifier.weight(1f))
                MetricaCard("Bajo Stock", bajoStock.toString(), Color(0xFFEAB308), cardBg, textPrimary, Modifier.weight(1f))
                MetricaCard("Por Vencer", proximos.toString(), Color(0xFFF97316), cardBg, textPrimary, Modifier.weight(1f))
            }
        }
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MetricaCard("Vencidos", vencidos.toString(), Color(0xFFEF4444), cardBg, textPrimary, Modifier.weight(1f))
                MetricaCard("Agotados", agotados.toString(), Color(0xFF0F172A), cardBg, textPrimary, Modifier.weight(1f))
            }
        }
        item { Text("Listado con Alertas", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textPrimary) }
        items(insumos) { insumo ->
            InsumoCard(insumo, cardBg, textPrimary, Color.Gray, senaGreen)
        }
    }
}

@Composable
fun MetricaCard(title: String, count: String, color: Color, cardBg: Color, textPrimary: Color, modifier: Modifier) {
    Card(modifier = modifier, shape = RoundedCornerShape(10.dp), colors = CardDefaults.cardColors(containerColor = cardBg)) {
        Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = title, fontSize = 10.sp, color = Color.Gray)
            Text(text = count, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrarInsumoSection(bgLight: Color, textPrimary: Color, senaGreen: Color, onSuccess: (InsumoItem) -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var cantidadStr by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("Guantes") }
    var estadoSeleccionado by remember { mutableStateOf("Disponible") }
    val estados = listOf("Disponible", "Bajo stock", "Próximo a vencer", "Vencido", "Agotado")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgLight)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Registrar Nuevo Insumo", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textPrimary)
        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre del insumo") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp))
        OutlinedTextField(value = cantidadStr, onValueChange = { cantidadStr = it }, label = { Text("Cantidad inicial") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp))

        Text("Estado del insumo", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = textPrimary)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(estados) { est ->
                FilterChip(
                    selected = estadoSeleccionado == est,
                    onClick = { estadoSeleccionado = est },
                    label = { Text(est) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = senaGreen.copy(alpha = 0.15f),
                        selectedLabelColor = senaGreen
                    )
                )
            }
        }

        Button(
            onClick = {
                val cant = cantidadStr.toIntOrNull() ?: 0
                if (nombre.isNotBlank()) {
                    onSuccess(InsumoItem(nombre, categoria, cant, "Unidades", estadoSeleccionado))
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = senaGreen)
        ) {
            Text("Guardar e Incluir", fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}