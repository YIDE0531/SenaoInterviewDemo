package com.yite.senaoapp.data.model

import android.icu.text.NumberFormat
import kotlinx.serialization.Serializable

/**
 * 表示單一商品的詳細資訊。
 *
 * @property finalPrice 最終售價（可能有折扣後的價格）
 * @property martName 商品完整名稱，通常含有促銷或活動資訊
 * @property stockAvailable 庫存數量
 * @property price 原始價格（未折扣）
 * @property martShortName 商品簡稱，較簡潔的名稱
 * @property imageUrl 商品圖片網址
 * @property martId 商品唯一識別 ID
 */
@Serializable
data class Product(
    val finalPrice: Int,
    val martName: String,
    val stockAvailable: Int,
    val price: Int,
    val martShortName: String,
    val imageUrl: String,
    val martId: Int,
) {
    fun getFormattedFinalPrice(): String {
        val numberFormat = NumberFormat.getNumberInstance()
        val formattedPrice: String = numberFormat.format(finalPrice)
        return formattedPrice
    }
}
