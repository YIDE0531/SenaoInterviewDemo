package com.yite.senaoapp.di

import com.yite.senaoapp.data.repository.DefaultProductRepository
import com.yite.senaoapp.data.repository.ProductRepository
import com.yite.senaoapp.data.source.network.NetworkDataSource
import com.yite.senaoapp.data.source.network.ProductNetworkDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindTaskRepository(repository: DefaultProductRepository): ProductRepository
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Singleton
    @Binds
    abstract fun bindNetworkDataSource(dataSource: ProductNetworkDataSource): NetworkDataSource
}
