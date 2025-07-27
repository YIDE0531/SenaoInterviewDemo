package com.yite.senaoapp.navigation

import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavType
import androidx.navigation.toRoute
import com.yite.senaoapp.data.model.Product
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

/**
 * 定義目前app的畫面
 */
sealed class AppRoute {

    /**
     * 商品列表頁
     */
    @Serializable
    object ProductList : AppRoute()

    /**
     * 商品詳細頁
     * @param product 商品資料
     */
    @Serializable
    data class ProductDetail(val product: Product) {
        companion object Companion {
            val typeMap = mapOf(typeOf<Product>() to serializableType<Product>())

            fun from(savedStateHandle: SavedStateHandle) =
                savedStateHandle.toRoute<ProductDetail>(typeMap)
        }
    }
}

/**
 * 處理導頁有nested objects的問題
 */
inline fun <reified T : Any> serializableType(
    isNullableAllowed: Boolean = false,
    json: Json = Json,
) = object : NavType<T>(isNullableAllowed = isNullableAllowed) {
    override fun get(bundle: Bundle, key: String) =
        bundle.getString(key)?.let<String, T>(json::decodeFromString)

    override fun parseValue(value: String): T = json.decodeFromString(value)

    override fun serializeAsValue(value: T): String = Uri.encode(json.encodeToString(value))

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putString(key, json.encodeToString(value))
    }
}