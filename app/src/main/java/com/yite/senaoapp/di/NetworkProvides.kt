package com.yite.senaoapp.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.yite.senaoapp.data.source.network.ProductApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @Provides
    fun provideBaseUrl(): String = "https://static-resrc.s3.amazonaws.com/"

    @Provides
    @Singleton
    fun provideRetrofit(
        baseUrl: String,
        okHttpClient: OkHttpClient,
    ): Retrofit =
        Retrofit
            .Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
    ): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(
                ChuckerInterceptor
                    .Builder(context)
                    .build(),
            ).build()

    @Provides
    @Singleton
    fun provideProductApiService(retrofit: Retrofit): ProductApiService = retrofit.create(ProductApiService::class.java)
}
