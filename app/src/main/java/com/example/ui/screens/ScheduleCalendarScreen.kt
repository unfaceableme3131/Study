package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SchedulePhase
import com.example.data.model.ScheduleTaskEntity
import com.example.ui.components.PhaseChip
import com.example.ui.theme.*
import com.example.ui.viewmodel.StudyViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ScheduleCalendarScreen(
    viewModel: StudyViewModel,
    modifier: Modifier = Modifier
) {
    val allTasks by viewModel.tasks.collectAsState()
    val selectedDateStr by viewModel.selectedDate.collectAsState()
    var selectedMonth by remember { mutableStateOf(11) } // 11 for Nov 2026, 12 for Dec 2026
    var filterPhase by remember { mutableStateOf<SchedulePhase?>(null) }

    var editingTask by remember { mutableStateOf<ScheduleTaskEntity?>(null) }
    var showAddTaskDialog by remember { mutableStateOf(false) }

    val daysInMonth = remember(selectedMonth) {
        if (selectedMonth == 11) {
            (3..30).map { day ->
                val dayStr = if (day < 10) "0$day" else "$day"
                "2026-11-$dayStr"
            }
        } else {
            (1..6).map { day ->
                val dayStr = if (day < 10) "0$day" else "$day"
                "2026-12-$dayStr"
            }
        }
    }

    val selectedDateTasks = remember(allTasks, selectedDateStr, filterPhase) {
        allTasks.filter { task ->
            task.dateStr == selectedDateStr && (filterPhase == null || task.phase == filterPhase)
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("schedule_calendar_screen")
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
                        text = "ACADEMIC TIMELINE",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            letterSpacing = 1.sp
                        )
                    )
                    Text(
                        text = "3 November – 6 December 2026 Schedule",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = NeonCyan
                        )
                    )
                }

                Button(
                    onClick = { showAddTaskDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("add_custom_task_button")
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add Task",
                        tint = DarkOnPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Add Slot",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DarkOnPrimary
                        )
                    )
                }
            }
        }

        // 2. MONTH TOGGLES & RADAR LEGEND
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MonthTabButton(
                            title = "November 2026",
                            subtitle = "Nov 3–30 (Syllabus & Rev 1)",
                            isSelected = selectedMonth == 11,
                            onClick = {
                                selectedMonth = 11
                                if (!selectedDateStr.startsWith("2026-11")) {
                                    viewModel.setSelectedDate("2026-11-03")
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )
                        MonthTabButton(
                            title = "December 2026",
                            subtitle = "Dec 1–6 (Rev 2 & Final Prep)",
                            isSelected = selectedMonth == 12,
                            onClick = {
                                selectedMonth = 12
                                if (!selectedDateStr.startsWith("2026-12")) {
                                    viewModel.setSelectedDate("2026-12-01")
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Color Legend
                    Text(
                        text = "STATUS & PHASE RADAR:",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            letterSpacing = 1.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        LegendItem(color = NeonCyan, label = "Syllabus")
                        LegendItem(color = SolarAmber, label = "Rev 1")
                        LegendItem(color = HologramViolet, label = "Rev 2")
                        LegendItem(color = CyberRose, label = "Practice")
                        LegendItem(color = CyberEmerald, label = "Done")
                    }
                }
            }
        }

        // 3. HORIZONTAL DATE PICKER STRIP
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("calendar_date_strip")
            ) {
                items(daysInMonth) { dateStr ->
                    val isSelected = dateStr == selectedDateStr
                    val dateObj = try { LocalDate.parse(dateStr) } catch (_: Exception) { LocalDate.of(2026, 11, 3) }
                    val dayOfMonth = dateObj.dayOfMonth
                    val dayOfWeek = dateObj.dayOfWeek.name.take(3)

                    val dayTasks = allTasks.filter { it.dateStr == dateStr }
                    val isAllDone = dayTasks.isNotEmpty() && dayTasks.all { it.isCompleted }
                    val phaseColor = when {
                        isAllDone -> CyberEmerald
                        dayTasks.any { it.phase == SchedulePhase.SECOND_REVISION } -> HologramViolet
                        dayTasks.any { it.phase == SchedulePhase.FIRST_REVISION } -> SolarAmber
                        dayTasks.any { it.phase == SchedulePhase.PRACTICE_MOCK } -> CyberRose
                        else -> NeonCyan
                    }

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                        border = androidx.compose.foundation.BorderStroke(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) NeonCyan else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        ),
                        modifier = Modifier
                            .width(66.dp)
                            .clickable { viewModel.setSelectedDate(dateStr) }
                            .testTag("date_chip_$dateStr")
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(vertical = 12.dp)
                        ) {
                            Text(
                                text = dayOfWeek,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 11.sp,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "$dayOfMonth",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isSelected) NeonCyan else MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(phaseColor)
                            )
                        }
                    }
                }
            }
        }

        // 4. SELECTED DATE SUMMARY HEADER
        item {
            val dateObj = try { LocalDate.parse(selectedDateStr) } catch (_: Exception) { LocalDate.of(2026, 11, 3) }
            val formattedDate = dateObj.format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy"))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = formattedDate.uppercase(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Text(
                        text = "${selectedDateTasks.size} scheduled study blocks",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }

                if (filterPhase != null) {
                    TextButton(onClick = { filterPhase = null }) {
                        Text("Show All", color = NeonCyan)
                    }
                }
            }
        }

        // 5. SCHEDULED SESSIONS FOR THIS DATE
        if (selectedDateTasks.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = "Empty",
                            tint = NeonCyan,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "No study sessions on this date",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedButton(
                            onClick = { showAddTaskDialog = true },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Add Study Block Here")
                        }
                    }
                }
            }
        } else {
            items(selectedDateTasks, key = { it.id }) { task ->
                ScheduleTaskItem(
                    task = task,
                    onToggleComplete = { viewModel.toggleTask(task) },
                    onEdit = { editingTask = task },
                    onDelete = { viewModel.deleteTask(task) }
                )
            }
        }
    }

    if (editingTask != null) {
        val task = editingTask!!
        EditTaskDialog(
            task = task,
            onDismiss = { editingTask = null },
            onSave = { updatedDate, updatedTime, updatedTitle ->
                viewModel.rescheduleTask(task.id, updatedDate, updatedTime)
                editingTask = null
            }
        )
    }

    if (showAddTaskDialog) {
        AddTaskDialog(
            initialDate = selectedDateStr,
            onDismiss = { showAddTaskDialog = false },
            onAdd = { dateStr, timeSlot, subjectId, subjectName, taskTitle, phase ->
                viewModel.addCustomTask(dateStr, timeSlot, subjectId, subjectName, taskTitle, phase)
                showAddTaskDialog = false
            }
        )
    }
}

