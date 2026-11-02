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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StudyUnitEntity
import com.example.data.model.SubjectEntity
import com.example.ui.components.CircularProgressBadge
import com.example.ui.theme.*
import com.example.ui.viewmodel.StudyViewModel

@Composable
fun SubjectsScreen(
    viewModel: StudyViewModel,
    modifier: Modifier = Modifier
) {
    val subjects by viewModel.subjects.collectAsState()
    val allUnits by viewModel.units.collectAsState()

    var selectedSubjectId by remember { mutableStateOf("FA") }
    var editingUnit by remember { mutableStateOf<StudyUnitEntity?>(null) }
    var addingUnitForSubjectId by remember { mutableStateOf<String?>(null) }

    val activeSubject = subjects.find { it.id == selectedSubjectId } ?: subjects.firstOrNull()
    val subjectUnits = remember(allUnits, selectedSubjectId) {
        allUnits.filter { it.subjectId == selectedSubjectId }
    }

    val completedCount = subjectUnits.count { it.isCompleted }
    val progressPercent = if (subjectUnits.isNotEmpty()) {
        (completedCount.toFloat() / subjectUnits.size) * 100f
    } else 0f

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("subjects_screen")
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
                        text = "SYLLABUS & SUBJECTS",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            letterSpacing = 1.sp
                        )
                    )
                    Text(
                        text = "Track units, revisions & personal notes",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }

                if (activeSubject?.isEditable == true) {
                    Button(
                        onClick = { addingUnitForSubjectId = activeSubject.id },
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("add_unit_button")
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add Unit",
                            tint = DarkOnPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Add Unit",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = DarkOnPrimary
                            )
                        )
                    }
                }
            }
        }

        // 2. SUBJECT TABS SELECTOR (7 Subjects)
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(subjects, key = { it.id }) { subject ->
                    val isSelected = subject.id == selectedSubjectId
                    val subUnits = allUnits.filter { it.subjectId == subject.id }
                    val done = subUnits.count { it.isCompleted }
                    val percent = if (subUnits.isNotEmpty()) (done * 100) / subUnits.size else 0

                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                        border = androidx.compose.foundation.BorderStroke(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) ElectricCyan else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        ),
                        modifier = Modifier
                            .clickable { selectedSubjectId = subject.id }
                            .testTag("subject_tab_${subject.id}")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(Color(subject.colorHex))
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = subject.code,
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) ElectricCyan else MaterialTheme.colorScheme.onSurface
                                    )
                                )
                                Text(
                                    text = "$percent%",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3. ACTIVE SUBJECT HERO CARD
        if (activeSubject != null) {
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        Color(activeSubject.colorHex).copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(activeSubject.colorHex).copy(alpha = 0.2f),
                                    modifier = Modifier.padding(end = 8.dp)
                                ) {
                                    Text(
                                        text = activeSubject.code,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color(activeSubject.colorHex),
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                                if (activeSubject.isEditable) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = AccentEmerald.copy(alpha = 0.15f)
                                    ) {
                                        Text(
                                            text = "Customizable",
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = AccentEmerald,
                                                fontSize = 10.sp
                                            )
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = activeSubject.name,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Text(
                                text = activeSubject.description,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Units: $completedCount of ${subjectUnits.size} completed",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = ElectricCyan,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }

                        CircularProgressBadge(
                            progressPercent = progressPercent,
                            size = 72.dp,
                            strokeWidth = 6.dp,
                            primaryColor = Color(activeSubject.colorHex),
                            label = "Progress"
                        )
                    }
                }
            }
        }

        // 4. UNITS LIST FOR ACTIVE SUBJECT
        items(subjectUnits, key = { it.id }) { unit ->
            UnitDetailCard(
                unit = unit,
                isEditableSubject = activeSubject?.isEditable == true,
                onToggleComplete = { viewModel.toggleUnit(unit) },
                onToggleRev1 = { viewModel.toggleRevision1(unit) },
                onToggleRev2 = { viewModel.toggleRevision2(unit) },
                onToggleWeak = { viewModel.toggleWeakArea(unit) },
                onEdit = { editingUnit = unit },
                onDelete = { viewModel.deleteUnit(unit) }
            )
        }
    }

    // Edit Unit Dialog
    if (editingUnit != null) {
        val unit = editingUnit!!
        EditUnitDialog(
            unit = unit,
            onDismiss = { editingUnit = null },
            onSave = { updatedTitle, updatedNotes, updatedTopics ->
                viewModel.updateUnitDetails(unit, updatedTitle, updatedNotes, updatedTopics)
                editingUnit = null
            }
        )
    }

    // Add Unit Dialog
    if (addingUnitForSubjectId != null) {
        val subjectId = addingUnitForSubjectId!!
        val nextUnitNum = subjectUnits.size + 1
        AddUnitDialog(
            unitNumber = nextUnitNum,
            onDismiss = { addingUnitForSubjectId = null },
            onAdd = { title, topics, notes ->
                viewModel.addCustomUnit(subjectId, nextUnitNum, title, topics, notes)
                addingUnitForSubjectId = null
            }
        )
    }
}

