package com.example.pkart.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductModel(
    @PrimaryKey val productId :String,
    val productName : String? ="",
    val productImage : String? = "",
    val productSp :  String? =""
)