@Composable
fun ScheduleTaskItem(
    task: ScheduleTaskEntity,
    onToggleComplete: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (task.isCompleted) {
                MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (task.isCompleted) CyberEmerald.copy(alpha = 0.6f)
            else MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
        ),
        modifier = modifier
            .fillMaxWidth()
            .testTag("schedule_item_${task.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onToggleComplete() },
                colors = CheckboxDefaults.colors(
                    checkedColor = CyberEmerald,
                    checkmarkColor = Color.Black
                ),
                modifier = Modifier.testTag("schedule_checkbox_${task.id}")
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PhaseChip(phase = task.phase)
                    Text(
                        text = task.timeSlot,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = NeonCyan,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = task.taskTitle,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (task.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                        textDecoration = if (task.isCompleted) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                    )
                )
                Text(
                    text = task.subjectName,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }

            IconButton(onClick = onEdit) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Edit Schedule",
                    tint = NeonCyan,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun MonthTabButton(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) NeonCyan.copy(alpha = 0.15f) else Color.Transparent,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isSelected) NeonCyan else MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
        ),
        modifier = modifier.clickable { onClick() }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) NeonCyan else MaterialTheme.colorScheme.onSurface
                )
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

@Composable
fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}

@Composable
fun EditTaskDialog(
    task: ScheduleTaskEntity,
    onDismiss: () -> Unit,
    onSave: (dateStr: String, timeSlot: String, title: String) -> Unit
) {
    var dateStr by remember { mutableStateOf(task.dateStr) }
    var timeSlot by remember { mutableStateOf(task.timeSlot) }
    var title by remember { mutableStateOf(task.taskTitle) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Scheduled Session") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Session Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = dateStr,
                    onValueChange = { dateStr = it },
                    label = { Text("Date (YYYY-MM-DD)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = timeSlot,
                    onValueChange = { timeSlot = it },
                    label = { Text("Time Slot (e.g. 4:00 PM – 6:00 PM)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(dateStr, timeSlot, title) },
                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
            ) {
                Text("Save Changes", color = DarkOnPrimary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun AddTaskDialog(
    initialDate: String,
    onDismiss: () -> Unit,
    onAdd: (dateStr: String, timeSlot: String, subjectId: String, subjectName: String, taskTitle: String, phase: SchedulePhase) -> Unit
) {
    var dateStr by remember { mutableStateOf(initialDate) }
    var timeSlot by remember { mutableStateOf("4:00 PM – 6:00 PM") }
    var subjectName by remember { mutableStateOf("Financial Accounting") }
    var taskTitle by remember { mutableStateOf("") }
    var selectedPhase by remember { mutableStateOf(SchedulePhase.SYLLABUS_COMPLETION) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Schedule Study Slot") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = taskTitle,
                    onValueChange = { taskTitle = it },
                    label = { Text("Task / Unit Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = subjectName,
                    onValueChange = { subjectName = it },
                    label = { Text("Subject Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = dateStr,
                    onValueChange = { dateStr = it },
                    label = { Text("Date (YYYY-MM-DD)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = timeSlot,
                    onValueChange = { timeSlot = it },
                    label = { Text("Time Slot") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (taskTitle.isNotBlank()) {
                        onAdd(dateStr, timeSlot, "CUSTOM", subjectName, taskTitle, selectedPhase)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
            ) {
                Text("Schedule", color = DarkOnPrimary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
