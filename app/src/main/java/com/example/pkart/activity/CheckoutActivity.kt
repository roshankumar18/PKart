package com.example.pkart.activity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.pkart.R
import com.example.pkart.databinding.ActivityCheckoutBinding
import com.example.pkart.databinding.ActivityOtpactivityBinding
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject

class CheckoutActivity : AppCompatActivity() ,PaymentResultListener{
    private lateinit var binding: ActivityCheckoutBinding
    private val TAG = "CheckoutActivity"
    private lateinit var checkout: Checkout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Checkout.preload(applicationContext)
        checkout = Checkout()
        checkout.setKeyID("rzp_test_0Do2l6qA0gwmTF")
        startPayment()
    }
    private fun startPayment() {

        try {
            val options = JSONObject()
            options.put("name","Razorpay Corp")
            options.put("description","Demoing Charges")
            //You can omit the image option to fetch the image from the dashboard
            options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
            options.put("theme.color", "#3399cc");
            options.put("currency","INR");
            options.put("order_id", "order_DBJOWzybf0sJbb");
            options.put("amount","50000")//pass amount in currency subunits

//            val retryObj = new JSONObject();
//            retryObj.put("enabled", true);
//            retryObj.put("max_count", 4);
//            options.put("retry", retryObj);

            val prefill = JSONObject()
            prefill.put("email","roshankumar@example.com")
            prefill.put("contact","8802338434")

            options.put("prefill",prefill)
            checkout.open(this,options)
        }catch (e: Exception){
            Log.w(TAG, "startPayment: ${e.message}")
            Toast.makeText(this,"Error in payment: "+ e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        TODO("Not yet implemented")
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "onPaymenterror", Toast.LENGTH_SHORT).show()
    }


}