package com.ferret

/**
 * Internal repository — holds all platform-specific resources for the Ferret library.
 *
 * Never exposed to consumers. Created once inside [FerretSdk.initialize] and passed
 * to [bootPlatform] for each feature to pull what it needs.
 *
 * commonMain holds only the type. Each platform's `actual` adds platform-specific
 * members (e.g. Android Context) that are only ever accessed from that platform's source set.
 */
internal expect class FerretRepository
