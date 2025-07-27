package com.yite.senaoapp.data.model

import kotlinx.serialization.Serializable

/**
 * 包含商品列表的 API 回應資料結構。
 *
 * @property data 商品清單
 */
@Serializable
data class ProductResponse(
    val data: List<Product>
)

