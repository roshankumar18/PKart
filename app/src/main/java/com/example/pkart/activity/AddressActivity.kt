package com.example.pkart.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pkart.R
import com.example.pkart.databinding.ActivityAddressBinding
import com.example.pkart.databinding.ActivityCategoryBinding

class AddressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddressBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}