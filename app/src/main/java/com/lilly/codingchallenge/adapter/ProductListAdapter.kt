package com.lilly.codingchallenge.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lilly.codingchallenge.R
import com.lilly.codingchallenge.data.model.ProductInfo

class ProductListAdapter(
    private val productList: List<ProductInfo>,
    private val addToCartClickListener: AddToCartActionInterface
) : RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cv_product_details, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val productInfo = productList[position]
        holder.updateViewHolder(productInfo, addToCartClickListener)
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return productList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        private val productImage: ImageView = itemView.findViewById(R.id.iv_product)
        private val tvProductName: TextView = itemView.findViewById(R.id.tv_product_name)
        private val tvProductDesc: TextView = itemView.findViewById(R.id.tv_product_desc)
        private val tvProductPrice: TextView = itemView.findViewById(R.id.tv_product_price)
        private val btnAddToCart: Button = itemView.findViewById(R.id.btn_add_to_cart)

        fun updateViewHolder(
            productInfo: ProductInfo,
            addToCartActionInterface: AddToCartActionInterface
        ) {
            tvProductName.text = productInfo.productName
            tvProductDesc.text = productInfo.productDescription
            tvProductPrice.text = productInfo.productPrice.toString()
            btnAddToCart.setOnClickListener {
                addToCartActionInterface.onItemAdded(it, productInfo)
            }
        }
    }

    interface AddToCartActionInterface {
        fun onItemAdded(view: View, productInfo: ProductInfo)
    }
}