package com.example.myapplication.order

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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityOrderviewBinding
import com.example.myapplication.databinding.ProductviewBinding
import com.example.myapplication.product.ProductListAdapter
import com.example.myapplication.product.ProductViewModel
import com.example.myapplication.product.ProductViewModelFactory
import com.example.myapplication.util.ApiStatus
import com.example.myapplication.util.ProgressDialog
import com.example.myapplication.util.ValidationUtil
import kotlinx.android.synthetic.main.custom_toast_layout.view.*

class OrderView : Fragment() {

    lateinit var binding: ActivityOrderviewBinding
    private lateinit var viewModel : OrderViewModel
    private lateinit var vi: View
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var loader: ProgressDialog
    lateinit var linearLayoutManager: LinearLayoutManager


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        //   vi = inflater.inflate(R.layout.fragment_registration_first,container,false)
        binding = DataBindingUtil.inflate<ActivityOrderviewBinding>(
            inflater, R.layout.activity_orderview, container, false)
        sharedPreferences =   (activity as AppCompatActivity).getSharedPreferences("dd", Context.MODE_PRIVATE)
        val application = requireNotNull(this.activity).application
        val viewModelFactory = OrderViewModelFactory(sharedPreferences, application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(OrderViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.message.observe(viewLifecycleOwner, Observer {
            it?.let {
            //    Toast.makeText(activity,it.toString(), Toast.LENGTH_SHORT).show()
            //    toast(it)
                ValidationUtil.toast(context as AppCompatActivity,it.toString())

            }
        })

        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.productrecylerview.setLayoutManager(linearLayoutManager)
        val adapter = OrderListAdapter(OnClick { data, type, position ->
            //  Toast.makeText(context, "${nightId}", Toast.LENGTH_LONG).show()
            Log.e("CARTLIST","click is....$position !! type...$type")
            if(type==2)
            {
                Log.e("DELETEITEM","delete item is....."+position)
                //  viewModel.delete(data,position)
            }
        })

        viewModel.GetCartListlist.observe(viewLifecycleOwner, Observer {
            Log.e("ADDTOPRODUCT","offer product is....."+it.size)
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        })

        binding.productrecylerview.adapter = adapter
        viewModel.status.observe(viewLifecycleOwner, Observer {
            when (it) {
                ApiStatus.LOADING -> {
                    loader.setLoading(true)
                }
                ApiStatus.ERROR -> {
                    loader.setLoading(false)
                }
                ApiStatus.DONE -> {
                    loader.setLoading(false)
                }
            }
        })

        viewModel.notifyItem.observe(viewLifecycleOwner, Observer {
            binding.productrecylerview.adapter?.notifyItemChanged(it!!)
        })

        viewModel.notifyItemRemove.observe(viewLifecycleOwner, Observer {
            Log.e("ADDIMAGE","notifyItem observer is calledd....")
//            binding.productrecylerview.adapter?.notifyItemRemoved(it!!)
//            binding.productrecylerview.adapter?.notifyItemRangeChanged(it!!,viewModel.productlist.value!!.size)
//            binding.productrecylerview.setItemAnimator(null);

            //   viewModel.updateposition()
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
