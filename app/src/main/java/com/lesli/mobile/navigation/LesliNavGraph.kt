package com.lesli.mobile.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lesli.mobile.ui.screens.assistant.AssistantScreen
import com.lesli.mobile.ui.screens.history.HistoryScreen
import com.lesli.mobile.ui.screens.home.HomeScreen
import com.lesli.mobile.ui.screens.profile.ProfileScreen
import com.lesli.mobile.ui.screens.tasks.TasksScreen
import com.lesli.mobile.viewmodel.AssistantViewModel
import com.lesli.mobile.viewmodel.HistoryViewModel
import com.lesli.mobile.viewmodel.HomeViewModel
import com.lesli.mobile.viewmodel.ProfileViewModel
import com.lesli.mobile.viewmodel.TaskViewModel

@Composable
fun LesliNavGraph(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    assistantViewModel: AssistantViewModel,
    taskViewModel: TaskViewModel,
    historyViewModel: HistoryViewModel,
    profileViewModel: ProfileViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Home.route,
        modifier = modifier
    ) {
        composable(NavRoutes.Home.route) {
            HomeScreen(
                viewModel = homeViewModel,
                onNavigateToAssistant = { initialQuery ->
                    if (!initialQuery.isNullOrBlank()) {
                        assistantViewModel.onInputTextChange(initialQuery)
                        assistantViewModel.sendMessage()
                    }
                    navController.navigate(NavRoutes.Assistant.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNavigateToTasks = {
                    navController.navigate(NavRoutes.Tasks.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNavigateToHistory = {
                    navController.navigate(NavRoutes.History.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        composable(NavRoutes.Assistant.route) {
            AssistantScreen(
                viewModel = assistantViewModel
            )
        }

        composable(NavRoutes.Tasks.route) {
            TasksScreen(
                viewModel = taskViewModel
            )
        }

        composable(NavRoutes.History.route) {
            HistoryScreen(
                viewModel = historyViewModel,
                onSelectConversation = {
                    navController.navigate(NavRoutes.Assistant.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        composable(NavRoutes.Profile.route) {
            ProfileScreen(
                viewModel = profileViewModel
            )
        }
    }
}
