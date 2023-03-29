package com.example.pkart.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.pkart.R
import com.example.pkart.databinding.ActivityAddressBinding
import com.example.pkart.databinding.ActivityRegisterBinding
import com.example.pkart.model.UserModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private var TAG = "RegisterActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button4.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
        binding.button.setOnClickListener {
            validateUser()
        }
    }

    private fun validateUser() {
        if (binding.userName.text!!.isEmpty() || binding.number.text!!.isEmpty()){
            Toast.makeText(this, "Please fill all fields ", Toast.LENGTH_SHORT).show()
        }else{
            storeData()
        }
    }

    private fun storeData() {
        val builder = AlertDialog.Builder(this)
            .setTitle("Loading...")
            .setMessage("Please Wait")
            .setCancelable(false)
            .create()
        builder.show()


        val data = UserModel(userName = binding.userName.text.toString(), userPhoneNumber =binding.number.text.toString() )
        Log.w(TAG, "storeData: ${binding.number.text.toString()}")
        Firebase.firestore.collection("Users").document(binding.number.text.toString())
            .set(data).addOnSuccessListener {
                builder.dismiss()
                Toast.makeText(this, "user registered", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            }.addOnFailureListener {
                builder.dismiss()
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

    }
}