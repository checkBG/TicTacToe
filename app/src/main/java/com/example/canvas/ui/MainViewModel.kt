package com.example.canvas.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.canvas.data.Line
import com.example.canvas.data.Player
import com.example.canvas.data.TicTacToe
import com.example.canvas.utils.allEquals
import com.example.canvas.utils.subListSeparately
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel : ViewModel() {
    private val _ticTacToeField =
        MutableStateFlow(
            mutableStateListOf<TicTacToe>()
        )
    val ticTacToeField
        get() = _ticTacToeField
    var currentTurn by mutableStateOf(Player.Blue)
        private set
    var winner: Player? by mutableStateOf(null)
        private set
    var line: Line? by mutableStateOf(null)
        private set

    init {
        resetGame()
    }

    fun onClickOnField(index: Int) {
        _ticTacToeField.value[index] = TicTacToe(
            isChosen = true,
            wasDoneByPlayer = currentTurn
        )
        checkWins()
        if (winner == null) {
            currentTurn = if (currentTurn == Player.Blue) Player.Red else Player.Blue
        }
    }

    fun resetGame() {
        _ticTacToeField.value = mutableStateListOf()
        repeat(9) {
            _ticTacToeField.value.add(TicTacToe())
        }
        winner = null
        line = null
        currentTurn = Player.Blue
    }

    private fun checkWins() {
        val list: List<Int?> = ticTacToeField.value.map {
            if (it.isChosen) {
                if (it.wasDoneByPlayer == Player.Blue) 0 else 1
            } else {
                null
            }
        }
        println(list)
        val listOfLines = mapOf(
            list.subList(0, 3) to Line.FirstHorizontalLine,
            list.subList(3, 6) to Line.SecondHorizontalLine,
            list.subList(6, 9) to Line.ThirdHorizontalLine,
            list.subListSeparately(0, 3, 6) to Line.FirstVerticalLine,
            list.subListSeparately(1, 4, 7) to Line.SecondVerticalLine,
            list.subListSeparately(2, 5, 8) to Line.ThirdVerticalLine,
            list.subListSeparately(0, 4, 8) to Line.DiagonalZeroToEight,
            list.subListSeparately(2, 4, 6) to Line.DiagonalTwoToSix,
        )

        listOfLines.forEach { (key, value) ->
            if (key.allEquals()) {
                winner = currentTurn
                line = value
                return@forEach
            }
        }

        if (ticTacToeField.value.all { it.isChosen }) {
            winner = Player.Nobody
        }
    }
}