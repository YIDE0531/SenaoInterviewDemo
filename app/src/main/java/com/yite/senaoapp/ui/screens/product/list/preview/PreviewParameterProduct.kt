package com.yite.senaoapp.ui.screens.product.list.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.yite.senaoapp.data.model.Product

class PreviewParameterProduct : PreviewParameterProvider<Product> {
    override val values: Sequence<Product>
        get() =
            sequenceOf(
                PreviewProduct,
                PreviewProduct2,
            )
}

val PreviewProduct =
    Product(
        finalPrice = 1299,
        martName = "超值優惠！最新款智能手機 Pro Max 128GB - 午夜黑",
        stockAvailable = 50,
        price = 1499,
        martShortName = "智能手機 Pro Max",
        imageUrl = "https://xxxx",
        martId = 1001,
    )

val PreviewProduct2 =
    Product(
        finalPrice = 30000,
        martName = "martName",
        stockAvailable = 0,
        price = 4000,
        martShortName = "martShortName",
        imageUrl = "https://xxxx",
        martId = 1003,
    )
