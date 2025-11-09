package com.example.distribuidora_do_zeh.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.distribuidora_do_zeh.ui.screens.*
import com.example.distribuidora_do_zeh.viewmodel.BebidaViewModel
import com.example.distribuidora_do_zeh.viewmodel.MovimentacaoViewModel
import com.example.distribuidora_do_zeh.viewmodel.RelatorioViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    bebidaViewModel: BebidaViewModel = viewModel(),
    movimentacaoViewModel: MovimentacaoViewModel = viewModel(),
    relatorioViewModel: RelatorioViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(Screen.Main.route) {
            MainScreen(
                onNavigateToListaBebidas = { navController.navigate(Screen.ListaBebidas.route) },
                onNavigateToCadastro = { navController.navigate(Screen.CadastroEdicaoBebida.createRoute()) },
                onNavigateToMovimentacoes = { navController.navigate(Screen.Movimentacoes.route) },
                onNavigateToRelatorios = { navController.navigate(Screen.Relatorios.route) }
            )
        }

        composable(Screen.ListaBebidas.route) {
            ListaBebidasScreen(
                viewModel = bebidaViewModel,
                onNavigateBack = { navController.popBackStack() },
                onBebidaClick = { bebidaId ->
                    navController.navigate(Screen.DetalhesBebida.createRoute(bebidaId))
                },
                onAddBebida = { navController.navigate(Screen.CadastroEdicaoBebida.createRoute()) }
            )
        }

        composable(
            route = Screen.DetalhesBebida.route,
            arguments = listOf(navArgument("bebidaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val bebidaId = backStackEntry.arguments?.getInt("bebidaId") ?: 0
            DetalhesBebidaScreen(
                bebidaId = bebidaId,
                viewModel = bebidaViewModel,
                movimentacaoViewModel = movimentacaoViewModel,
                onNavigateBack = { navController.popBackStack() },
                onEditBebida = { id ->
                    navController.navigate(Screen.CadastroEdicaoBebida.createRoute(id))
                }
            )
        }

        composable(
            route = Screen.CadastroEdicaoBebida.route,
            arguments = listOf(navArgument("bebidaId") {
                type = NavType.IntType
                defaultValue = -1
            })
        ) { backStackEntry ->
            val bebidaId = backStackEntry.arguments?.getInt("bebidaId")?.takeIf { it != -1 }
            CadastroEdicaoBebidaScreen(
                bebidaId = bebidaId,
                viewModel = bebidaViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Movimentacoes.route) {
            MovimentacoesScreen(
                bebidaViewModel = bebidaViewModel,
                movimentacaoViewModel = movimentacaoViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Relatorios.route) {
            RelatoriosScreen(
                viewModel = relatorioViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
