package com.lilly.codingchallenge.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lilly.codingchallenge.R
import com.lilly.codingchallenge.data.database.entity.CartItems

class CartItemsAdapter (
    private val cartItems: List<CartItems>,
    private val removeItemFromCartClickListener: RemoveItemFromCartActionInterface
        ): RecyclerView.Adapter<CartItemsAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cv_cart_items, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartItem = cartItems[position]
        holder.updateViewHolder(cartItem, removeItemFromCartClickListener)
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return cartItems.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        private val productImage: ImageView = itemView.findViewById(R.id.iv_cart)
        private val tvCartItemName: TextView = itemView.findViewById(R.id.tv_cart_item_name)
        private val tvCartItemQuantity: TextView = itemView.findViewById(R.id.tv_cart_item_quantity)
        private val tvCartItemPrice: TextView = itemView.findViewById(R.id.tv_cart_item_price)
        private val btnDeleteItemFromCart: Button = itemView.findViewById(R.id.btn_delete)

        fun updateViewHolder(
            cartItem: CartItems,
            addToCartActionInterface: RemoveItemFromCartActionInterface
        ) {
            tvCartItemName.text = cartItem.itemName
            tvCartItemQuantity.text = "Qty : ${cartItem.itemQuantity}"
            tvCartItemPrice.text = "Price : ${cartItem.itemPrice}"
            btnDeleteItemFromCart.setOnClickListener {
                addToCartActionInterface.onItemDeleted(it, cartItem)
            }
        }
    }

    interface RemoveItemFromCartActionInterface {
        fun onItemDeleted(view: View, cartItems: CartItems)
    }
}
