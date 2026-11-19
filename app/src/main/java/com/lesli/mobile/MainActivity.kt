package com.lesli.mobile

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.lesli.mobile.data.model.UserProfile
import com.lesli.mobile.di.LesliViewModelFactory
import com.lesli.mobile.navigation.LesliNavGraph
import com.lesli.mobile.ui.components.LesliBottomBar
import com.lesli.mobile.ui.components.LesliTopAppBar
import com.lesli.mobile.ui.theme.LesliMobileTheme
import com.lesli.mobile.viewmodel.AssistantViewModel
import com.lesli.mobile.viewmodel.HistoryViewModel
import com.lesli.mobile.viewmodel.HomeViewModel
import com.lesli.mobile.viewmodel.ProfileViewModel
import com.lesli.mobile.viewmodel.TaskViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val appContainer = (application as LesliApplication).container
        val factory = LesliViewModelFactory(appContainer)

        // ViewModels managed via Android ViewModelProvider to survive configuration changes
        val homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
        val assistantViewModel = ViewModelProvider(this, factory)[AssistantViewModel::class.java]
        val taskViewModel = ViewModelProvider(this, factory)[TaskViewModel::class.java]
        val historyViewModel = ViewModelProvider(this, factory)[HistoryViewModel::class.java]
        val profileViewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]

        setContent {
            val userProfile by profileViewModel.userProfile.collectAsState(initial = UserProfile())

            LesliMobileTheme(darkTheme = userProfile.isDarkMode) {
                LesliMainApp(
                    homeViewModel = homeViewModel,
                    assistantViewModel = assistantViewModel,
                    taskViewModel = taskViewModel,
                    historyViewModel = historyViewModel,
                    profileViewModel = profileViewModel,
                    onNotificationClick = {
                        Toast.makeText(
                            this,
                            "Notificaciones de Lesli Mobile activadas",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
        }
    }
}

@Composable
fun LesliMainApp(
    homeViewModel: HomeViewModel,
    assistantViewModel: AssistantViewModel,
    taskViewModel: TaskViewModel,
    historyViewModel: HistoryViewModel,
    profileViewModel: ProfileViewModel,
    onNotificationClick: () -> Unit
) {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            LesliTopAppBar(
                title = "Lesli Mobile",
                showNotificationIcon = true,
                onNotificationClick = onNotificationClick
            )
        },
        bottomBar = {
            LesliBottomBar(navController = navController)
        }
    ) { innerPadding ->
        LesliNavGraph(
            navController = navController,
            homeViewModel = homeViewModel,
            assistantViewModel = assistantViewModel,
            taskViewModel = taskViewModel,
            historyViewModel = historyViewModel,
            profileViewModel = profileViewModel,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
