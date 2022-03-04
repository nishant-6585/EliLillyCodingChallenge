package com.lilly.codingchallenge.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lilly.codingchallenge.data.database.entity.CartItems
import com.lilly.codingchallenge.data.model.ProductInfo
import com.lilly.codingchallenge.data.model.StoreInfo
import com.lilly.codingchallenge.data.repository.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val storeRepository: StoreRepository
) : ViewModel() {
        companion object {
            val TAG: String = StoreViewModel::class.java.simpleName
        }

    private var storeInfoJSONData: String? = storeRepository.getJsonDataFromAsset( "store_info.json")
    private var productInfoJSONData: String? = storeRepository.getJsonDataFromAsset( "product_info.json")

    private val _storeInfoList = MutableLiveData<List<StoreInfo>>()
        .also {
            it.value = parseStoreInfoData()
        }
    var storeInfoList: LiveData<List<StoreInfo>> = _storeInfoList

    private val _productInfoList = MutableLiveData<List<ProductInfo>>()
        .also {
            it.value =  parseProductInfoData()
        }
    var productInfoList: LiveData<List<ProductInfo>> = _productInfoList

    private fun parseStoreInfoData(): List<StoreInfo>{
        Log.i(TAG, "storeInfoJSONData : $storeInfoJSONData")
        val gson = Gson()
        val listStoreInfoTypeToken = object : TypeToken<List<StoreInfo>>() {}.type

        val storeInfoList: List<StoreInfo> = gson.fromJson(storeInfoJSONData, listStoreInfoTypeToken)
        storeInfoList.forEachIndexed { idx, storeInfo -> Log.i("Store info data", "> Item $idx:\n$storeInfo") }
        return storeInfoList
    }

    private fun parseProductInfoData(): List<ProductInfo> {
        Log.i(TAG, "productInfoJSONData : $productInfoJSONData")
        val gson = Gson()
        val listProductInfoTypeToken = object : TypeToken<List<ProductInfo>>() {}.type

        val productInfoList: List<ProductInfo> = gson.fromJson(productInfoJSONData, listProductInfoTypeToken)
        productInfoList.forEachIndexed { idx, productInfo -> Log.i("product info data", "> Item $idx:\n$productInfo") }
        return productInfoList
    }

    fun addItemToCart(productInfo: ProductInfo): CartItems {
        val cartItem = CartItems(
            productID = productInfo.productId,
            itemName = productInfo.productName,
            itemQuantity = 1,
            itemPrice = productInfo.productPrice
        )
        viewModelScope.launch {
            storeRepository.insert(cartItem)
        }
        return cartItem
    }

}