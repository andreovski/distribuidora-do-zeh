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
import com.example.distribuidora_do_zeh.data.entity.Bebida
import com.example.distribuidora_do_zeh.viewmodel.BebidaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CadastroEdicaoBebidaScreen(
    bebidaId: Int?,
    viewModel: BebidaViewModel,
    onNavigateBack: () -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var categoriaId by remember { mutableStateOf(1) }
    var volume by remember { mutableStateOf("") }
    var quantidadeEstoque by remember { mutableStateOf("") }
    var precoCompra by remember { mutableStateOf("") }
    var precoVenda by remember { mutableStateOf("") }
    var expandedCategoria by remember { mutableStateOf(false) }

    val categorias by viewModel.allCategorias.collectAsState()
    val isEdicao = bebidaId != null

    LaunchedEffect(bebidaId) {
        if (bebidaId != null) {
            viewModel.loadBebidaById(bebidaId)
        }
    }

    val selectedBebida by viewModel.selectedBebida.collectAsState()

    LaunchedEffect(selectedBebida) {
        selectedBebida?.let { bebidaComCategoria ->
            val bebida = bebidaComCategoria.bebida
            nome = bebida.nome
            categoriaId = bebida.categoriaId
            volume = bebida.volume
            quantidadeEstoque = bebida.quantidadeEstoque.toString()
            precoCompra = bebida.precoCompra.toString()
            precoVenda = bebida.precoVenda.toString()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEdicao) "Editar Bebida" else "Cadastrar Bebida") },
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
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome da Bebida") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            ExposedDropdownMenuBox(
                expanded = expandedCategoria,
                onExpandedChange = { expandedCategoria = it }
            ) {
                OutlinedTextField(
                    value = categorias.find { it.id == categoriaId }?.nome ?: "Selecione",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoria") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategoria) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedCategoria,
                    onDismissRequest = { expandedCategoria = false }
                ) {
                    categorias.forEach { categoria ->
                        DropdownMenuItem(
                            text = { Text(categoria.nome) },
                            onClick = {
                                categoriaId = categoria.id
                                expandedCategoria = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = volume,
                onValueChange = { volume = it },
                label = { Text("Volume (ex: 350ml, 2L)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = quantidadeEstoque,
                onValueChange = { quantidadeEstoque = it.filter { char -> char.isDigit() } },
                label = { Text("Quantidade em Estoque") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            OutlinedTextField(
                value = precoCompra,
                onValueChange = { precoCompra = it },
                label = { Text("Preço de Compra") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                prefix = { Text("R$ ") },
                singleLine = true
            )

            OutlinedTextField(
                value = precoVenda,
                onValueChange = { precoVenda = it },
                label = { Text("Preço de Venda") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                prefix = { Text("R$ ") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    try {
                        val bebida = Bebida(
                            id = bebidaId ?: 0,
                            categoriaId = categoriaId,
                            nome = nome,
                            volume = volume,
                            quantidadeEstoque = quantidadeEstoque.toIntOrNull() ?: 0,
                            precoCompra = precoCompra.toDoubleOrNull() ?: 0.0,
                            precoVenda = precoVenda.toDoubleOrNull() ?: 0.0
                        )

                        if (isEdicao) {
                            viewModel.updateBebida(bebida)
                        } else {
                            viewModel.insertBebida(bebida)
                        }
                        onNavigateBack()
                    } catch (e: Exception) {
                        // Handle error
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = nome.isNotBlank() && 
                         volume.isNotBlank() && 
                         quantidadeEstoque.isNotBlank() &&
                         precoCompra.isNotBlank() && 
                         precoVenda.isNotBlank()
            ) {
                Text(if (isEdicao) "Salvar Alterações" else "Cadastrar Bebida")
            }

            if (isEdicao) {
                OutlinedButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar")
                }
            }
        }
    }
}
