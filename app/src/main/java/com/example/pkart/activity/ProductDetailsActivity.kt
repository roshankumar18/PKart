package com.example.pkart.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.denzcoskun.imageslider.models.SlideModel
import com.example.pkart.MainActivity
import com.example.pkart.databinding.ActivityProductDetailsBinding
import com.example.pkart.model.ProductModels
import com.example.pkart.roomdb.AppDatabase
import com.example.pkart.roomdb.ProductDao
import com.example.pkart.roomdb.ProductModelDb
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                val data = it.toObject(ProductModels::class.java)
                binding.textView7.text = data!!.productName
                binding.textView8.text = data.productSp
                binding.textView9.text = data.productDescription
                val imageList = ArrayList<SlideModel>()
                for (image in data.productImages){
                    imageList.add(SlideModel(image))
                }
                binding.imageSlider.setImageList(imageList)
                cartAction(proID,data.productName,data.productSp,data.productCoverImg)
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to load ", Toast.LENGTH_SHORT).show()
            }
    }

    private fun cartAction(
        proID: String,
        productName: String?,
        productSp: String?,
        productCoverImg: String?
    ) {
        val productDao = AppDatabase.getDatabase(this).productDao()
        lifecycleScope.launch(Dispatchers.IO) {
            if (productDao.isExist(proID) != null) {
                withContext(Dispatchers.Main) {
                    binding.textView10.text = "Go To Cart"
                }
            } else {
                withContext(Dispatchers.Main) {
                    binding.textView10.text = "Add To Cart"
                }
            }
        }

            binding.textView10.setOnClickListener {
                lifecycleScope.launch(Dispatchers.IO){
                if (productDao.isExist(proID) != null) {
                    openCart()
                } else {
                    addToCart(productDao, proID, productName, productSp, productCoverImg)
                }
                    }
            }
        }



    private fun openCart() {
        val preference = this.getSharedPreferences("info", MODE_PRIVATE)
        val editor = preference.edit()
        editor.putBoolean("isCart",true)
        editor.apply()
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }

    private fun addToCart(
        productDao: ProductDao,
        proID: String,
        productName: String?,
        productSp: String?,
        productCoverImg: String?
    ) {
        val data = ProductModelDb(proID,productName,productCoverImg,productSp)
        lifecycleScope.launch(Dispatchers.IO){
            productDao.insertProduct(data)
        }
    }
}