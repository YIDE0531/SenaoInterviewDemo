package com.yite.senaoapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yite.senaoapp.ui.screens.product.detail.ProductDetailScreen
import com.yite.senaoapp.ui.screens.product.list.ProductListScreen

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: AppRoute = AppRoute.ProductList,
    navActions: AppNavigationActions =
        remember(navController) {
            AppNavigationActions(navController)
        },
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable<AppRoute.ProductList> { backStackEntry ->
            ProductListScreen(
                onItemClick = { navActions.navigateToProductDetail(it) },
                onAddCart = { },
                onFavoriteClick = { },
            )
        }
        composable<AppRoute.ProductDetail>(
            typeMap = AppRoute.ProductDetail.typeMap,
        ) { backStackEntry ->
            ProductDetailScreen(
                onBack = { navController.popBackStack() },
                onAddCart = { },
                onFavoriteClick = { },
            )
        }
    }
}
