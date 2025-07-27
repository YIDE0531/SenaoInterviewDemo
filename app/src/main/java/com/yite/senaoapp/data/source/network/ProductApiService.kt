package com.yite.senaoapp.data.source.network

import com.yite.senaoapp.data.model.ProductResponse
import retrofit2.http.GET

interface ProductApiService {
    @GET("app/test/marttest.json")
    suspend fun getProducts(): ProductResponse
}
