package com.example.pkart.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductDao {
    @Insert
    suspend fun insertProduct(productModel: ProductModel)
    @Delete
    suspend fun deleteProduct(productModel: ProductModel)
    @Query("SELECT * FROM products")
    fun getAll(): LiveData<List<ProductModel>>
    @Query("SELECT * FROM products where productId = :id")
    fun isExist(id:String) : ProductModel
}