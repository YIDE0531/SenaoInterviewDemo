package com.yite.senaoapp.data.repository

import com.yite.senaoapp.data.model.ProductResponse
import com.yite.senaoapp.data.source.network.NetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultProductRepository
    @Inject
    constructor(
        private val networkDataSource: NetworkDataSource,
    ) : ProductRepository {
        override fun getProducts(): Flow<ProductResponse> =
            flow {
                emit(networkDataSource.getProducts())
            }
    }
