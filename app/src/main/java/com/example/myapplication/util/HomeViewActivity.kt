package com.example.myapplication.util

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.edit
import androidx.core.view.isGone
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.Activity_Login
import com.example.myapplication.R
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.home_view_activity.*
import kotlinx.android.synthetic.main.nav_header.*



class HomeViewActivity : BaseActivity() {
    private lateinit var navController: NavController
    lateinit var sharedPreferences: SharedPreferences
    var navigationView: NavigationView? = null
    var drawerlayout: DrawerLayout?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       /* sharedPreferences = (this).getSharedPreferences(Constant.SHAREDPREFERENCEFILE, Context.MODE_PRIVATE)
        if(sharedPreferences.getString(Constant.PAYMENT,"").equals("COMP"))
        {
        }
        else {
        }*/
        setContentView(R.layout.home_view_activity)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sharedPreferences =  getSharedPreferences("dd", Context.MODE_PRIVATE)
       /* window.apply {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
           // or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }*/
        val navigationView = findViewById(R.id.navigation_view) as NavigationView
         drawerlayout = findViewById(R.id.drawerLayout) as DrawerLayout
        val headerView = navigationView.getHeaderView(0)
        val faq = headerView.findViewById(R.id.faq) as TextView
        val contactus = headerView.findViewById(R.id.contactus) as TextView
        val howitworks = headerView.findViewById(R.id.howitworks) as TextView
        val addtocart = headerView.findViewById(R.id.addtocart) as LinearLayout
        val addtowishlist = headerView.findViewById(R.id.addtowishlist) as LinearLayout
        val logout = headerView.findViewById(R.id.logout) as TextView
        val product = headerView.findViewById(R.id.product) as TextView
        val order = headerView.findViewById(R.id.order) as TextView
        val profile = headerView.findViewById(R.id.profile) as LinearLayout
        val language = headerView.findViewById(R.id.language) as LinearLayout
        val aboutuslayout = headerView.findViewById(R.id.aboutuslayout) as LinearLayout
        var token =sharedPreferences.getString(Constant.TOKEN,"")
        if(token.equals(""))
        {
            logout.text=resources.getString(R.string.login)
        }
        else
        {
            logout.text=resources.getString(R.string.logout)
        }
        faq.setOnClickListener {
            drawerLayout.closeDrawers();
            Handler().postDelayed({
                navView(Constant.FAQ)
            }, 300)
        }
       howitworks.setOnClickListener {
            drawerLayout.closeDrawers();
            Handler().postDelayed({
                howitworks(Constant.FAQ)
            }, 300)
        }

        addtocart.setOnClickListener {
            drawerLayout.closeDrawers();
            Handler().postDelayed({
                var token =sharedPreferences.getString(Constant.TOKEN,"")
                if(token.equals(""))
                {
                    logOut()
                }
                else
                {
                    val intent = Intent(this@HomeViewActivity, MainActivity::class.java)
                    intent.putExtra("viewType", Constant.ADDTOCARTLISTVIEW)
                    startActivity(intent)
                }
            }, 300)
        }
        product.setOnClickListener {
            drawerLayout.closeDrawers();
            Handler().postDelayed({
                val intent = Intent(this@HomeViewActivity, MainActivity::class.java)
                intent.putExtra("viewType", Constant.PRODUCTVIEW)
                startActivity(intent)
            }, 300)
        }

      /*  addtocart.setOnClickListener {
            drawerLayout.closeDrawers();
            Handler().postDelayed({
                val intent = Intent(this@HomeViewActivity, MainActivity::class.java)
                intent.putExtra("viewType", Constant.ADDTOCARTLISTVIEW)
                startActivity(intent)
            }, 300)
        }*/


        logout.setOnClickListener {
            drawerLayout.closeDrawers();
            Handler().postDelayed({
              /* val intent =Intent(this@HomeViewActivity,
                    Activity_Login::class.java);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finishAffinity();*/

                logOut()

            }, 300)
        }

        addtowishlist.setOnClickListener {
            drawerLayout.closeDrawers();
            Handler().postDelayed({


                var token =sharedPreferences.getString(Constant.TOKEN,"")

                if(token.equals(""))
                {
                    logOut()
                }
                else
                {
                    val intent = Intent(this@HomeViewActivity, MainActivity::class.java)
                    intent.putExtra("viewType", Constant.WISHLISTVIEW)
                    startActivity(intent)
                }
            }, 300)
        }

