package com.example.pkart.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pkart.R
import com.example.pkart.activity.CategoryActivity
import com.example.pkart.databinding.LayoutCategoryItemBinding
import com.example.pkart.model.CategoryModel



class CategoryAdapter(var context : Context, val list:ArrayList<CategoryModel>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding = LayoutCategoryItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_category_item,parent,false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        Glide.with(context).load(list[position].img).into(holder.binding.imageView)
        holder.binding.textView2.text = list[position].cate

        holder.itemView.setOnClickListener {
            val intent = Intent(context,CategoryActivity::class.java)
            context.startActivity(intent)
            intent.putExtra("cat",list[position].cate)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}