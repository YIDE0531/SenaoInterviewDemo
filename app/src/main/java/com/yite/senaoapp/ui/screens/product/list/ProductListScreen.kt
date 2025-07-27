package com.yite.senaoapp.ui.screens.product.list

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.gif.AnimatedImageDecoder
import coil3.gif.GifDecoder
import com.yite.senaoapp.R
import com.yite.senaoapp.data.model.Product
import com.yite.senaoapp.extension.noRippleClickable
import com.yite.senaoapp.navigation.AppRoute
import com.yite.senaoapp.ui.component.CustomSearchBar
import com.yite.senaoapp.ui.screens.product.list.preview.PreviewParameterProduct
import com.yite.senaoapp.ui.theme.SenaoInterviewDemoTheme

@Composable
fun ProductListScreen(
    modifier: Modifier = Modifier,
    viewModel: ProductListViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onItemClick: (AppRoute.ProductDetail) -> Unit,
    onAddCart: (Product) -> Unit,
    onFavoriteClick: (Product) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    when (uiState) {
        is ProductListUiState.Loading -> {
            LoadingScreen(modifier = modifier.fillMaxSize())
        }
        is ProductListUiState.Success -> {
            val items by viewModel.productItems.collectAsStateWithLifecycle()
            val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

            ResultScreen(
                modifier = modifier.fillMaxWidth(),
                productItems = items,
                searchQuery = searchQuery,
                onItemClick = onItemClick,
                onAddCart = onAddCart,
                onFavoriteClick = onFavoriteClick,
                onFilterSearch = {
                    viewModel.filterSearch(it)
                }
            )
        }
        is ProductListUiState.Error -> {
            ErrorScreen(
                modifier = modifier.fillMaxSize(),
                onRetryClick = {
                    viewModel.reload()
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    modifier: Modifier = Modifier,
    productItems: List<Product>,
    searchQuery: String,
    onItemClick: (AppRoute.ProductDetail) -> Unit,
    onAddCart: (Product) -> Unit,
    onFavoriteClick: (Product) -> Unit,
    onFilterSearch: (String) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    scrolledContainerColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    CustomSearchBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.surfaceContainer,
                                shape = RoundedCornerShape(50)
                            )
                            .padding(top = 6.dp, bottom = 6.dp, start = 8.dp, end = 8.dp),
                        value = searchQuery,
                        onValueChange = {
                            onFilterSearch.invoke(it)
                        }
                    )
                },
                scrollBehavior = scrollBehavior
            )
        },
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .consumeWindowInsets(contentPadding)
                .padding(horizontal = 4.dp),
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                HorizontalDivider(
                    thickness = 4.dp,
                    color = Color.Transparent
                )
            }
            items(
                items = productItems,
                key = { productItem ->
                    productItem.martId
                }
            ) { productItem ->
                ProductListItem(
                    productItem = productItem,
                    onItemClick = onItemClick,
                    onAddCart = onAddCart,
                    onFavoriteClick = onFavoriteClick,
                    lazyItemScope = this
                )
            }
        }
    }
}

@Composable
fun ProductListItem(
    modifier: Modifier = Modifier,
    productItem: Product,
    onItemClick: (AppRoute.ProductDetail) -> Unit,
    onAddCart: (Product) -> Unit,
    onFavoriteClick: (Product) -> Unit,
    lazyItemScope: LazyItemScope
) {
    with(lazyItemScope) {
        ConstraintLayout(
            modifier = modifier
                .fillMaxWidth()
                .animateItem()
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(top = 8.dp, bottom = 8.dp, start = 12.dp, end = 12.dp)
                .noRippleClickable {
                    onItemClick(
                        AppRoute.ProductDetail(
                            product = productItem
                        )
                    )
                }
        ) {
            val (
                productImageRes,
                productNameRes,
                productPriceRes,
                favoriteIconRes,
                addCartIconRes
            ) = createRefs()
            AsyncImage(
                modifier = Modifier
                    .size(width = 100.dp, height = 150.dp)
                    .constrainAs(productImageRes) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(productNameRes.start)
                    },
                model = productItem.imageUrl,
                contentDescription = stringResource(R.string.product_image_content_description),
            )
            Text(
                modifier = Modifier
                    .constrainAs(productNameRes) {
                        top.linkTo(parent.top, margin = 16.dp)
                        start.linkTo(productImageRes.end, margin = 16.dp)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                        horizontalChainWeight = 1f
                    },
                text = productItem.martName,
                color = MaterialTheme.colorScheme.onPrimary,
            )
            Text(
                modifier = Modifier
                    .constrainAs(productPriceRes) {
                        top.linkTo(productNameRes.bottom, margin = 16.dp)
                        start.linkTo(productImageRes.end, margin = 16.dp)
                    },
                text = stringResource(R.string.price_sign) + productItem.getFormattedFinalPrice(),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .constrainAs(favoriteIconRes) {
                        end.linkTo(addCartIconRes.start, margin = 8.dp)
                        bottom.linkTo(parent.bottom)
                    }.noRippleClickable {
                        onFavoriteClick.invoke(productItem)
                    },
                painter = painterResource(
                    id = R.drawable.icon_favorite,
                ),
                contentDescription = stringResource(R.string.icon_favorite_content_description),
                tint = MaterialTheme.colorScheme.secondary
            )
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .constrainAs(addCartIconRes) {
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }.noRippleClickable {
                        onAddCart.invoke(productItem)
                    },
                painter = painterResource(
                    id = R.drawable.icon_add_cart,
                ),
                contentDescription = stringResource(R.string.icon_add_cart_content_description),
                tint = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
    ) {
        AsyncImage(
            modifier = modifier
                .wrapContentSize()
                .align(Alignment.Center)
                .size(64.dp),
            model = R.drawable.icon_loading,
            imageLoader = ImageLoader.Builder(LocalContext.current)
                .components {
                    if ( SDK_INT >= 28 ) {
                        add(AnimatedImageDecoder.Factory())
                    } else {
                        add(GifDecoder.Factory())
                    }
                }.build(),
            contentDescription = stringResource(R.string.loading)
        )
    }
}

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    onRetryClick: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
        Button(
            modifier = Modifier
                .padding(16.dp),
            onClick = {
                onRetryClick.invoke()            }
        ) {
            Text(text = stringResource(R.string.retry))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    SenaoInterviewDemoTheme {
        LoadingScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun ResultScreenPreview() {
    SenaoInterviewDemoTheme {
        ResultScreen(
            productItems = listOf(),
            searchQuery = "",
            onItemClick = {},
            onAddCart = {},
            onFavoriteClick = {},
            onFilterSearch = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProductListItemPreview(
    @PreviewParameter(PreviewParameterProduct::class)
    productInfo: Product
) {
    SenaoInterviewDemoTheme {
        LazyColumn(modifier = Modifier.padding(8.dp)) {
            item {
                ProductListItem(
                    modifier = Modifier,
                    productItem = productInfo,
                    onItemClick = {},
                    onAddCart = {},
                    onFavoriteClick = {},
                    lazyItemScope = this
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorScreenPreview() {
    SenaoInterviewDemoTheme {
        ErrorScreen(
            onRetryClick = {}
        )
    }
}
