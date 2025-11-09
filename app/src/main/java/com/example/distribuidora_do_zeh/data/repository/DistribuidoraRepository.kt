package com.example.distribuidora_do_zeh.data.repository

import com.example.distribuidora_do_zeh.data.dao.BebidaDao
import com.example.distribuidora_do_zeh.data.dao.CategoriaDao
import com.example.distribuidora_do_zeh.data.dao.MovimentacaoDao
import com.example.distribuidora_do_zeh.data.entity.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*

class DistribuidoraRepository(
    private val categoriaDao: CategoriaDao,
    private val bebidaDao: BebidaDao,
    private val movimentacaoDao: MovimentacaoDao
) {
    // Categoria
    val allCategorias: Flow<List<Categoria>> = categoriaDao.getAllCategorias()
    
    suspend fun getCategoriaById(id: Int) = categoriaDao.getCategoriaById(id)
    suspend fun getCategoriaByNome(nome: String) = categoriaDao.getCategoriaByNome(nome)
    suspend fun insertCategoria(categoria: Categoria) = categoriaDao.insertCategoria(categoria)
    suspend fun updateCategoria(categoria: Categoria) = categoriaDao.updateCategoria(categoria)
    suspend fun deleteCategoria(categoria: Categoria) = categoriaDao.deleteCategoria(categoria)

    // Bebida
    val allBebidas: Flow<List<Bebida>> = bebidaDao.getAllBebidas()
    val allBebidasComCategoria: Flow<List<BebidaComCategoria>> = bebidaDao.getAllBebidasComCategoria()
    
    suspend fun getBebidaById(id: Int) = bebidaDao.getBebidaById(id)
    suspend fun getBebidaComCategoriaById(id: Int) = bebidaDao.getBebidaComCategoriaById(id)
    suspend fun getBebidaComMovimentacoes(id: Int) = bebidaDao.getBebidaComMovimentacoes(id)
    suspend fun searchBebidaByNome(nome: String) = bebidaDao.searchBebidaByNome(nome)
    fun getBebidasByCategoria(categoriaId: Int) = bebidaDao.getBebidasByCategoria(categoriaId)
    fun getBebidasComEstoqueBaixo(quantidade: Int = 10) = bebidaDao.getBebidasComEstoqueBaixo(quantidade)
    
    suspend fun insertBebida(bebida: Bebida) = bebidaDao.insertBebida(bebida)
    suspend fun updateBebida(bebida: Bebida) = bebidaDao.updateBebida(bebida)
    suspend fun deleteBebida(bebida: Bebida) = bebidaDao.deleteBebida(bebida)

    // Movimentação
    val allMovimentacoes: Flow<List<Movimentacao>> = movimentacaoDao.getAllMovimentacoes()
    
    suspend fun getMovimentacaoById(id: Int) = movimentacaoDao.getMovimentacaoById(id)
    fun getMovimentacoesByBebida(bebidaId: Int) = movimentacaoDao.getMovimentacoesByBebida(bebidaId)
    fun getMovimentacoesByTipo(tipo: String) = movimentacaoDao.getMovimentacoesByTipo(tipo)
    fun getMovimentacoesByPeriodo(dataInicio: String, dataFim: String) = 
        movimentacaoDao.getMovimentacoesByPeriodo(dataInicio, dataFim)
    
    suspend fun insertMovimentacao(movimentacao: Movimentacao) = movimentacaoDao.insertMovimentacao(movimentacao)
    suspend fun updateMovimentacao(movimentacao: Movimentacao) = movimentacaoDao.updateMovimentacao(movimentacao)
    suspend fun deleteMovimentacao(movimentacao: Movimentacao) = movimentacaoDao.deleteMovimentacao(movimentacao)

    // Operações especiais
    suspend fun registrarEntrada(bebidaId: Int, quantidade: Int, observacao: String = "") {
        bebidaDao.adicionarEstoque(bebidaId, quantidade)
        val dataAtual = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        movimentacaoDao.insertMovimentacao(
            Movimentacao(
                bebidaId = bebidaId,
                tipo = "Entrada",
                quantidade = quantidade,
                data = dataAtual,
                observacao = observacao
            )
        )
    }

    suspend fun registrarSaida(bebidaId: Int, quantidade: Int, observacao: String = "") {
        val bebida = bebidaDao.getBebidaById(bebidaId)
        if (bebida != null && bebida.quantidadeEstoque >= quantidade) {
            bebidaDao.removerEstoque(bebidaId, quantidade)
            val dataAtual = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            movimentacaoDao.insertMovimentacao(
                Movimentacao(
                    bebidaId = bebidaId,
                    tipo = "Saída",
                    quantidade = quantidade,
                    data = dataAtual,
                    observacao = observacao
                )
            )
        } else {
            throw IllegalArgumentException("Estoque insuficiente")
        }
    }

    // Relatórios
    data class RelatorioEstoque(
        val valorTotalCompra: Double,
        val valorTotalVenda: Double,
        val lucroPotencial: Double,
        val totalItens: Int,
        val totalUnidades: Int
    )

    fun calcularRelatorioEstoque(): Flow<RelatorioEstoque> = bebidaDao.getAllBebidas().map { bebidas ->
        var valorTotalCompra = 0.0
        var valorTotalVenda = 0.0
        var totalItens = 0
        var totalUnidades = 0

        bebidas.forEach { bebida ->
            valorTotalCompra += bebida.precoCompra * bebida.quantidadeEstoque
            valorTotalVenda += bebida.precoVenda * bebida.quantidadeEstoque
            totalItens++
            totalUnidades += bebida.quantidadeEstoque
        }

        RelatorioEstoque(
            valorTotalCompra = valorTotalCompra,
            valorTotalVenda = valorTotalVenda,
            lucroPotencial = valorTotalVenda - valorTotalCompra,
            totalItens = totalItens,
            totalUnidades = totalUnidades
        )
    }
}
