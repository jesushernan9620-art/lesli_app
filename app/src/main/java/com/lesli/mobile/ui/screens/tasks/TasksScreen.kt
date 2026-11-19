package com.lesli.mobile.ui.screens.tasks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Task
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lesli.mobile.R
import com.lesli.mobile.data.local.DemoData
import com.lesli.mobile.data.model.TaskItem
import com.lesli.mobile.data.repository.TaskRepository
import com.lesli.mobile.ui.components.EmptyStateView
import com.lesli.mobile.ui.components.LesliCard
import com.lesli.mobile.ui.components.TaskCard
import com.lesli.mobile.ui.components.TaskDialog
import com.lesli.mobile.ui.components.TaskFilter
import com.lesli.mobile.ui.components.TaskFilterChips
import com.lesli.mobile.ui.theme.LesliMobileTheme
import com.lesli.mobile.viewmodel.TaskViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun TasksScreen(
    viewModel: TaskViewModel,
    modifier: Modifier = Modifier
) {
    val tasks by viewModel.tasks.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()
    val isDialogVisible by viewModel.isDialogVisible.collectAsState()
    val taskToEdit by viewModel.taskToEdit.collectAsState()

    val pendingCount = tasks.count { !it.isCompleted }
    val completedCount = tasks.count { it.isCompleted }

    val filteredTasks = when (selectedFilter) {
        TaskFilter.TODAS -> tasks
        TaskFilter.PENDIENTES -> tasks.filter { !it.isCompleted }
        TaskFilter.COMPLETADAS -> tasks.filter { it.isCompleted }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.openCreateDialog() },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.cd_add_task)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.tasks_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Counters Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                LesliCard(
                    modifier = Modifier.weight(1f),
                    padding = 12.dp
                ) {
                    Text(
                        text = stringResource(R.string.tasks_pending_label),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "$pendingCount",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                LesliCard(
                    modifier = Modifier.weight(1f),
                    padding = 12.dp
                ) {
                    Text(
                        text = stringResource(R.string.tasks_completed_label),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "$completedCount",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Filter Chips
            TaskFilterChips(
                selectedFilter = selectedFilter,
                onFilterSelected = { viewModel.selectFilter(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Task List
            if (filteredTasks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyStateView(
                        icon = Icons.Default.Task,
                        title = stringResource(R.string.tasks_empty_title),
                        description = stringResource(R.string.tasks_empty_desc)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = filteredTasks,
                        key = { it.id }
                    ) { task ->
                        TaskCard(
                            task = task,
                            onToggleComplete = { viewModel.toggleTaskCompletion(task.id) },
                            onEdit = { viewModel.openEditDialog(task) },
                            onDelete = { viewModel.deleteTask(task.id) }
                        )
                    }
                }
            }
        }

        // Add / Edit Task Dialog
        if (isDialogVisible) {
            TaskDialog(
                taskToEdit = taskToEdit,
                onDismiss = { viewModel.closeDialog() },
                onSave = { title, desc, date, priority ->
                    viewModel.saveTask(title, desc, date, priority)
                }
            )
        }
    }
}

private class PreviewTaskRepo : TaskRepository {
    override val tasks: Flow<List<TaskItem>> = flowOf(DemoData.getInitialTasks())
    override suspend fun addTask(task: TaskItem) {}
    override suspend fun updateTask(task: TaskItem) {}
    override suspend fun toggleTaskCompletion(taskId: String) {}
    override suspend fun deleteTask(taskId: String) {}
}

@Preview(showBackground = true)
@Composable
fun TasksScreenPreview() {
    LesliMobileTheme(darkTheme = true) {
        TasksScreen(
            viewModel = TaskViewModel(PreviewTaskRepo())
        )
    }
}
