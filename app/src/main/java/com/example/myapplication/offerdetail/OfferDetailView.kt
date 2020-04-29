package com.example.myapplication.offerdetail

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
import com.example.myapplication.databinding.OfferdetailviewBinding
import com.example.myapplication.databinding.ProductDetailviewBinding
import com.example.myapplication.network.OfferProducts
import com.example.myapplication.network.Offers
import com.example.myapplication.util.Constant
import com.example.myapplication.util.ProgressDialog
import com.example.myapplication.util.ValidationUtil
import kotlinx.android.synthetic.main.custom_toast_layout.view.*

class OfferDetailView : Fragment() {

    lateinit var binding: OfferdetailviewBinding
    private lateinit var viewModel : OfferDetailViewModel
    private lateinit var vi: View
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var loader: ProgressDialog
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var offerDetail: Offers
    private var dotscount: Int = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        //   vi = inflater.inflate(R.layout.fragment_registration_first,container,false)
        binding = DataBindingUtil.inflate<OfferdetailviewBinding>(
            inflater, R.layout.offerdetailview, container, false)
       // Log.e("APIRESPONSE","product list api is called called click is....")

        offerDetail = arguments?.getSerializable(Constant.OFFERDETAIL)!! as Offers

        Log.e("APIRESPONSE","final product detail ..........."+offerDetail)

        sharedPreferences =   (activity as AppCompatActivity).getSharedPreferences("dd", Context.MODE_PRIVATE)
        val application = requireNotNull(this.activity).application
        val viewModelFactory = OfferDetailViewModelFactory(sharedPreferences, offerDetail,application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(OfferDetailViewModel::class.java)
        binding.viewModel = viewModel





        viewModel.message.observe(viewLifecycleOwner, Observer {
            it?.let {
             //   Toast.makeText(activity,it.toString(), Toast.LENGTH_SHORT).show()
            //    toast(it)
                ValidationUtil.toast(context as AppCompatActivity,it.toString())

            }
        })

        binding.backarrow.setOnClickListener {
            (activity as AppCompatActivity).onBackPressed()
        }
        return  binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loader = ProgressDialog(view.context)
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

}