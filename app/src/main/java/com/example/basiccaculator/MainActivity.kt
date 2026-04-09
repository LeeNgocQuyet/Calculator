package com.example.basiccaculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.basiccaculator.ui.theme.BasicCaculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BasicCaculatorTheme {
                basicCalculatorApp()
            }
        }
    }
}

@Composable
fun basicCalculatorApp() {
    var expression by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        fun handleClick(btn: String) {
            when (btn) {
                "C" -> {
                    expression = ""
                    result = ""
                }
                "=" -> {
                    result = calculate(expression)
                }
                else -> {
                    expression = expression.plus(btn)
                }
            }
        }

        Text(
            modifier = Modifier.padding(bottom = 16.dp),
            text = expression,
            fontSize = 32.sp
        )

        Text(
            modifier = Modifier.padding(bottom = 16.dp),

            text = result,
            fontSize = 24.sp,
        )

        Spacer(modifier = Modifier.height(16.dp))

        val buttons = listOf(
            listOf("7", "8", "9", "/"),
            listOf("4", "5", "6", "*"),
            listOf("1", "2", "3", "-"),
            listOf("0", "C", "=", "+")
        )

        buttons.forEach { row ->
            Row {
                row.forEach { btn ->
                    Button(
                        onClick = { handleClick(btn) },
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                    ) {
                        Text(btn)
                    }
                }
            }
        }
    }
}

private fun calculate(expression: String): String {
    val numbers = expression.split("+", "-", "*", "/").map { it.toDouble() }
    val operators = expression.filter { it in "+-*/" }

    if (numbers.isEmpty()) return ""

    val stack = mutableListOf<Double>()
    stack.add(numbers[0])

    var opIndex = 0
        for (i in 1 until numbers.size) {
            val nextNum = numbers[i]
            val op = if (opIndex < operators.length) operators[opIndex] else '+'
            opIndex++

            when (op) {
                '+' -> stack.add(nextNum)
                '-' -> stack.add(-nextNum)
                '*' -> {
                    val last = stack.removeAt(stack.size - 1)
                    stack.add(last * nextNum)
                }
                '/' -> {
                    val last = stack.removeAt(stack.size - 1)
                    if (nextNum == 0.0) return "Error"
                    stack.add(last / nextNum)
                }
            }
        }

        val finalResult = stack.sum()

    return finalResult.toString()

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BasicCaculatorTheme {
        basicCalculatorApp()
    }
}
