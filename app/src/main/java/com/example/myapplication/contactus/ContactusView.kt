package com.example.myapplication.contactus

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
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
import com.example.myapplication.R
import com.example.myapplication.confirmpassword.ConfirmPasswordViewModel
import com.example.myapplication.confirmpassword.ConfirmPasswordViewModelFactory
import com.example.myapplication.databinding.ConfirmpasswordviewBinding
import com.example.myapplication.databinding.ContactUsBinding
import com.example.myapplication.util.ApiStatus
import com.example.myapplication.util.MainActivity
import com.example.myapplication.util.ProgressDialog
import com.example.myapplication.util.ValidationUtil
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.contact_us.*
import kotlinx.android.synthetic.main.custom_toast_layout.view.*
import kotlinx.android.synthetic.main.fragment_register.*

class ContactusView : Fragment() {

    lateinit var binding: ContactUsBinding
    private lateinit var viewModel : ContactusViewModel
    private lateinit var vi: View
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var loader: ProgressDialog


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //   vi = inflater.inflate(R.layout.fragment_registration_first,container,false)
        binding = DataBindingUtil.inflate<ContactUsBinding>(
            inflater, R.layout.contact_us, container, false)

        sharedPreferences =   (activity as AppCompatActivity).getSharedPreferences("dd", Context.MODE_PRIVATE)
        val application = requireNotNull(this.activity).application
        val viewModelFactory = ContactViewModelFactory(sharedPreferences, application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ContactusViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.message.observe(viewLifecycleOwner, Observer {
            it?.let {
              //  Toast.makeText(activity,it.toString(), Toast.LENGTH_SHORT).show()
               /* val snackbar = Snackbar
                    .make(contactustoplayout, it.toString(), Snackbar.LENGTH_SHORT)
                snackbar.view.setBackgroundColor(ContextCompat.getColor(activity!!,R.color.snackbarcolor))
                snackbar.setActionTextColor(Color.WHITE)
                snackbar.show()*/
               // toast(it)
                ValidationUtil.toast(context as AppCompatActivity,it.toString())

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
