package com.example.myapplication.userreg

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentRegisterBinding
import com.example.myapplication.util.ApiStatus
import com.example.myapplication.util.ProgressDialog
import com.example.myapplication.util.ValidationUtil
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.custom_toast_layout.view.*
import kotlinx.android.synthetic.main.fragment_register.*


class RegisterView : Fragment()
{

    lateinit var binding: FragmentRegisterBinding
    private lateinit var viewModel : RegisterViewModel
    private lateinit var vi: View
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var loader: ProgressDialog


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //   vi = inflater.inflate(R.layout.fragment_registration_first,container,false)
        binding = DataBindingUtil.inflate<FragmentRegisterBinding>(
            inflater, R.layout.fragment_register, container, false)

        sharedPreferences =
            (activity as AppCompatActivity).getSharedPreferences("dd", Context.MODE_PRIVATE)
        val application = requireNotNull(this.activity).application
        val viewModelFactory = RegisterViewModelFactory(sharedPreferences, application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RegisterViewModel::class.java)

        viewModel.message.observe(viewLifecycleOwner, Observer {
            it?.let {

              //  binding.usernametext.setError("Error message")

             //   Toast.makeText(activity,it.toString(), Toast.LENGTH_SHORT).show()
              /*  val snackbar = Snackbar
                    .make(nestedlayout, it.toString(), Snackbar.LENGTH_SHORT)
                snackbar.view.setBackgroundColor(ContextCompat.getColor(activity!!,R.color.snackbarcolor))
                snackbar.setActionTextColor(Color.WHITE)
                snackbar.show()*/

            //    toast(it)
                ValidationUtil.toast(context as AppCompatActivity,it.toString())



                /*    val sbView = snackbar.view
                    val params =
                        view!!.layoutParams as FrameLayout.LayoutParams
                    params.gravity = Gravity.TOP
                    sbView.setLayoutParams(params);
                    sbView.setBackgroundColor(ContextCompat.getColor(activity as AppCompatActivity, R.color.yellow));*/


            }
        })

        viewModel.navigateVerification.observe(viewLifecycleOwner, Observer {
            Log.e("SECONDREG","regist api is called....")
            it?.let {
                if(it==1)
                {
                  Log.e("CHECKDATA","ondata is called")
                  this.findNavController().navigate(
                        RegisterViewDirections.actionRegisterViewToVerificationView(viewModel.otp.value.toString()))
                    viewModel.complete()
                }
            }
        })

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

        binding.accepttermcondition.setOnClickListener {
            if(binding.accepttermcondition.isChecked)
            {
                viewModel.setTermCondition(true)
            }
            else
            {
                viewModel.setTermCondition(false)
            }
        }

        binding.backarrow.setOnClickListener {
          //  Toast.makeText(activity as AppCompatActivity,"Hello",Toast.LENGTH_SHORT).show()
            (activity as AppCompatActivity).onBackPressed()
        }

        binding.viewModel = viewModel
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