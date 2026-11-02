package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SchedulePhase
import com.example.ui.theme.*

@Composable
fun CyberGlassCard(
    modifier: Modifier = Modifier,
    borderColor: Color = NeonCyan.copy(alpha = 0.35f),
    cornerSize: Dp = 20.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .border(
                1.dp,
                Brush.linearGradient(
                    listOf(
                        borderColor,
                        DarkOutline,
                        borderColor.copy(alpha = 0.1f)
                    )
                ),
                RoundedCornerShape(cornerSize)
            ),
        shape = RoundedCornerShape(cornerSize),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

@Composable
fun MultiRingProgressDisplay(
    syllabusPercent: Float,
    rev1Percent: Float,
    rev2Percent: Float,
    overallPercent: Float,
    size: Dp = 110.dp,
    modifier: Modifier = Modifier
) {
    val animatedSyl by animateFloatAsState(
        targetValue = (syllabusPercent / 100f).coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "syl_anim"
    )
    val animatedR1 by animateFloatAsState(
        targetValue = (rev1Percent / 100f).coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
        label = "r1_anim"
    )
    val animatedR2 by animateFloatAsState(
        targetValue = (rev2Percent / 100f).coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 1400, easing = FastOutSlowInEasing),
        label = "r2_anim"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(size)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeW = 5.dp.toPx()

            // Outer Ring: Syllabus Completion (Cyan)
            val rOuter = size.toPx() / 2 - strokeW / 2
            drawCircle(
                color = DarkSurfaceVariant.copy(alpha = 0.6f),
                radius = rOuter,
                style = Stroke(width = strokeW)
            )
            drawArc(
                brush = Brush.sweepGradient(listOf(NeonCyan, ElectricBlue, NeonCyan)),
                startAngle = -90f,
                sweepAngle = 360f * animatedSyl,
                useCenter = false,
                style = Stroke(width = strokeW, cap = StrokeCap.Round),
                topLeft = Offset(strokeW / 2, strokeW / 2),
                size = androidx.compose.ui.geometry.Size(rOuter * 2, rOuter * 2)
            )

            // Middle Ring: Rev 1 (Amber)
            val rMid = rOuter - strokeW - 4.dp.toPx()
            val offsetMid = size.toPx() / 2 - rMid
            drawCircle(
                color = DarkSurfaceVariant.copy(alpha = 0.5f),
                radius = rMid,
                style = Stroke(width = strokeW)
            )
            drawArc(
                brush = Brush.sweepGradient(listOf(SolarAmber, Color(0xFFF97316), SolarAmber)),
                startAngle = -90f,
                sweepAngle = 360f * animatedR1,
                useCenter = false,
                style = Stroke(width = strokeW, cap = StrokeCap.Round),
                topLeft = Offset(offsetMid, offsetMid),
                size = androidx.compose.ui.geometry.Size(rMid * 2, rMid * 2)
            )

            // Inner Ring: Rev 2 (Purple)
            val rInner = rMid - strokeW - 4.dp.toPx()
            val offsetInner = size.toPx() / 2 - rInner
            drawCircle(
                color = DarkSurfaceVariant.copy(alpha = 0.4f),
                radius = rInner,
                style = Stroke(width = strokeW)
            )
            drawArc(
                brush = Brush.sweepGradient(listOf(HologramViolet, Color(0xFFC084FC), HologramViolet)),
                startAngle = -90f,
                sweepAngle = 360f * animatedR2,
                useCenter = false,
                style = Stroke(width = strokeW, cap = StrokeCap.Round),
                topLeft = Offset(offsetInner, offsetInner),
                size = androidx.compose.ui.geometry.Size(rInner * 2, rInner * 2)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${overallPercent.toInt()}%",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Text(
                text = "READY",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 8.sp,
                    color = NeonCyan,
                    letterSpacing = 1.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
fun TelemetryBadge(
    label: String,
    statusText: String,
    accentColor: Color = NeonCyan,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alphaAnim by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Surface(
        shape = RoundedCornerShape(10.dp),
        color = accentColor.copy(alpha = 0.12f),
        border = androidx.compose.foundation.BorderStroke(1.dp, accentColor.copy(alpha = 0.35f)),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = alphaAnim))
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "$label: ",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            Text(
                text = statusText,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = accentColor
                )
            )
        }
    }
}

@Composable
fun HexMetricCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color = NeonCyan,
    modifier: Modifier = Modifier,
    testTag: String = "hex_metric_card"
) {
    Card(
        modifier = modifier
            .testTag(testTag)
            .border(
                1.dp,
                Brush.verticalGradient(
                    listOf(accentColor.copy(alpha = 0.45f), MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                ),
                RoundedCornerShape(18.dp)
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Subtle glowing accent notch on top-left
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .height(3.dp)
                    .background(
                        Brush.horizontalGradient(listOf(accentColor, Color.Transparent))
                    )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            letterSpacing = 1.2.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = value,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = accentColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(accentColor.copy(alpha = 0.15f))
                        .border(1.dp, accentColor.copy(alpha = 0.35f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = accentColor,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CircularProgressBadge(
    progressPercent: Float,
    size: Dp = 80.dp,
    strokeWidth: Dp = 7.dp,
    primaryColor: Color = NeonCyan,
    trackColor: Color = DarkSurfaceVariant,
    label: String = "Progress",
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = (progressPercent / 100f).coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 800),
        label = "progress_anim"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(size)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = trackColor.copy(alpha = 0.5f),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                brush = Brush.sweepGradient(
                    listOf(primaryColor, primaryColor.copy(alpha = 0.6f), primaryColor)
                ),
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${progressPercent.toInt()}%",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            if (label.isNotEmpty()) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 9.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    }
}

@Composable
fun MetricStatCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color = NeonCyan,
    modifier: Modifier = Modifier,
    testTag: String = "metric_stat_card"
) {
    HexMetricCard(
        title = title,
        value = value,
        subtitle = subtitle,
        icon = icon,
        accentColor = iconTint,
        modifier = modifier,
        testTag = testTag
    )
}

@Composable
fun PhaseChip(phase: SchedulePhase, modifier: Modifier = Modifier) {
    val (label, bgColor, textColor) = when (phase) {
        SchedulePhase.SYLLABUS_COMPLETION -> Triple("Syllabus", NeonCyan.copy(alpha = 0.15f), NeonCyan)
        SchedulePhase.FIRST_REVISION -> Triple("Rev 1", SolarAmber.copy(alpha = 0.15f), SolarAmber)
        SchedulePhase.SECOND_REVISION -> Triple("Rev 2", HologramViolet.copy(alpha = 0.15f), HologramViolet)
        SchedulePhase.PRACTICE_MOCK -> Triple("Practice", CyberRose.copy(alpha = 0.15f), CyberRose)
        SchedulePhase.FINAL_PREPARATION -> Triple("Final Prep", CyberEmerald.copy(alpha = 0.15f), CyberEmerald)
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = bgColor,
        border = androidx.compose.foundation.BorderStroke(1.dp, textColor.copy(alpha = 0.45f))
    ) {
        Text(
            text = label.uppercase(),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.ExtraBold,
                color = textColor,
                fontSize = 9.sp,
                letterSpacing = 0.5.sp
            )
        )
    }
}
