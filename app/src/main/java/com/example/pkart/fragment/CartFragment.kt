package com.example.pkart.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.pkart.R
import com.example.pkart.activity.AddressActivity
import com.example.pkart.adapter.CartAdapter
import com.example.pkart.databinding.FragmentCartBinding
import com.example.pkart.roomdb.AppDatabase
import com.example.pkart.roomdb.ProductModelDb


class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(layoutInflater)
        val preference = requireContext().getSharedPreferences("info", AppCompatActivity.MODE_PRIVATE)
        val editor = preference.edit()
        editor.putBoolean("isCart",false)
        editor.apply()
        val dao = AppDatabase.getDatabase(requireContext()).productDao()
        dao.getAll().observe(requireActivity()){
            binding.cartRecycler.adapter = CartAdapter(requireContext(),it)
            totalCost(it)
            binding.textView1.text = "Total item in cart is : ${it.size}"
        }

        return binding.root
    }

    private fun totalCost(data: List<ProductModelDb>?) {
        var price :Int = 0
        for (i in data!!){
           price += Integer.parseInt(i.productSp)
       }
        binding.textView2.text = "Total cost : $price"
        binding.checkout.setOnClickListener {
            val intent = Intent(context,AddressActivity::class.java)
            intent.putExtra("totalCost",price)
            startActivity(intent)
        }
    }

}