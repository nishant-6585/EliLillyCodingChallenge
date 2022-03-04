package com.lilly.codingchallenge.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lilly.codingchallenge.adapter.ProductListAdapter
import com.lilly.codingchallenge.data.model.ProductInfo
import com.lilly.codingchallenge.databinding.ActivityMainBinding
import com.lilly.codingchallenge.viewmodel.StoreViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ProductListAdapter.AddToCartActionInterface {

    companion object {
        val TAG: String = MainActivity::class.java.simpleName
    }

    private lateinit var binding: ActivityMainBinding
    private val viewModel: StoreViewModel by viewModels()

    private val cartItems = ArrayList<ProductInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnOrderSummary.isEnabled = false
        binding.btnOrderSummary.setOnClickListener {
            val intent = Intent(this@MainActivity, OrderSummaryActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        addStoreInfoDataObserver()
        addProductInfoDataObserver()
    }

    private fun addStoreInfoDataObserver() {
        viewModel.storeInfoList.observe(this) {
            Log.i(TAG, "received store info list")
            val storeInfo = it[0]
            binding.tvStoreName.text = storeInfo.storeName
            binding.tvCity.text = storeInfo.city
            binding.tvPhone.text = storeInfo.storePhone
        }
    }

    private fun addProductInfoDataObserver() {
        viewModel.productInfoList.observe(this) {
            Log.i(TAG, "received product info list")
            binding.rvProductDetails.layoutManager = LinearLayoutManager(this@MainActivity)

            // This will pass the ArrayList to our Adapter
            val adapter = ProductListAdapter(it, this@MainActivity)

            binding.rvProductDetails.adapter = adapter
        }
    }

    override fun onItemAdded(view: View, productInfo: ProductInfo) {
        cartItems.add(productInfo)
        binding.btnOrderSummary.isEnabled = cartItems.size > 0
        view.isEnabled = false
        (view as TextView).text = "Added to cart"

        viewModel.addItemToCart(productInfo)
    }

}