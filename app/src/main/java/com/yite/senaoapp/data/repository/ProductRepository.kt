package com.yite.senaoapp.data.repository

import com.yite.senaoapp.data.model.ProductResponse
import kotlinx.coroutines.flow.Flow

/**
 * Interface to the data layer.
 */
interface ProductRepository {

    fun getProducts(): Flow<ProductResponse>
}
