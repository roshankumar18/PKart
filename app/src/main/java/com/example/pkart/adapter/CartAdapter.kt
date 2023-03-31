package com.example.pkart.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pkart.R
import com.example.pkart.activity.ProductDetailsActivity
import com.example.pkart.databinding.LayoutCartItemBinding
import com.example.pkart.model.ProductModels
import com.example.pkart.roomdb.AppDatabase
import com.example.pkart.roomdb.ProductModelDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CartAdapter(var context: Context ,val list: List<ProductModelDb>): RecyclerView.Adapter<CartAdapter.CartHolder>() {
    class CartHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val binding = LayoutCartItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_cart_item,parent,false)
        return CartHolder(view)
    }

    override fun onBindViewHolder(holder: CartHolder, position: Int) {
        Glide.with(context).load(list[position].productImage).into(holder.binding.imageView3)
        holder.binding.textView11.text = list[position].productName
        holder.binding.textView12.text = list[position].productSp
        val dao = AppDatabase.getDatabase(context).productDao()
        holder.binding.imageView4.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO){
                dao.deleteProduct(list[position])
            }
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra("id",list[position].productId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}