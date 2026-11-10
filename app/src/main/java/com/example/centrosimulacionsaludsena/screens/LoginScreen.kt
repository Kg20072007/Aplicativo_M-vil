package com.example.centrosimulacionsaludsena.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Reemplaza 'R.drawable.fondo_centro' con el nombre exacto de tu archivo en res/drawable
import com.example.centrosimulacionsaludsena.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onBackClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {},
    onLoginSuccess: (String) -> Unit = {}
) {
    val senaGreen = Color(0xFF006837)
    val cardBg = Color(0xFFF8FAFC)
    val textPrimary = Color(0xFF1E293B)
    val textSecondary = Color(0xFF64748B)

    var usuario by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Estados para el flujo de solicitud de Invitado con PIN
    var showSolicitudDialog by remember { mutableStateOf(false) }
    var nombreVisitante by remember { mutableStateOf("") }
    var entidadVisitante by remember { mutableStateOf("") }
    var mensajeSolicitud by remember { mutableStateOf("") }

    fun validarLogin() {
        val userClean = usuario.trim()
        val passClean = password.trim()

        if (userClean.isEmpty() || passClean.isEmpty()) {
            errorMessage = "Por favor ingrese usuario y contraseña."
            return
        }

        when {
            userClean.contains("admin", ignoreCase = true) -> onLoginSuccess("Admin")
            userClean.contains("instructor", ignoreCase = true) -> onLoginSuccess("Instructor")
            userClean.contains("aprendiz", ignoreCase = true) -> onLoginSuccess("Aprendiz")
            userClean.contains("invitado", ignoreCase = true) || passClean == "8492" -> onLoginSuccess("Invitado")
            else -> errorMessage = "Credenciales incorrectas o acceso de invitado no autorizado."
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Iniciar Sesión", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = senaGreen)
            )
        }
    ) { paddingValues ->
        // Box contenedor principal para la imagen de fondo y el velo de transparencia
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 1. Imagen de fondo del Centro de Simulación
            Image(
                painter = painterResource(id = R.drawable.logo), // Nombre de tu imagen en res/drawable
                contentDescription = "Fondo Centro de Simulación",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop // Cubre toda la pantalla manteniendo la proporción
            )

            // 2. Velo semi-transparente para garantizar contraste e legibilidad
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.45f)) // Capa oscura al 45% de opacidad
            )

            // 3. Contenido principal (Formulario y Textos)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Centro de Simulación en Salud",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "SENA Regional Cauca",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Ingreso al Sistema",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = textPrimary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        val textFieldColors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = senaGreen,
                            unfocusedBorderColor = Color(0xFFCBD5E1),
                            focusedContainerColor = cardBg,
                            unfocusedContainerColor = cardBg,
                            focusedTextColor = textPrimary,
                            unfocusedTextColor = textPrimary
                        )

                        OutlinedTextField(
                            value = usuario,
                            onValueChange = {
                                usuario = it
                                errorMessage = ""
                            },
                            label = { Text("Usuario / Correo / PIN de Invitado") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = senaGreen) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            colors = textFieldColors
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = {
                                password = it
                                errorMessage = ""
                            },
                            label = { Text("Contraseña / PIN temporal") },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = senaGreen) },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = if (passwordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                                        contentDescription = "Mostrar contraseña",
                                        tint = textSecondary
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            colors = textFieldColors
                        )

                        if (errorMessage.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = errorMessage, color = Color.Red, fontSize = 12.sp)
                        }

                        if (mensajeSolicitud.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = mensajeSolicitud, color = senaGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        TextButton(
                            onClick = onForgotPasswordClick,
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("¿Olvidaste tu contraseña?", color = senaGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { validarLogin() },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = senaGreen)
                        ) {
                            Text("Iniciar Sesión", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedButton(
                            onClick = { showSolicitudDialog = true },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = senaGreen)
                        ) {
                            Text("Solicitar Acceso como Invitado", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick = onRegisterClick) {
                    Text("¿No tienes una cuenta? Regístrate aquí", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }

    // Diálogo para solicitar acceso de invitado
    if (showSolicitudDialog) {
        AlertDialog(
            onDismissRequest = { showSolicitudDialog = false },
            title = { Text("Solicitud de Acceso Visitante", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Ingrese sus datos para que el Administrador autorice su ingreso temporal:", fontSize = 13.sp)
                    OutlinedTextField(
                        value = nombreVisitante,
                        onValueChange = { nombreVisitante = it },
                        label = { Text("Nombre Completo") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = entidadVisitante,
                        onValueChange = { entidadVisitante = it },
                        label = { Text("Institución / Entidad de procedencia") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (nombreVisitante.isNotBlank() && entidadVisitante.isNotBlank()) {
                            showSolicitudDialog = false
                            mensajeSolicitud = "¡Solicitud enviada al Administrador! Su PIN temporal de acceso es: 8492"
                            nombreVisitante = ""
                            entidadVisitante = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = senaGreen)
                ) {
                    Text("Enviar Solicitud")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showSolicitudDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}