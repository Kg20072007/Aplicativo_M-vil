package com.example.centrosimulacionsaludsena.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactoScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val senaCyan = Color(0xFF087F8C)
    val senaCyanDark = Color(0xFF066873)
    val bgLight = Color(0xFFF7FAFC)

    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    fun enviarMensaje() {
        if (nombre.isBlank() || correo.isBlank() || mensaje.isBlank()) {
            Toast.makeText(context, "Por favor completa todos los campos.", Toast.LENGTH_SHORT).show()
            return
        }
        Toast.makeText(context, "Mensaje enviado correctamente.", Toast.LENGTH_SHORT).show()
        nombre = ""
        correo = ""
        mensaje = ""
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Contacto", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Centro de Simulación SENA", fontSize = 12.sp, color = Color.White.copy(alpha = 0.9f))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = senaCyanDark)
            )
        },
        containerColor = bgLight
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Título de la sección
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "CONTACTO",
                    color = senaCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Estamos para ayudarte",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF243447)
                )
            }

            // Tarjetas de Información de Contacto
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InfoContactCard(
                    icono = "📍",
                    titulo = "Ubicación",
                    detalle = "SENA Regional Cauca"
                )
                InfoContactCard(
                    icono = "📧",
                    titulo = "Correo",
                    detalle = "centrosimulacion@sena.edu.co"
                )
                InfoContactCard(
                    icono = "📞",
                    titulo = "Atención",
                    detalle = "Horario institucional SENA"
                )
            }

            // Formulario de Contacto
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Envíanos un mensaje",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF243447)
                    )

                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre completo") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )

                    OutlinedTextField(
                        value = correo,
                        onValueChange = { correo = it },
                        label = { Text("Correo electrónico") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )

                    OutlinedTextField(
                        value = mensaje,
                        onValueChange = { mensaje = it },
                        label = { Text("Escribe tu mensaje...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Button(
                        onClick = ::enviarMensaje,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = senaCyan),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Enviar mensaje",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InfoContactCard(icono: String, titulo: String, detalle: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFE6F6F6),
                modifier = Modifier.size(46.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = icono, fontSize = 22.sp)
                }
            }

            Column {
                Text(
                    text = titulo,
                    fontSize = 13.sp,
                    color = Color(0xFF718096),
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = detalle,
                    fontSize = 15.sp,
                    color = Color(0xFF243447),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}