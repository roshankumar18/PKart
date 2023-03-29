package com.example.pkart.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.pkart.R
import com.example.pkart.databinding.ActivityAddressBinding
import com.example.pkart.databinding.ActivityCategoryBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddressBinding
    private lateinit var  preferences: SharedPreferences
    private lateinit var builder:AlertDialog
    private var TAG = "AddressActivity"
    private lateinit var totalCost :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)
        preferences =  this.getSharedPreferences("user", MODE_PRIVATE)
        setContentView(binding.root)
        totalCost = intent.getStringExtra("totalCost")!!
        loadUserInfo()
        builder = AlertDialog.Builder(this)
            .setTitle("Loading...")
            .setMessage("Please Wait")
            .setCancelable(false)
            .create()
        builder.show()
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
        map["pinCode"]=pincode

        Firebase.firestore.collection("Users").document(preferences.getString("number","")!!)
            .update(map).addOnSuccessListener {
                val intent = Intent(this,PaymentActivity::class.java)
                intent.putStringArrayListExtra("productIds",intent.getStringArrayListExtra("productIds"))
                intent.putExtra("totalCost",totalCost)
                startActivity(intent)
                finish()
                Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                Log.w(TAG, "storeData: $it", )
            }
    }

    private fun loadUserInfo() {

        binding.number.setText(preferences.getString("number","")!!)
        Firebase.firestore.collection("Users").document(preferences.getString("number","")!!)
            .get().addOnSuccessListener {
                binding.userName.setText(it.getString("userName"))
                binding.pinCode.setText(it.getString("pinCode"))
                binding.city.setText(it.getString("city"))
                binding.state.setText(it.getString("state"))
                binding.village.setText(it.getString("village"))
                builder.dismiss()
            }.addOnFailureListener {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                builder.dismiss()
            }
    }
}