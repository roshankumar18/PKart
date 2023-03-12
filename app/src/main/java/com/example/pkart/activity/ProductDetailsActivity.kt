package com.example.pkart.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.denzcoskun.imageslider.models.SlideModel
import com.example.pkart.R
import com.example.pkart.databinding.ActivityProductDetailsBinding
import com.example.pkart.model.ProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityProductDetailsBinding
    private val TAG = "ProductDetailsActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getProductDetails(intent.getStringExtra("id"))
    }

    private fun getProductDetails(proID: String?) {
        Firebase.firestore.collection("products").document(proID!!)
            .get().addOnSuccessListener {
//                val list= it.get("productImages") as ArrayList<String>
//                Log.d(TAG, "getProductDetails: ${it.toObject<ProductModel>()}")
                val data = it.toObject(ProductModel::class.java)
                binding.textView7.text = data!!.productName
                binding.textView8.text = data.productSp
                binding.textView9.text = data.productDescription
                val imageList = ArrayList<SlideModel>()
                for (image in data.productImages){
                    imageList.add(SlideModel(image))
                }
                binding.imageSlider.setImageList(imageList)

            }
    }
}