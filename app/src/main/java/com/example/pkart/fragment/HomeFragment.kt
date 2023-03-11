package com.example.pkart.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pkart.R
import com.example.pkart.adapter.CategoryAdapter
import com.example.pkart.adapter.ProductAdapter
import com.example.pkart.databinding.FragmentHomeBinding
import com.example.pkart.model.CategoryModel
import com.example.pkart.model.ProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {
    private lateinit var binding:FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        getCategories()
        getProducts()
        return binding.root
    }

    private fun getProducts() {
        val list = ArrayList<ProductModel>()
        Firebase.firestore.collection("products")
            .get()
            .addOnSuccessListener {
//            list.clear()
                for (document in it.documents){

                    val data = document.toObject<ProductModel>()
                    list.add(data!!)

                }

                binding.productRecycler.adapter = ProductAdapter(requireContext(),list)
            }
    }

    private fun getCategories() {
        val list = ArrayList<CategoryModel>()
        Firebase.firestore.collection("category")
            .get()
            .addOnSuccessListener {
//            list.clear()
                for (document in it.documents){

                    val data = document.toObject<CategoryModel>()
                    list.add(data!!)

                }

                binding.categoryRecycler.adapter = CategoryAdapter(requireContext(),list)
            }
    }


}