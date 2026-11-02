package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CircularProgressBadge
import com.example.ui.components.MetricStatCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.StudyViewModel

@Composable
fun AnalyticsScreen(
    viewModel: StudyViewModel,
    modifier: Modifier = Modifier
) {
    val metrics by viewModel.dashboardMetrics.collectAsState()
    val allTasks by viewModel.tasks.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val logs by viewModel.studyLogs.collectAsState()

    val completedTasksCount = allTasks.count { it.isCompleted }
    val pendingTasksCount = (allTasks.size - completedTasksCount).coerceAtLeast(0)
    val missedTasksCount = allTasks.count { it.isMissed }

    // Calculate subject-wise completed study hours
    val subjectHours = remember(logs, subjects) {
        subjects.associate { sub ->
            val hours = logs.filter { it.subjectName.contains(sub.name, ignoreCase = true) || it.subjectName.contains(sub.code, ignoreCase = true) }
                .sumOf { it.durationMinutes } / 60f
            sub to hours
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("analytics_screen")
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. HEADER
        item {
            Column {
                Text(
                    text = "PRODUCTIVITY ANALYTICS",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        letterSpacing = 1.sp
                    )
                )
                Text(
                    text = "Actual verified hours logged upon completion",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = ElectricCyan
                    )
                )
            }
        }

        // 2. OVERALL PREPARATION KPI CARD
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "TOTAL PREPARATION INDEX",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = ElectricCyan,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "${metrics.overallScorePercent.toInt()}% Ready",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Composite score based on Syllabus (50%), Rev 1 (30%), and Rev 2 (20%).",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }

                    CircularProgressBadge(
                        progressPercent = metrics.overallScorePercent,
                        size = 80.dp,
                        strokeWidth = 7.dp,
                        primaryColor = ElectricCyan,
                        label = "Overall"
                    )
                }
            }
        }

        // 3. ACTUAL STUDY HOURS STATS
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricStatCard(
                    title = "Completed Hours",
                    value = String.format("%.1f hrs", metrics.completedHours),
                    subtitle = "${logs.size} sessions finished",
                    icon = Icons.Default.Timer,
                    iconTint = ElectricCyan,
                    modifier = Modifier.weight(1f),
                    testTag = "stat_completed_hours"
                )
                MetricStatCard(
                    title = "Daily Streak",
                    value = "${metrics.streakDays} Days",
                    subtitle = "Consistency Target",
                    icon = Icons.Default.Whatshot,
                    iconTint = Color(0xFFF59E0B),
                    modifier = Modifier.weight(1f),
                    testTag = "stat_streak_days"
                )
            }
        }

        // 4. COMPLETED VS PENDING TASKS
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "TASK EXECUTION SUMMARY",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "$completedTasksCount Completed",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = AccentEmerald
                                )
                            )
                            Text(
                                text = "$pendingTasksCount Pending",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                            if (missedTasksCount > 0) {
                                Text(
                                    text = "$missedTasksCount Missed (in backlog)",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color(0xFFEF4444)
                                    )
                                )
                            }
                        }

                        val taskProgress = if (allTasks.isNotEmpty()) {
                            (completedTasksCount.toFloat() / allTasks.size) * 100f
                        } else 0f

                        CircularProgressBadge(
                            progressPercent = taskProgress,
                            size = 72.dp,
                            strokeWidth = 6.dp,
                            primaryColor = AccentEmerald,
                            label = "Tasks"
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    LinearProgressIndicator(
                        progress = { if (allTasks.isNotEmpty()) completedTasksCount.toFloat() / allTasks.size else 0f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = AccentEmerald,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }
        }

        // 5. SUBJECT-WISE STUDY TIME BREAKDOWN
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "SUBJECT-WISE STUDY HOURS",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    subjects.forEach { subject ->
                        val hours = subjectHours[subject] ?: 0f
                        val maxHours = 20f // Benchmark hours
                        val ratio = (hours / maxHours).coerceIn(0f, 1f)

                        Column(modifier = Modifier.padding(vertical = 6.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = subject.name,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                                Text(
                                    text = String.format("%.1f hrs", hours),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color(subject.colorHex),
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { ratio },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = Color(subject.colorHex),
                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // 6. DAILY CONSISTENCY & MILESTONES
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "CONSISTENCY & MOTIVATION",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF59E0B).copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.MilitaryTech,
                                contentDescription = "Badge",
                                tint = Color(0xFFF59E0B),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "B.Com Honors Excellence Track",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Text(
                                text = "Daily Routine: 4 AM Morning + 4 PM & 6:30 PM Evening slots.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
