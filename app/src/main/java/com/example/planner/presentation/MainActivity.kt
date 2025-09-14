package com.example.planner.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.planner.presentation.bottomNavigationBar.BottomNavigationBar
import com.example.planner.presentation.dashboard.DashboardScreen
import com.example.planner.presentation.daysScreen.DayScreen
import com.example.planner.presentation.login.LoginScreen
import com.example.planner.presentation.registration.RegistrationScreen
import com.example.planner.presentation.setPlan.SetPlanScreen
import com.example.planner.presentation.settings.SettingsScreen
import com.example.planner.ui.theme.PlannerTheme
import com.example.planner.presentation.splashScreen.SplashScreen
import com.example.planner.presentation.viewPlan.ViewPlanScreen
import com.example.planner.presentation.weeksScreen.WeekScreen
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val requestNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->

        }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        enableEdgeToEdge()
        setContent {
            PlannerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "splashScreen") {
                        composable("splashScreen") { SplashScreen(navController) }
                        composable("login") { LoginScreen(navController) }
                        composable("registration") { RegistrationScreen(navController) }
                        composable("dashboard") { DashboardScreen(navController) } // home
                        composable("navigation") { BottomNavigationBar(navController, 0) }
                        composable("settings") { SettingsScreen(navController) }
                        composable("weeks") { WeekScreen(navController) }
                        composable("days") { backStackEntry ->
                            val dateArg = backStackEntry.arguments?.getString("date")
                            val date = dateArg?.let { LocalDate.parse(it) } ?: LocalDate.now()
                            DayScreen(navController, date)
                        }
                        composable("plan") { SetPlanScreen(navController) }

                        composable(
                            "viewPlan/{title}/{description}/{startDate}/{endDate}",
                            arguments = listOf(
                                navArgument("title") { type = NavType.StringType },
                                navArgument("description") { type = NavType.StringType },
                                navArgument("startDate") { type = NavType.StringType },
                                navArgument("endDate") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            ViewPlanScreen(
                                navController = navController,
                                title = backStackEntry.arguments?.getString("title") ?: "",
                                description = backStackEntry.arguments?.getString("description") ?: "",
                                startDate = backStackEntry.arguments?.getString("startDate") ?: "",
                                endDate = backStackEntry.arguments?.getString("endDate") ?: ""
                            )
                        }

                        composable(
                            route = "days/{date}",
                            arguments = listOf(
                                navArgument("date") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val dateStr = backStackEntry.arguments?.getString("date")!!
                            val date = LocalDate.parse(dateStr)

                            DayScreen(
                                navController = navController,
                                date = date
                            )
                        }
                    }
                }
            }
        }
    }
}
