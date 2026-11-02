package com.example.ui.screens

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AlarmItemEntity
import com.example.ui.theme.*
import com.example.ui.viewmodel.StudyViewModel

@Composable
fun AlarmsScreen(
    viewModel: StudyViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val alarms by viewModel.alarms.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    // Android 13+ Notification permission launcher
    var hasNotificationPermission by remember { mutableStateOf(true) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasNotificationPermission = isGranted
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("alarms_screen")
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. HEADER
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "SMART ALARMS & ROUTINES",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            letterSpacing = 1.sp
                        )
                    )
                    Text(
                        text = "Android AlarmManager exact daily study alarms",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = ElectricCyan
                        )
                    )
                }

                Button(
                    onClick = { showAddDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("add_alarm_button")
                ) {
                    Icon(
                        Icons.Default.AddAlarm,
                        contentDescription = "New Alarm",
                        tint = DarkOnPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Add",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DarkOnPrimary
                        )
                    )
                }
            }
        }

        // 2. PERMISSION BANNER (ON ANDROID 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasNotificationPermission) {
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF59E0B).copy(alpha = 0.15f)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF59E0B)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.Default.NotificationsActive,
                                contentDescription = "Enable Notifications",
                                tint = Color(0xFFF59E0B)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Enable Notifications to receive loud study session alerts and wake-up chimes.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                        Button(
                            onClick = { permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF59E0B)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Grant", color = DarkOnPrimary)
                        }
                    }
                }
            }
        }

        // 3. DAILY ROUTINE DESCRIPTION CARD
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.AccessTimeFilled,
                            contentDescription = "Routine",
                            tint = ElectricCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "COMMAND CENTER DAILY DISCIPLINE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = ElectricCyan,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "9 pre-configured alarm checkpoints ensure strict adherence: 4:00 AM Wakeup, 4:05 AM Revision, 4:00 PM Study, 6:30 PM Second Study, and 9:00 PM Practice.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }

        // 4. LIST OF ALARMS
        items(alarms, key = { it.id }) { alarm ->
            AlarmCard(
                alarm = alarm,
                onToggleEnabled = { viewModel.toggleAlarm(alarm) },
                onTestAlarm = { viewModel.triggerAlarmTest(alarm, context) },
                onDelete = { viewModel.deleteAlarm(alarm) }
            )
        }
    }

    if (showAddDialog) {
        AddAlarmDialog(
            onDismiss = { showAddDialog = false },
            onSave = { title, hour, minute, sound ->
                val timeStr = formatTime(hour, minute)
                val newAlarm = AlarmItemEntity(
                    title = title,
                    timeStr = timeStr,
                    hour = hour,
                    minute = minute,
                    isEnabled = true,
                    isDailyRoutine = false,
                    soundName = sound
                )
                viewModel.saveAlarm(newAlarm)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun AlarmCard(
    alarm: AlarmItemEntity,
    onToggleEnabled: () -> Unit,
    onTestAlarm: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (alarm.isEnabled) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (alarm.isEnabled) ElectricCyan.copy(alpha = 0.4f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        ),
        modifier = modifier
            .fillMaxWidth()
            .testTag("alarm_card_${alarm.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = alarm.timeStr,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = if (alarm.isEnabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    if (alarm.isDailyRoutine) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = ElectricCyan.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "Daily Routine",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = ElectricCyan,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = alarm.title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Text(
                    text = "Sound: ${alarm.soundName} • Loud + Vibrate",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        fontSize = 10.sp
                    )
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Test Ring Button
                IconButton(
                    onClick = onTestAlarm,
                    modifier = Modifier.testTag("test_alarm_${alarm.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Test Sound",
                        tint = ElectricCyan,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Switch(
                    checked = alarm.isEnabled,
                    onCheckedChange = { onToggleEnabled() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = ElectricCyan,
                        checkedTrackColor = ElectricCyan.copy(alpha = 0.3f)
                    ),
                    modifier = Modifier.testTag("switch_alarm_${alarm.id}")
                )

                if (!alarm.isDailyRoutine) {
                    IconButton(onClick = onDelete) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete Alarm",
                            tint = Color(0xFFEF4444).copy(alpha = 0.7f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AddAlarmDialog(
    onDismiss: () -> Unit,
    onSave: (title: String, hour: Int, minute: Int, sound: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var hour by remember { mutableStateOf("16") }
    var minute by remember { mutableStateOf("00") }
    var sound by remember { mutableStateOf("Command Clarion") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Set Custom Alarm") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Alarm Label / Reminder") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = hour,
                        onValueChange = { hour = it },
                        label = { Text("Hour (0-23)") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = minute,
                        onValueChange = { minute = it },
                        label = { Text("Minute (0-59)") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
                OutlinedTextField(
                    value = sound,
                    onValueChange = { sound = it },
                    label = { Text("Alarm Sound Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val h = hour.toIntOrNull()?.coerceIn(0, 23) ?: 16
                    val m = minute.toIntOrNull()?.coerceIn(0, 59) ?: 0
                    if (title.isNotBlank()) {
                        onSave(title, h, m, sound)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan)
            ) {
                Text("Set Alarm", color = DarkOnPrimary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

fun formatTime(hour: Int, minute: Int): String {
    val amPm = if (hour >= 12) "PM" else "AM"
    val h = when {
        hour == 0 -> 12
        hour > 12 -> hour - 12
        else -> hour
    }
    val mStr = if (minute < 10) "0$minute" else "$minute"
    return "$h:$mStr $amPm"
}
