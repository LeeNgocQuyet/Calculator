package com.example.basiccaculator.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.room.util.copy
import com.example.basiccaculator.model.CalculatorUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CalculatorViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(CalculatorUiState("",""))
    val uiState: StateFlow<CalculatorUiState> =
        _uiState.asStateFlow()
    fun onButtonClick(btn: String) {
        when (btn) {
            "C" -> {
                _uiState.value = _uiState.value.copy(
                    expression = "",
                    result = ""
                )
            }


            "=" -> {
                val result = calculate(_uiState.value.expression)
                _uiState.value = _uiState.value.copy(result = result)
            }

            else -> {
                val expression = _uiState.value.expression
                val operators = "+-*/"

                val isCurrentOperator = btn in operators
                val isLastCharOperator =
                    expression.isNotEmpty() && expression.last() in operators

                val newExpression = when {
                    isCurrentOperator && expression.isEmpty() -> {
                        if (btn == "-") btn else expression
                    }

                    isCurrentOperator && isLastCharOperator -> {
                        expression.dropLast(1) + btn
                    }

                    else -> {
                        expression + btn
                    }
                }

                _uiState.value = _uiState.value.copy(
                    expression = newExpression
                )
            }

        }
        Log.d("ViewModel","Button clicked: $btn")
        Log.d("viewModel"," UIState: ${uiState.value.expression} ${uiState.value.result}")

    }
    private fun calculate(expression: String): String {
        if (expression.isEmpty()) return ""

        var cleanExpression = expression
        while (cleanExpression.isNotEmpty() && cleanExpression.last() in "+-*/") {
            cleanExpression = cleanExpression.dropLast(1)
        }

        if (cleanExpression.isEmpty()) return ""

        try {
            val numbers = cleanExpression.split("+", "-", "*", "/").map { it.toDouble() }
            val operators = cleanExpression.filter { it in "+-*/" }

            if (numbers.isEmpty()) return ""

            val stack = mutableListOf<Double>()
            stack.add(numbers[0])

            for (i in 0 until operators.length) {
                val op = operators[i]
                if (i + 1 >= numbers.size) break

                val nextNum = numbers[i + 1]

                when (op) {
                    '+' -> stack.add(nextNum)
                    '-' -> stack.add(-nextNum)
                    '*' -> {
                        val last = stack.removeAt(stack.size - 1)
                        stack.add(last * nextNum)
                    }
                    '/' -> {
                        val last = stack.removeAt(stack.size - 1)
                        if (nextNum == 0.0) return "Invalid"
                        stack.add(last / nextNum)
                    }
                }
            }

            val finalResult = stack.sum()

            return finalResult.toString()

        } catch (e: Exception) {
            return "Error"
        }
    }
}

