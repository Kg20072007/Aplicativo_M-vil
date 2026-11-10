package com.example.centrosimulacionsaludsena.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvitadoDashboardScreen(
    onMenuClick: () -> Unit = {},
    onLogout: () -> Unit = {},
    onNavigateTo: (String) -> Unit = {},
    onNavigateGuias: () -> Unit = {},
    onNavigateNotificaciones: () -> Unit = {},
    onNavigatePerfil: () -> Unit = {}
) {
    val senaGreen = Color(0xFF006837)
    val bgLight = Color(0xFFFFFFFF)     // Fondo blanco puro institucional
    val cardBg = Color(0xFFF8FAFC)      // Tarjetas con gris muy sutil
    val textDark = Color(0xFF1E293B)
    val textGray = Color(0xFF64748B)

    var searchQuery by remember { mutableStateOf("") }
    var selectedBottomTab by remember { mutableIntStateOf(0) }

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
                            text = "Rol: Invitado / Visitante Externo",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigatePerfil) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = "Perfil Invitado",
                                tint = Color.White
                            )
                        }
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
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = selectedBottomTab == 0,
                    onClick = {
                        selectedBottomTab = 0
                        onNavigateTo("inicio")
                    },
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = senaGreen,
                        selectedTextColor = senaGreen,
                        indicatorColor = senaGreen.copy(alpha = 0.15f),
                        unselectedIconColor = textGray,
                        unselectedTextColor = textGray
                    )
                )

                NavigationBarItem(
                    selected = selectedBottomTab == 1,
                    onClick = {
                        selectedBottomTab = 1
                        onNavigateGuias()
                    },
                    icon = { Icon(Icons.Outlined.MenuBook, contentDescription = "Guías") },
                    label = { Text("Guías", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = senaGreen,
                        selectedTextColor = senaGreen,
                        indicatorColor = senaGreen.copy(alpha = 0.15f),
                        unselectedIconColor = textGray,
                        unselectedTextColor = textGray
                    )
                )

                NavigationBarItem(
                    selected = selectedBottomTab == 2,
                    onClick = {
                        selectedBottomTab = 2
                        onNavigateNotificaciones()
                    },
                    icon = { Icon(Icons.Outlined.Notifications, contentDescription = "Avisos") },
                    label = { Text("Avisos", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = senaGreen,
                        selectedTextColor = senaGreen,
                        indicatorColor = senaGreen.copy(alpha = 0.15f),
                        unselectedIconColor = textGray,
                        unselectedTextColor = textGray
                    )
                )

                NavigationBarItem(
                    selected = selectedBottomTab == 3,
                    onClick = {
                        selectedBottomTab = 3
                        onNavigatePerfil()
                    },
                    icon = { Icon(Icons.Outlined.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = senaGreen,
                        selectedTextColor = senaGreen,
                        indicatorColor = senaGreen.copy(alpha = 0.15f),
                        unselectedIconColor = textGray,
                        unselectedTextColor = textGray
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
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Spacer(modifier = Modifier.height(6.dp))

            // Tarjeta de bienvenida institucional para invitados
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
                            imageVector = Icons.Default.Business,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "¡Bienvenido, Invitado!",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Consulta de ambientes, equipos y guías clínicas",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }

            // --- BUSCADOR ---
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Buscar módulos o ambientes...", color = Color(0xFF94A3B8), fontSize = 14.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Buscar",
                        tint = textGray
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = cardBg,
                    unfocusedContainerColor = cardBg,
                    focusedBorderColor = senaGreen,
                    unfocusedBorderColor = Color(0xFFE2E8F0),
                    focusedTextColor = textDark,
                    unfocusedTextColor = textDark
                ),
                singleLine = true
            )

            Text(
                text = "Módulos de Consulta y Visita",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = textDark
            )

            // Lista de módulos accesibles para el invitado
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                InvitadoModuleItem("Ambientes Clínicos", "Consultar disponibilidad y salas de simulación", Icons.Outlined.MeetingRoom, senaGreen, cardBg, textDark, textGray) { onNavigateTo("ambiente") }
                InvitadoModuleItem("Cronograma de Actividades", "Ver agenda de prácticas y eventos", Icons.Outlined.CalendarMonth, senaGreen, cardBg, textDark, textGray) { onNavigateTo("cronograma") }
                InvitadoModuleItem("Simulaciones y Escenarios", "Revisar casos clínicos disponibles", Icons.Outlined.PlayCircle, senaGreen, cardBg, textDark, textGray) { onNavigateTo("simulacion") }
                InvitadoModuleItem("Guías de Aprendizaje", "Consultar documentación institucional", Icons.Outlined.Description, senaGreen, cardBg, textDark, textGray) { onNavigateTo("guia") }
                InvitadoModuleItem("Evaluaciones y Rúbricas", "Visualizar criterios de simulación", Icons.Outlined.Assignment, senaGreen, cardBg, textDark, textGray) { onNavigateTo("evaluacion") }
                InvitadoModuleItem("Avisos y Notificaciones", "Ver comunicados generales", Icons.Outlined.Notifications, senaGreen, cardBg, textDark, textGray) { onNavigateNotificaciones() }
                InvitadoModuleItem("Ajustes de Cuenta", "Configuración de preferencias", Icons.Outlined.Settings, senaGreen, cardBg, textDark, textGray) { onNavigateTo("ajuste") }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun InvitadoModuleItem(
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