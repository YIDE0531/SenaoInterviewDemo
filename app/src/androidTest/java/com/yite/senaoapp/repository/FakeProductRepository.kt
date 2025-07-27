package com.yite.senaoapp.repository

import com.yite.senaoapp.data.model.ProductResponse
import com.yite.senaoapp.data.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

sealed class Task {
    data class Success(
        val data: ProductResponse
    ) : Task()

    object Failure : Task()

    object Loading : Task()
}

class FakeProductRepository(
    val task: Task
) : ProductRepository {
    override fun getProducts(): Flow<ProductResponse> =
        flow {
            when (task) {
                is Task.Success -> {
                    emit(
                        task.data
                    )
                }
                is Task.Failure -> {
                    throw Throwable()
                }
                is Task.Loading -> {
                }
            }
        }
}
