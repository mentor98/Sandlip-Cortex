package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.data.local.CortexDatabase
import com.example.data.repository.CortexRepository
import com.example.ui.navigation.CortexNavHost
import com.example.ui.navigation.Screen
import com.example.ui.theme.CortexDeepVoid
import com.example.ui.theme.SandlipCortexTheme

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    val database = CortexDatabase.getInstance(applicationContext)
    val repository = CortexRepository(database.cortexDao())

    setContent {
      SandlipCortexTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = CortexDeepVoid
        ) {
          CortexNavHost(
            repository = repository,
            startDestination = Screen.Home.route
          )
        }
      }
    }
  }
}
