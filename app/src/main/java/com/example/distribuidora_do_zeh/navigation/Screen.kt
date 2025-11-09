package com.example.distribuidora_do_zeh.navigation

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object ListaBebidas : Screen("lista_bebidas")
    object DetalhesBebida : Screen("detalhes_bebida/{bebidaId}") {
        fun createRoute(bebidaId: Int) = "detalhes_bebida/$bebidaId"
    }
    object CadastroEdicaoBebida : Screen("cadastro_edicao_bebida?bebidaId={bebidaId}") {
        fun createRoute(bebidaId: Int? = null) = 
            if (bebidaId != null) "cadastro_edicao_bebida?bebidaId=$bebidaId"
            else "cadastro_edicao_bebida"
    }
    object Movimentacoes : Screen("movimentacoes")
    object Relatorios : Screen("relatorios")
}
