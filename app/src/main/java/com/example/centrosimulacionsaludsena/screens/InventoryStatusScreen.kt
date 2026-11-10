package com.example.centrosimulacionsaludsena.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Definición local del modelo Producto para evitar errores de referencia
data class Producto(
    val nombre: String,
    val codigoBarras: String,
    val lote: String,
    val cantidad: Int,
    val stockMinimo: Int,
    val fechaVencimiento: String,
    val ubicacion: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryStatusScreen(
    onBackClick: () -> Unit
) {
    val senaGreen = Color(0xFF006837)
    val textColor = MaterialTheme.colorScheme.onSurface
    val textGray = MaterialTheme.colorScheme.onSurfaceVariant

    // Lista de productos de prueba para el estado del inventario
    val productos = remember {
        listOf(
            Producto("Guantes Talla M", "77012345", "L-001", 12, 20, "2027-05-10", "Estante A1"),
            Producto("Jeringa 10ml", "77098765", "L-002", 5, 15, "2026-11-20", "Estante B2"),
            Producto("Sonda Vesical", "77045678", "L-003", 0, 5, "2026-08-15", "Estante C1"),
            Producto("Bata Quirúrgica", "77011223", "L-004", 45, 10, "2028-01-01", "Armario 2")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Estado del Inventario",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = senaGreen)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Reporte de Estado y Stock",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Monitoreo de existencias críticas y vencimientos",
                    fontSize = 12.sp,
                    color = textGray
                )
            }

            items(productos) { producto ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = producto.nombre,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )

                            val statusColor = when {
                                producto.cantidad <= 0 -> Color(0xFFEF4444) // Rojo (Agotado)
                                producto.cantidad <= producto.stockMinimo -> Color(0xFFEAB308) // Amarillo (Bajo Stock)
                                else -> Color(0xFF22C55E) // Verde (Disponible)
                            }

                            val statusText = when {
                                producto.cantidad <= 0 -> "Agotado"
                                producto.cantidad <= producto.stockMinimo -> "Bajo Stock"
                                else -> "Disponible"
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(statusColor.copy(alpha = 0.15f))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = statusText,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = statusColor
                                )
                            }
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = "Código: ${producto.codigoBarras}", fontSize = 12.sp, color = textGray)
                                Text(text = "Lote: ${producto.lote}", fontSize = 12.sp, color = textGray)
                                Text(text = "Ubicación: ${producto.ubicacion}", fontSize = 12.sp, color = textGray)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Stock: ${producto.cantidad} (Mín: ${producto.stockMinimo})",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = textColor
                                )
                                Text(text = "Vence: ${producto.fechaVencimiento}", fontSize = 12.sp, color = textGray)
                            }
                        }
                    }
                }
            }
        }
    }
}