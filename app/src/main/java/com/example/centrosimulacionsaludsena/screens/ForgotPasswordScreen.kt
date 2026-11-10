package com.example.centrosimulacionsaludsena.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    onBackClick: () -> Unit,
    onSendCodeClick: () -> Unit
) {
    // Variable para saber si el usuario eligió "Correo" (true) o "Celular" (false)
    var isEmailSelected by remember { mutableStateOf(true) }

    // Variable para guardar lo que escribe el usuario en la caja de texto
    var contactInput by remember { mutableStateOf("") }

    val senaGreen = Color(0xFF006837)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver atrás",
                            tint = Color(0xFF64748B)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                // Título principal
                Text(
                    text = "Recuperar contraseña",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Subtítulo explicativo
                Text(
                    text = "Ingresa tu correo o número de celular\ny te enviaremos un código de verificación.",
                    fontSize = 14.sp,
                    color = Color(0xFF64748B),
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(28.dp))

                // --- PESTAÑAS DE SELECCIÓN (Correo / Celular) ---
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF1F5F9)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Botón Pestaña Correo
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(4.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isEmailSelected) senaGreen else Color.Transparent)
                            .clickable {
                                isEmailSelected = true
                                contactInput = "" // Limpia al cambiar
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Correo",
                            color = if (isEmailSelected) Color.White else Color(0xFF64748B),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    // Botón Pestaña Celular
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(4.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (!isEmailSelected) senaGreen else Color.Transparent)
                            .clickable {
                                isEmailSelected = false
                                contactInput = "" // Limpia al cambiar
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Celular",
                            color = if (!isEmailSelected) Color.White else Color(0xFF64748B),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Etiqueta dinámica según la opción elegida
                Text(
                    text = if (isEmailSelected) "Correo electrónico o celular" else "Número de celular",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF334155),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 6.dp)
                )

                // Campo de texto para ingresar el contacto
                OutlinedTextField(
                    value = contactInput,
                    onValueChange = { contactInput = it },
                    placeholder = {
                        Text(
                            text = if (isEmailSelected) "ejemplo@correo.com" else "+57 300 123 4567",
                            color = Color(0xFF94A3B8)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = if (isEmailSelected) KeyboardType.Email else KeyboardType.Phone
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = senaGreen,
                        unfocusedBorderColor = Color(0xFFE2E8F0)
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Botón Enviar Código
                Button(
                    onClick = onSendCodeClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = senaGreen)
                ) {
                    Text(
                        text = "Enviar código",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // Enlace inferior para regresar al Login
            Text(
                text = "Volver a iniciar sesión",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = senaGreen,
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .clickable { onBackClick() }
            )
        }
    }
}