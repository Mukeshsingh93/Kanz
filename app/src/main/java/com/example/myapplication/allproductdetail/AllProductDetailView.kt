package com.example.myapplication.allproductdetail

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
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
import com.example.myapplication.databinding.AllproductDetailviewBinding
import com.example.myapplication.databinding.ProductDetailviewBinding
import com.example.myapplication.network.OfferProducts
import com.example.myapplication.network.Product
import com.example.myapplication.network.SliderImages
import com.example.myapplication.productdetail.ProductBannerAdapter
import com.example.myapplication.productdetail.ProductDetailViewModel
import com.example.myapplication.productdetail.ProductDetailViewModelFactory
import com.example.myapplication.util.Constant
import com.example.myapplication.util.ProgressDialog
import com.example.myapplication.util.ValidationUtil
import kotlinx.android.synthetic.main.custom_toast_layout.view.*

class AllProductDetailView : Fragment() {

    lateinit var binding: AllproductDetailviewBinding
    private lateinit var viewModel : AllProductDetailViewModel
    private lateinit var vi: View
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var loader: ProgressDialog
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var productdetail: Product
    private lateinit var pagerAdapter: ProductBannerAdapter
    private var dotscount: Int = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        //   vi = inflater.inflate(R.layout.fragment_registration_first,container,false)
        binding = DataBindingUtil.inflate<AllproductDetailviewBinding>(
            inflater, R.layout.allproduct_detailview, container, false)
        Log.e("APIRESPONSE","product list api is called called click is....")

        productdetail = arguments?.getSerializable(Constant.ALLPRODUCTDETAIL)!! as Product

        Log.e("APIRESPONSE","final product detail ..........."+productdetail)

        sharedPreferences =   (activity as AppCompatActivity).getSharedPreferences("dd", Context.MODE_PRIVATE)
        val application = requireNotNull(this.activity).application
        val viewModelFactory = AllProductDetailViewModelFactory(sharedPreferences, productdetail,application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AllProductDetailViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.sliderimg.observe(viewLifecycleOwner, Observer {
            Log.e("CHECKDATA","it it"+it.size)
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
               // Toast.makeText(activity,it.toString(), Toast.LENGTH_SHORT).show()
               // toast(it)
                ValidationUtil.toast(context as AppCompatActivity,it.toString())

            }
        })

        binding.backarrow.setOnClickListener {
            //  Toast.makeText(activity as AppCompatActivity,"Hello",Toast.LENGTH_SHORT).show()
            (activity as AppCompatActivity).onBackPressed()
        }

        return  binding.root

    }

    fun toast(text:String)
    {
        val inflater = LayoutInflater.from(context as AppCompatActivity)
        val layout = inflater.inflate(R.layout.custom_toast_layout, (context as AppCompatActivity).findViewById(R.id.custom_toast_layout))
        layout.custom_toast_image.setImageDrawable(ContextCompat.getDrawable(context as AppCompatActivity, R.drawable.ic_home))
        val drawable = ContextCompat.getDrawable(context as AppCompatActivity, R.drawable.toast_round_background)
        drawable?.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(context as AppCompatActivity, R.color.toast), PorterDuff.Mode.MULTIPLY)
        layout.background = drawable
        layout.custom_toast_message.setTextColor(Color.WHITE)
        layout.custom_toast_message.text = text
        /* font?.let {
             layout.custom_toast_message.typeface = font
         }*/
        val toast = Toast(context as AppCompatActivity)
        toast.duration = Toast.LENGTH_SHORT
        if (80 == 80) {
            toast.setGravity(80, 0, 80)
        } else {
            toast.setGravity(80, 0, 0)
        }
        toast.view = layout//setting the view of custom toast layout
        toast.show()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loader = ProgressDialog(view.context)
    }

}