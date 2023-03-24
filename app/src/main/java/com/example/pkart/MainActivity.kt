package com.example.pkart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.PopupMenu


import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.pkart.activity.LoginActivity
import com.example.pkart.activity.RegisterActivity
import com.example.pkart.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var navController : NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (FirebaseAuth.getInstance().currentUser == null){
            startActivity(Intent(this,RegisterActivity::class.java))
            finish()
        }
        val popUpMenu = PopupMenu(this,null)
        popUpMenu.inflate(R.menu.menu)
        val navHostFragment =  supportFragmentManager.findFragmentById(R.id.fragment_container)
        val navController = navHostFragment!!.findNavController()
        binding.bottomBar.setupWithNavController(popUpMenu.menu,navController)

    }



}