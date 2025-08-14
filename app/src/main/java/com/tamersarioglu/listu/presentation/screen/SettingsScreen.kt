package com.tamersarioglu.listu.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tamersarioglu.listu.domain.model.AppTheme
import com.tamersarioglu.listu.domain.model.CacheSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    var selectedTheme by remember { mutableStateOf(AppTheme.SYSTEM) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var nsfwContentEnabled by remember { mutableStateOf(false) }
    var autoRefreshEnabled by remember { mutableStateOf(true) }
    var selectedCacheSize by remember { mutableStateOf(CacheSize.MEDIUM) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "Settings",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                SettingsSection(title = "Appearance") {
                    Column {
                        SettingsSectionHeader(text = "Theme")
                        Spacer(modifier = Modifier.height(8.dp))

                        AppTheme.entries.forEach { theme ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .selectable(
                                        selected = selectedTheme == theme,
                                        onClick = { selectedTheme = theme },
                                        role = Role.RadioButton
                                    )
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selectedTheme == theme,
                                    onClick = { selectedTheme = theme }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = theme.displayName,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }
            }

            item {
                SettingsSection(title = "Content") {
                    Column {
                        SettingsToggle(
                            icon = Icons.Default.Visibility,
                            title = "Show NSFW Content",
                            description = "Display adult or mature content",
                            checked = nsfwContentEnabled,
                            onCheckedChange = { nsfwContentEnabled = it }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        SettingsToggle(
                            icon = Icons.Default.Refresh,
                            title = "Auto Refresh",
                            description = "Automatically refresh content when opening the app",
                            checked = autoRefreshEnabled,
                            onCheckedChange = { autoRefreshEnabled = it }
                        )
                    }
                }
            }

            item {
                SettingsSection(title = "Notifications") {
                    SettingsToggle(
                        icon = Icons.Default.Notifications,
                        title = "Enable Notifications",
                        description = "Receive updates about new episodes and releases",
                        checked = notificationsEnabled,
                        onCheckedChange = { notificationsEnabled = it }
                    )
                }
            }

            item {
                SettingsSection(title = "Storage") {
                    Column {
                        SettingsSectionHeader(text = "Cache Size")
                        Spacer(modifier = Modifier.height(8.dp))

                        CacheSize.entries.forEach { cacheSize ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .selectable(
                                        selected = selectedCacheSize == cacheSize,
                                        onClick = { selectedCacheSize = cacheSize },
                                        role = Role.RadioButton
                                    )
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selectedCacheSize == cacheSize,
                                    onClick = { selectedCacheSize = cacheSize }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = cacheSize.displayName,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedButton(
                            onClick = { /* TODO: Implement cache clearing */ },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Clear Cache")
                        }
                    }
                }
            }

            item {
                SettingsSection(title = "About") {
                    Column {
                        SettingsItem(
                            icon = Icons.Default.Info,
                            title = "Version",
                            description = "1.0.0",
                            onClick = { }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        SettingsItem(
                            icon = Icons.Default.Code,
                            title = "Open Source",
                            description = "View source code on GitHub",
                            onClick = { /* TODO: Open GitHub */ }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        SettingsItem(
                            icon = Icons.Default.BugReport,
                            title = "Report Issue",
                            description = "Found a bug? Let us know",
                            onClick = { /* TODO: Open issue tracker */ }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            content()
        }
    }
}

@Composable
private fun SettingsSectionHeader(
    text: String
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
private fun SettingsToggle(
    icon: ImageVector,
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsItem(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}