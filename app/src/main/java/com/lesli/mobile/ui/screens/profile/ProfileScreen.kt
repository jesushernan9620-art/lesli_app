package com.lesli.mobile.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lesli.mobile.R
import com.lesli.mobile.data.model.UserProfile
import com.lesli.mobile.data.repository.UserPreferencesRepository
import com.lesli.mobile.ui.components.LesliCard
import com.lesli.mobile.ui.components.LesliMoonLogo
import com.lesli.mobile.ui.components.LesliOutlinedButton
import com.lesli.mobile.ui.components.LesliPrimaryButton
import com.lesli.mobile.ui.theme.LesliMobileTheme
import com.lesli.mobile.viewmodel.ProfileViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val isEditDialogVisible by viewModel.isEditDialogVisible.collectAsState()
    val isLogoutDialogVisible by viewModel.isLogoutDialogVisible.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.profile_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Avatar & Info Header Card
        LesliCard(
            modifier = Modifier.fillMaxWidth(),
            padding = 16.dp
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = stringResource(R.string.cd_avatar),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = userProfile.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = userProfile.email,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = userProfile.role,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                IconButton(onClick = { viewModel.openEditDialog() }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(R.string.cd_edit_profile),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Sección Preferencias
        Text(
            text = stringResource(R.string.profile_section_preferences),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(10.dp))

        LesliCard(padding = 8.dp) {
            PreferenceSwitchItem(
                icon = Icons.Default.Notifications,
                title = stringResource(R.string.profile_pref_notifications_title),
                subtitle = stringResource(R.string.profile_pref_notifications_sub),
                checked = userProfile.isNotificationsEnabled,
                onCheckedChange = { viewModel.toggleNotifications(it) }
            )

            PreferenceSwitchItem(
                icon = Icons.Default.DarkMode,
                title = stringResource(R.string.profile_pref_dark_mode_title),
                subtitle = stringResource(R.string.profile_pref_dark_mode_sub),
                checked = userProfile.isDarkMode,
                onCheckedChange = { viewModel.toggleDarkMode(it) }
            )

            PreferenceSwitchItem(
                icon = Icons.Default.VolumeUp,
                title = stringResource(R.string.profile_pref_sound_title),
                subtitle = stringResource(R.string.profile_pref_sound_sub),
                checked = userProfile.soundEnabled,
                onCheckedChange = { viewModel.toggleSound(it) }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Sección Información de la Aplicación
        Text(
            text = stringResource(R.string.profile_section_app_info),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(10.dp))

        LesliCard(padding = 16.dp) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                LesliMoonLogo(size = 42.dp, showGlow = true)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = stringResource(R.string.profile_app_version),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = stringResource(R.string.profile_app_desc),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = stringResource(R.string.profile_app_status),
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón Cerrar Sesión
        LesliOutlinedButton(
            text = stringResource(R.string.profile_btn_logout),
            onClick = { viewModel.openLogoutDialog() },
            icon = Icons.Default.Logout,
            modifier = Modifier.fillMaxWidth()
        )
    }

    // Edit Profile Dialog
    if (isEditDialogVisible) {
        var editName by remember { mutableStateOf(userProfile.name) }
        var editEmail by remember { mutableStateOf(userProfile.email) }

        AlertDialog(
            onDismissRequest = { viewModel.closeEditDialog() },
            title = { Text(stringResource(R.string.profile_edit_dialog_title)) },
            text = {
                Column {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text(stringResource(R.string.profile_edit_name_label)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = editEmail,
                        onValueChange = { editEmail = it },
                        label = { Text(stringResource(R.string.profile_edit_email_label)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                LesliPrimaryButton(
                    text = stringResource(R.string.btn_save),
                    onClick = { viewModel.updateProfile(editName, editEmail) }
                )
            },
            dismissButton = {
                TextButton(onClick = { viewModel.closeEditDialog() }) {
                    Text(stringResource(R.string.btn_cancel), color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(16.dp)
        )
    }

    // Logout Confirmation Dialog
    if (isLogoutDialogVisible) {
        AlertDialog(
            onDismissRequest = { viewModel.closeLogoutDialog() },
            title = { Text(stringResource(R.string.profile_logout_dialog_title)) },
            text = { Text(stringResource(R.string.profile_logout_dialog_desc)) },
            confirmButton = {
                LesliPrimaryButton(
                    text = stringResource(R.string.profile_logout_confirm),
                    onClick = { viewModel.logout() }
                )
            },
            dismissButton = {
                TextButton(onClick = { viewModel.closeLogoutDialog() }) {
                    Text(stringResource(R.string.btn_cancel), color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
fun PreferenceSwitchItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                uncheckedThumbColor = MaterialTheme.colorScheme.onSurfaceVariant,
                uncheckedTrackColor = MaterialTheme.colorScheme.surface
            )
        )
    }
}

private class PreviewUserPrefRepo : UserPreferencesRepository {
    override val userProfile: Flow<UserProfile> = flowOf(UserProfile())
    override suspend fun updateProfile(name: String, email: String) {}
    override suspend fun toggleNotifications(enabled: Boolean) {}
    override suspend fun toggleDarkMode(enabled: Boolean) {}
    override suspend fun toggleSound(enabled: Boolean) {}
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    LesliMobileTheme(darkTheme = true) {
        ProfileScreen(
            viewModel = ProfileViewModel(PreviewUserPrefRepo())
        )
    }
}
