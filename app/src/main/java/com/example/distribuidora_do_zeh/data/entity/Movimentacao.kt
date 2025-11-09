package com.example.distribuidora_do_zeh.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "movimentacoes",
    foreignKeys = [
        ForeignKey(
            entity = Bebida::class,
            parentColumns = ["id"],
            childColumns = ["bebidaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("bebidaId")]
)
data class Movimentacao(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val bebidaId: Int,
    val tipo: String, // "Entrada" ou "Saída"
    val quantidade: Int,
    val data: String,
    val observacao: String = ""
)
