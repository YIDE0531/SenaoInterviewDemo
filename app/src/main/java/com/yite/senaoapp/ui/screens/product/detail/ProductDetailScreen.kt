package com.yite.senaoapp.ui.screens.product.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.yite.senaoapp.R
import com.yite.senaoapp.data.model.Product
import com.yite.senaoapp.extension.noRippleClickable
import com.yite.senaoapp.ui.screens.product.list.preview.PreviewParameterProduct
import com.yite.senaoapp.ui.theme.SenaoInterviewDemoTheme
import com.yite.senaoapp.ui.theme.price12BoldTextStyle
import com.yite.senaoapp.ui.theme.priceTextBoldStyle18

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: ProductDetailViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onAddCart: (Product) -> Unit,
    onFavoriteClick: (Product) -> Unit,
) {
    val productInfo by viewModel.productInfo.collectAsStateWithLifecycle()
    ProductDetailContent(
        modifier = modifier,
        productInfo = productInfo,
        onBack = onBack,
        onAddCart = onAddCart,
        onFavoriteClick = onFavoriteClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailContent(
    modifier: Modifier = Modifier,
    productInfo: Product,
    onBack: () -> Unit,
    onAddCart: (Product) -> Unit,
    onFavoriteClick: (Product) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    scrolledContainerColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        stringResource(R.string.product_detail_title),
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBack.invoke() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.icon_back_content_description),
                            tint = Color.White
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .verticalScroll(rememberScrollState())
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth(),
                model = productInfo.imageUrl,
                contentDescription = stringResource(R.string.product_image_content_description)
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp),
                text = stringResource(R.string.product_id, productInfo.martId),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 4.dp),
                text = productInfo.martName,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = buildAnnotatedString {
                        withStyle(
                            style = price12BoldTextStyle.copy(
                                color = MaterialTheme.colorScheme.error
                            )
                        ) {
                            append(stringResource(R.string.price_sign))
                        }
                        withStyle(
                            style = priceTextBoldStyle18.copy(
                                color = MaterialTheme.colorScheme.error
                            )
                        ) {
                            append(productInfo.getFormattedFinalPrice())
                        }
                    }
                )
                Icon(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(24.dp)
                        .noRippleClickable {
                            onFavoriteClick.invoke(productInfo)
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
                        .noRippleClickable {
                            onAddCart.invoke(productInfo)
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
}

@Preview
@Composable
fun LoadingScreenPreview(
    @PreviewParameter(PreviewParameterProduct::class, limit = 1)
    productInfo: Product
) {
    SenaoInterviewDemoTheme {
        ProductDetailContent(
            productInfo = productInfo,
            onBack = {},
            onAddCart = {},
            onFavoriteClick = {}
        )
    }
}