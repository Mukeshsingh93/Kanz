package com.example.myapplication

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.stripe.android.ApiResultCallback
import com.stripe.android.PaymentConfiguration
import com.stripe.android.PaymentIntentResult
import com.stripe.android.Stripe
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.model.StripeIntent
import kotlinx.android.synthetic.main.content_checkout.*
import java.lang.ref.WeakReference

class CardPayment : AppCompatActivity()
{

    private lateinit var paymentIntentClientSecret: String
    private lateinit var stripe: Stripe



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_checkout)
        startCheckout()
    }

    private fun startCheckout() {
        // ...

        // Hook up the pay button to the card widget and stripe instance
        val payButton: Button = findViewById(R.id.payButton)
        payButton.setOnClickListener {
            val params = cardInputWidget.paymentMethodCreateParams
            if (params != null) {
                Log.e("CHECKOUT","params is not not null null nullcheckout is success clicked......")

                val confirmParams = ConfirmPaymentIntentParams
                    .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret)
                stripe = Stripe(applicationContext, PaymentConfiguration.getInstance(applicationContext).publishableKey)
                stripe.confirmPayment(this, confirmParams)
            }
            else{
                Log.e("CHECKOUT","checkout is null null null null success clicked......")

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val weakActivity = WeakReference<Activity>(this)

        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, object : ApiResultCallback<PaymentIntentResult> {
            override fun onSuccess(result: PaymentIntentResult) {
                val paymentIntent = result.intent
                val status = paymentIntent.status
                if (status == StripeIntent.Status.Succeeded) {
                    val gson = GsonBuilder().setPrettyPrinting().create()
                 //   displayAlert(weakActivity.get(), "Payment succeeded", gson.toJson(paymentIntent), restartDemo = true)
                    Log.e("CHECKOUT","checkout is success clicked......"+gson.toString())
                } else {
                  //  displayAlert(weakActivity.get(), "Payment failed", paymentIntent.lastPaymentError?.message ?: "")
                    Log.e("CHECKOUT","checkout last failure payment is clicked......"+ paymentIntent.lastPaymentError?.message )

                }
            }

            override fun onError(e: Exception) {
             //   displayAlert(weakActivity.get(), "Payment failed", e.toString())
            }
        })
    }


}