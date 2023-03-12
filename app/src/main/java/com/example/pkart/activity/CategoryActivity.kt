package com.example.pkart.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pkart.R
import com.example.pkart.adapter.ProductAdapter
import com.example.pkart.databinding.ActivityCategoryBinding
import com.example.pkart.model.ProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class CategoryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCategoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getProducts(intent.getStringExtra("cat"))
    }

    private fun getProducts(category: String?) {
        val list = ArrayList<ProductModel>()
        Firebase.firestore.collection("products").whereEqualTo("productCategory",category)
            .get()
            .addOnSuccessListener {
//            list.clear()
                for (document in it.documents){

                    val data = document.toObject<ProductModel>()
                    list.add(data!!)

                }

                binding.recyclerView.adapter = ProductAdapter(this,list)
            }
    }
}