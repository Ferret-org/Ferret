package com.ferret.app.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

// ── Data ─────────────────────────────────────────────────────────────────────

enum class LevelState { COMPLETED, CURRENT, LOCKED }

data class Level(
    val number: Int,
    val title: String,
    val subtitle: String,
    val state: LevelState,
    val xp: Int,
)

private val sampleLevels = listOf(
    Level(1, "Getting Started",   "Intro to the basics",        LevelState.COMPLETED, 100),
    Level(2, "Core Concepts",     "Dive into fundamentals",     LevelState.COMPLETED, 200),
    Level(3, "Hands-On Practice", "Apply what you've learned",  LevelState.CURRENT,   300),
    Level(4, "Advanced Topics",   "Level up your skills",       LevelState.LOCKED,    400),
    Level(5, "Real-World Build",  "Build a complete project",   LevelState.LOCKED,    500),
    Level(6, "Mastery",           "You're an expert now",       LevelState.LOCKED,    600),
)

// ── Screen ────────────────────────────────────────────────────────────────────

@Composable
fun LevelSelectionScreen(
    levels: List<Level> = sampleLevels,
    onLevelClick: (Level) -> Unit = {},
) {
    val completedCount = levels.count { it.state == LevelState.COMPLETED }
    val totalXp = levels.filter { it.state == LevelState.COMPLETED }.sumOf { it.xp }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF1A1A2E), Color(0xFF16213E))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(Modifier.height(20.dp))

            // Header
            Text(
                text = "Your Journey",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    fontSize = 30.sp,
                )
            )
            Text(
                text = "Keep going — you're doing great!",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White.copy(alpha = 0.55f)
                )
            )

            Spacer(Modifier.height(20.dp))

            // XP summary card
            XpSummaryCard(completedCount = completedCount, total = levels.size, totalXp = totalXp)

            Spacer(Modifier.height(32.dp))

            // Level list
            levels.forEachIndexed { index, level ->
                LevelRow(
                    level = level,
                    isLast = index == levels.lastIndex,
                    animationDelay = index * 80L,
                    onClick = { if (level.state != LevelState.LOCKED) onLevelClick(level) }
                )
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

// ── XP summary ────────────────────────────────────────────────────────────────

@Composable
private fun XpSummaryCard(completedCount: Int, total: Int, totalXp: Int) {
    val progress = if (total == 0) 0f else completedCount / total.toFloat()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(listOf(Color(0xFF6C63FF), Color(0xFF9C8FFF)))
            )
            .padding(20.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(
                        text = "$totalXp XP",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                        )
                    )
                    Text(
                        text = "Total earned",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White.copy(alpha = 0.75f)
                        )
                    )
                }
                Text(
                    text = "$completedCount / $total",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                )
            }

            Spacer(Modifier.height(14.dp))

            // Progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color.White.copy(alpha = 0.25f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .height(8.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color.White)
                )
            }
        }
    }
}

// ── Level row ─────────────────────────────────────────────────────────────────

@Composable
private fun LevelRow(
    level: Level,
    isLast: Boolean,
    animationDelay: Long,
    onClick: () -> Unit,
) {
    val scale = remember { Animatable(0.6f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(level.number) {
        delay(animationDelay)
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium,
            )
        )
    }
    LaunchedEffect(level.number) {
        delay(animationDelay)
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
        )
    }

    Row(
        modifier = Modifier
            .scale(scale.value)
            .alpha(alpha.value)
    ) {
        // Left column: connector line + circle
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            LevelCircle(level = level, onClick = onClick)
            if (!isLast) {
                ConnectorLine(fromState = level.state)
            }
        }

        Spacer(Modifier.width(16.dp))

        // Right: card
        LevelCard(level = level, onClick = onClick)
    }
}

// ── Level circle ──────────────────────────────────────────────────────────────

