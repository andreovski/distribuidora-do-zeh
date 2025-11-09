package com.example.distribuidora_do_zeh.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.distribuidora_do_zeh.data.entity.BebidaComCategoria
import com.example.distribuidora_do_zeh.viewmodel.BebidaViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaBebidasScreen(
    viewModel: BebidaViewModel,
    onNavigateBack: () -> Unit,
    onBebidaClick: (Int) -> Unit,
    onAddBebida: () -> Unit
) {
    val bebidas by viewModel.allBebidasComCategoria.collectAsState()
    var searchText by remember { mutableStateOf("") }
    var showSearch by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    if (showSearch) {
                        TextField(
                            value = searchText,
                            onValueChange = { searchText = it },
                            placeholder = { Text("Buscar bebida...") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    } else {
                        Text("Lista de Bebidas") 
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    IconButton(onClick = { 
                        showSearch = !showSearch
                        if (!showSearch) searchText = ""
                    }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddBebida) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Bebida")
            }
        }
    ) { paddingValues ->
        val bebidasFiltradas = if (searchText.isBlank()) {
            bebidas
        } else {
            bebidas.filter { 
                it.bebida.nome.contains(searchText, ignoreCase = true) ||
                it.categoria.nome.contains(searchText, ignoreCase = true)
            }
        }

        if (bebidasFiltradas.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (searchText.isBlank()) "Nenhuma bebida cadastrada" else "Nenhuma bebida encontrada",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(bebidasFiltradas) { bebidaComCategoria ->
                    BebidaCard(
                        bebidaComCategoria = bebidaComCategoria,
                        onClick = { onBebidaClick(bebidaComCategoria.bebida.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun BebidaCard(
    bebidaComCategoria: BebidaComCategoria,
    onClick: () -> Unit
) {
    val bebida = bebidaComCategoria.bebida
    val categoria = bebidaComCategoria.categoria
    val numberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = bebida.nome,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = categoria.nome,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = bebida.volume,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = numberFormat.format(bebida.precoVenda),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Estoque: ${bebida.quantidadeEstoque}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (bebida.quantidadeEstoque < 10) 
                            MaterialTheme.colorScheme.error 
                        else 
                            MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
