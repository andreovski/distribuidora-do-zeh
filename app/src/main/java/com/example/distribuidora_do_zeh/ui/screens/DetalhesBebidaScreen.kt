package com.example.distribuidora_do_zeh.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.distribuidora_do_zeh.data.entity.Movimentacao
import com.example.distribuidora_do_zeh.viewmodel.BebidaViewModel
import com.example.distribuidora_do_zeh.viewmodel.MovimentacaoViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalhesBebidaScreen(
    bebidaId: Int,
    viewModel: BebidaViewModel,
    movimentacaoViewModel: MovimentacaoViewModel,
    onNavigateBack: () -> Unit,
    onEditBebida: (Int) -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(bebidaId) {
        viewModel.loadBebidaComMovimentacoes(bebidaId)
    }

    val bebidaComMovimentacoes by viewModel.bebidaComMovimentacoes.collectAsState()
    val bebida = bebidaComMovimentacoes?.bebida

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes da Bebida") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    IconButton(onClick = { bebida?.let { onEditBebida(it.id) } }) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Excluir")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        if (bebida == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    BebidaInfoCard(bebida)
                }

                item {
                    Text(
                        text = "Histórico de Movimentações",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                val movimentacoes = bebidaComMovimentacoes?.movimentacoes ?: emptyList()
                if (movimentacoes.isEmpty()) {
                    item {
                        Text(
                            text = "Nenhuma movimentação registrada",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    items(movimentacoes) { movimentacao ->
                        MovimentacaoCard(movimentacao)
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirmar exclusão") },
            text = { Text("Deseja realmente excluir esta bebida? Esta ação não pode ser desfeita.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        bebida?.let { viewModel.deleteBebida(it) }
                        showDeleteDialog = false
                        onNavigateBack()
                    }
                ) {
                    Text("Excluir")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun BebidaInfoCard(bebida: com.example.distribuidora_do_zeh.data.entity.Bebida) {
    val numberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = bebida.nome,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Divider()

            InfoRow("Volume", bebida.volume)
            InfoRow("Quantidade em Estoque", bebida.quantidadeEstoque.toString())
            InfoRow("Preço de Compra", numberFormat.format(bebida.precoCompra))
            InfoRow("Preço de Venda", numberFormat.format(bebida.precoVenda))
            
            val lucro = bebida.precoVenda - bebida.precoCompra
            InfoRow(
                "Lucro por Unidade", 
                numberFormat.format(lucro),
                valueColor = if (lucro > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
            
            val valorEstoque = bebida.precoVenda * bebida.quantidadeEstoque
            InfoRow("Valor Total em Estoque", numberFormat.format(valorEstoque))
        }
    }
}

@Composable
fun InfoRow(
    label: String, 
    value: String,
    valueColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = valueColor
        )
    }
}

@Composable
fun MovimentacaoCard(movimentacao: Movimentacao) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (movimentacao.tipo == "Entrada")
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            else
                MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = movimentacao.tipo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (movimentacao.tipo == "Entrada")
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.error
                )
                Text(
                    text = "${if (movimentacao.tipo == "Entrada") "+" else "-"}${movimentacao.quantidade}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = movimentacao.data,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (movimentacao.observacao.isNotBlank()) {
                Text(
                    text = movimentacao.observacao,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
