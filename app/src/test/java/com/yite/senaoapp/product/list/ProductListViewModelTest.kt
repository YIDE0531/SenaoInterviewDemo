package com.yite.senaoapp.product.list

import app.cash.turbine.test
import com.yite.senaoapp.data.model.Product
import com.yite.senaoapp.data.model.ProductResponse
import com.yite.senaoapp.data.repository.ProductRepository
import com.yite.senaoapp.ui.screens.product.list.ProductListUiState
import com.yite.senaoapp.ui.screens.product.list.ProductListViewModel
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class ProductListViewModelTest {
    private lateinit var testDispatcher: TestDispatcher

    private lateinit var mockProductRepository: ProductRepository

    private lateinit var viewModel: ProductListViewModel

    @Before
    fun setUp() {
        testDispatcher = UnconfinedTestDispatcher(TestCoroutineScheduler())
        Dispatchers.setMain(testDispatcher)

        mockProductRepository = mockk()
        coEvery {
            mockProductRepository.getProducts()
        } returns flowOf(createFakeProductResponse(emptyList()))

        viewModel = ProductListViewModel(mockProductRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    private fun createFakeProduct(
        id: Int,
        name: String
    ): Product =
        Product(
            finalPrice = 100 * id,
            martName = "$name $id",
            stockAvailable = 10,
            price = 120 * id,
            martShortName = name,
            imageUrl = "url/$id",
            martId = id
        )

    private fun createFakeProductResponse(products: List<Product>): ProductResponse =
        ProductResponse(
            data = products
        )

    @Test
    fun `initial uiState is Success with empty list when repository returns empty`() =
        runTest {
            viewModel.uiState.test {
                val successState = awaitItem() as ProductListUiState.Success
                assertTrue(successState.data.data.isEmpty())
                cancelAndConsumeRemainingEvents()
            }

            viewModel.productItems.test {
                assertTrue(awaitItem().isEmpty())
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `uiState becomes Success with data when repository returns products`() =
        runTest {
            val fakeProducts = listOf(createFakeProduct(1, "Product A"), createFakeProduct(2, "Product B"))
            val fakeResponse = createFakeProductResponse(fakeProducts)
            coEvery { mockProductRepository.getProducts() } returns flowOf(fakeResponse)

            viewModel.uiState.test {
                val successState = awaitItem() as ProductListUiState.Success
                assertEquals(fakeProducts.size, successState.data.data.size)
                assertEquals(
                    "Product A 1",
                    successState.data.data
                        .first()
                        .martName
                )
                cancelAndConsumeRemainingEvents()
            }

            viewModel.productItems.test {
                assertEquals(fakeProducts.size, awaitItem().size)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `uiState becomes Error when repository throws exception`() =
        runTest {
            coEvery {
                mockProductRepository.getProducts()
            } returns
                flow {
                    throw IOException("Network error")
                }

            viewModel.uiState.test {
                assertTrue(awaitItem() is ProductListUiState.Error)
                cancelAndConsumeRemainingEvents()
            }

            viewModel.productItems.test {
                assertTrue(awaitItem().isEmpty())
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `searchQuery filters productItems correctly`() =
        runTest {
            val products =
                listOf(
                    createFakeProduct(1, "Apple iPhone"),
                    createFakeProduct(2, "Samsung Galaxy"),
                    createFakeProduct(3, "Apple iPad")
                )
            val fakeResponse = createFakeProductResponse(products)
            coEvery { mockProductRepository.getProducts() } returns flowOf(fakeResponse)

            viewModel.reload() // 載入初始資料

            viewModel.productItems.test {
                assertEquals(3, awaitItem().size)
                cancelAndIgnoreRemainingEvents()
            }

            viewModel.filterSearch("Apple")
            advanceUntilIdle()

            viewModel.productItems.test {
                val filteredItems = awaitItem()
                assertEquals(2, filteredItems.size)
                assertTrue(filteredItems.any { it.martName == "Apple iPhone 1" })
                assertTrue(filteredItems.any { it.martName == "Apple iPad 3" })
                cancelAndConsumeRemainingEvents()
            }

            viewModel.filterSearch("galaxy")
            advanceUntilIdle()

            viewModel.productItems.test {
                val filteredItems = awaitItem()
                assertEquals(1, filteredItems.size)
                assertTrue(filteredItems.any { it.martName == "Samsung Galaxy 2" })
                cancelAndConsumeRemainingEvents()
            }

            viewModel.filterSearch("")
            advanceUntilIdle()

            viewModel.productItems.test {
                assertEquals(3, awaitItem().size)
                cancelAndConsumeRemainingEvents()
            }

            viewModel.filterSearch("Nokia")
            advanceUntilIdle()

            viewModel.productItems.test {
                assertTrue(awaitItem().isEmpty())
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `initial searchQuery is empty`() {
        assertEquals("", viewModel.searchQuery.value)
    }

    @Test
    fun `filterSearch updates searchQuery`() =
        runTest {
            val query = "test query"
            viewModel.filterSearch(query)
            viewModel.searchQuery.test {
                assertEquals(query, awaitItem())
                cancelAndConsumeRemainingEvents()
            }
        }
}