@Composable
private fun LevelCircle(level: Level, onClick: () -> Unit) {
    val (bg, iconTint) = when (level.state) {
        LevelState.COMPLETED -> Brush.linearGradient(
            listOf(Color(0xFF6C63FF), Color(0xFF9C8FFF))
        ) to Color.White
        LevelState.CURRENT   -> Brush.linearGradient(
            listOf(Color(0xFF00BFA5), Color(0xFF1DE9B6))
        ) to Color.White
        LevelState.LOCKED    -> Brush.linearGradient(
            listOf(Color(0xFF2A2A3E), Color(0xFF2A2A3E))
        ) to Color(0xFF555577)
    }

    Box(
        modifier = Modifier
            .size(52.dp)
            .shadow(if (level.state != LevelState.LOCKED) 10.dp else 0.dp, CircleShape)
            .clip(CircleShape)
            .background(bg)
            .then(
                if (level.state == LevelState.CURRENT)
                    Modifier.border(2.dp, Color(0xFF1DE9B6), CircleShape)
                else Modifier
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center,
    ) {
        when (level.state) {
            LevelState.COMPLETED -> Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Completed",
                tint = iconTint,
                modifier = Modifier.size(24.dp)
            )
            LevelState.CURRENT   -> Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Current",
                tint = iconTint,
                modifier = Modifier.size(24.dp)
            )
            LevelState.LOCKED    -> Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Locked",
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// ── Connector line ────────────────────────────────────────────────────────────

@Composable
private fun ConnectorLine(fromState: LevelState) {
    val color = when (fromState) {
        LevelState.COMPLETED -> Color(0xFF6C63FF).copy(alpha = 0.6f)
        LevelState.CURRENT   -> Color(0xFF00BFA5).copy(alpha = 0.4f)
        LevelState.LOCKED    -> Color(0xFF2A2A3E)
    }
    Box(
        modifier = Modifier
            .width(3.dp)
            .height(72.dp)
            .clip(RoundedCornerShape(50))
            .background(color)
    )
}

// ── Level card ────────────────────────────────────────────────────────────────

@Composable
private fun LevelCard(level: Level, onClick: () -> Unit) {
    val cardBg = when (level.state) {
        LevelState.COMPLETED -> Color(0xFF1E1E35)
        LevelState.CURRENT   -> Color(0xFF1A2E2A)
        LevelState.LOCKED    -> Color(0xFF16162A)
    }
    val borderColor = when (level.state) {
        LevelState.COMPLETED -> Color(0xFF6C63FF).copy(alpha = 0.4f)
        LevelState.CURRENT   -> Color(0xFF00BFA5).copy(alpha = 0.6f)
        LevelState.LOCKED    -> Color(0xFF2A2A3E)
    }
    val badgeText = when (level.state) {
        LevelState.COMPLETED -> "✓ Done"
        LevelState.CURRENT   -> "▶ Start"
        LevelState.LOCKED    -> "🔒 Locked"
    }
    val badgeColor = when (level.state) {
        LevelState.COMPLETED -> Color(0xFF6C63FF)
        LevelState.CURRENT   -> Color(0xFF00BFA5)
        LevelState.LOCKED    -> Color(0xFF333355)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
            .shadow(if (level.state == LevelState.CURRENT) 12.dp else 4.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(cardBg)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                enabled = level.state != LevelState.LOCKED,
                onClick = onClick,
            )
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Level ${level.number}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (level.state == LevelState.LOCKED)
                                Color(0xFF555577) else Color(0xFF9C8FFF),
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.sp,
                        )
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = level.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (level.state == LevelState.LOCKED)
                                Color.White.copy(alpha = 0.35f) else Color.White,
                        )
                    )
                    Text(
                        text = level.subtitle,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = if (level.state == LevelState.LOCKED)
                                Color.White.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.55f),
                        )
                    )
                }

                Spacer(Modifier.width(8.dp))

                // XP badge
                if (level.state != LevelState.LOCKED) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF6C63FF).copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "+${level.xp} XP",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFF9C8FFF),
                                fontWeight = FontWeight.Bold,
                            )
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Action badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(badgeColor.copy(alpha = 0.15f))
                    .padding(horizontal = 12.dp, vertical = 5.dp)
            ) {
                Text(
                    text = badgeText,
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = badgeColor,
                        fontWeight = FontWeight.SemiBold,
                    )
                )
            }
        }
    }
}
