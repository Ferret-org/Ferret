package com.ferret.app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform