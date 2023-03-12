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
import com.example.pkart.databinding.CategoryProductLayoutBinding
import com.example.pkart.model.CategoryModel
import com.example.pkart.model.ProductModel

class CategoryProductAdapter(val context: Context , val list : ArrayList<ProductModel>) : RecyclerView.Adapter<CategoryProductAdapter.CategoryProductViewHolder>() {
    class CategoryProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding = CategoryProductLayoutBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryProductViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.category_product_layout,parent,false)
        return CategoryProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryProductViewHolder, position: Int) {
        val data = list[position]
        holder.binding.textView5.text = data.productName
        holder.binding.textView6.text = data.productSp
        Glide.with(context).load(data.productCoverImg).into(holder.binding.imageView2)

        holder.itemView.setOnClickListener{
            val intent = Intent(context,ProductDetailsActivity::class.java)
            intent.putExtra("id",list[position].productId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}