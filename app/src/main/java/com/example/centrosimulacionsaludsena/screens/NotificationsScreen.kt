package com.example.centrosimulacionsaludsena.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import kotlinx.coroutines.delay
import org.json.JSONArray
import org.json.JSONObject

data class NotificacionItem(
    val id: Int,
    val icono: String,
    val titulo: String,
    val texto: String,
    val tiempo: String,
    val tipo: String,
    var leida: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val senaGreen = Color(0xFF006837)     // Verde institucional oscuro
    val senaGreenLight = Color(0xFFE6F3EA) // Fondo verde claro para chips y acentos
    val bgLight = Color(0xFFFFFFFF)        // Fondo blanco puro

    val notificacionesIniciales = remember {
        listOf(
            NotificacionItem(1, "📅", "Nueva actividad programada", "Se programó una simulación de signos vitales.", "Hace 10 minutos", "Actividad", false),
            NotificacionItem(2, "📝", "Evaluación disponible", "Tienes una nueva evaluación pendiente.", "Hace 1 hora", "Evaluación", false),
            NotificacionItem(3, "📚", "Nueva guía educativa", "Se publicó una nueva guía educativa.", "Ayer", "Material", true),
            NotificacionItem(4, "🩺", "Recordatorio de simulación", "Recuerda verificar los equipos antes de iniciar la práctica.", "Hace 2 horas", "Recordatorio", false)
        )
    }

    val sharedPreferences = remember {
        context.getSharedPreferences("sena_notificaciones_prefs", Context.MODE_PRIVATE)
    }

    fun cargarNotificaciones(): MutableList<NotificacionItem> {
        val jsonString = sharedPreferences.getString("notificacionesSENA", "") ?: ""
        val lista = mutableListOf<NotificacionItem>()
        if (jsonString.isBlank()) {
            return notificacionesIniciales.toMutableList()
        }
        try {
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                lista.add(
                    NotificacionItem(
                        id = obj.optInt("id"),
                        icono = obj.optString("icono"),
                        titulo = obj.optString("titulo"),
                        texto = obj.optString("texto"),
                        tiempo = obj.optString("tiempo"),
                        tipo = obj.optString("tipo"),
                        leida = obj.optBoolean("leida")
                    )
                )
            }
        } catch (_: Exception) {
            return notificacionesIniciales.toMutableList()
        }
        return lista
    }

    fun guardarNotificaciones(lista: List<NotificacionItem>) {
        val jsonArray = JSONArray()
        for (n in lista) {
            val obj = JSONObject().apply {
                put("id", n.id)
                put("icono", n.icono)
                put("titulo", n.titulo)
                put("texto", n.texto)
                put("tiempo", n.tiempo)
                put("tipo", n.tipo)
                put("leida", n.leida)
            }
            jsonArray.put(obj)
        }
        sharedPreferences.edit().putString("notificacionesSENA", jsonArray.toString()).apply()
    }

    var notificaciones by remember { mutableStateOf(cargarNotificaciones()) }
    var filtro by remember { mutableStateOf("Todas") }
    var mensaje by remember { mutableStateOf("") }

    LaunchedEffect(mensaje) {
        if (mensaje.isNotEmpty()) {
            delay(2500)
            mensaje = ""
        }
    }

    val pendientes = notificaciones.count { !it.leida }

    val notificacionesFiltradas = notificaciones.filter { notif ->
        when (filtro) {
            "No leídas" -> !notif.leida
            "Leídas" -> notif.leida
            else -> true
        }
    }

    fun mostrarToast(texto: String) {
        mensaje = texto
    }

    fun marcarComoLeida(id: Int) {
        notificaciones = notificaciones.map {
            if (it.id == id) it.copy(leida = true) else it
        }.toMutableList()
        guardarNotificaciones(notificaciones)
        mostrarToast("Notificación marcada como leída.")
    }

    fun marcarTodasComoLeidas() {
        notificaciones = notificaciones.map { it.copy(leida = true) }.toMutableList()
        guardarNotificaciones(notificaciones)
        mostrarToast("Todas las notificaciones están leídas.")
    }

    fun eliminarNotificacion(id: Int) {
        notificaciones = notificaciones.filter { it.id != id }.toMutableList()
        guardarNotificaciones(notificaciones)
        mostrarToast("Notificación eliminada.")
    }

    fun restaurarNotificaciones() {
        notificaciones = notificacionesIniciales.map { it.copy() }.toMutableList()
        guardarNotificaciones(notificaciones)
        filtro = "Todas"
        mostrarToast("Notificaciones de ejemplo restauradas.")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Notificaciones", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Centro de Simulación SENA", fontSize = 12.sp, color = Color.White.copy(alpha = 0.9f))
                    }
                },
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(bgLight)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("CENTRO DE SIMULACIÓN", color = senaGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text("Avisos y Alertas", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                        Text("Mantente al día con las actividades del centro.", fontSize = 13.sp, color = Color(0xFF64748B))

                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            color = senaGreenLight,
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = "🔔 $pendientes ${if (pendientes == 1) "aviso pendiente" else "avisos pendientes"}",
                                color = senaGreen,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { restaurarNotificaciones() },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Restaurar", fontSize = 12.sp, color = Color(0xFF1E293B))
                    }

                    Button(
                        onClick = { marcarTodasComoLeidas() },
                        enabled = pendientes > 0,
                        colors = ButtonDefaults.buttonColors(containerColor = senaGreen),
                        modifier = Modifier.weight(1.2f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("✓ Marcar leídas", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("Todas", "No leídas", "Leídas").forEach { opcion ->
                            val isActive = filtro == opcion
                            Button(
                                onClick = { filtro = opcion },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isActive) senaGreenLight else Color(0xFFF1F5F9),
                                    contentColor = if (isActive) senaGreen else Color(0xFF64748B)
                                ),
                                shape = RoundedCornerShape(20.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                elevation = ButtonDefaults.buttonElevation(0.dp)
                            ) {
                                Text(
                                    text = if (opcion == "No leídas") "$opcion ($pendientes)" else opcion,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Mostrando ${notificacionesFiltradas.size} de ${notificaciones.size} avisos",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (notificacionesFiltradas.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        elevation = CardDefaults.cardElevation(1.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(35.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("🔔", fontSize = 36.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (filtro == "No leídas") "¡Todo al día!" else "No hay notificaciones",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color(0xFF1E293B)
                            )
                            Text(
                                text = if (filtro == "No leídas") "No tienes avisos pendientes por revisar." else "No hay avisos para mostrar con este filtro.",
                                fontSize = 12.sp,
                                color = Color(0xFF64748B),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        notificacionesFiltradas.forEach { notif ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (!notif.leida) Color(0xFFF2F9F4) else Color.White
                                ),
                                elevation = CardDefaults.cardElevation(1.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = senaGreenLight,
                                        modifier = Modifier.size(44.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(notif.icono, fontSize = 20.sp)
                                        }
                                    }

                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = notif.titulo,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 15.sp,
                                                color = Color(0xFF1E293B),
                                                modifier = Modifier.weight(1f)
                                            )
                                            if (!notif.leida) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(8.dp)
                                                        .background(senaGreen, shape = CircleShape)
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(notif.texto, fontSize = 13.sp, color = Color(0xFF475569))

                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(notif.tiempo, fontSize = 11.sp, color = Color(0xFF94A3B8))
                                            Surface(
                                                color = Color(0xFFF1F5F9),
                                                shape = RoundedCornerShape(10.dp)
                                            ) {
                                                Text(
                                                    text = notif.tipo,
                                                    fontSize = 10.sp,
                                                    color = Color(0xFF475569),
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(10.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.End,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            if (!notif.leida) {
                                                OutlinedButton(
                                                    onClick = { marcarComoLeida(notif.id) },
                                                    shape = RoundedCornerShape(6.dp),
                                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = senaGreen)
                                                ) {
                                                    Text("Marcar leída", fontSize = 11.sp)
                                                }
                                                Spacer(modifier = Modifier.width(6.dp))
                                            }

                                            OutlinedButton(
                                                onClick = { eliminarNotificacion(notif.id) },
                                                shape = RoundedCornerShape(6.dp),
                                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF4444)),
                                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                            ) {
                                                Text("🗑️", fontSize = 11.sp)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (mensaje.isNotEmpty()) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(20.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF004D29),
                    shadowElevation = 6.dp
                ) {
                    Text(
                        text = "✓ $mensaje",
                        color = Color.White,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                    )
                }
            }
        }
    }
}