package com.example.centrosimulacionsaludsena.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoleSelectionScreen(
    onBackClick: () -> Unit = {},
    onRoleSelected: (String) -> Unit = {}
) {
    val senaGreen = Color(0xFF006837)
    val bgLight = Color(0xFFFFFFFF)
    val cardBg = Color(0xFFF8FAFC)
    val textPrimary = Color(0xFF1E293B)
    val textSecondary = Color(0xFF64748B)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Selección de Rol", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = senaGreen)
            )
        },
        containerColor = bgLight
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(bgLight)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Selecciona tu Rol de Ingreso",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary
            )
            Text(
                text = "Elige la vista correspondiente para acceder al Centro de Simulación",
                fontSize = 13.sp,
                color = textSecondary
            )

            Spacer(modifier = Modifier.height(10.dp))

            RoleCardItem("Administrador", "Gestión total del centro y usuarios", Icons.Default.AdminPanelSettings, senaGreen, cardBg, textPrimary, textSecondary) { onRoleSelected("Admin") }
            RoleCardItem("Instructor", "Programación de prácticas y guías", Icons.Default.School, senaGreen, cardBg, textPrimary, textSecondary) { onRoleSelected("Instructor") }
            RoleCardItem("Aprendiz", "Consulta de cronograma y recursos", Icons.Default.Person, senaGreen, cardBg, textPrimary, textSecondary) { onRoleSelected("Aprendiz") }
            RoleCardItem("Invitado", "Acceso de consulta para externos", Icons.Default.Visibility, senaGreen, cardBg, textPrimary, textSecondary) { onRoleSelected("Invitado") }
        }
    }
}

@Composable
fun RoleCardItem(
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
        shape = RoundedCornerShape(14.dp),
        color = cardBg,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(senaGreen.copy(alpha = 0.12f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = senaGreen, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                Text(text = subtitle, fontSize = 12.sp, color = textSecondary)
            }
            Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = textSecondary)
        }
    }
}