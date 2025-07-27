package com.yite.senaoapp.ui.screens.product.list

import com.yite.senaoapp.data.model.ProductResponse

sealed interface ProductListUiState {
    object Loading : ProductListUiState
    data class Error(val throwable: Throwable) : ProductListUiState
    data class Success(val data: ProductResponse) : ProductListUiState
}