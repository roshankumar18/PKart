package com.example.pkart.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.pkart.R
import com.example.pkart.databinding.ActivityAddressBinding
import com.example.pkart.databinding.ActivityCategoryBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddressBinding
    private val preferences: SharedPreferences = this.getSharedPreferences("user", MODE_PRIVATE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadUserInfo()
        binding.proceed.setOnClickListener {
            validateData(binding.userName.text.toString(),
                binding.number.text.toString(),
                binding.city.text.toString(),
                binding.state.text.toString(),
                binding.village.text.toString(),
                binding.pinCode.text.toString())
        }
    }

    private fun validateData(name: String, number: String, city: String, state: String, village: String, pincode: String) {
        if (city.isEmpty()||village.isEmpty()||pincode.isEmpty()||state.isEmpty()){
            Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
        }else
            storeData(name,number,city,state,village,pincode)
    }

    private fun storeData(name: String, number: String, city: String, state: String, village: String, pincode: String) {
        val map = hashMapOf<String,Any>()
        map["village"]=village
        map["state"]=state
        map["city"]=city
        map["pincode"]=pincode

        Firebase.firestore.collection("Users").document(preferences.getString("number","")!!)
            .update(map).addOnSuccessListener {
                startActivity(Intent(this,CheckoutActivity::class.java))
                finish()
            }.addOnFailureListener {

            }
    }

    private fun loadUserInfo() {


        Firebase.firestore.collection("Users").document(preferences.getString("number","")!!)
            .get().addOnSuccessListener {
                binding.userName.setText(it.getString("userName"))
            }
    }
}