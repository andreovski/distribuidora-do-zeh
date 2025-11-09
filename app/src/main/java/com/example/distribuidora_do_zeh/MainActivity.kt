package com.example.distribuidora_do_zeh

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.distribuidora_do_zeh.navigation.AppNavigation
import com.example.distribuidora_do_zeh.ui.theme.DistribuidoradozehTheme
import com.example.distribuidora_do_zeh.viewmodel.BebidaViewModel
import com.example.distribuidora_do_zeh.viewmodel.MovimentacaoViewModel
import com.example.distribuidora_do_zeh.viewmodel.RelatorioViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DistribuidoradozehTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val bebidaViewModel: BebidaViewModel = viewModel()
                    val movimentacaoViewModel: MovimentacaoViewModel = viewModel()
                    val relatorioViewModel: RelatorioViewModel = viewModel()

                    AppNavigation(
                        navController = navController,
                        bebidaViewModel = bebidaViewModel,
                        movimentacaoViewModel = movimentacaoViewModel,
                        relatorioViewModel = relatorioViewModel
                    )
                }
            }
        }
    }
}