/*
 * Author: Don Freddy <freddytamwo@gmail.com>
 * Github: https://github.com/donfreddy
 * Website: https://donfreddy.com
 *
 * Application: Troona
 * Homepage: https://github.com/donfreddy/troona
 * License: https://github.com/donfreddy/troona/blob/main/LICENSE
 * Copyright: © 2023, Don Freddy. All rights reserved.
 */

package com.donfreddy.troona

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.donfreddy.troona.ui.theme.TroonaTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      TroonaTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
          Greeting(
            name = "Android",
            modifier = Modifier.padding(innerPadding)
          )
        }
      }
    }
  }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(
    text = "Hello $name!",
    modifier = modifier
  )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  TroonaTheme {
    Greeting("Android")
  }
}