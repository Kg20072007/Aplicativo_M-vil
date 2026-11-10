package com.example.centrosimulacionsaludsena

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

// Importación de pantallas del proyecto SENA
import com.example.centrosimulacionsaludsena.screens.AdminDashboardScreen
import com.example.centrosimulacionsaludsena.screens.AdminDrawerScreen
import com.example.centrosimulacionsaludsena.screens.AmbientesScreen
import com.example.centrosimulacionsaludsena.screens.AprendizDashboardScreen
import com.example.centrosimulacionsaludsena.screens.CarruselFotosScreen
import com.example.centrosimulacionsaludsena.screens.ContactoScreen
import com.example.centrosimulacionsaludsena.screens.CronogramaScreen
import com.example.centrosimulacionsaludsena.screens.EvaluacionesScreen
import com.example.centrosimulacionsaludsena.screens.ForgotPasswordScreen
import com.example.centrosimulacionsaludsena.screens.GuiasScreen
import com.example.centrosimulacionsaludsena.screens.InstructorDashboardScreen
import com.example.centrosimulacionsaludsena.screens.InventoryScreen
import com.example.centrosimulacionsaludsena.screens.InventoryStatusScreen
import com.example.centrosimulacionsaludsena.screens.InvitadoDashboardScreen
import com.example.centrosimulacionsaludsena.screens.LoginScreen
import com.example.centrosimulacionsaludsena.screens.NewPasswordScreen
import com.example.centrosimulacionsaludsena.screens.NotificationsScreen
import com.example.centrosimulacionsaludsena.screens.ProfileScreen
import com.example.centrosimulacionsaludsena.screens.RegisterInventoryScreen
import com.example.centrosimulacionsaludsena.screens.RegisterScreen
import com.example.centrosimulacionsaludsena.screens.RegistroEquiposScreen
import com.example.centrosimulacionsaludsena.screens.RoleSelectionScreen
import com.example.centrosimulacionsaludsena.screens.SettingsScreen
import com.example.centrosimulacionsaludsena.screens.SimulacionesScreen
import com.example.centrosimulacionsaludsena.screens.SplashScreen
import com.example.centrosimulacionsaludsena.screens.VerifyCodeScreen
import com.example.centrosimulacionsaludsena.screens.WelcomeScreen
import com.example.centrosimulacionsaludsena.ui.theme.CentroSimulacionSaludSenaTheme

// Máquina de estados oficial de la aplicación
enum class AppState {
    SPLASH,
    WELCOME,
    LOGIN,
    REGISTER,
    FORGOT_PASSWORD,
    VERIFY_CODE,
    NEW_PASSWORD,
    ROLE_SELECTION,
    DASHBOARD_ADMIN,
    DRAWER_ADMIN,
    DASHBOARD_INSTRUCTOR,
    DASHBOARD_APRENDIZ,
    DASHBOARD_INVITADO,
    INVENTORY,
    REGISTER_INVENTORY,
    INVENTORY_STATUS,
    REGISTRO_EQUIPOS,
    SIMULACIONES,
    GUIAS,
    EVALUACIONES,
    CRONOGRAMA,
    AMBIENTES,
    PERFIL,
    AJUSTES,
    NOTIFICACIONES,
    CONTACTO,
    GALERIA_FOTOS
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CentroSimulacionSaludSenaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var currentState by remember { mutableStateOf(AppState.SPLASH) }

                    // Rastrea el último panel activo para la navegación dinámica de regreso
                    var lastDashboardState by remember { mutableStateOf(AppState.DASHBOARD_ADMIN) }

