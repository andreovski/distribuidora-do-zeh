package com.example.distribuidora_do_zeh.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class BebidaComCategoria(
    @Embedded val bebida: Bebida,
    @Relation(
        parentColumn = "categoriaId",
        entityColumn = "id"
    )
    val categoria: Categoria
)
