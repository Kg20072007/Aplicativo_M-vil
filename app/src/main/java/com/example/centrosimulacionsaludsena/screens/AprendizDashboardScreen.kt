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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AprendizDashboardScreen(
    onMenuClick: () -> Unit = {},
    onNavigateCalendario: () -> Unit = {},
    onLogout: () -> Unit = {},
    onNavigateContacto: () -> Unit = {},
    onNavigateNotificaciones: () -> Unit = {},
    onNavigatePerfil: () -> Unit = {},
    onNavigateGuias: () -> Unit = {}
) {
    val senaGreen = Color(0xFF006837)
    val bgLight = Color(0xFFFFFFFF)      // Fondo blanco puro
    val cardBg = Color(0xFFF8FAFC)       // Tarjetas con gris muy sutil
    val textPrimary = Color(0xFF1E293B)  // Texto oscuro principal
    val textSecondary = Color(0xFF64748B)// Texto secundario

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
                            text = "Rol: Aprendiz",
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
            // Barra de navegación inferior unificada
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = {
                        selectedTab = 0
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
            // Tarjeta de bienvenida
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
                            imageVector = Icons.Default.LocalHospital,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "¡Hola, Aprendiz!",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Listo para tu sesión de práctica clínica",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }

            // Métricas rápidas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = cardBg),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = senaGreen, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "5", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                        Text(text = "Prácticas Realizadas", fontSize = 10.sp, color = textSecondary)
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = cardBg),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFEAB308), modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "4.8", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                        Text(text = "Calificación Prom.", fontSize = 10.sp, color = textSecondary)
                    }
                }
            }

            Text(
                text = "Mi Próxima Simulación",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(senaGreen.copy(alpha = 0.12f), RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Outlined.Event, contentDescription = null, tint = senaGreen)
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Manejo de Urgencias Médicas", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                        Text(text = "Ambiente 104 • Mañana, 08:00 a. m.", fontSize = 11.sp, color = textSecondary)
                    }
                }
            }

            Text(
                text = "Recursos de Estudio",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(
                    onClick = onNavigateGuias,
                    shape = RoundedCornerShape(12.dp),
                    color = cardBg,
                    shadowElevation = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Outlined.MenuBook, contentDescription = null, tint = senaGreen)
                        Spacer(modifier = Modifier.width(14.dp))
                        Text(text = "Ver Guías de Aprendizaje", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = textPrimary, modifier = Modifier.weight(1f))
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = textSecondary)
                    }
                }

                Surface(
                    onClick = onNavigateCalendario,
                    shape = RoundedCornerShape(12.dp),
                    color = cardBg,
                    shadowElevation = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Outlined.CalendarMonth, contentDescription = null, tint = senaGreen)
                        Spacer(modifier = Modifier.width(14.dp))
                        Text(text = "Consultar Cronograma", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = textPrimary, modifier = Modifier.weight(1f))
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = textSecondary)
                    }
                }
            }
        }
    }
}