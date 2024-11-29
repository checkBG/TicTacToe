package com.example.canvas

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.canvas.data.Line
import com.example.canvas.data.Player
import com.example.canvas.data.TicTacToe


@Composable
fun DrawField(
    modifier: Modifier = Modifier,
    screenWidth: Dp,
    line: Line?,
) {
    var startAnimation by remember { mutableStateOf(false) }
    val endOffsetAnimation by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = TweenSpec(
            durationMillis = 700,
            easing = FastOutLinearInEasing
        ),
        label = "DrawResult"
    )

    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .size(width = screenWidth, height = screenWidth)
        ) {
            drawLine(
                color = Color.Black,
                start = Offset(size.width / 3, 0f),
                end = Offset(size.width / 3, size.width),
                strokeWidth = 3.dp.toPx(),
            )
            drawLine(
                color = Color.Black,
                start = Offset(size.width / 3 * 2, 0f),
                end = Offset(size.width / 3 * 2, size.width),
                strokeWidth = 3.dp.toPx(),
            )

            drawLine(
                color = Color.Black,
                start = Offset(0f, size.width / 3),
                end = Offset(size.width, size.width / 3),
                strokeWidth = 3.dp.toPx()
            )
            drawLine(
                color = Color.Black,
                start = Offset(0f, size.width / 3 * 2),
                end = Offset(size.width, size.width / 3 * 2),
                strokeWidth = 3.dp.toPx()
            )
            if (line != null) {
                drawLine(
                    color = Color.Black,
                    strokeWidth = 5.dp.toPx(),
                    start = when (line) {
                        Line.FirstHorizontalLine -> Offset(0f, size.width / 6)
                        Line.SecondHorizontalLine -> Offset(0f, size.width / 2)
                        Line.ThirdHorizontalLine ->
                            Offset(0f, size.width / 3 * 2 + size.width / 6)

                        Line.FirstVerticalLine -> Offset(size.width / 6, 0f)
                        Line.SecondVerticalLine -> Offset(size.width / 2, 0f)
                        Line.ThirdVerticalLine ->
                            Offset(size.width / 3 * 2 + size.width / 6, 0f)

                        Line.DiagonalZeroToEight -> Offset(0f, 0f)
                        Line.DiagonalTwoToSix -> Offset(0f, size.width)
                    },
                    end = when (line) {
                        Line.FirstHorizontalLine ->
                            Offset(size.width * endOffsetAnimation, size.width / 6)

                        Line.SecondHorizontalLine ->
                            Offset(size.width * endOffsetAnimation, size.width / 2)

                        Line.ThirdHorizontalLine ->
                            Offset(
                                size.width * endOffsetAnimation,
                                size.width / 3 * 2 + size.width / 6
                            )

                        Line.FirstVerticalLine -> Offset(
                            size.width / 6,
                            size.width * endOffsetAnimation
                        )

                        Line.SecondVerticalLine -> Offset(
                            size.width / 2,
                            size.width * endOffsetAnimation
                        )

                        Line.ThirdVerticalLine ->
                            Offset(
                                size.width / 3 * 2 + size.width / 6,
                                size.width * endOffsetAnimation
                            )

                        Line.DiagonalZeroToEight -> Offset(
                            size.width * endOffsetAnimation,
                            size.width * endOffsetAnimation
                        )

                        Line.DiagonalTwoToSix -> Offset(
                            size.width * endOffsetAnimation,
                            size.width * (1 - endOffsetAnimation)
                        )
                    }
                )
                startAnimation = true
            } else {
                startAnimation = false
            }
        }
    }
}

@Composable
fun DrawOrNotTurn(
    modifier: Modifier = Modifier,
    screenWidth: Dp,
    isGameOver: Boolean,
    field: TicTacToe,
    onClick: () -> Unit,
) {
    var startAnimation by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .size(width = screenWidth / 3, height = screenWidth / 3)
            .clickable(onClick = onClick, enabled = (!field.isChosen && isGameOver))
    ) {
        if (field.isChosen) {
            val endOffsetAnimation by animateFloatAsState(
                targetValue = if (startAnimation) 1f else 0f,
                animationSpec = TweenSpec(
                    durationMillis = 500,
                    easing = FastOutLinearInEasing
                ), label = "endOffsetAnimation"
            )

            Canvas(
                modifier = Modifier
                    .padding(20.dp)
                    .size(
                        width = screenWidth / 3,
                        height = screenWidth / 3
                    )
            ) {
                if (field.wasDoneByPlayer == Player.Blue) {

                    drawLine(
                        color = Player.Blue.color,
                        start = Offset.Zero,
                        end = Offset(
                            size.width * endOffsetAnimation,
                            size.height * endOffsetAnimation
                        ),
                        strokeWidth = 10f,
                        blendMode = BlendMode.Darken
                    )
                    drawLine(
                        color = Player.Blue.color,
                        start = Offset(0f, size.height),
                        end = Offset(
                            size.height * endOffsetAnimation,
                            size.height * (1 - endOffsetAnimation)
                        ),
                        strokeWidth = 10f,
                        blendMode = BlendMode.Darken
                    )
                } else if (field.wasDoneByPlayer == Player.Red) {
                    drawArc(
                        color = Player.Red.color,
                        startAngle = 0f,
                        sweepAngle = 360f * endOffsetAnimation,
                        style = Stroke(
                            width = 10f
                        ),
                        useCenter = false,
                        blendMode = BlendMode.Darken
                    )
                }
                startAnimation = true
            }
        } else {
            startAnimation = false
        }
    }
}