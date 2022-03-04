package com.lilly.codingchallenge.viewmodel


import android.content.Context
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.lilly.codingchallenge.data.database.ProductDatabase
import com.lilly.codingchallenge.data.database.entity.CartItems
import com.lilly.codingchallenge.data.model.ProductInfo
import com.lilly.codingchallenge.data.repository.StoreRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ViewModelTest {

    private lateinit var db: ProductDatabase
    private lateinit var storeViewModel: StoreViewModel
    private lateinit var cartViewModel: CartViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, ProductDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        val storeRepository = StoreRepository(context, db)
        storeViewModel = StoreViewModel(storeRepository)
        cartViewModel = CartViewModel(storeRepository)

    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun addProductToCart() {
        val productInfo = ProductInfo(
            productId = "0000P003",
            productName = "Asparagus",
            productType = "vegetable",
            productDescription = "Asparagus with ham on the wooden table",
            productPrice = 100.00,
            productImage = "img_asparagus.jpg"
        )
        val cartItem = storeViewModel.addItemToCart(productInfo)

        runBlocking {
            val result = cartViewModel.loadCartItemsFromDB().find {
                it.productID == cartItem.productID && it.itemName == cartItem.itemName
                        && it.itemQuantity == cartItem.itemQuantity && it.itemPrice == cartItem.itemPrice
            }
            assertThat(result).isNotNull()
        }

    }

    @Test
    fun deleteItemFromCart() {
        val productInfo = ProductInfo(
            productId = "0000P006",
            productName = "Pesto with basil",
            productType = "vegetable",
            productDescription = "Italian traditional pesto with basil, cheese and oil",
            productPrice = 99.99,
            productImage = "img_Pesto.jpg"
        )

        val cartItem = storeViewModel.addItemToCart(productInfo)
        runBlocking {
            delay(1000)
            val temp = cartViewModel.loadCartItemsFromDB()
            Log.d("Nishant", "temp size: ${temp.size}")
            val result = cartViewModel.loadCartItemsFromDB().find {
                it.productID == cartItem.productID && it.itemName == cartItem.itemName
                        && it.itemQuantity == cartItem.itemQuantity && it.itemPrice == cartItem.itemPrice
            }
            assertThat(result).isNotNull()
        }

        runBlocking {
            val cartItems = cartViewModel.loadCartItemsFromDB()
            cartViewModel.deleteItemFromCart(cartItems[0])
            val result = cartViewModel.loadCartItemsFromDB().find {
                it.productID == cartItem.productID && it.itemName == cartItem.itemName
                        && it.itemQuantity == cartItem.itemQuantity && it.itemPrice == cartItem.itemPrice
            }
            assertThat(result).isNull()
        }
    }

    @Test
    fun processOrder() {
        val cartItemList = ArrayList<CartItems>()
        val cartItem = CartItems(
            productID = "0000P003",
            itemName = "Asparagus",
            itemQuantity = 1,
            itemPrice = 100.00,
        )
        cartItemList.add(cartItem)

        val address = " My Address"
        cartViewModel.processOrder(cartItemList, address)

        runBlocking {
            val result = cartViewModel.loadOrderSummaryFromDB().find {
                it.productID == cartItemList[0].productID && it.productName == cartItemList[0].itemName
                        && it.itemPrice == cartItemList[0].itemPrice
                        && it.itemQuantity == cartItemList[0].itemQuantity
            }
            assertThat(result).isNotNull()
        }

    }
}