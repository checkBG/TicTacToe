package com.example.canvas.data

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.example.canvas.R

enum class Player(val color: Color, @StringRes val playerName: Int) {
    Red(color = Color.Red, playerName = R.string.red),
    Blue(color = Color.Blue, playerName = R.string.blue),
    Nobody(color = Color.Green, playerName = -1)
}