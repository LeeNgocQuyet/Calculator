package com.example.basiccaculator

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.basiccaculator.model.CalculatorUiState
import com.example.basiccaculator.ui.CalculatorViewModel
import com.example.basiccaculator.ui.theme.BasicCaculatorTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BasicCaculatorTheme {
                BasicCalculatorApp()
            }
        }
    }
}

@Composable
fun BasicCalculatorApp(
    viewmodel: CalculatorViewModel = viewModel()
) {
    val uiState by viewmodel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(bottom = 16.dp),
            text = uiState.expression,
            fontSize = 32.sp
        )

        Text(
            modifier = Modifier.padding(bottom = 16.dp),
            text = uiState.result,
            fontSize = 24.sp,
            color = Color.Gray
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
                        onClick = { 
                            viewmodel.onButtonClick(btn)
                            Log.d("Calculator", "Clicked: $btn -> New State: ${uiState.expression}")
                        },
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


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BasicCaculatorTheme {
        BasicCalculatorApp()
    }
}
