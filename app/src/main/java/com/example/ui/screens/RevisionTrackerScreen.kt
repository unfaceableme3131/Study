package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.ui.components.CircularProgressBadge
import com.example.ui.theme.*
import com.example.ui.viewmodel.StudyViewModel

enum class RevisionSection {
    SYLLABUS_COMPLETION,
    REVISION_1,
    REVISION_2
}

@Composable
fun RevisionTrackerScreen(
    viewModel: StudyViewModel,
    modifier: Modifier = Modifier
) {
    val subjects by viewModel.subjects.collectAsState()
    val allUnits by viewModel.units.collectAsState()
    val metrics by viewModel.dashboardMetrics.collectAsState()

    var activeSection by remember { mutableStateOf(RevisionSection.SYLLABUS_COMPLETION) }
    var filterWeakOnly by remember { mutableStateOf(false) }

    val totalUnits = allUnits.size.coerceAtLeast(1)
    val phaseInfo = when (activeSection) {
        RevisionSection.SYLLABUS_COMPLETION -> PhaseProgressInfo(
            completedCount = allUnits.count { it.isCompleted },
            percent = metrics.syllabusPercent,
            title = "Syllabus Completion (Phase 1)",
            color = NeonCyan
        )
        RevisionSection.REVISION_1 -> PhaseProgressInfo(
            completedCount = allUnits.count { it.revision1Completed },
            percent = metrics.revision1Percent,
            title = "First Revision (25–30 Nov)",
            color = SolarAmber
        )
        RevisionSection.REVISION_2 -> PhaseProgressInfo(
            completedCount = allUnits.count { it.revision2Completed },
            percent = metrics.revision2Percent,
            title = "Second Revision (1–5 Dec)",
            color = HologramViolet
        )
    }
    val completedForActiveSection = phaseInfo.completedCount
    val activePercent = phaseInfo.percent
    val sectionTitle = phaseInfo.title
    val sectionColor = phaseInfo.color

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("revision_tracker_screen")
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. HEADER
        item {
            Column {
                Text(
                    text = "REVISION & MASTERY TRACKER",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        letterSpacing = 1.sp
                    )
                )
                Text(
                    text = "Double revision guarantee: complete syllabus & revise twice",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }

        // 2. THREE CLEARLY SEPARATED SECTION BUTTONS
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        TabSegmentButton(
                            title = "Syllabus",
                            subtitle = "${metrics.syllabusPercent.toInt()}%",
                            isSelected = activeSection == RevisionSection.SYLLABUS_COMPLETION,
                            accentColor = ElectricCyan,
                            onClick = { activeSection = RevisionSection.SYLLABUS_COMPLETION },
                            modifier = Modifier.weight(1f)
                        )
                        TabSegmentButton(
                            title = "Rev 1",
                            subtitle = "${metrics.revision1Percent.toInt()}%",
                            isSelected = activeSection == RevisionSection.REVISION_1,
                            accentColor = AccentAmber,
                            onClick = { activeSection = RevisionSection.REVISION_1 },
                            modifier = Modifier.weight(1f)
                        )
                        TabSegmentButton(
                            title = "Rev 2",
                            subtitle = "${metrics.revision2Percent.toInt()}%",
                            isSelected = activeSection == RevisionSection.REVISION_2,
                            accentColor = AccentPurple,
                            onClick = { activeSection = RevisionSection.REVISION_2 },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Filter Weak Areas:",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                        FilterChip(
                            selected = filterWeakOnly,
                            onClick = { filterWeakOnly = !filterWeakOnly },
                            label = { Text("Starred Weak Topics Only") },
                            leadingIcon = {
                                Icon(
                                    imageVector = if (filterWeakOnly) Icons.Default.Star else Icons.Default.StarBorder,
                                    contentDescription = null,
                                    tint = if (filterWeakOnly) Color(0xFFF59E0B) else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        )
                    }
                }
            }
        }

        // 3. PHASE SUMMARY HERO CARD
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, sectionColor.copy(alpha = 0.4f)),
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
                        Text(
                            text = sectionTitle.uppercase(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = sectionColor,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$completedForActiveSection / $totalUnits Units Completed",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = when (activeSection) {
                                RevisionSection.SYLLABUS_COMPLETION -> "Initial thorough study of all foundational concepts."
                                RevisionSection.REVISION_1 -> "Consolidating formulas, case law provisions & numericals."
                                RevisionSection.REVISION_2 -> "Rapid recall, weak topic elimination & exam simulation."
                            },
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }

                    CircularProgressBadge(
                        progressPercent = activePercent,
                        size = 72.dp,
                        strokeWidth = 6.dp,
                        primaryColor = sectionColor,
                        label = "Done"
                    )
                }
            }
        }

        // 4. SUBJECT-WISE BREAKDOWN CARDS
        items(subjects, key = { it.id }) { subject ->
            val subUnits = allUnits.filter { unit ->
                unit.subjectId == subject.id && (!filterWeakOnly || unit.isWeakArea)
            }

            if (subUnits.isNotEmpty() || !filterWeakOnly) {
                SubjectRevisionCard(
                    subject = subject,
                    units = subUnits,
                    activeSection = activeSection,
                    onToggleUnitStatus = { unit ->
                        when (activeSection) {
                            RevisionSection.SYLLABUS_COMPLETION -> viewModel.toggleUnit(unit)
                            RevisionSection.REVISION_1 -> viewModel.toggleRevision1(unit)
                            RevisionSection.REVISION_2 -> viewModel.toggleRevision2(unit)
                        }
                    },
                    onToggleWeak = { unit -> viewModel.toggleWeakArea(unit) }
                )
            }
        }
    }
}

