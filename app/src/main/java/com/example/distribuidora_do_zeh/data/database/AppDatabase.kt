package com.example.distribuidora_do_zeh.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.distribuidora_do_zeh.data.dao.BebidaDao
import com.example.distribuidora_do_zeh.data.dao.CategoriaDao
import com.example.distribuidora_do_zeh.data.dao.MovimentacaoDao
import com.example.distribuidora_do_zeh.data.entity.Bebida
import com.example.distribuidora_do_zeh.data.entity.Categoria
import com.example.distribuidora_do_zeh.data.entity.Movimentacao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Categoria::class, Bebida::class, Movimentacao::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoriaDao(): CategoriaDao
    abstract fun bebidaDao(): BebidaDao
    abstract fun movimentacaoDao(): MovimentacaoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "distribuidora_database"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateDatabase(database)
                }
            }
        }

        suspend fun populateDatabase(database: AppDatabase) {
            val categoriaDao = database.categoriaDao()
            val bebidaDao = database.bebidaDao()

            // Categorias iniciais
            val categoriasCervejas = Categoria(nome = "Cerveja")
            val categoriasRefrigerantes = Categoria(nome = "Refrigerante")
            val categoriasSucos = Categoria(nome = "Suco")
            val categoriasAguas = Categoria(nome = "Água")
            val categoriasEnergeticos = Categoria(nome = "Energético")

            val idCerveja = categoriaDao.insertCategoria(categoriasCervejas).toInt()
            val idRefrigerante = categoriaDao.insertCategoria(categoriasRefrigerantes).toInt()
            val idSuco = categoriaDao.insertCategoria(categoriasSucos).toInt()
            val idAgua = categoriaDao.insertCategoria(categoriasAguas).toInt()
            val idEnergetico = categoriaDao.insertCategoria(categoriasEnergeticos).toInt()

            // Bebidas iniciais
            bebidaDao.insertBebida(
                Bebida(
                    categoriaId = idCerveja,
                    nome = "Skol Pilsen",
                    volume = "350ml",
                    quantidadeEstoque = 50,
                    precoCompra = 2.50,
                    precoVenda = 4.00
                )
            )

            bebidaDao.insertBebida(
                Bebida(
                    categoriaId = idCerveja,
                    nome = "Brahma Duplo Malte",
                    volume = "350ml",
                    quantidadeEstoque = 30,
                    precoCompra = 3.00,
                    precoVenda = 4.50
                )
            )

            bebidaDao.insertBebida(
                Bebida(
                    categoriaId = idRefrigerante,
                    nome = "Coca-Cola",
                    volume = "2L",
                    quantidadeEstoque = 40,
                    precoCompra = 5.00,
                    precoVenda = 8.00
                )
            )

            bebidaDao.insertBebida(
                Bebida(
                    categoriaId = idRefrigerante,
                    nome = "Guaraná Antarctica",
                    volume = "2L",
                    quantidadeEstoque = 35,
                    precoCompra = 4.50,
                    precoVenda = 7.50
                )
            )

            bebidaDao.insertBebida(
                Bebida(
                    categoriaId = idSuco,
                    nome = "Del Valle Laranja",
                    volume = "1L",
                    quantidadeEstoque = 25,
                    precoCompra = 4.00,
                    precoVenda = 6.50
                )
            )

            bebidaDao.insertBebida(
                Bebida(
                    categoriaId = idAgua,
                    nome = "Água Crystal",
                    volume = "500ml",
                    quantidadeEstoque = 100,
                    precoCompra = 1.00,
                    precoVenda = 2.00
                )
            )

            bebidaDao.insertBebida(
                Bebida(
                    categoriaId = idEnergetico,
                    nome = "Red Bull",
                    volume = "250ml",
                    quantidadeEstoque = 20,
                    precoCompra = 6.00,
                    precoVenda = 10.00
                )
            )
        }
    }
}
