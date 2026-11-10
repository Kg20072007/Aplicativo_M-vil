package com.example.centrosimulacionsaludsena.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val CATEGORIAS_MAP = mapOf(
    "guantes" to "Guantes",
    "jeringas" to "Jeringas",
    "gasas" to "Gasas y curación",
    "soluciones" to "Soluciones",
    "cateteres" to "Catéteres",
    "curacion" to "Curación",
    "ropa" to "Ropa clínica",
    "carro" to "Carro de paro"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterInventoryScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val senaGreen = Color(0xFF006837)
    val senaGreenDark = Color(0xFF066B2D)
    val bgLight = Color(0xFFF3F7F5)

    // Estados del formulario
    var nombre by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("guantes") }
    var codigoBarras by remember { mutableStateOf("") }
    var lote by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var stockMinimo by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }
    var tieneVencimiento by remember { mutableStateOf(true) }
    var fechaVencimiento by remember { mutableStateOf("") }
    var mensajeExito by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Registro de Inventario", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Registra los insumos y productos que ingresan al centro.", fontSize = 12.sp, color = Color.White.copy(alpha = 0.9f))
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
                .verticalScroll(scrollState)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título de la sección
            Text("📦 NUEVO PRODUCTO", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = senaGreen)
            Text("Registrar insumo", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = senaGreenDark)
            Text("Completa la información del producto para agregarlo al inventario.", fontSize = 14.sp, color = Color(0xFF68747D))

            Spacer(modifier = Modifier.height(16.dp))

            if (mensajeExito.isNotEmpty()) {
                Surface(
                    color = Color(0xFFDFF5E5),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "✅ $mensajeExito",
                        color = Color(0xFF137333),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(14.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Tarjeta 1: Información del producto
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("🩺 Información del producto", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = senaGreenDark)
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre del producto *") },
                        placeholder = { Text("Ej: Guantes Talla M") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = senaGreen)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Selector de Categoría simple
                    Text("Categoría *", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF34433B))
                    Spacer(modifier = Modifier.height(4.dp))

                    // Nota: Para simplificar la selección en Compose vertical, se puede mostrar de forma limpia o desplegable
                    OutlinedTextField(
                        value = CATEGORIAS_MAP[categoria] ?: "",
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            // Aquí podrías desplegar un Menu o manejar la categoría
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = codigoBarras,
                        onValueChange = { codigoBarras = it },
                        label = { Text("Código de barras *") },
                        placeholder = { Text("Ej: 7701234567890") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = senaGreen)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = lote,
                        onValueChange = { lote = it },
                        label = { Text("Número de lote *") },
                        placeholder = { Text("Ej: LT-2026-001") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = senaGreen)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tarjeta 2: Control de existencias
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("📊 Control de existencias", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = senaGreenDark)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = cantidad,
                            onValueChange = { cantidad = it },
                            label = { Text("Cantidad inicial *") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = senaGreen)
                        )
                        OutlinedTextField(
                            value = stockMinimo,
                            onValueChange = { stockMinimo = it },
                            label = { Text("Stock mínimo *") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = senaGreen)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = ubicacion,
                        onValueChange = { ubicacion = it },
                        label = { Text("Ubicación *") },
                        placeholder = { Text("Ej: Estante A - Canto 1") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = senaGreen)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tarjeta 3: Vencimiento
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("📅 Fecha de vencimiento", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = senaGreenDark)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = tieneVencimiento,
                            onCheckedChange = { tieneVencimiento = it },
                            colors = CheckboxDefaults.colors(checkedColor = senaGreen)
                        )
                        Text("Este producto tiene fecha de vencimiento", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }

                    if (tieneVencimiento) {
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = fechaVencimiento,
                            onValueChange = { fechaVencimiento = it },
                            label = { Text("Fecha (AAAA-MM-DD) *") },
                            placeholder = { Text("2027-12-31") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = senaGreen)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        nombre = ""
                        codigoBarras = ""
                        lote = ""
                        cantidad = ""
                        stockMinimo = ""
                        ubicacion = ""
                        fechaVencimiento = ""
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE2E7E4)),
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("🗑️ Limpiar", color = Color(0xFF46534C), fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        if (nombre.isBlank() || codigoBarras.isBlank() || cantidad.isBlank() || lote.isBlank() || ubicacion.isBlank()) {
                            Toast.makeText(context, "Por favor completa todos los campos obligatorios.", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        mensajeExito = "Producto \"$nombre\" registrado correctamente."
                        Toast.makeText(context, mensajeExito, Toast.LENGTH_LONG).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = senaGreen),
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("💾 Registrar producto", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}