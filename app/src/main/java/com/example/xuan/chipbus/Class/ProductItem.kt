package com.example.xuan.chipbus.Class

import com.example.xuan.chipbus.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.show_product.view.*

 class ProductItem(val products: Products) :Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.productname_textview.text = products.productName
        viewHolder.itemView.detail_product_textview.text = products.productInformation
        viewHolder.itemView.price_product_textview.text = products.productPrice

        // upload image to show product
        val uri =products.profileImageProductURL
        val productImage = viewHolder.itemView.product_image
        Picasso.get().load(uri).into(productImage)
    }

     override fun getLayout(): Int {
         return R.layout.show_product
     }
}