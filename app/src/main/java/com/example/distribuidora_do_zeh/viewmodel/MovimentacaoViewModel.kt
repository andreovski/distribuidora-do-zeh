package com.example.distribuidora_do_zeh.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.distribuidora_do_zeh.data.database.AppDatabase
import com.example.distribuidora_do_zeh.data.entity.Movimentacao
import com.example.distribuidora_do_zeh.data.repository.DistribuidoraRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MovimentacaoViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DistribuidoraRepository
    
    val allMovimentacoes: StateFlow<List<Movimentacao>>
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = DistribuidoraRepository(
            database.categoriaDao(),
            database.bebidaDao(),
            database.movimentacaoDao()
        )
        
        allMovimentacoes = repository.allMovimentacoes
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }

    fun registrarEntrada(bebidaId: Int, quantidade: Int, observacao: String = "") = viewModelScope.launch {
        try {
            repository.registrarEntrada(bebidaId, quantidade, observacao)
            _successMessage.value = "Entrada registrada com sucesso!"
            _errorMessage.value = null
        } catch (e: Exception) {
            _errorMessage.value = "Erro ao registrar entrada: ${e.message}"
        }
    }

    fun registrarSaida(bebidaId: Int, quantidade: Int, observacao: String = "") = viewModelScope.launch {
        try {
            repository.registrarSaida(bebidaId, quantidade, observacao)
            _successMessage.value = "Saída registrada com sucesso!"
            _errorMessage.value = null
        } catch (e: Exception) {
            _errorMessage.value = e.message ?: "Erro ao registrar saída"
        }
    }

    fun getMovimentacoesByBebida(bebidaId: Int): Flow<List<Movimentacao>> {
        return repository.getMovimentacoesByBebida(bebidaId)
    }

    fun clearMessages() {
        _errorMessage.value = null
        _successMessage.value = null
    }
}
