package com.example.myapplication.util

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import com.example.myapplication.Activity_Login
import com.example.myapplication.R
import com.example.myapplication.network.OfferProducts
import com.example.myapplication.network.Offers
import com.example.myapplication.util.LocaleManager.setNewLocale
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_toast_layout.view.*

class MainActivity : BaseActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var productdetail: OfferProducts
    lateinit var offerDetail: Offers


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        val classType = MainActivityArgs.fromBundle(
            intent?.extras!!
        ).viewType
        val navHostFragment = fragment as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.main)
        Log.e("MAINACTIVITY","ondata is called"+classType)
        var bundle :Bundle ?=intent.extras
        var productDetail = bundle!!.getSerializable(Constant.PRODUCTDETAIL) // 1
        var offerDetail = bundle!!.getSerializable(Constant.OFFERDETAIL) // 1
        var allproductDetail = bundle!!.getSerializable(Constant.ALLPRODUCTDETAIL) // 1
        Log.e("MAINACTIVITY","ondata is called........."+productDetail)
        if(classType.equals(Constant.LOGINGVIEW))
        {
            val bundle = Bundle()
            graph.startDestination = (R.id.loginView)
            navHostFragment.navController.setGraph(graph,bundle)
        }
       else if(classType.equals(Constant.PRODUCTDETAIL))
        {
            Log.e("PRODUCTVIEW","product view is called onData......")
            // val marsProperty = Activity_DetailArgs.fromBundle(intent?.extras!!).selectedProduct
            val bundle = Bundle()
            bundle.putSerializable(Constant.PRODUCTDETAIL,productDetail)
            graph.startDestination = (R.id.productDetailView)
            navHostFragment.navController.setGraph(graph,bundle)
            // navController.setGraph(navController.graph,bundle)
        }
        else if(classType.equals(Constant.ALLPRODUCTDETAIL))
        {
            Log.e("PRODUCTVIEW","product view is called onData......")
            val bundle = Bundle()
            bundle.putSerializable(Constant.ALLPRODUCTDETAIL,allproductDetail)
            graph.startDestination = (R.id.allproductDetailView)
            navHostFragment.navController.setGraph(graph,bundle)
        }
        else if(classType.equals(Constant.OFFERDETAIL))
        {
            val bundle = Bundle()
            bundle.putSerializable(Constant.OFFERDETAIL,offerDetail)
            graph.startDestination = (R.id.offerDetailView)
            navHostFragment.navController.setGraph(graph,bundle)
        }
        else if(classType.equals(Constant.FAQ))
        {
            val bundle = Bundle()
            graph.startDestination = (R.id.FAQView)
            navHostFragment.navController.setGraph(graph,bundle)
        }
        else if(classType.equals(Constant.ADDTOCARTLISTVIEW))
        {
            val bundle = Bundle()
            graph.startDestination = (R.id.addtoCartScreen)
            navHostFragment.navController.setGraph(graph,bundle)
        }
        else if(classType.equals(Constant.WEBVIEW))
        {
            val bundle = Bundle()
            graph.startDestination = (R.id.webViewFragment)
            navHostFragment.navController.setGraph(graph,bundle)
        }
        else if(classType.equals(Constant.WISHLISTVIEW))
        {
            Log.e("WISHLISTVIEW","product view is called onData......")
            val bundle = Bundle()
            graph.startDestination = (R.id.whishListView)
            navHostFragment.navController.setGraph(graph,bundle)
        }
        else if(classType.equals(Constant.PRODUCTVIEW))
        {
            val bundle = Bundle()
            graph.startDestination = (R.id.productView)
            navHostFragment.navController.setGraph(graph,bundle)
        }
        else if(classType.equals(Constant.CONTACTUS))
        {
            val bundle = Bundle()
            graph.startDestination = (R.id.contactusView)
            navHostFragment.navController.setGraph(graph,bundle)
        }
        else if(classType.equals(Constant.PROFILEVIEW))
        {
            val bundle = Bundle()
            graph.startDestination = (R.id.profileView)
            navHostFragment.navController.setGraph(graph,bundle)
        }
        else if(classType.equals(Constant.ORDERVIEW))
        {
            val bundle = Bundle()
            graph.startDestination = (R.id.orderView)
            navHostFragment.navController.setGraph(graph,bundle)
        }
       // setNewLocale(this, LocaleManager.HINDI)

    }

    private fun setNewLocale(mContext: AppCompatActivity, @LocaleManager.LocaleDef language: String) {
        LocaleManager.setNewLocale(this, language)
        Log.e("LANGUAGE","language is......"+language)
       // sharedPreferences.edit { putString(Constant.LANGUAGAE,language)}
        val intent = mContext.intent
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
    }



    fun logOut()
    {
        val intent = Intent(this@MainActivity, Activity_Login::class.java);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finishAffinity();
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}
