package com.yite.senaoapp.ui.screens.product.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.yite.senaoapp.navigation.AppRoute
import com.yite.senaoapp.data.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val initialProductDetail: AppRoute.ProductDetail = AppRoute.ProductDetail.from(savedStateHandle)

    private val _productInfo = MutableStateFlow(initialProductDetail.product)
    val productInfo: StateFlow<Product> = _productInfo.asStateFlow()
}