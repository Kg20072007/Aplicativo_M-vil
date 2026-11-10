package com.example.centrosimulacionsaludsena.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AdminDrawerScreen(
    onCloseDrawer: () -> Unit = {},
    onNavigateTo: (String) -> Unit = {}
) {
    val senaGreenDark = Color(0xFF006837)
    val lightBackground = Color(0xFFFFFFFF)
    val dividerColor = Color(0xFFE2E8F0)
    val textPrimary = Color(0xFF1E293B)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(lightBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            // Botón de cierre (X) superior
            IconButton(
                onClick = onCloseDrawer,
                modifier = Modifier.align(Alignment.Start)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cerrar menú",
                    tint = textPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Perfil de Sandra
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(senaGreenDark),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = "Sandra",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )
                    Text(
                        text = "Administrador",
                        fontSize = 12.sp,
                        color = senaGreenDark,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = dividerColor, thickness = 1.dp)
            Spacer(modifier = Modifier.height(8.dp))

            // Opciones del menú (navegación directa sin conflictos)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                DrawerItemRow("Inicio", Icons.Outlined.Home, senaGreenDark, textPrimary) {
                    onNavigateTo("inicio")
                }
                DrawerItemRow("Inventario", Icons.Outlined.Inventory2, senaGreenDark, textPrimary) {
                    onNavigateTo("inventario")
                }
                DrawerItemRow("Equipos", Icons.Outlined.MedicalServices, senaGreenDark, textPrimary) {
                    onNavigateTo("equipo")
                }
                DrawerItemRow("Ambientes", Icons.Outlined.MeetingRoom, senaGreenDark, textPrimary) {
                    onNavigateTo("ambiente")
                }
                DrawerItemRow("Cronograma", Icons.Outlined.CalendarMonth, senaGreenDark, textPrimary) {
                    onNavigateTo("cronograma")
                }
                DrawerItemRow("Simulaciones", Icons.Outlined.PlayCircle, senaGreenDark, textPrimary) {
                    onNavigateTo("simulacion")
                }
                DrawerItemRow("Guías", Icons.Outlined.Description, senaGreenDark, textPrimary) {
                    onNavigateTo("guia")
                }
                DrawerItemRow("Evaluaciones", Icons.AutoMirrored.Outlined.Assignment, senaGreenDark, textPrimary) {
                    onNavigateTo("evaluacion")
                }
                DrawerItemRow("Mi Perfil", Icons.Outlined.Person, senaGreenDark, textPrimary) {
                    onNavigateTo("perfil")
                }
                DrawerItemRow("Ajustes", Icons.Outlined.Settings, senaGreenDark, textPrimary) {
                    onNavigateTo("ajuste")
                }
                DrawerItemRow("Notificaciones", Icons.Outlined.Notifications, senaGreenDark, textPrimary) {
                    onNavigateTo("notificac")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = dividerColor, thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))

            // Botón de Cerrar Sesión inferior
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(senaGreenDark.copy(alpha = 0.1f))
                    .clickable {
                        onNavigateTo("inicio")
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Cerrar Sesión",
                        tint = senaGreenDark,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Cerrar Sesión",
                        fontSize = 14.sp,
                        color = senaGreenDark,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun DrawerItemRow(
    title: String,
    icon: ImageVector,
    iconColor: Color,
    textColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = iconColor,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            fontSize = 14.sp,
            color = textColor,
            fontWeight = FontWeight.Medium
        )
    }
}