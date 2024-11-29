package com.example.canvas

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.canvas.data.Line
import com.example.canvas.data.Player
import com.example.canvas.data.TicTacToe
import com.example.canvas.ui.MainViewModel
import com.example.canvas.ui.theme.CanvasTheme

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel
) {
    val fields by mainViewModel.ticTacToeField.collectAsState()
    Log.d("model", fields.toString())
    Log.d("winner", mainViewModel.winner.toString())
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp - 30.dp

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (mainViewModel.winner == null) {
            CurrentTurn(
                currentTurn = mainViewModel.currentTurn,
                screenWidth = screenWidth
            )
        } else {
            GameOver()
        }
        Field(
            fields = fields,
            onClick = { index -> mainViewModel.onClickOnField(index = index) },
            isGameOver = mainViewModel.winner == null,
            screenWidth = screenWidth,
            line = mainViewModel.line
        )
        AnimatedVisibility(
            visible = mainViewModel.winner != null
        ) {
            Column {
                Spacer(modifier = Modifier.height(50.dp))
                if (mainViewModel.winner != null) {
                    Winner(
                        winner = mainViewModel.winner as Player,
                        screenWidth = screenWidth,
                    )
                }
                ElevatedButton(
                    onClick = { mainViewModel.resetGame() },
                    elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 8.dp),
                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                ) {
                    Text(text = stringResource(R.string.play_again), fontSize = 30.sp)
                }
            }
        }
    }
}

@Composable
fun GameOver(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(id = R.string.game_over),
        fontSize = 40.sp,
        modifier = modifier
    )
}

@Composable
fun CurrentTurn(
    modifier: Modifier = Modifier,
    currentTurn: Player,
    screenWidth: Dp
) {
    val textMeasurer = rememberTextMeasurer()
    val annotatedString = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                brush = Brush.linearGradient(
                    colors = if (currentTurn == Player.Blue) {
                        listOf(
                            Color(0xFF0000FF),
                            Color(0xFF0000CD),
                            Color(0xFF191970)
                        )
                    } else {
                        listOf(
                            Color(0xFFFF0000),
                            Color(0xFFCD0000),
                            Color(0xFF970191),
                        )
                    }
                ),
                fontSize = 35.sp,
            )
        ) {
            append(stringResource(id = currentTurn.playerName))
        }
        withStyle(
            style = SpanStyle(
                fontSize = 35.sp
            )
        ) {
            append(" ${stringResource(R.string.is_moving)}")
        }
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize(align = Alignment.Center)
    ) {
        Canvas(
            modifier = Modifier
                .size(height = 100.dp, width = screenWidth)
        ) {
            drawText(
                textMeasurer = textMeasurer,
                text = annotatedString
            )
        }
    }
}

@Composable
fun Winner(
    modifier: Modifier = Modifier,
    winner: Player,
    screenWidth: Dp,
) {
    val textMeasurer = rememberTextMeasurer()
    val annotatedString = if (winner != Player.Nobody) {
        buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    brush = Brush.linearGradient(
                        colors = if (winner == Player.Blue) {
                            listOf(
                                Color(0xFF0000FF),
                                Color(0xFF0000CD),
                                Color(0xFF191970)
                            )
                        } else {
                            listOf(
                                Color(0xFFFF0000),
                                Color(0xFFCD0000),
                                Color(0xFF970191),
                            )
                        }
                    ),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    shadow = Shadow(
                        color = if (winner == Player.Blue) {
                            Color(0x440000FF)
                        } else {
                            Color(0x44FF0000)
                        }, blurRadius = 3f
                    )
                )
            ) {
                append(stringResource(id = winner.playerName))
            }
            withStyle(
                style = SpanStyle(
                    fontSize = 28.sp,
                )
            ) {
                append(" ${stringResource(id = R.string.player_won)}")
            }
        }
    } else {
        buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontSize = 26.sp,
                )
            ) {
                append(stringResource(id = R.string.draw))
            }
        }
    }
    Box(
        modifier = modifier
    ) {
        Canvas(
            modifier = Modifier
                .size(height = 57.dp, width = screenWidth)
        ) {
            drawText(
                textMeasurer = textMeasurer,
                text = annotatedString,
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Field(
    modifier: Modifier = Modifier,
    fields: SnapshotStateList<TicTacToe>,
    onClick: (Int) -> Unit,
    isGameOver: Boolean,
    screenWidth: Dp,
    line: Line?,
) {
    Box(
        modifier = modifier
            .padding(15.dp),
    ) {
        DrawField(
            screenWidth = screenWidth,
            line = line
        )

        FlowRow(
            maxItemsInEachRow = 3,
        ) {
            fields.forEachIndexed { index, currentField ->
                DrawOrNotTurn(
                    screenWidth = screenWidth,
                    field = currentField,
                    onClick = { onClick(index) },
                    isGameOver = isGameOver
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun FieldPreview() {
    CanvasTheme {
        MainScreen(mainViewModel = MainViewModel())
    }
}
