package com.example.distribuidora_do_zeh.data.dao

import androidx.room.*
import com.example.distribuidora_do_zeh.data.entity.Movimentacao
import kotlinx.coroutines.flow.Flow

@Dao
interface MovimentacaoDao {
    @Query("SELECT * FROM movimentacoes ORDER BY data DESC")
    fun getAllMovimentacoes(): Flow<List<Movimentacao>>
    
    @Query("SELECT * FROM movimentacoes WHERE id = :id")
    suspend fun getMovimentacaoById(id: Int): Movimentacao?
    
    @Query("SELECT * FROM movimentacoes WHERE bebidaId = :bebidaId ORDER BY data DESC")
    fun getMovimentacoesByBebida(bebidaId: Int): Flow<List<Movimentacao>>
    
    @Query("SELECT * FROM movimentacoes WHERE tipo = :tipo ORDER BY data DESC")
    fun getMovimentacoesByTipo(tipo: String): Flow<List<Movimentacao>>
    
    @Query("SELECT * FROM movimentacoes WHERE data BETWEEN :dataInicio AND :dataFim ORDER BY data DESC")
    fun getMovimentacoesByPeriodo(dataInicio: String, dataFim: String): Flow<List<Movimentacao>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovimentacao(movimentacao: Movimentacao): Long
    
    @Update
    suspend fun updateMovimentacao(movimentacao: Movimentacao)
    
    @Delete
    suspend fun deleteMovimentacao(movimentacao: Movimentacao)
    
    @Query("DELETE FROM movimentacoes")
    suspend fun deleteAllMovimentacoes()
}
