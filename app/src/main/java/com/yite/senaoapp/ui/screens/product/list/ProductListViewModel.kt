package com.yite.senaoapp.ui.screens.product.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yite.senaoapp.data.model.Product
import com.yite.senaoapp.data.model.ProductResponse
import com.yite.senaoapp.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel
    @Inject
    constructor(
        productRepository: ProductRepository,
    ) : ViewModel() {
        private val _searchQuery = MutableStateFlow("")
        val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

        private val retryTrigger = MutableSharedFlow<Unit>(replay = 1, extraBufferCapacity = 1)

        @OptIn(ExperimentalCoroutinesApi::class)
        val uiState: StateFlow<ProductListUiState> =
            retryTrigger
                .flatMapLatest {
                    productRepository
                        .getProducts()
                        .map<ProductResponse, ProductListUiState> { ProductListUiState.Success(it) }
                        .catch { emit(ProductListUiState.Error(it)) }
                }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProductListUiState.Loading)

        val productItems: StateFlow<List<Product>> =
            combine(uiState, _searchQuery) { currentUiState, query ->
                when (currentUiState) {
                    is ProductListUiState.Success -> {
                        val allProducts = currentUiState.data.data
                        if (query.isBlank()) {
                            allProducts
                        } else {
                            allProducts.filter { product ->
                                product.martName.contains(query, ignoreCase = true)
                            }
                        }
                    }

                    else -> {
                        emptyList()
                    }
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList(),
            )

        init {
            viewModelScope.launch {
                retryTrigger.emit(value = Unit)
            }
        }

        /**
         * 更新搜尋查詢。
         * @param query 搜尋參數。
         */
        fun filterSearch(query: String) {
            _searchQuery.value = query
        }

        /**
         * 重新讀取列表資料。
         */
        fun reload() =
            viewModelScope.launch {
                retryTrigger.emit(value = Unit)
            }
    }
