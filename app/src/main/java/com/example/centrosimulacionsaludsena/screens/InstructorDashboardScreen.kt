package com.example.centrosimulacionsaludsena.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstructorDashboardScreen(
    onMenuClick: () -> Unit = {},
    onNavigateTo: (String) -> Unit = {},
    onLogout: () -> Unit = {},
    onNavigateGuias: () -> Unit = {},
    onNavigateNotificaciones: () -> Unit = {},
    onNavigatePerfil: () -> Unit = {}
) {
    val senaGreen = Color(0xFF006837)
    val bgLight = Color(0xFFFFFFFF)     // Fondo blanco puro
    val cardBg = Color(0xFFF8FAFC)      // Tarjetas con gris muy sutil
    val textPrimary = Color(0xFF1E293B) // Texto oscuro principal
    val textSecondary = Color(0xFF64748B) // Texto secundario

    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Centro de Simulación en Salud",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Rol: Instructor",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigatePerfil) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Mi Perfil",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Cerrar sesión",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = senaGreen)
            )
        },
        bottomBar = {
            // Barra inferior idéntica y ordenada
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = {
                        selectedTab = 0
                        onNavigateTo("inicio")
                    },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio", fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = senaGreen,
                        selectedTextColor = senaGreen,
                        unselectedIconColor = textSecondary,
                        unselectedTextColor = textSecondary,
                        indicatorColor = senaGreen.copy(alpha = 0.15f)
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                        onNavigateGuias()
                    },
                    icon = { Icon(Icons.Outlined.MenuBook, contentDescription = "Guías") },
                    label = { Text("Guías", fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = senaGreen,
                        selectedTextColor = senaGreen,
                        unselectedIconColor = textSecondary,
                        unselectedTextColor = textSecondary,
                        indicatorColor = senaGreen.copy(alpha = 0.15f)
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = {
                        selectedTab = 2
                        onNavigateNotificaciones()
                    },
                    icon = { Icon(Icons.Outlined.Notifications, contentDescription = "Avisos") },
                    label = { Text("Avisos", fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = senaGreen,
                        selectedTextColor = senaGreen,
                        unselectedIconColor = textSecondary,
                        unselectedTextColor = textSecondary,
                        indicatorColor = senaGreen.copy(alpha = 0.15f)
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = {
                        selectedTab = 3
                        onNavigatePerfil()
                    },
                    icon = { Icon(Icons.Outlined.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil", fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = senaGreen,
                        selectedTextColor = senaGreen,
                        unselectedIconColor = textSecondary,
                        unselectedTextColor = textSecondary,
                        indicatorColor = senaGreen.copy(alpha = 0.15f)
                    )
                )
            }
        },
        containerColor = bgLight
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(bgLight)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Bienvenida
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = senaGreen),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "¡Bienvenido, Instructor!",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Gestión de prácticas y ambientes clínicos",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }

            Text(
                text = "Módulos Académicos y Operativos",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                InstructorItem("Cronograma de Ambientes", "Planificar y reservar espacios", Icons.Outlined.CalendarMonth, senaGreen, cardBg, textPrimary, textSecondary) { onNavigateTo("cronograma") }
                InstructorItem("Inventario y Materiales", "Consultar disponibilidad de insumos", Icons.Outlined.Inventory2, senaGreen, cardBg, textPrimary, textSecondary) { onNavigateTo("inventario") }
                InstructorItem("Ambientes Clínicos", "Gestionar estados y salas", Icons.Outlined.MeetingRoom, senaGreen, cardBg, textPrimary, textSecondary) { onNavigateTo("ambiente") }
                InstructorItem("Simulaciones y Escenarios", "Programar y revisar casos", Icons.Outlined.PlayCircle, senaGreen, cardBg, textPrimary, textSecondary) { onNavigateTo("simulacion") }
                InstructorItem("Guías de Aprendizaje", "Revisar documentación y protocolos", Icons.Outlined.Description, senaGreen, cardBg, textPrimary, textSecondary) { onNavigateTo("guia") }
                InstructorItem("Evaluaciones de Aprendices", "Revisar rubricas y resultados", Icons.Outlined.Assignment, senaGreen, cardBg, textPrimary, textSecondary) { onNavigateTo("evaluacion") }
            }
        }
    }
}

@Composable
fun InstructorItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    senaGreen: Color,
    cardBg: Color,
    textPrimary: Color,
    textSecondary: Color,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = cardBg,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(senaGreen.copy(alpha = 0.12f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = senaGreen,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = textSecondary
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = textSecondary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}