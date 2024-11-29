package com.example.canvas.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
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
        val listOfLines = listOf(
            list.subList(0, 3),
            list.subList(3, 6),
            list.subList(6, 9),
            list.subListSeparately(0, 3, 6),
            list.subListSeparately(1, 4, 7),
            list.subListSeparately(2, 5, 8),
            list.subListSeparately(0, 4, 8),
            list.subListSeparately(2, 4, 6),
        )
        if (
            listOfLines.any { it.allEquals() }
        ) {
            winner = currentTurn
            return
        }

        if (ticTacToeField.value.all { it.isChosen }) {
            winner = Player.Nobody
        }
    }
}