package com.example.distribuidora_do_zeh.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.distribuidora_do_zeh.data.database.AppDatabase
import com.example.distribuidora_do_zeh.data.entity.Bebida
import com.example.distribuidora_do_zeh.data.entity.BebidaComCategoria
import com.example.distribuidora_do_zeh.data.entity.BebidaComMovimentacoes
import com.example.distribuidora_do_zeh.data.entity.Categoria
import com.example.distribuidora_do_zeh.data.repository.DistribuidoraRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BebidaViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DistribuidoraRepository
    
    val allBebidasComCategoria: StateFlow<List<BebidaComCategoria>>
    val allCategorias: StateFlow<List<Categoria>>
    
    private val _searchResults = MutableStateFlow<List<Bebida>>(emptyList())
    val searchResults: StateFlow<List<Bebida>> = _searchResults.asStateFlow()
    
    private val _selectedBebida = MutableStateFlow<BebidaComCategoria?>(null)
    val selectedBebida: StateFlow<BebidaComCategoria?> = _selectedBebida.asStateFlow()
    
    private val _bebidaComMovimentacoes = MutableStateFlow<BebidaComMovimentacoes?>(null)
    val bebidaComMovimentacoes: StateFlow<BebidaComMovimentacoes?> = _bebidaComMovimentacoes.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = DistribuidoraRepository(
            database.categoriaDao(),
            database.bebidaDao(),
            database.movimentacaoDao()
        )
        
        allBebidasComCategoria = repository.allBebidasComCategoria
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
        
        allCategorias = repository.allCategorias
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }

    fun insertBebida(bebida: Bebida) = viewModelScope.launch {
        try {
            repository.insertBebida(bebida)
            _errorMessage.value = null
        } catch (e: Exception) {
            _errorMessage.value = "Erro ao cadastrar bebida: ${e.message}"
        }
    }

    fun updateBebida(bebida: Bebida) = viewModelScope.launch {
        try {
            repository.updateBebida(bebida)
            _errorMessage.value = null
        } catch (e: Exception) {
            _errorMessage.value = "Erro ao atualizar bebida: ${e.message}"
        }
    }

    fun deleteBebida(bebida: Bebida) = viewModelScope.launch {
        try {
            repository.deleteBebida(bebida)
            _errorMessage.value = null
        } catch (e: Exception) {
            _errorMessage.value = "Erro ao excluir bebida: ${e.message}"
        }
    }

    fun searchBebidaByNome(nome: String) = viewModelScope.launch {
        try {
            val results = repository.searchBebidaByNome(nome)
            _searchResults.value = results
            _errorMessage.value = null
        } catch (e: Exception) {
            _errorMessage.value = "Erro ao buscar bebida: ${e.message}"
        }
    }

    fun loadBebidaById(id: Int) = viewModelScope.launch {
        try {
            val bebida = repository.getBebidaComCategoriaById(id)
            _selectedBebida.value = bebida
            _errorMessage.value = null
        } catch (e: Exception) {
            _errorMessage.value = "Erro ao carregar bebida: ${e.message}"
        }
    }

    fun loadBebidaComMovimentacoes(id: Int) = viewModelScope.launch {
        try {
            val bebida = repository.getBebidaComMovimentacoes(id)
            _bebidaComMovimentacoes.value = bebida
            _errorMessage.value = null
        } catch (e: Exception) {
            _errorMessage.value = "Erro ao carregar movimentações: ${e.message}"
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun clearSelectedBebida() {
        _selectedBebida.value = null
    }
}
