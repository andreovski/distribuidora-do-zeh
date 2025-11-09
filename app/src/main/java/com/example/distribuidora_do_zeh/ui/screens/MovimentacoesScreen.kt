package com.example.distribuidora_do_zeh.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.distribuidora_do_zeh.viewmodel.BebidaViewModel
import com.example.distribuidora_do_zeh.viewmodel.MovimentacaoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovimentacoesScreen(
    bebidaViewModel: BebidaViewModel,
    movimentacaoViewModel: MovimentacaoViewModel,
    onNavigateBack: () -> Unit
) {
    var bebidaId by remember { mutableStateOf(0) }
    var quantidade by remember { mutableStateOf("") }
    var observacao by remember { mutableStateOf("") }
    var tipoMovimentacao by remember { mutableStateOf("Entrada") }
    var expandedBebida by remember { mutableStateOf(false) }
    var expandedTipo by remember { mutableStateOf(false) }

    val bebidas by bebidaViewModel.allBebidasComCategoria.collectAsState()
    val errorMessage by movimentacaoViewModel.errorMessage.collectAsState()
    val successMessage by movimentacaoViewModel.successMessage.collectAsState()

    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }

    LaunchedEffect(successMessage) {
        if (successMessage != null) {
            showSuccessDialog = true
        }
    }

    LaunchedEffect(errorMessage) {
        if (errorMessage != null) {
            showErrorDialog = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Movimentações de Estoque") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Registrar Movimentação",
                style = MaterialTheme.typography.titleLarge
            )

            ExposedDropdownMenuBox(
                expanded = expandedBebida,
                onExpandedChange = { expandedBebida = it }
            ) {
                OutlinedTextField(
                    value = bebidas.find { it.bebida.id == bebidaId }?.let { 
                        "${it.bebida.nome} - ${it.categoria.nome}"
                    } ?: "Selecione uma bebida",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Bebida") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedBebida) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedBebida,
                    onDismissRequest = { expandedBebida = false }
                ) {
                    bebidas.forEach { bebidaComCategoria ->
                        DropdownMenuItem(
                            text = { 
                                Column {
                                    Text(bebidaComCategoria.bebida.nome)
                                    Text(
                                        text = "${bebidaComCategoria.categoria.nome} - Estoque: ${bebidaComCategoria.bebida.quantidadeEstoque}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            },
                            onClick = {
                                bebidaId = bebidaComCategoria.bebida.id
                                expandedBebida = false
                            }
                        )
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = expandedTipo,
                onExpandedChange = { expandedTipo = it }
            ) {
                OutlinedTextField(
                    value = tipoMovimentacao,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo de Movimentação") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTipo) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedTipo,
                    onDismissRequest = { expandedTipo = false }
                ) {
                    listOf("Entrada", "Saída").forEach { tipo ->
                        DropdownMenuItem(
                            text = { Text(tipo) },
                            onClick = {
                                tipoMovimentacao = tipo
                                expandedTipo = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = quantidade,
                onValueChange = { quantidade = it.filter { char -> char.isDigit() } },
                label = { Text("Quantidade") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            OutlinedTextField(
                value = observacao,
                onValueChange = { observacao = it },
                label = { Text("Observação (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )

            val bebidaSelecionada = bebidas.find { it.bebida.id == bebidaId }
            if (bebidaSelecionada != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Estoque Atual: ${bebidaSelecionada.bebida.quantidadeEstoque}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        val qtd = quantidade.toIntOrNull() ?: 0
                        val novoEstoque = if (tipoMovimentacao == "Entrada") {
                            bebidaSelecionada.bebida.quantidadeEstoque + qtd
                        } else {
                            bebidaSelecionada.bebida.quantidadeEstoque - qtd
                        }
                        Text(
                            text = "Novo Estoque: $novoEstoque",
                            style = MaterialTheme.typography.titleMedium,
                            color = if (novoEstoque < 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val qtd = quantidade.toIntOrNull() ?: 0
                    if (tipoMovimentacao == "Entrada") {
                        movimentacaoViewModel.registrarEntrada(bebidaId, qtd, observacao)
                    } else {
                        movimentacaoViewModel.registrarSaida(bebidaId, qtd, observacao)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = bebidaId > 0 && quantidade.isNotBlank() && (quantidade.toIntOrNull() ?: 0) > 0
            ) {
                Text("Registrar Movimentação")
            }
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                movimentacaoViewModel.clearMessages()
                quantidade = ""
                observacao = ""
            },
            title = { Text("Sucesso") },
            text = { Text(successMessage ?: "") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSuccessDialog = false
                        movimentacaoViewModel.clearMessages()
                        quantidade = ""
                        observacao = ""
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = {
                showErrorDialog = false
                movimentacaoViewModel.clearMessages()
            },
            title = { Text("Erro") },
            text = { Text(errorMessage ?: "") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showErrorDialog = false
                        movimentacaoViewModel.clearMessages()
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
}
