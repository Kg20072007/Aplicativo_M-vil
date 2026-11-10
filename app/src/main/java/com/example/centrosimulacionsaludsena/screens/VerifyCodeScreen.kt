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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyCodeScreen(
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit
) {
    // Creamos una lista de 6 variables para guardar cada dígito del código por separado
    var code1 by remember { mutableStateOf("") }
    var code2 by remember { mutableStateOf("") }
    var code3 by remember { mutableStateOf("") }
    var code4 by remember { mutableStateOf("") }
    var code5 by remember { mutableStateOf("") }
    var code6 by remember { mutableStateOf("") }

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
                    text = "Verifica tu cuenta",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Texto explicativo tal como en el diseño original
                Text(
                    text = "Te hemos enviado un código a tu correo\no celular. Ingresa el código para continuar.",
                    fontSize = 14.sp,
                    color = Color(0xFF64748B),
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(36.dp))

                // --- FILA DE 6 CAJAS DE TEXTO PARA EL CÓDIGO ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CodeDigitField(value = code1) { code1 = it }
                    CodeDigitField(value = code2) { code2 = it }
                    CodeDigitField(value = code3) { code3 = it }
                    CodeDigitField(value = code4) { code4 = it }
                    CodeDigitField(value = code5) { code5 = it }
                    CodeDigitField(value = code6) { code6 = it }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Enlace de reenvío con temporizador simulado
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Reenviar código ",
                        fontSize = 13.sp,
                        color = Color(0xFF64748B)
                    )
                    Text(
                        text = "(00:45)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = senaGreen
                    )
                }

                Spacer(modifier = Modifier.height(36.dp))

                // Botón verde Continuar
                Button(
                    onClick = onContinueClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = senaGreen)
                ) {
                    Text(
                        text = "Continuar",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// Componente auxiliar pequeño para cada una de las 6 cajitas de los números
@Composable
fun CodeDigitField(value: String, onValueChange: (String) -> Unit) {
    val senaGreen = Color(0xFF006837)

    OutlinedTextField(
        value = value,
        onValueChange = {
            // Limitamos a que solo acepte 1 dígito por cajita
            if (it.length <= 1) onValueChange(it)
        },
        modifier = Modifier
            .size(46.dp, 56.dp),
        shape = RoundedCornerShape(10.dp),
        textStyle = LocalTextStyle.current.copy(
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = senaGreen,
            unfocusedBorderColor = Color(0xFFE2E8F0)
        ),
        singleLine = true
    )
}