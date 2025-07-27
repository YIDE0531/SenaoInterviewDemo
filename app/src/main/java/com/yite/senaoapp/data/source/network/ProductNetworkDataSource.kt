package com.yite.senaoapp.data.source.network

import com.yite.senaoapp.data.model.ProductResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductNetworkDataSource @Inject constructor(
    private val productApiService: ProductApiService
) : NetworkDataSource {

    override suspend fun getProducts(): ProductResponse {
        return productApiService.getProducts()
    }
}