package com.example.xuan.chipbus

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.example.xuan.chipbus.Class.ProductItem
import com.example.xuan.chipbus.Class.Products
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_latest_product.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder

class LatestProductActivity : AppCompatActivity() {

    companion object {
        val TAG = "LatestProductActivity"
    }

    val adapter = GroupAdapter<ViewHolder> ()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_product)

        //show product
        fetchProduct()
        ListenForLatesProduct()

        add_product_activity_btn.setOnClickListener {
            val intent = Intent(this,AddProductsActivity::class.java)
            startActivity(intent)
        }

        //recycle view show latest production
        //recyclerview_latest_product.adapter = adapter
        //recyclerview_latest_product.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))


    }

    //Put the latest product on the top
    val latestProductMap = HashMap<String,Products> ()


    private fun refreshRecycleViewProduct() {
        adapter.clear()
        latestProductMap.values.forEach {
            adapter.add(ProductItem(it))
        }
    }

    private fun fetchProduct() {
        //read values in the Firebase
        val ref = FirebaseDatabase.getInstance().getReference("/product-data")
            ref.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(p0: DataSnapshot) {
                    //val adapter = GroupAdapter<ViewHolder> ()

                    p0.children.forEach {
                        Log.d(TAG,it.toString())
                        // show product to user
                        val product = it.getValue(Products::class.java)
//                        if(product != null) {
//                            //adapter.add(ProductItem(product))
//                            // if you want latest product putted on the top, add the HashMap
//                            latestProductMap[p0.key!!] = product
//                            refreshRecycleViewProduct()
//                        }
                    }
                    recyclerview_latest_product.adapter = adapter

                }
                override fun onCancelled(p0: DatabaseError) {
                }
            })
    }

    private fun ListenForLatesProduct () {

        val ref = FirebaseDatabase.getInstance().getReference("/product-data")
        ref.addChildEventListener(object :ChildEventListener {
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
//                val products = p0.getValue(Products::class.java) ?: return
//                    latestProductMap[p0.key!!] = products
//                    refreshRecycleViewProduct()
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    val products = p0.getValue(Products::class.java) ?: return
                        latestProductMap[p0.key!!] = products
                        refreshRecycleViewProduct()

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })

    }
}

