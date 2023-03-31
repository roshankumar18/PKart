package com.example.pkart.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.pkart.MainActivity
import com.example.pkart.R
import com.example.pkart.roomdb.AppDatabase
import com.example.pkart.roomdb.ProductModelDb
import com.google.android.material.tabs.TabLayout.TabGravity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.razorpay.Checkout
import com.razorpay.ExternalWalletListener
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class PaymentActivity : AppCompatActivity(), PaymentResultWithDataListener,
    ExternalWalletListener{
    private lateinit var co :Checkout
    private  var price:Int =0
    private var TAG= "PaymentActivity"
    private lateinit var productIds:ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        Checkout.preload(applicationContext)
        co = Checkout()
        co.setKeyID("rzp_test_0Do2l6qA0gwmTF")
        Checkout.sdkCheckIntegration(this)
        productIds = ArrayList()
        productIds.clear()
//        productIds.add(intent.getStringArrayListExtra("productIds").toString())
        productIds = intent.getStringArrayListExtra("productIds") as ArrayList<String>
        Log.w(TAG, "onCreate: $productIds", )
        price = intent.getStringExtra("totalCost")!!.toInt()
        startPayment()
    }

    private fun startPayment() {
        /*
        *  You need to pass the current activity to let Razorpay create CheckoutActivity
        * */
        val activity: Activity = this

        try {
            val options = JSONObject()
            options.put("name","Razorpay Corp")
            options.put("description","Demoing Charges")
            //You can omit the image option to fetch the image from the dashboard
            options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
            options.put("theme.color", "#3399cc");
            options.put("currency","INR");
//            options.put("order_id", "order_DBJOWzybf0sJbb");
            Log.w(TAG, "startPayment: ${price::class.java.typeName},${price}", )
            options.put("amount",price*100)//pass amount in currency subunits

            val prefill = JSONObject()
            prefill.put("email","gaurav.kumar@example.com")
            prefill.put("contact","9876543210")

            options.put("prefill",prefill)
            co.open(activity,options)
        }catch (e: Exception){
            Log.w(TAG, "startPayment: ${e.message}")
            Toast.makeText(activity,"Error in payment: "+ e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }
    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        try {
            Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show()
            uploadData()
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
        catch (exception:Exception){
            Log.w(TAG, "onPaymentSuccess: ${exception.message}", )
        }
    }

    private fun uploadData() {
        Log.w(TAG, "uploadData: IN upload data method", )
        val id = productIds

//        Toast.makeText(this, "products ${id?.size}", Toast.LENGTH_SHORT).show()
        Log.w(TAG, "uploadData: ${id.size}", )
        for (current in id){
            fetchData(current)
        }

    }

    private fun fetchData(productId: String?) {
        val dao = AppDatabase.getDatabase(this).productDao()
        Firebase.firestore.collection("products").document(productId!!)
            .get().addOnSuccessListener {
                val db = ProductModelDb(
                    productId,
                    it.getString("productName"),
                    it.getString("productCoverImg"),
                    it.getString("productSp")
                )
                GlobalScope.launch(Dispatchers.IO) {
                    dao.deleteProduct(db)
                }
                Log.w(TAG, "fetchData: Ended", )
                saveData(it.getString("productCoverImg"),
                    it.getString("productSp"),productId,
                    it.getString("productName"))
            }
    }

    private fun saveData(coverImg: String?, productSp: String?, productId: String, productName: String?) {
        val preference = this.getSharedPreferences("user", MODE_PRIVATE)
        val data = hashMapOf<String,Any>()
        data["name"]= productName!!
        data["image"]=coverImg!!
        data["id"]= productId
        data["sp"]= productSp!!
        data["status"]="Ordered"
        data["userId"] = preference.getString("number","")!!
        Log.w(TAG, "saveData: In Save data", )
        val firestore = Firebase.firestore.collection("allOrders")
        val key = firestore.document().id
        data["orderId"]=key
        firestore.document(key).set(data).addOnSuccessListener {
            Log.w(TAG, "saveData: In Firestore", )
            Log.w(TAG, "saveData: $data", )
            Toast.makeText(this, "Order Placed", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Order Failed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        Toast.makeText(this, "Payment error", Toast.LENGTH_SHORT).show()
    }

    override fun onExternalWalletSelected(p0: String?, p1: PaymentData?) {
        TODO("Not yet implemented")
    }
}