package com.example.centrosimulacionsaludsena.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userRole: String = "Administrador",
    onBackClick: () -> Unit,
    onNavigateAjustes: () -> Unit,
    onNavigateNotificaciones: () -> Unit
) {
    val context = LocalContext.current
    val senaGreen = Color(0xFF006837)
    val senaGreenDark = Color(0xFF066B2D)
    val bgLight = Color(0xFFFFFFFF)
    val cardBg = Color(0xFFF8FAFC)
    val textPrimary = Color(0xFF1E293B)
    val textSecondary = Color(0xFF64748B)

    // Valores dinámicos según el rol
    val defaultNombre = when {
        userRole.contains("Admin", ignoreCase = true) -> "Sandra"
        userRole.contains("Instructor", ignoreCase = true) -> "Instructor SENA"
        userRole.contains("Aprendiz", ignoreCase = true) -> "Aprendiz SENA"
        else -> "Invitado SENA"
    }

    val defaultCorreo = when {
        userRole.contains("Admin", ignoreCase = true) -> "admin@sena.edu.co"
        userRole.contains("Instructor", ignoreCase = true) -> "instructor@sena.edu.co"
        userRole.contains("Aprendiz", ignoreCase = true) -> "aprendiz@sena.edu.co"
        else -> "invitado@sena.edu.co"
    }

    var nombre by remember { mutableStateOf(defaultNombre) }
    var documento by remember { mutableStateOf("1098765432") }
    var telefono by remember { mutableStateOf("3101234567") }
    var correo by remember { mutableStateOf(defaultCorreo) }

    var editando by remember { mutableStateOf(false) }
    var mensaje by remember { mutableStateOf("") }

    val inicial = if (nombre.isNotEmpty()) nombre.take(1).uppercase() else "U"

    fun guardarCambios() {
        editando = false
        mensaje = "Los datos del perfil se actualizaron correctamente."
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Mi Perfil", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Información de la cuenta ($userRole)", fontSize = 12.sp, color = Color.White.copy(alpha = 0.9f))
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("MI CUENTA", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = senaGreen, letterSpacing = 1.5.sp)
                    Text("Perfil de $userRole", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                }

                if (!editando) {
                    Button(
                        onClick = { editando = true },
                        colors = ButtonDefaults.buttonColors(containerColor = senaGreen),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("✏️ Editar", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tarjeta principal del usuario
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .background(
                                brush = Brush.linearGradient(listOf(senaGreen, Color(0xFF39A900))),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(inicial, color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(nombre, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(color = Color(0xFFF0F7F3), shape = RoundedCornerShape(12.dp)) {
                            Text("👤 $userRole", fontSize = 11.sp, color = senaGreen, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("✉️ $correo", fontSize = 12.sp, color = textSecondary)
                    }
                }
            }

            if (mensaje.isNotEmpty()) {
                Spacer(modifier = Modifier.height(14.dp))
                Surface(
                    color = Color(0xFFEDF9F1),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0xFFB9E2C7))
                ) {
                    Text(
                        text = "✓ $mensaje",
                        color = Color(0xFF176B3D),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Formulario
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Información de la cuenta", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                    Spacer(modifier = Modifier.height(16.dp))

                    val textFieldColors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = senaGreen,
                        unfocusedBorderColor = Color(0xFFCBD5E1),
                        focusedContainerColor = cardBg,
                        unfocusedContainerColor = cardBg,
                        focusedTextColor = textPrimary,
                        unfocusedTextColor = textPrimary,
                        disabledTextColor = textPrimary,
                        disabledContainerColor = cardBg,
                        disabledBorderColor = Color(0xFFE2E8F0)
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { nombre = it },
                            label = { Text("Nombre Completo") },
                            enabled = editando,
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp),
                            colors = textFieldColors
                        )

                        OutlinedTextField(
                            value = documento,
                            onValueChange = { documento = it },
                            label = { Text("Documento de identidad") },
                            enabled = editando,
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp),
                            colors = textFieldColors
                        )

                        OutlinedTextField(
                            value = correo,
                            onValueChange = { correo = it },
                            label = { Text("Correo institucional") },
                            enabled = editando,
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp),
                            colors = textFieldColors
                        )
                    }

                    if (editando) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { guardarCambios() },
                            colors = ButtonDefaults.buttonColors(containerColor = senaGreen),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Guardar Cambios", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = textPrimary)
            ) {
                Text("🏠 Volver a mi panel ($userRole)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}