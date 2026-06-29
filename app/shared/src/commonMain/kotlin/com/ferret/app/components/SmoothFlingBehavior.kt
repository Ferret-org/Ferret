package com.ferret.app.components

import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.animateDecay
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

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