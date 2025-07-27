package com.yite.senaoapp.navigation

import androidx.navigation.NavHostController

/**
 * 導頁處理
 */
class AppNavigationActions(
    private val navController: NavHostController,
) {
    fun navigateToProductDetail(productDetail: AppRoute.ProductDetail) {
        navController.navigate(productDetail)
    }
}