@Composable
fun UnitDetailCard(
    unit: StudyUnitEntity,
    isEditableSubject: Boolean,
    onToggleComplete: () -> Unit,
    onToggleRev1: () -> Unit,
    onToggleRev2: () -> Unit,
    onToggleWeak: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (unit.isCompleted) AccentEmerald.copy(alpha = 0.5f)
            else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        ),
        modifier = modifier
            .fillMaxWidth()
            .testTag("unit_card_${unit.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Main Top Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = unit.isCompleted,
                    onCheckedChange = { onToggleComplete() },
                    colors = CheckboxDefaults.colors(
                        checkedColor = AccentEmerald,
                        checkmarkColor = Color.White
                    ),
                    modifier = Modifier.testTag("unit_checkbox_${unit.id}")
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { expanded = !expanded }
                ) {
                    Text(
                        text = "Unit ${unit.unitNumber}: ${unit.title}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            textDecoration = if (unit.isCompleted) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                        )
                    )
                    Text(
                        text = "Target Deadline: ${unit.deadlineDate}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    )
                }

                // Weak Area Bookmark Star
                IconButton(onClick = onToggleWeak) {
                    Icon(
                        imageVector = if (unit.isWeakArea) Icons.Default.Star else Icons.Default.StarBorder,
                        contentDescription = "Weak Area",
                        tint = if (unit.isWeakArea) Color(0xFFF59E0B) else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = "Expand Unit Details",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Quick Revision Badges Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 48.dp, top = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Revision 1 Chip Button
                FilterChip(
                    selected = unit.revision1Completed,
                    onClick = onToggleRev1,
                    label = { Text("Rev 1") },
                    leadingIcon = {
                        if (unit.revision1Completed) {
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                        }
                    },
                    modifier = Modifier.testTag("rev1_chip_${unit.id}")
                )

                // Revision 2 Chip Button
                FilterChip(
                    selected = unit.revision2Completed,
                    onClick = onToggleRev2,
                    label = { Text("Rev 2") },
                    leadingIcon = {
                        if (unit.revision2Completed) {
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                        }
                    },
                    modifier = Modifier.testTag("rev2_chip_${unit.id}")
                )

                if (unit.isWeakArea) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFF59E0B).copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF59E0B).copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = "Weak Topic",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFFF59E0B),
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        )
                    }
                }
            }

            // Expandable Content: Important topics & Personal notes
            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, start = 12.dp, end = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                    // Important Topics
                    if (unit.importantTopics.isNotBlank()) {
                        Text(
                            text = "IMPORTANT TOPICS:",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = ElectricCyan,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )
                        Text(
                            text = unit.importantTopics,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }

                    // Personal Notes
                    if (unit.personalNotes.isNotBlank()) {
                        Text(
                            text = "PERSONAL REVISION NOTES:",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = AccentAmber,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )
                        Text(
                            text = unit.personalNotes,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }

                    // Edit & Delete row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = onEdit) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Edit Notes / Topics")
                        }

                        if (isEditableSubject) {
                            TextButton(
                                onClick = onDelete,
                                colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFEF4444))
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Delete")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EditUnitDialog(
    unit: StudyUnitEntity,
    onDismiss: () -> Unit,
    onSave: (title: String, notes: String, topics: String) -> Unit
) {
    var title by remember { mutableStateOf(unit.title) }
    var topics by remember { mutableStateOf(unit.importantTopics) }
    var notes by remember { mutableStateOf(unit.personalNotes) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Unit Details") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Unit Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = topics,
                    onValueChange = { topics = it },
                    label = { Text("Important Exam Topics") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Personal Revision Notes") },
                    maxLines = 4,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(title, notes, topics) },
                colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan)
            ) {
                Text("Save Notes", color = DarkOnPrimary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun AddUnitDialog(
    unitNumber: Int,
    onDismiss: () -> Unit,
    onAdd: (title: String, topics: String, notes: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var topics by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Unit $unitNumber") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Unit Title / Chapter Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = topics,
                    onValueChange = { topics = it },
                    label = { Text("Core Topics") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Study Notes") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) onAdd(title, topics, notes)
                },
                colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan)
            ) {
                Text("Add Unit", color = DarkOnPrimary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
