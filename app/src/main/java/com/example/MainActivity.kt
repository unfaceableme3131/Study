package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.ScreenTab
import com.example.ui.viewmodel.StudyViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configure Firebase App Check with launch intent before UI/ViewModels
        com.example.ai.configureAppCheck(this, intent)

        enableEdgeToEdge()
        setContent {
            val studyViewModel: StudyViewModel = viewModel()
            val profile by studyViewModel.userProfile.collectAsState()
            val isDark = profile?.isDarkMode ?: true

            StudyCommandTheme(darkTheme = isDark) {
                MainAppScaffold(viewModel = studyViewModel)
            }
        }
    }
}

data class BottomNavDestination(
    val tab: ScreenTab,
    val icon: ImageVector,
    val label: String,
    val testTag: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScaffold(viewModel: StudyViewModel) {
    val currentTab by viewModel.currentTab.collectAsState()

    // Handle back button press on sub-screens to return to Dashboard
    BackHandler(enabled = currentTab != ScreenTab.DASHBOARD) {
        viewModel.navigateTo(ScreenTab.DASHBOARD)
    }

    val destinations = remember {
        listOf(
            BottomNavDestination(ScreenTab.DASHBOARD, Icons.Default.Dashboard, "Home", "nav_dashboard"),
            BottomNavDestination(ScreenTab.CALENDAR, Icons.Default.CalendarMonth, "Schedule", "nav_calendar"),
            BottomNavDestination(ScreenTab.SUBJECTS, Icons.Default.MenuBook, "Syllabus", "nav_syllabus"),
            BottomNavDestination(ScreenTab.REVISION, Icons.Default.Loop, "Revision", "nav_revision"),
            BottomNavDestination(ScreenTab.ANALYTICS, Icons.Default.BarChart, "Analytics", "nav_analytics"),
            BottomNavDestination(ScreenTab.ALARMS, Icons.Default.Alarm, "Alarms", "nav_alarms")
        )
    }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse_header")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(NeonCyan.copy(alpha = 0.15f))
                                .border(1.dp, NeonCyan.copy(alpha = 0.45f), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = "Shield Emblem",
                                tint = NeonCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "STUDY COMMAND",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 1.2.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(CyberEmerald.copy(alpha = pulseAlpha))
                                )
                            }
                            Text(
                                text = "B.Com (Hons.) Sem 1 • Exam Deck",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = NeonCyan,
                                    fontSize = 10.sp,
                                    letterSpacing = 0.5.sp
                                )
                            )
                        }
                    }
                },
                actions = {
                    FilledTonalButton(
                        onClick = { viewModel.navigateTo(ScreenTab.AI_COPILOT) },
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = if (currentTab == ScreenTab.AI_COPILOT) NeonCyan else NeonCyan.copy(alpha = 0.15f),
                            contentColor = if (currentTab == ScreenTab.AI_COPILOT) DarkOnPrimary else NeonCyan
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        modifier = Modifier.testTag("top_bar_ai_tutor_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Gemini AI",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "AI Tutor",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    IconButton(
                        onClick = { viewModel.navigateTo(ScreenTab.SETTINGS) },
                        modifier = Modifier.testTag("top_bar_settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Settings",
                            tint = if (currentTab == ScreenTab.SETTINGS) NeonCyan else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
                ),
                modifier = Modifier.testTag("bottom_nav_bar")
            ) {
                NavigationBar(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ) {
                    destinations.forEach { dest ->
                        val isSelected = currentTab == dest.tab
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { viewModel.navigateTo(dest.tab) },
                            icon = {
                                Icon(
                                    imageVector = dest.icon,
                                    contentDescription = dest.label,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = dest.label,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = NeonCyan,
                                selectedTextColor = NeonCyan,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                indicatorColor = NeonCyan.copy(alpha = 0.15f)
                            ),
                            modifier = Modifier.testTag(dest.testTag)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Crossfade(
            targetState = currentTab,
            label = "tab_crossfade",
            modifier = Modifier.padding(innerPadding)
        ) { tab ->
            when (tab) {
                ScreenTab.DASHBOARD -> DashboardScreen(viewModel = viewModel)
                ScreenTab.CALENDAR -> ScheduleCalendarScreen(viewModel = viewModel)
                ScreenTab.SUBJECTS -> SubjectsScreen(viewModel = viewModel)
                ScreenTab.REVISION -> RevisionTrackerScreen(viewModel = viewModel)
                ScreenTab.ANALYTICS -> AnalyticsScreen(viewModel = viewModel)
                ScreenTab.ALARMS -> AlarmsScreen(viewModel = viewModel)
                ScreenTab.AI_COPILOT -> AiTutorScreen(viewModel = viewModel)
                ScreenTab.SETTINGS -> SettingsScreen(viewModel = viewModel)
            }
        }
    }
}
