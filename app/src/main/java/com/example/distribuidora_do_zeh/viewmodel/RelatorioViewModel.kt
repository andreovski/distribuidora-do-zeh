package com.example.distribuidora_do_zeh.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.distribuidora_do_zeh.data.database.AppDatabase
import com.example.distribuidora_do_zeh.data.entity.Bebida
import com.example.distribuidora_do_zeh.data.repository.DistribuidoraRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RelatorioViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DistribuidoraRepository
    
    val relatorioEstoque: StateFlow<DistribuidoraRepository.RelatorioEstoque?>
    val bebidasEstoqueBaixo: StateFlow<List<Bebida>>

    init {
        val database = AppDatabase.getDatabase(application)
        repository = DistribuidoraRepository(
            database.categoriaDao(),
            database.bebidaDao(),
            database.movimentacaoDao()
        )
        
        relatorioEstoque = repository.calcularRelatorioEstoque()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )
        
        bebidasEstoqueBaixo = repository.getBebidasComEstoqueBaixo(10)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }

    fun carregarRelatorioEstoque() {
        // O Flow já atualiza automaticamente, mas esta função pode ser mantida
        // para compatibilidade ou para forçar recálculo se necessário no futuro
    }
}
