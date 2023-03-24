package com.example.pkart.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.pkart.MainActivity
import com.example.pkart.R
import com.example.pkart.databinding.ActivityLoginBinding
import com.example.pkart.databinding.ActivityOtpactivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class OTPActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpactivityBinding
    private val TAG = "OTPActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener {
            if (binding.userOTP.text!!.isEmpty()){
                Toast.makeText(this, "Please Enter OTP", Toast.LENGTH_SHORT).show()
            }   else{
                verifyUser(binding.userOTP.text.toString())
            }
        }
    }

    private fun verifyUser(OTP: String) {
        val credential = PhoneAuthProvider.getCredential(intent.getStringExtra("verificationId")!!,OTP)
        signInWithPhoneAuthCredential(credential)
    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val preferences = this.getSharedPreferences("user", MODE_PRIVATE)
                    val editor = preferences.edit()
                    editor.putString("number",intent.getStringExtra("number"))
                    editor.apply()
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                    val user = task.result?.user
                } else {
                    // Sign in failed, display a message and update the UI
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
    }
}