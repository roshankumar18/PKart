package com.example.pkart.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pkart.R
import com.example.pkart.activity.AddressActivity
import com.example.pkart.adapter.CartAdapter
import com.example.pkart.databinding.FragmentCartBinding
import com.example.pkart.roomdb.AppDatabase
import com.example.pkart.roomdb.ProductModelDb


class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private lateinit var list: ArrayList<String>
    private lateinit var adapter: CartAdapter
    private var TAG = "CartFragment"
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
        list = ArrayList()
        adapter = CartAdapter(requireContext(),ArrayList<ProductModelDb>())
        dao.getAll().observe(requireActivity()){
            list.clear()
            for (data in it){
                list.add(data.productId)
                Log.w(TAG, "onCreateView: ${data.productId}", )
                Log.w(TAG, "onCreateView: ${list}", )
            }
            adapter = CartAdapter(requireContext(),it)
            binding.cartRecycler.adapter = adapter
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
            Toast.makeText(requireContext(), "$price", Toast.LENGTH_SHORT).show()
            intent.putExtra("totalCost",price.toString())
            Log.w(TAG, "totalCost: $list", )
            intent.putStringArrayListExtra("productIds",list)
            startActivity(intent)
        }
    }



}