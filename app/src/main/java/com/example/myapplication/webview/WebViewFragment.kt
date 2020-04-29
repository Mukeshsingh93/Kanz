package com.example.myapplication.webview

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.util.Constant

class WebViewFragment : Fragment()
{

    var progressBar: ProgressBar? = null
    lateinit var sharedPreferences: SharedPreferences

    //    internal var url = "http://facebook.com"
     var url = "https://www.hotshelf.com/dev/terms-condiitons"
    var webView: WebView? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view :View =inflater.inflate(R.layout.webviewfragment,container,false)
        sharedPreferences =  (activity as AppCompatActivity).getSharedPreferences("dd", Context.MODE_PRIVATE)
        //  val id = SubCategoryViewArgs.fromBundle(arguments!!).id
      //  val title = WebViewFragmentArgs.fromBundle(arguments!!).title
       if(sharedPreferences.getString(Constant.WEBVIEWTYPE,"").equals(Constant.ABOUTUS))
        {
            url="https://kanz.app/demo/en/about-us-mobile"
        }
        else if(sharedPreferences.getString(Constant.WEBVIEWTYPE,"").equals(Constant.HOWITWORKS))
        {
               url="https://kanz.app/demo/en/how-it-works-mobile"
        }
       else if(sharedPreferences.getString(Constant.WEBVIEWTYPE,"").equals(Constant.TERMCONDITION))
        {
           url="https://kanz.app/demo/how-it-works"
        }
       /* else if(title.equals(Constant.BLOG))
        {
            url="https://www.hotshelf.com/dev/blog"
        }
        else if(title.equals(Constant.DISCLAIMER))
        {
            url="https://www.hotshelf.com/content/disclaimer-policy"
        }
        else if(title.equals(Constant.TERMOFUSE))
        {
            url="https://www.hotshelf.com/content/terms-of-use"
        }
        else if(title.equals(Constant.PRIVACYPOLICY))
        {
            url="https://www.hotshelf.com/content/privacy-policy"
        }
        else if(title.equals(Constant.RETURNCANCELPOLICY))
        {
            url="https://www.hotshelf.com/content/return-and-cancellation-policy"
        }*/

        var wv: WebView? = null
        wv = view.findViewById<WebView>(R.id.webviewlayout)
        progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
     //   wv.webViewClient = myWebClient()
        wv.settings.javaScriptEnabled = true
        wv.settings.builtInZoomControls = true
        wv.settings.displayZoomControls = false
        wv.loadUrl(url)

        wv.setWebViewClient(object : WebViewClient() {

//            override fun shouldOverrideUrlLoading(view: WebView?,request: WebResourceRequest?): Boolean {
//                progressBar!!.visibility = View.VISIBLE
//                view!!.loadUrl(url)
//                return true
//            }
            override fun onPageFinished(view: WebView, url: String) {
                progressBar!!.visibility = View.GONE
                Log.e("PROGRESSBAR","ondata finished is called")
            }
        })
        return view

    }


}