@Composable
fun SubjectRevisionCard(
    subject: com.example.data.model.SubjectEntity,
    units: List<StudyUnitEntity>,
    activeSection: RevisionSection,
    onToggleUnitStatus: (StudyUnitEntity) -> Unit,
    onToggleWeak: (StudyUnitEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(true) }

    val doneCount = units.count { unit ->
        when (activeSection) {
            RevisionSection.SYLLABUS_COMPLETION -> unit.isCompleted
            RevisionSection.REVISION_1 -> unit.revision1Completed
            RevisionSection.REVISION_2 -> unit.revision2Completed
        }
    }
    val percent = if (units.isNotEmpty()) (doneCount * 100) / units.size else 0

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(subject.colorHex).copy(alpha = 0.2f),
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text(
                                text = subject.code,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color(subject.colorHex),
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        Text(
                            text = subject.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$doneCount / ${units.size} units ($percent%)",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }

                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = "Expand Units",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Subject Progress Bar
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { if (units.isNotEmpty()) doneCount.toFloat() / units.size else 0f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = Color(subject.colorHex),
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            // Units List
            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    units.forEach { unit ->
                        val isDone = when (activeSection) {
                            RevisionSection.SYLLABUS_COMPLETION -> unit.isCompleted
                            RevisionSection.REVISION_1 -> unit.revision1Completed
                            RevisionSection.REVISION_2 -> unit.revision2Completed
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isDone) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f) else Color.Transparent)
                                .padding(vertical = 4.dp, horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isDone,
                                onCheckedChange = { onToggleUnitStatus(unit) },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = AccentEmerald,
                                    checkmarkColor = Color.White
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Unit ${unit.unitNumber}: ${unit.title}",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (isDone) FontWeight.Normal else FontWeight.Medium,
                                    color = if (isDone) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                                    textDecoration = if (isDone) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                                ),
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = { onToggleWeak(unit) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = if (unit.isWeakArea) Icons.Default.Star else Icons.Default.StarBorder,
                                    contentDescription = "Star Weak Topic",
                                    tint = if (unit.isWeakArea) Color(0xFFF59E0B) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TabSegmentButton(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) accentColor.copy(alpha = 0.15f) else Color.Transparent,
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = if (isSelected) accentColor else MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
        ),
        modifier = modifier
            .clickable { onClick() }
            .testTag("tab_segment_$title")
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) accentColor else MaterialTheme.colorScheme.onSurface
                )
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = if (isSelected) accentColor else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                )
            )
        }
    }
}

private data class PhaseProgressInfo(
    val completedCount: Int,
    val percent: Float,
    val title: String,
    val color: Color
)
