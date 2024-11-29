package com.example.canvas

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Field(
            fields = fields,
            onClick = { index -> mainViewModel.onClickOnField(index = index) },
            isGameOver = mainViewModel.winner == null,
        )
        AnimatedVisibility(visible = mainViewModel.winner != null) {
            Winner(winner = mainViewModel.winner as Player)
        }
    }
}

@Composable
fun Winner(modifier: Modifier = Modifier, winner: Player) {
    val textMeasurer = rememberTextMeasurer()
    val annotatedString = if (winner != Player.Nobody) {
        buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF0000FF),
                            Color(0xFF0000CD),
                            Color(0xFF191970)
                        )
                    ),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    shadow = Shadow(color = Color(0x440000FF), blurRadius = 10f)
                )
            ) {
                append(winner.playerName)
            }
            withStyle(
                style = SpanStyle(
                    fontSize = 30.sp,
                )
            ) {
                append(stringResource(id = ""))
            }
        }
    } else {
        buildAnnotatedString {

        }
    }
    Canvas(modifier = modifier.size(height = 100.dp, width = 800.dp)) {
        drawText(
            textMeasurer = textMeasurer,
            text = annotatedString
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Field(
    modifier: Modifier = Modifier,
    fields: SnapshotStateList<TicTacToe>,
    onClick: (Int) -> Unit,
    isGameOver: Boolean,
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp - 30.dp

    Box(
        modifier = modifier
            .padding(15.dp),
    ) {
        DrawField(screenWidth = screenWidth)

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













