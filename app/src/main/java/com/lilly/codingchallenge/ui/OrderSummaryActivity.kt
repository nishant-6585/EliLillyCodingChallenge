package com.lilly.codingchallenge.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.lilly.codingchallenge.adapter.CartItemsAdapter
import com.lilly.codingchallenge.data.database.entity.CartItems
import com.lilly.codingchallenge.databinding.ActivityOrderSummaryBinding
import com.lilly.codingchallenge.viewmodel.CartViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OrderSummaryActivity : AppCompatActivity(),
    CartItemsAdapter.RemoveItemFromCartActionInterface {

    companion object {
        val TAG: String = OrderSummaryActivity::class.java.simpleName
    }

    private lateinit var binding: ActivityOrderSummaryBinding
    private val viewModel: CartViewModel by viewModels()

    private val cartItems = ArrayList<CartItems>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderSummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCheckout.setOnClickListener {
            if (binding.etAddress.text?.isNotEmpty() == true) {
                binding.btnCheckout.isEnabled = false
                binding.rvCartItems.isEnabled= false
                binding.etAddress.isEnabled = false
                binding.progressBarID.visibility = View.VISIBLE
                viewModel.processOrder(cartItems, binding.etAddress.text.toString())
                startTimer()
            } else {
                Snackbar
                    .make(
                        binding.root,
                        "Please enter delivery address to proceed", Snackbar.LENGTH_SHORT
                    )
                    .show()
            }

        }
        addCartItemsDataObserver()

    }

    private fun startTimer() {
        val handler = Handler(mainLooper)
        handler.postDelayed({
            binding.progressBarID.visibility = View.GONE
            val intent = Intent(
                this@OrderSummaryActivity, SuccessScreenActivity::class.java
            )
            startActivity(intent)
            finish()
        }, 5000)

    }

    private fun addCartItemsDataObserver() {
        viewModel.cartItemList.observe(this) {
            Log.i(TAG, "received cart list")

            binding.rvCartItems.layoutManager = LinearLayoutManager(this@OrderSummaryActivity)

            binding.btnCheckout.isEnabled = it.isNotEmpty()
            cartItems.clear()
            cartItems.addAll( it)
            // This will pass the ArrayList to our Adapter
            val adapter = CartItemsAdapter(it, this@OrderSummaryActivity)

            binding.rvCartItems.adapter = adapter
            var totalPrice = 0.0
            for (cartItem in it) {
                totalPrice += cartItem.itemPrice
            }
            binding.tvTotalPrice.text = "Total: Rs $totalPrice"
        }
    }

    override fun onItemDeleted(view: View, cartItems: CartItems) {
        viewModel.deleteItemFromCart(cartItems)

    }

}