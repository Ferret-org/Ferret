package com.ferret.app.components

import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.animateDecay
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

class SmoothFlingBehavior(
    private val decaySpec: DecayAnimationSpec<Float>
) : FlingBehavior {
    override suspend fun ScrollScope.performFling(initialVelocity: Float): Float {
        if (kotlin.math.abs(initialVelocity) <= 1f) return initialVelocity
        var velocityLeft = initialVelocity
        var lastValue = 0f
        AnimationState(
            initialValue = 0f,
            initialVelocity = initialVelocity
        ).animateDecay(decaySpec) {
            val delta = value - lastValue
            val consumed = scrollBy(delta)
            lastValue = value
            velocityLeft = this.velocity
            // Stop if scroll was blocked (hit edge)
            if (kotlin.math.abs(delta - consumed) > 0.5f) cancelAnimation()
        }
        return velocityLeft
    }
}

@Composable
fun rememberSmoothFlingBehavior(): FlingBehavior {
    // splineBasedDecay gives the natural "finger-flick" deceleration curve
    val density = androidx.compose.ui.platform.LocalDensity.current
    return remember(density) {
        SmoothFlingBehavior(
            decaySpec = splineBasedDecay(density)
        )
    }
}

private val topicPalette = listOf(
    Color(0xFF6C63FF), Color(0xFF00BFA5), Color(0xFFFF6B6B), Color(0xFFFFA000),
    Color(0xFF26A69A), Color(0xFFE91E63), Color(0xFF5C6BC0), Color(0xFF43A047),
    Color(0xFFFF7043), Color(0xFF8D6E63),
)

private val topicEmojis = mapOf(
    "Android" to "🤖", "Kotlin" to "💜", "Architecture" to "🏗️",
    "Testing" to "🧪", "Build" to "🛠️", "KMP" to "🌐"
)

fun emojiForTopic(topic: String): String = topicEmojis[topic] ?: "📄"

fun colorForTopic(topic: String): Color {
    val index = kotlin.math.abs(topic.hashCode()) % topicPalette.size
    return topicPalette[index]
}

fun timeAgo(createdAt: String, nowEpochMillis: Long): String {
    return try {
        val datePart = createdAt.substringBefore("T")
        val timePart = createdAt.substringAfter("T").removeSuffix("Z")
        val (year, month, day) = datePart.split("-").map { it.toInt() }
        val timeBits = timePart.split(":")
        val hour = timeBits[0].toInt()
        val minute = timeBits[1].toInt()
        val second = timeBits[2].substringBefore(".").toInt()

        fun daysFromCivil(y: Int, m: Int, d: Int): Long {
            val yy = if (m <= 2) y - 1 else y
            val era = (if (yy >= 0) yy else yy - 399) / 400
            val yoe = yy - era * 400
            val mp = (m + 9) % 12
            val doy = (153 * mp + 2) / 5 + d - 1
            val doe = yoe * 365 + yoe / 4 - yoe / 100 + doy
            return era * 146097L + doe - 719468L
        }

        val epochSeconds = daysFromCivil(year, month, day) * 86400L + hour * 3600L + minute * 60L + second
        val diffSeconds = (nowEpochMillis / 1000L) - epochSeconds

        when {
            diffSeconds < 60 -> "just now"
            diffSeconds < 3600 -> "${diffSeconds / 60}m ago"
            diffSeconds < 86400 -> "${diffSeconds / 3600}h ago"
            else -> "${diffSeconds / 86400}d ago"
        }
    } catch (e: Exception) {
        ""
    }
}