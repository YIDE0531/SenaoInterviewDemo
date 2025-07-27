package com.yite.senaoapp.data.source.network

import com.yite.senaoapp.data.model.ProductResponse

interface NetworkDataSource {
    suspend fun getProducts(): ProductResponse
}
