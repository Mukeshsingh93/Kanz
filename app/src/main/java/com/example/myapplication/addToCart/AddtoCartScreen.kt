package com.example.myapplication.addToCart

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
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.AddtocartscreenBinding
import com.example.myapplication.util.ApiStatus
import com.example.myapplication.util.MainActivity
import com.example.myapplication.util.ProgressDialog
import com.example.myapplication.util.ValidationUtil
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.addtocartscreen.*
import kotlinx.android.synthetic.main.custom_toast_layout.view.*
import kotlinx.android.synthetic.main.fragment_login.*


class AddtoCartScreen : Fragment() {

    lateinit var binding: AddtocartscreenBinding
    private lateinit var viewModel : AddToCartViewModel
    private lateinit var vi: View
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var loader: ProgressDialog
    lateinit var linearLayoutManager: LinearLayoutManager


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //   vi = inflater.inflate(R.layout.fragment_registration_first,container,false)
        binding = DataBindingUtil.inflate<AddtocartscreenBinding>(
            inflater, R.layout.addtocartscreen, container, false)

        sharedPreferences =   (activity as AppCompatActivity).getSharedPreferences("dd", Context.MODE_PRIVATE)
        val application = requireNotNull(this.activity).application
        val viewModelFactory = AddToCartViewModelFactory(sharedPreferences, application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddToCartViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.message.observe(viewLifecycleOwner, Observer {
            it?.let {
             //   Toast.makeText(activity,it.toString(), Toast.LENGTH_SHORT).show()

            //    toast(it)
                ValidationUtil.toast(context as AppCompatActivity,it.toString())

                /*val snackbar = Snackbar
                    .make(addtocarlayout, it.toString(), Snackbar.LENGTH_SHORT)
                snackbar.view.setBackgroundColor(ContextCompat.getColor(activity!!,R.color.snackbarcolor))
                snackbar.setActionTextColor(Color.WHITE)
                snackbar.show()*/

            }
        })

        viewModel.notifyView.observe(viewLifecycleOwner, Observer {
            it?.let {
               if(it==1)
                {
                    binding.viewModel = viewModel
                }
            }
        })

        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.addtocartrecyclerview.setLayoutManager(linearLayoutManager)
        val adapter = AddToCartAdapter(OnClick { data, type, position ->
            //  Toast.makeText(context, "${nightId}", Toast.LENGTH_LONG).show()
            Log.e("CARTLIST","click is....$position !! type...$type")
            if(type==3)
            {

                Log.e("DELETEITEM","delete item is....."+position)
                    viewModel.delete(data,position)
            }
            else if(type==4)
            {
             //   Log.e("CARTLIST","click is....$position !! type...$type")
               // Log.e("CARTLIST","product data......"+data)
                viewModel.updateQuantity(data,position,1)
            }
            else if(type==5)
            {
                viewModel.updateQuantity(data,position,0)
            }
            //  viewModel.deleteImage(data,position)
            //   binding.addToCart.adapter?.notifyItemChanged(1)
            //  activityStart(data.id!!)
        })

        viewModel.offerProduct.observe(viewLifecycleOwner, Observer {
            Log.e("ADDTOPRODUCT","offer product is....."+it.size)
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        })

        binding.addtocartrecyclerview.adapter = adapter
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
            binding.addtocartrecyclerview.adapter?.notifyItemChanged(it!!)
        })

        viewModel.notifyItemRemove.observe(viewLifecycleOwner, Observer {
            Log.e("ADDIMAGE","notifyItem observer is calledd....")

            binding.addtocartrecyclerview.adapter?.notifyItemRemoved(it!!)
            binding.addtocartrecyclerview.adapter?.notifyItemRangeChanged(it!!,viewModel.offerProduct.value!!.size)
            binding.addtocartrecyclerview.setItemAnimator(null);

        })


        binding.donatebutton.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { cb, on ->
            if (on) { //Do something when Switch button is on/checked

            Log.e("DONATE","donate on is called")

                viewModel.donate(1)

            } else { //Do something when Switch is off/unchecked

                Log.e("DONATE","donate off off is called")
                viewModel.donate(0)

            }
        })


        viewModel.sessionEx.observe(viewLifecycleOwner,androidx.lifecycle.Observer {
            it?.let {
                if (it == 5) {
                    (activity as MainActivity).logOut()
                    //   Toast.makeText(this,it.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })


        binding.backarrow.setOnClickListener {
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