        order.setOnClickListener {
            drawerLayout.closeDrawers();
            Handler().postDelayed({

                var token =sharedPreferences.getString(Constant.TOKEN,"")
                if(token.equals(""))
                {
                    logOut()
                }
                else
                {
                    val intent = Intent(this@HomeViewActivity, MainActivity::class.java)
                    intent.putExtra("viewType", Constant.ORDERVIEW)
                    startActivity(intent)
                }

            }, 300)
        }

        contactus.setOnClickListener {
            drawerLayout.closeDrawers();
            Handler().postDelayed({
                val intent = Intent(this@HomeViewActivity, MainActivity::class.java)
                intent.putExtra("viewType", Constant.CONTACTUS)
                startActivity(intent)
//                setNewLocale(this, LocaleManager.HINDI)
            }, 300)
        }

        language.setOnClickListener {
            drawerLayout.closeDrawers();
            Handler().postDelayed({
                showDialog()
               // setNewLocale(this, LocaleManager.HINDI)
            }, 300)
        }

        aboutuslayout.setOnClickListener {
            val intent = Intent(this@HomeViewActivity, MainActivity::class.java)
            sharedPreferences.edit { putString(Constant.WEBVIEWTYPE,Constant.ABOUTUS)}
            intent.putExtra("viewType", Constant.WEBVIEW)
            startActivity(intent)
        }

        profile.setOnClickListener {
            drawerLayout.closeDrawers();
            Handler().postDelayed({

                var token =sharedPreferences.getString(Constant.TOKEN,"")

                if(token.equals(""))
                {
                    logOut()
                }
                else {
                    val intent = Intent(this@HomeViewActivity, MainActivity::class.java)
                    intent.putExtra("viewType", Constant.PROFILEVIEW)
                    startActivity(intent)
                }
            }, 300)
        }
        navController = Navigation.findNavController(this,
            R.id.fragment
        )
        bottomNav.setupWithNavController(navController)
    }

        fun showDialog() {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.custom_dialogbox_otp)
            val arabic: CardView = dialog.findViewById(R.id.arabic) as CardView
            val english: CardView = dialog.findViewById(R.id.english) as CardView
            dialog.getWindow()!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
            arabic.setOnClickListener {
                setNewLocale(this, LocaleManager.HINDI)
            }
            english.setOnClickListener {
                setNewLocale(this, LocaleManager.ENGLISH)
            }
           dialog.show()
        }

    private fun setNewLocale(mContext: AppCompatActivity, @LocaleManager.LocaleDef language: String) {
        LocaleManager.setNewLocale(this, language)
        Log.e("LANGUAGE","language is......"+language)
        sharedPreferences.edit { putString(Constant.LANGUAGAE,language)}
        val intent = mContext.intent
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    fun navView(title:String)
    {
        val intent = Intent(this@HomeViewActivity, MainActivity::class.java)
        intent.putExtra("viewType", Constant.FAQ)
        startActivity(intent)
    }

    fun howitworks(title:String)
    {
        val intent = Intent(this@HomeViewActivity, MainActivity::class.java)
        sharedPreferences.edit { putString(Constant.WEBVIEWTYPE,Constant.HOWITWORKS)}
        intent.putExtra("viewType", Constant.WEBVIEW)
        startActivity(intent)
    }

    fun logOut()
    {
        sharedPreferences.edit().clear().apply()
        val intent = Intent(this@HomeViewActivity, Activity_Login::class.java);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finishAffinity();
    }

    fun addToCart()
    {
        val intent = Intent(this@HomeViewActivity, MainActivity::class.java)
        intent.putExtra("viewType", Constant.ADDTOCARTLISTVIEW)
        startActivity(intent)
    }

    fun slideUp()
    {
        if(sharedPreferences.getString(Constant.LANGUAGAE,"en").equals("ar"))
        {
            drawerLayout.openDrawer(Gravity.LEFT); //Edit Gravity.START need API 14
        }
        else
        {
            drawerLayout.openDrawer(Gravity.LEFT); //Edit Gravity.START need API 14
        }
        Log.e("ARGUMENT","slide is called called argument is....")
    }

    //Setting Up the back button
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }
}
