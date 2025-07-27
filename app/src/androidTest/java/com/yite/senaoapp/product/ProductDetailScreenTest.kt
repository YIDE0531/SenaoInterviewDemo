package com.yite.senaoapp.product

import androidx.activity.ComponentActivity
import androidx.compose.material3.Surface
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.yite.senaoapp.R
import com.yite.senaoapp.data.model.Product
import com.yite.senaoapp.ui.screens.product.detail.ProductDetailContent
import com.yite.senaoapp.ui.theme.SenaoInterviewDemoTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@OptIn(ExperimentalCoroutinesApi::class)
class ProductDetailScreenTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private val activity get() = composeTestRule.activity

    private val testProduct =
        Product(
            martId = 123,
            martName = "Test Product Name",
            imageUrl = "http://example.com/image.png",
            finalPrice = 999,
            price = 1200,
            stockAvailable = 10,
            martShortName = "TPN"
        )

    @Before
    fun init() {
        hiltRule.inject()
    }

    private fun setContent(productToDisplay: Product) {
        composeTestRule.setContent {
            SenaoInterviewDemoTheme {
                Surface {
                    ProductDetailContent(
                        productInfo = productToDisplay,
                        onBack = {},
                        onAddCart = {},
                        onFavoriteClick = {}
                    )
                }
            }
        }
    }

    @Test
    fun displayProductInfo_whenDataIsAvailable() =
        runTest {
            setContent(testProduct)

            composeTestRule.onNodeWithText(activity.getString(R.string.product_detail_title)).assertIsDisplayed()
            composeTestRule.onNodeWithText(activity.getString(R.string.product_id, testProduct.martId)).assertIsDisplayed()
            composeTestRule.onNodeWithText(testProduct.martName).assertIsDisplayed()
            composeTestRule
                .onNodeWithText(
                    activity.getString(R.string.price_sign) + testProduct.getFormattedFinalPrice()
                ).assertIsDisplayed()
        }
}
