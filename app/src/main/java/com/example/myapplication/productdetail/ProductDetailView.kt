package com.example.myapplication.productdetail

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityOrderviewBinding
import com.example.myapplication.databinding.ProductDetailviewBinding
import com.example.myapplication.network.OfferProducts
import com.example.myapplication.network.SliderImages
import com.example.myapplication.order.OnClick
import com.example.myapplication.order.OrderListAdapter
import com.example.myapplication.order.OrderViewModel
import com.example.myapplication.order.OrderViewModelFactory
import com.example.myapplication.util.ApiStatus
import com.example.myapplication.util.Constant
import com.example.myapplication.util.ProgressDialog
import com.raywenderlich.favoritemovies.AdvertiserBannerAdapter

class ProductDetailView  : Fragment() {

    lateinit var binding: ProductDetailviewBinding
    private lateinit var viewModel : ProductDetailViewModel
    private lateinit var vi: View
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var loader: ProgressDialog
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var productdetail: OfferProducts
    private lateinit var pagerAdapter: ProductBannerAdapter
    private var dotscount: Int = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        //   vi = inflater.inflate(R.layout.fragment_registration_first,container,false)
        binding = DataBindingUtil.inflate<ProductDetailviewBinding>(
            inflater, R.layout.product_detailview, container, false)
        Log.e("APIRESPONSE","product list api is called called click is....")
        productdetail = arguments?.getSerializable(Constant.PRODUCTDETAIL)!! as OfferProducts
        Log.e("APIRESPONSE","final product detail ..........."+productdetail)
        sharedPreferences =   (activity as AppCompatActivity).getSharedPreferences("dd", Context.MODE_PRIVATE)
        val application = requireNotNull(this.activity).application
        val viewModelFactory = ProductDetailViewModelFactory(sharedPreferences, productdetail,application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ProductDetailViewModel::class.java)
        binding.viewModel = viewModel
        viewModel.sliderimg.observe(viewLifecycleOwner, Observer {
            pagerAdapter = ProductBannerAdapter(childFragmentManager, it as  MutableList<SliderImages> )
            binding.viewpager.adapter = pagerAdapter
            dotscount = pagerAdapter.count
            var dots: Array<ImageView> = Array<ImageView>(dotscount) { index -> ImageView(activity as AppCompatActivity) }
            for (i in 0 until dotscount) {
                dots.set(i, ImageView(activity as AppCompatActivity))
                dots!![i].setImageDrawable(ContextCompat.getDrawable(activity as AppCompatActivity, R.drawable.non_active_dot))
                val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                params.setMargins(8, 0, 8, 0)
                binding.sliderDots.addView(dots!![i], params)
            }
            dots!![0].setImageDrawable(ContextCompat.getDrawable(activity as AppCompatActivity, R.drawable.active_dot))
            binding.viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                }
                override fun onPageSelected(position: Int) {
                    for (i in 0 until dotscount) {
                        dots!![i].setImageDrawable(ContextCompat.getDrawable(activity as AppCompatActivity, R.drawable.non_active_dot))
                    }
                    dots!![position].setImageDrawable(ContextCompat.getDrawable(activity as AppCompatActivity, R.drawable.active_dot))
                }
                override fun onPageScrollStateChanged(state: Int) {
                }
            })
        })

        viewModel.message.observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(activity,it.toString(), Toast.LENGTH_SHORT).show()
            }
        })

        binding.backarrow.setOnClickListener {
          //  Toast.makeText(activity as AppCompatActivity,"Hello",Toast.LENGTH_SHORT).show()
            (activity as AppCompatActivity).onBackPressed()
        }

        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loader = ProgressDialog(view.context)
    }

}