package com.example.distribuidora_do_zeh.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class BebidaComMovimentacoes(
    @Embedded val bebida: Bebida,
    @Relation(
        parentColumn = "id",
        entityColumn = "bebidaId"
    )
    val movimentacoes: List<Movimentacao>
)
