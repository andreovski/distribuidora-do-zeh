package com.example.distribuidora_do_zeh.data.dao

import androidx.room.*
import com.example.distribuidora_do_zeh.data.entity.Bebida
import com.example.distribuidora_do_zeh.data.entity.BebidaComCategoria
import com.example.distribuidora_do_zeh.data.entity.BebidaComMovimentacoes
import kotlinx.coroutines.flow.Flow

@Dao
interface BebidaDao {
    @Query("SELECT * FROM bebidas ORDER BY nome ASC")
    fun getAllBebidas(): Flow<List<Bebida>>
    
    @Transaction
    @Query("SELECT * FROM bebidas ORDER BY nome ASC")
    fun getAllBebidasComCategoria(): Flow<List<BebidaComCategoria>>
    
    @Query("SELECT * FROM bebidas WHERE id = :id")
    suspend fun getBebidaById(id: Int): Bebida?
    
    @Transaction
    @Query("SELECT * FROM bebidas WHERE id = :id")
    suspend fun getBebidaComCategoriaById(id: Int): BebidaComCategoria?
    
    @Transaction
    @Query("SELECT * FROM bebidas WHERE id = :id")
    suspend fun getBebidaComMovimentacoes(id: Int): BebidaComMovimentacoes?
    
    @Query("SELECT * FROM bebidas WHERE nome LIKE '%' || :nome || '%'")
    suspend fun searchBebidaByNome(nome: String): List<Bebida>
    
    @Query("SELECT * FROM bebidas WHERE categoriaId = :categoriaId")
    fun getBebidasByCategoria(categoriaId: Int): Flow<List<Bebida>>
    
    @Query("SELECT * FROM bebidas WHERE quantidadeEstoque < :quantidade")
    fun getBebidasComEstoqueBaixo(quantidade: Int = 10): Flow<List<Bebida>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBebida(bebida: Bebida): Long
    
    @Update
    suspend fun updateBebida(bebida: Bebida)
    
    @Delete
    suspend fun deleteBebida(bebida: Bebida)
    
    @Query("DELETE FROM bebidas")
    suspend fun deleteAllBebidas()
    
    @Query("UPDATE bebidas SET quantidadeEstoque = quantidadeEstoque + :quantidade WHERE id = :bebidaId")
    suspend fun adicionarEstoque(bebidaId: Int, quantidade: Int)
    
    @Query("UPDATE bebidas SET quantidadeEstoque = quantidadeEstoque - :quantidade WHERE id = :bebidaId")
    suspend fun removerEstoque(bebidaId: Int, quantidade: Int)
}
