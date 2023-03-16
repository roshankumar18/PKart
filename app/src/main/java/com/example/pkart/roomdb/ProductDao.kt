package com.example.pkart.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductDao {
    @Insert
    suspend fun insertProduct(productModelDb: ProductModelDb)
    @Delete
    suspend fun deleteProduct(productModelDB: ProductModelDb)
    @Query("SELECT * FROM products")
    fun getAll(): LiveData<List<ProductModelDb>>
    @Query("SELECT * FROM products WHERE productId = :id")
    suspend fun isExist(id:String) : ProductModelDb
}