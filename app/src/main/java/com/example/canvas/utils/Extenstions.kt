package com.example.canvas.utils

fun List<Int?>.allEquals(): Boolean {
    return if (any { it == null }) {
        false
    } else {
        all { it == first() }
    }
}

fun List<Int?>.subListSeparately(vararg index: Int): List<Int?> {
    return index.map { this[it] }
}