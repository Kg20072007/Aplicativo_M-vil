package com.example.centrosimulacionsaludsena.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Importa tus recursos gráficos de drawable (ej: fotos del centro de simulación)
import com.example.centrosimulacionsaludsena.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarruselFotosScreen(
    onBackClick: () -> Unit = {}
) {
    val senaGreen = Color(0xFF006837)
    val bgLight = Color(0xFFFFFFFF)
    val textPrimary = Color(0xFF1E293B)
    val textSecondary = Color(0xFF64748B)

    // Lista de imágenes locales almacenadas en res/drawable
    // Asegúrate de tener estas imágenes o reemplazarlas por tus nombres reales
    val listaFotos = listOf(
        R.drawable.geriatria, // Reemplaza con tus recursos reales
        R.drawable.rcp,
        R.drawable.monitoreo
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Galería de Ambientes", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White) },
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
                .padding(vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = "Recorrido Visual",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )
                Text(
                    text = "Desliza horizontalmente para ver las instalaciones del centro",
                    fontSize = 13.sp,
                    color = textSecondary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Carrusel horizontal desplazable (LazyRow)
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(listaFotos) { imagenRes ->
                    Card(
                        modifier = Modifier
                            .width(280.dp)
                            .height(180.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Image(
                                painter = painterResource(id = imagenRes),
                                contentDescription = "Foto ambiente",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )

                            // Etiqueta superpuesta opcional
                            Surface(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(12.dp),
                                color = Color.Black.copy(alpha = 0.6f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "Simulación Clínica SENA",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}