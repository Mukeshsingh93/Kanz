package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.util.HomeViewActivity
import com.example.myapplication.wishlist.CheckOutActivity

class SplashActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({

            val inten = Intent(this@SplashActivity, HomeViewActivity::class.java)
            startActivity(inten)
            finish()
//            val inten = Intent(this@SplashActivity, CheckOutActivity::class.java)
//            startActivity(inten)
//            finish()
   }, 3000)
    }

}