                    when (currentState) {

                        AppState.SPLASH -> {
                            SplashScreen(
                                onTimeout = { currentState = AppState.WELCOME }
                            )
                        }

                        AppState.WELCOME -> {
                            WelcomeScreen(
                                onStartClick = { currentState = AppState.LOGIN }
                            )
                        }

                        AppState.LOGIN -> {
                            LoginScreen(
                                onBackClick = { currentState = AppState.WELCOME },
                                onForgotPasswordClick = { currentState = AppState.FORGOT_PASSWORD },
                                onRegisterClick = { currentState = AppState.REGISTER },
                                onLoginSuccess = { role ->
                                    val cleaned = role.trim()
                                    val targetDashboard = when {
                                        cleaned.contains("admin", ignoreCase = true) -> AppState.DASHBOARD_ADMIN
                                        cleaned.contains("instructor", ignoreCase = true) -> AppState.DASHBOARD_INSTRUCTOR
                                        cleaned.contains("aprendiz", ignoreCase = true) -> AppState.DASHBOARD_APRENDIZ
                                        cleaned.contains("invitado", ignoreCase = true) -> AppState.DASHBOARD_INVITADO
                                        else -> AppState.DASHBOARD_INVITADO
                                    }
                                    lastDashboardState = targetDashboard
                                    currentState = targetDashboard
                                }
                            )
                        }

                        AppState.REGISTER -> {
                            RegisterScreen(
                                onBackClick = { currentState = AppState.LOGIN },
                                onLoginClick = { currentState = AppState.LOGIN },
                                onRegisterSuccess = { currentState = AppState.ROLE_SELECTION }
                            )
                        }

                        AppState.FORGOT_PASSWORD -> {
                            ForgotPasswordScreen(
                                onBackClick = { currentState = AppState.LOGIN },
                                onSendCodeClick = { currentState = AppState.VERIFY_CODE }
                            )
                        }

                        AppState.VERIFY_CODE -> {
                            VerifyCodeScreen(
                                onBackClick = { currentState = AppState.FORGOT_PASSWORD },
                                onContinueClick = { currentState = AppState.NEW_PASSWORD }
                            )
                        }

                        AppState.NEW_PASSWORD -> {
                            NewPasswordScreen(
                                onBackClick = { currentState = AppState.VERIFY_CODE },
                                onChangePasswordSuccess = { currentState = AppState.LOGIN }
                            )
                        }

                        AppState.ROLE_SELECTION -> {
                            RoleSelectionScreen(
                                onBackClick = { currentState = AppState.LOGIN },
                                onRoleSelected = { role ->
                                    val cleaned = role.trim()
                                    val targetDashboard = when {
                                        cleaned.contains("admin", ignoreCase = true) -> AppState.DASHBOARD_ADMIN
                                        cleaned.contains("instructor", ignoreCase = true) -> AppState.DASHBOARD_INSTRUCTOR
                                        cleaned.contains("aprendiz", ignoreCase = true) -> AppState.DASHBOARD_APRENDIZ
                                        cleaned.contains("invitado", ignoreCase = true) -> AppState.DASHBOARD_INVITADO
                                        else -> AppState.DASHBOARD_INVITADO
                                    }
                                    lastDashboardState = targetDashboard
                                    currentState = targetDashboard
                                }
                            )
                        }

                        AppState.DASHBOARD_ADMIN -> {
                            lastDashboardState = AppState.DASHBOARD_ADMIN
                            AdminDashboardScreen(
                                onMenuClick = { currentState = AppState.DRAWER_ADMIN },
                                onNavigateTo = { destination ->
                                    val cleaned = destination.trim().lowercase()
                                    currentState = when {
                                        cleaned.contains("inicio") -> AppState.DASHBOARD_ADMIN
                                        cleaned.contains("inventario") -> AppState.INVENTORY
                                        cleaned.contains("equipo") || cleaned.contains("registroequipos") -> AppState.REGISTRO_EQUIPOS
                                        cleaned.contains("ambiente") -> AppState.AMBIENTES
                                        cleaned.contains("cronograma") -> AppState.CRONOGRAMA
                                        cleaned.contains("simulacion") -> AppState.SIMULACIONES
                                        cleaned.contains("guia") -> AppState.GUIAS
                                        cleaned.contains("evaluacion") -> AppState.EVALUACIONES
                                        cleaned.contains("perfil") -> AppState.PERFIL
                                        cleaned.contains("ajuste") -> AppState.AJUSTES
                                        cleaned.contains("notificac") -> AppState.NOTIFICACIONES
                                        cleaned.contains("contacto") -> AppState.CONTACTO
                                        cleaned.contains("galeria") || cleaned.contains("foto") -> AppState.GALERIA_FOTOS
                                        else -> AppState.DASHBOARD_ADMIN
                                    }
                                }
                            )
                        }

                        AppState.DRAWER_ADMIN -> {
                            AdminDrawerScreen(
                                onCloseDrawer = { currentState = AppState.DASHBOARD_ADMIN },
                                onNavigateTo = { destination ->
                                    val cleaned = destination.trim().lowercase()
                                    currentState = when {
                                        cleaned.contains("inicio") -> AppState.DASHBOARD_ADMIN
                                        cleaned.contains("inventario") -> AppState.INVENTORY
                                        cleaned.contains("equipo") -> AppState.REGISTRO_EQUIPOS
                                        cleaned.contains("ambiente") -> AppState.AMBIENTES
                                        cleaned.contains("cronograma") -> AppState.CRONOGRAMA
                                        cleaned.contains("simulacion") -> AppState.SIMULACIONES
                                        cleaned.contains("guia") -> AppState.GUIAS
                                        cleaned.contains("evaluacion") -> AppState.EVALUACIONES
                                        cleaned.contains("perfil") -> AppState.PERFIL
                                        cleaned.contains("ajuste") -> AppState.AJUSTES
                                        cleaned.contains("notificac") -> AppState.NOTIFICACIONES
                                        cleaned.contains("contacto") -> AppState.CONTACTO
                                        cleaned.contains("galeria") -> AppState.GALERIA_FOTOS
                                        else -> AppState.DASHBOARD_ADMIN
                                    }
                                }
                            )
                        }

                        AppState.DASHBOARD_INSTRUCTOR -> {
                            lastDashboardState = AppState.DASHBOARD_INSTRUCTOR
                            InstructorDashboardScreen(
                                onMenuClick = { currentState = AppState.ROLE_SELECTION },
                                onLogout = { currentState = AppState.LOGIN },
                                onNavigateGuias = { currentState = AppState.GUIAS },
                                onNavigateNotificaciones = { currentState = AppState.NOTIFICACIONES },
                                onNavigatePerfil = { currentState = AppState.PERFIL },
                                onNavigateTo = { destination ->
                                    val cleaned = destination.trim().lowercase()
                                    currentState = when {
                                        cleaned.contains("inicio") -> AppState.DASHBOARD_INSTRUCTOR
                                        cleaned.contains("inventario") -> AppState.INVENTORY
                                        cleaned.contains("ambiente") -> AppState.AMBIENTES
                                        cleaned.contains("cronograma") -> AppState.CRONOGRAMA
                                        cleaned.contains("simulacion") -> AppState.SIMULACIONES
                                        cleaned.contains("guia") -> AppState.GUIAS
                                        cleaned.contains("evaluacion") -> AppState.EVALUACIONES
                                        cleaned.contains("perfil") -> AppState.PERFIL
                                        cleaned.contains("notificac") -> AppState.NOTIFICACIONES
                                        cleaned.contains("galeria") -> AppState.GALERIA_FOTOS
                                        else -> AppState.DASHBOARD_INSTRUCTOR
                                    }
                                }
                            )
                        }

                        AppState.DASHBOARD_APRENDIZ -> {
                            lastDashboardState = AppState.DASHBOARD_APRENDIZ
                            AprendizDashboardScreen(
                                onMenuClick = { currentState = AppState.ROLE_SELECTION },
                                onNavigateCalendario = { currentState = AppState.CRONOGRAMA },
                                onLogout = { currentState = AppState.LOGIN },
                                onNavigateContacto = { currentState = AppState.CONTACTO },
                                onNavigateNotificaciones = { currentState = AppState.NOTIFICACIONES },
                                onNavigatePerfil = { currentState = AppState.PERFIL },
                                onNavigateGuias = { currentState = AppState.GUIAS }
                            )
                        }

                        AppState.DASHBOARD_INVITADO -> {
                            lastDashboardState = AppState.DASHBOARD_INVITADO
                            InvitadoDashboardScreen(
                                onMenuClick = { currentState = AppState.ROLE_SELECTION },
                                onLogout = { currentState = AppState.LOGIN },
                                onNavigateGuias = { currentState = AppState.GUIAS },
                                onNavigateNotificaciones = { currentState = AppState.NOTIFICACIONES },
                                onNavigatePerfil = { currentState = AppState.PERFIL },
                                onNavigateTo = { destination ->
                                    val cleaned = destination.trim().lowercase()
                                    currentState = when {
                                        cleaned.contains("inicio") -> AppState.DASHBOARD_INVITADO
                                        cleaned.contains("ambiente") -> AppState.AMBIENTES
                                        cleaned.contains("cronograma") -> AppState.CRONOGRAMA
                                        cleaned.contains("simulacion") -> AppState.SIMULACIONES
                                        cleaned.contains("guia") -> AppState.GUIAS
                                        cleaned.contains("evaluacion") -> AppState.EVALUACIONES
                                        cleaned.contains("ajuste") -> AppState.AJUSTES
                                        cleaned.contains("notificac") -> AppState.NOTIFICACIONES
                                        cleaned.contains("perfil") -> AppState.PERFIL
                                        cleaned.contains("galeria") -> AppState.GALERIA_FOTOS
                                        else -> AppState.DASHBOARD_INVITADO
                                    }
                                }
                            )
                        }

                        // MÓDULOS CON RETORNO DINÁMICO
                        AppState.INVENTORY -> {
                            InventoryScreen(
                                onBackClick = { currentState = lastDashboardState }
                            )
                        }

                        AppState.REGISTRO_EQUIPOS -> {
                            RegistroEquiposScreen(
                                onBackClick = { currentState = lastDashboardState },
                                onNavigateCalendario = { currentState = AppState.CRONOGRAMA }
                            )
                        }

                        AppState.REGISTER_INVENTORY -> {
                            RegisterInventoryScreen(
                                onBackClick = { currentState = AppState.INVENTORY }
                            )
                        }

                        AppState.INVENTORY_STATUS -> {
                            InventoryStatusScreen(
                                onBackClick = { currentState = AppState.INVENTORY }
                            )
                        }

                        AppState.SIMULACIONES -> {
                            SimulacionesScreen(
                                onBackClick = { currentState = lastDashboardState }
                            )
                        }

                        AppState.GUIAS -> {
                            GuiasScreen(
                                onBackClick = { currentState = lastDashboardState },
                                onNavigateEvaluaciones = { currentState = AppState.EVALUACIONES }
                            )
                        }

                        AppState.EVALUACIONES -> {
                            EvaluacionesScreen(
                                onBackClick = { currentState = AppState.GUIAS }
                            )
                        }

                        AppState.CRONOGRAMA -> {
                            CronogramaScreen(
                                onBackClick = { currentState = lastDashboardState }
                            )
                        }

                        AppState.AMBIENTES -> {
                            AmbientesScreen(
                                onBackClick = { currentState = lastDashboardState }
                            )
                        }

                        AppState.GALERIA_FOTOS -> {
                            CarruselFotosScreen(
                                onBackClick = { currentState = lastDashboardState }
                            )
                        }

                        AppState.PERFIL -> {
                            val roleName = when (lastDashboardState) {
                                AppState.DASHBOARD_ADMIN -> "Administrador"
                                AppState.DASHBOARD_INSTRUCTOR -> "Instructor"
                                AppState.DASHBOARD_APRENDIZ -> "Aprendiz"
                                else -> "Invitado"
                            }

                            ProfileScreen(
                                userRole = roleName,
                                onBackClick = { currentState = lastDashboardState },
                                onNavigateAjustes = { currentState = AppState.AJUSTES },
                                onNavigateNotificaciones = { currentState = AppState.NOTIFICACIONES }
                            )
                        }

                        AppState.AJUSTES -> {
                            SettingsScreen(
                                onBackClick = { currentState = lastDashboardState }
                            )
                        }

                        AppState.NOTIFICACIONES -> {
                            NotificationsScreen(
                                onBackClick = { currentState = lastDashboardState }
                            )
                        }

                        AppState.CONTACTO -> {
                            ContactoScreen(
                                onBackClick = { currentState = lastDashboardState }
                            )
                        }
                    }
                }
            }
        }
    }
}