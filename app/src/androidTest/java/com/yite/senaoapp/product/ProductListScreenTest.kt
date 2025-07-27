package com.yite.senaoapp.product

import androidx.activity.ComponentActivity
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.DialogNavigator
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.yite.senaoapp.R
import com.yite.senaoapp.data.model.Product
import com.yite.senaoapp.data.model.ProductResponse
import com.yite.senaoapp.navigation.AppRoute
import com.yite.senaoapp.repository.FakeProductRepository
import com.yite.senaoapp.repository.Task
import com.yite.senaoapp.ui.screens.product.list.ProductListScreen
import com.yite.senaoapp.ui.screens.product.list.ProductListViewModel
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
@MediumTest
@HiltAndroidTest
@OptIn(ExperimentalCoroutinesApi::class)
class ProductListScreenTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private val activity get() = composeTestRule.activity

    private lateinit var testNavHostController: androidx.navigation.testing.TestNavHostController
    private val productResponse =
        ProductResponse(
            data =
                listOf(
                    Product(martId = 1, martName = "Product A", imageUrl = "url_a", finalPrice = 100, price = 120, stockAvailable = 2, martShortName = "1")
                )
        )

    @Before
    fun init() {
        hiltRule.inject()

        testNavHostController = androidx.navigation.testing.TestNavHostController(activity)
        testNavHostController.navigatorProvider.addNavigator(ComposeNavigator())
        testNavHostController.navigatorProvider.addNavigator(DialogNavigator())
    }

    private fun setContent(
        viewModel: ProductListViewModel = ProductListViewModel(FakeProductRepository(Task.Success(productResponse))),
        onItemClick: (AppRoute.ProductDetail) -> Unit = {},
        onAddCart: (Product) -> Unit = {},
        onFavoriteClick: (Product) -> Unit = {}
    ) {
        composeTestRule.setContent {
            SenaoInterviewDemoTheme {
                Surface {
                    val snackbarHostState = remember { SnackbarHostState() }
                    ProductListScreen(
                        viewModel = viewModel,
                        snackbarHostState = snackbarHostState,
                        onItemClick = onItemClick,
                        onAddCart = onAddCart,
                        onFavoriteClick = onFavoriteClick
                    )
                }
            }
        }
    }

    @Test
    fun loadingState_showsLoadingIndicator() =
        runTest {
            setContent(
                viewModel = ProductListViewModel(FakeProductRepository(Task.Loading))
            )
            composeTestRule.onNodeWithContentDescription(activity.getString(R.string.loading)).assertIsDisplayed()
        }

    @Test
    fun errorState_showsErrorScreen() =
        runTest {
            setContent(
                viewModel = ProductListViewModel(FakeProductRepository(Task.Failure))
            )
            composeTestRule.waitForIdle()

            composeTestRule.onNodeWithText(activity.getString(R.string.loading_failed)).assertIsDisplayed()
            composeTestRule.onNodeWithText(activity.getString(R.string.retry)).assertIsDisplayed()
        }

    @Test
    fun successState_displaysProductList() =
        runTest {
            setContent()
            composeTestRule.waitForIdle()

            composeTestRule.onNodeWithText(productResponse.data.first().martName).assertIsDisplayed()
        }

    @Test
    fun searchProducts_filtersListCorrectly() =
        runTest {
            setContent()
            composeTestRule.waitForIdle()

            composeTestRule.onNodeWithTag("MY_SEARCH_TAG").performTextInput("Product")

            composeTestRule.waitForIdle()

            composeTestRule.onNodeWithText(productResponse.data.first().martName).assertIsDisplayed()

            composeTestRule.onNodeWithTag("MY_SEARCH_TAG").performTextClearance()
            composeTestRule.onNodeWithTag("MY_SEARCH_TAG").performTextInput("")

            composeTestRule.waitForIdle()

            composeTestRule.onNodeWithText(productResponse.data.first().martName).assertIsDisplayed()
        }
}
