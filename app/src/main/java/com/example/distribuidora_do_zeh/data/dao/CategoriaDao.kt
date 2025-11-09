package com.example.distribuidora_do_zeh.data.dao

import androidx.room.*
import com.example.distribuidora_do_zeh.data.entity.Categoria
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaDao {
    @Query("SELECT * FROM categorias ORDER BY nome ASC")
    fun getAllCategorias(): Flow<List<Categoria>>
    
    @Query("SELECT * FROM categorias WHERE id = :id")
    suspend fun getCategoriaById(id: Int): Categoria?
    
    @Query("SELECT * FROM categorias WHERE nome = :nome")
    suspend fun getCategoriaByNome(nome: String): Categoria?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategoria(categoria: Categoria): Long
    
    @Update
    suspend fun updateCategoria(categoria: Categoria)
    
    @Delete
    suspend fun deleteCategoria(categoria: Categoria)
    
    @Query("DELETE FROM categorias")
    suspend fun deleteAllCategorias()
}
