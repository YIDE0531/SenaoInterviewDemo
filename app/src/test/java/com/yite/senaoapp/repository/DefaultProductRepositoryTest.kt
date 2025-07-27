package com.yite.senaoapp.repository

import app.cash.turbine.test
import com.yite.senaoapp.data.model.Product
import com.yite.senaoapp.data.model.ProductResponse
import com.yite.senaoapp.data.repository.DefaultProductRepository
import com.yite.senaoapp.data.source.network.NetworkDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultProductRepositoryTest {
    private lateinit var mockNetworkDataSource: NetworkDataSource
    private lateinit var repository: DefaultProductRepository

    private val fakeSuccessProductResponse =
        ProductResponse(
            data =
                listOf(
                    Product(
                        finalPrice = 100,
                        martName = "martName",
                        stockAvailable = 10,
                        price = 120,
                        martShortName = "martShortName",
                        imageUrl = "url",
                        martId = 100
                    )
                )
        )

    @Before
    fun setUp() {
        mockNetworkDataSource = mockk()
        repository = DefaultProductRepository(mockNetworkDataSource)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `getProducts calls networkDataSource and emits its response`() =
        runTest {
            coEvery { mockNetworkDataSource.getProducts() } returns fakeSuccessProductResponse

            val resultFlow = repository.getProducts()

            resultFlow.test {
                val emittedResponse = awaitItem()
                Assert.assertEquals(fakeSuccessProductResponse, emittedResponse)
                awaitComplete()
            }

            coVerify(exactly = 1) { mockNetworkDataSource.getProducts() }
        }

    @Test
    fun `getProducts propagates exception from networkDataSource`() =
        runTest {
            val expectedException = IOException("Network Error")
            coEvery { mockNetworkDataSource.getProducts() } throws expectedException

            val resultFlow = repository.getProducts()

            resultFlow.test {
                val actualException = awaitError()
                Assert.assertEquals(expectedException, actualException)
            }

            coVerify(exactly = 1) { mockNetworkDataSource.getProducts() }
        }

    @Test
    fun `getProducts propagates exception_alternativeStyle`() =
        runTest {
            val expectedException = IOException("Simulated Network Error")
            coEvery { mockNetworkDataSource.getProducts() } throws expectedException

            try {
                repository.getProducts().first()
                Assert.fail("Expected exception was not thrown")
            } catch (e: IOException) {
                Assert.assertEquals(expectedException.message, e.message)
            }

            coVerify(exactly = 1) { mockNetworkDataSource.getProducts() }
        }
}
