package com.tamersarioglu.listu.presentation.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.tamersarioglu.listu.core.performance.PerformanceMonitor
import com.tamersarioglu.listu.domain.model.AppTheme
import com.tamersarioglu.listu.domain.model.CacheSize
import com.tamersarioglu.listu.presentation.ui.theme.AccentColor
import com.tamersarioglu.listu.presentation.ui.theme.ThemeManager
import com.tamersarioglu.listu.presentation.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    themeManager: ThemeManager,
    performanceMonitor: PerformanceMonitor
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentTheme by themeManager.currentTheme.collectAsState()
    val currentAccent by themeManager.accentColor.collectAsState()
    val useDynamicColor by themeManager.useDynamicColor.collectAsState()
    val useHighContrast by themeManager.useHighContrast.collectAsState()
    val performanceMetrics by performanceMonitor.metrics.collectAsState()

    var showPerformanceDetails by remember { mutableStateOf(false) }

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
                ),
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Advanced Theming Section
            item {
                SettingsSection(title = "Appearance") {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Theme Selection
                        SettingsSectionHeader(text = "Theme")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            AppTheme.entries.forEach { theme ->
                                FilterChip(
                                    selected = currentTheme == theme,
                                    onClick = { themeManager.setTheme(theme) },
                                    label = { Text(theme.displayName) },
                                    modifier = Modifier.weight(1f),
                                    leadingIcon = {
                                        Icon(
                                            when (theme) {
                                                AppTheme.SYSTEM -> Icons.Default.PhoneAndroid
                                                AppTheme.LIGHT -> Icons.Default.LightMode
                                                AppTheme.DARK -> Icons.Default.DarkMode
                                            },
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                )
                            }
                        }

                        // Accent Color Selection
                        SettingsSectionHeader(text = "Accent Color")
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(AccentColor.entries) { color ->
                                Surface(
                                    onClick = { themeManager.setAccentColor(color) },
                                    shape = CircleShape,
                                    color = color.primary,
                                    modifier = Modifier.size(48.dp),
                                    border = if (currentAccent == color) {
                                        BorderStroke(3.dp, MaterialTheme.colorScheme.outline)
                                    } else null
                                ) {
                                    if (currentAccent == color) {
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = "${color.displayName} selected",
                                            tint = color.onPrimary,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(12.dp)
                                        )
                                    }
                                }
                            }
                        }

                        // Dynamic Color Toggle
                        SettingsToggle(
                            icon = Icons.Default.Palette,
                            title = "Material You",
                            description = "Use system dynamic colors (Android 12+)",
                            checked = useDynamicColor,
                            onCheckedChange = { themeManager.setUseDynamicColor(it) }
                        )

                        // High Contrast Toggle
                        SettingsToggle(
                            icon = Icons.Default.Contrast,
                            title = "High Contrast",
                            description = "Increase color contrast for better accessibility",
                            checked = useHighContrast,
                            onCheckedChange = { themeManager.setUseHighContrast(it) }
                        )
                    }
                }
            }

            // Content Settings
            item {
                SettingsSection(title = "Content") {
                    Column {
                        SettingsToggle(
                            icon = Icons.Default.Visibility,
                            title = "Show NSFW Content",
                            description = "Display adult or mature content",
                            checked = uiState.showNsfwContent,
                            onCheckedChange = { viewModel.updateNsfwContent(it) }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        SettingsToggle(
                            icon = Icons.Default.Refresh,
                            title = "Auto Refresh",
                            description = "Automatically refresh content when opening the app",
                            checked = uiState.autoRefresh,
                            onCheckedChange = { viewModel.updateAutoRefresh(it) }
                        )
                    }
                }
            }

            // Notifications
            item {
                SettingsSection(title = "Notifications") {
                    SettingsToggle(
                        icon = Icons.Default.Notifications,
                        title = "Enable Notifications",
                        description = "Receive updates about new episodes and releases",
                        checked = uiState.notificationsEnabled,
                        onCheckedChange = { viewModel.updateNotifications(it) }
                    )
                }
            }

            // Storage & Cache
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
                                        selected = uiState.cacheSize == cacheSize,
                                        onClick = { viewModel.updateCacheSize(cacheSize) },
                                        role = Role.RadioButton
                                    )
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = uiState.cacheSize == cacheSize,
                                    onClick = { viewModel.updateCacheSize(cacheSize) }
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
                            onClick = { viewModel.clearCache() },
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

            // Performance Metrics
            item {
                SettingsSection(title = "Performance") {
                    Column {
                        SettingsItem(
                            icon = Icons.Default.Speed,
                            title = "Performance Metrics",
                            description = "View app performance statistics",
                            onClick = { showPerformanceDetails = !showPerformanceDetails }
                        )

                        if (showPerformanceDetails) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(
                                        text = "Performance Summary",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text("Total Operations: ${performanceMetrics.totalOperations}")

                                    performanceMetrics.operations.values.take(5)
                                        .forEach { operation ->
                                            Text(
                                                "${operation.name}: ${operation.averageTime}ms avg (${operation.count}x)",
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }

                                    Spacer(modifier = Modifier.height(8.dp))
                                    OutlinedButton(
                                        onClick = { performanceMonitor.clearMetrics() },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Clear Metrics")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Accessibility
            item {
                SettingsSection(title = "Accessibility") {
                    Column {
                        SettingsItem(
                            icon = Icons.Default.Accessibility,
                            title = "Text Size",
                            description = "Adjust text size for better readability",
                            onClick = { /* TODO: Implement text size adjustment */ }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        SettingsToggle(
                            icon = Icons.Default.TouchApp,
                            title = "Reduce Motion",
                            description = "Minimize animations and motion effects",
                            checked = uiState.reduceMotion,
                            onCheckedChange = { viewModel.updateReduceMotion(it) }
                        )
                    }
                }
            }

            // About Section
            item {
                SettingsSection(title = "About") {
                    Column {
                        SettingsItem(
                            icon = Icons.Default.Info,
                            title = "Version",
                            description = "1.0.0 (Phase 4)",
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

                        Spacer(modifier = Modifier.height(8.dp))

                        SettingsItem(
                            icon = Icons.Default.PrivacyTip,
                            title = "Privacy Policy",
                            description = "Learn how we protect your data",
                            onClick = { /* TODO: Open privacy policy */ }
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