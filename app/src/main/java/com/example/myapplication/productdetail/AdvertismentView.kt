package com.example.myapplication.productdetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.myapplication.R
import com.example.myapplication.homeview.Advertisement
import com.example.myapplication.network.ImageModel
import com.example.myapplication.network.Slider
import com.example.myapplication.network.SliderImages
import kotlinx.android.synthetic.main.fragment_banner.*
import java.io.Serializable

class AdvertismentView : Fragment() {

    lateinit var advertisementlist :MutableList<Advertisement>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState:
    Bundle?): View? {
        val view = inflater.inflate(R.layout.product_detailbanner, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = arguments?.getSerializable("VALUE")
       var response :SliderImages = data as SliderImages
           Glide.with(view.context)
                .load("https://kanz.app/demo/"+response.image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(advertisementImg)
        Log.e("ADVERTISEMENT","name is the value..."+data.toString())
    }

    companion object {
       fun newInstance(movies: SliderImages): AdvertismentView {
            val args = Bundle()
         //   args.putString("VALUE", movie)
            args.putSerializable("VALUE", movies as Serializable)
            val fragment = AdvertismentView()
            fragment.arguments = args
            return fragment
        }
    }

}



