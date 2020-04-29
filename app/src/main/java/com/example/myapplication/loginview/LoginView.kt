package com.example.myapplication.loginview

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentLoginBinding
import com.example.myapplication.util.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.custom_toast_layout.view.*
import kotlinx.android.synthetic.main.fragment_login.*


class LoginView : Fragment() {

    lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel : LoginViewModel
    private lateinit var vi: View
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var loader: ProgressDialog


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //   vi = inflater.inflate(R.layout.fragment_registration_first,container,false)
        binding = DataBindingUtil.inflate<FragmentLoginBinding>(
            inflater, R.layout.fragment_login, container, false)


        sharedPreferences =
            (activity as AppCompatActivity).getSharedPreferences("dd", Context.MODE_PRIVATE)
        val application = requireNotNull(this.activity).application
        val viewModelFactory = LoginViewModelFactory(sharedPreferences, application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)

        binding.viewModel = viewModel


        viewModel.navigateToScreen.observe(viewLifecycleOwner, Observer {
            Log.e("SECONDREG","regist api is called....")
            it?.let {
                if(it==1)
                {
                    Log.e("CHECKDATA","ondata is called")
                    val action = LoginViewDirections.actionLoginViewToRegisterView()
                    NavHostFragment.findNavController(this).navigate(action)
                    viewModel.complete()
                }
                else if(it==2)
                {
                    Log.e("CHECKDATA","ondata is called")
                    val action = LoginViewDirections.actionLoginViewToForgetPasswordView()
                    NavHostFragment.findNavController(this).navigate(action)
                    viewModel.complete()
                }
                else if(it==3)
                {
                    val intent = Intent(activity as AppCompatActivity, HomeViewActivity::class.java)
                    startActivity(intent)
                    (activity as AppCompatActivity).finish()
                }
            }
        })



        binding.emailtext.setOnFocusChangeListener { v, hasFocus ->
            Log.e("ERRORTEXT","error text is called");
            if(!hasFocus){
                binding.emailtext.error=null
            }
        }

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

      /*  // This callback will only be called when MyFragment is at least Started.
        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true *//* enabled by default *//*) {
                override fun handleOnBackPressed() {
                    Log.e("CHECKDATA","ondata is called of the data")
                    // Handle the back button event
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)*/


       /* binding.signupbtn.setOnClickListener {

            showInfoToastWithTypeface(view: View)
        }*/

        viewModel.message.observe(viewLifecycleOwner, Observer {
            it?.let {
                //   Toast.makeText(activity,it.toString(), Toast.LENGTH_SHORT).show()
                //    binding.email.setError("Email is required")
                //   toast(it)
                ValidationUtil.toast(context as AppCompatActivity,it.toString())
             //   toast(it)
//                Log.e("CUSTOMTOAST","custom toast is called")
//                KCustomToast.infoToast(context as AppCompatActivity, "This is a custom info Toast with custom font", KCustomToast.GRAVITY_BOTTOM, ResourcesCompat.getFont(context as AppCompatActivity,R.font.comfortaa_medium))
                //     binding.emailtext.setError("Error message")
                /*  val snackbar = Snackbar
                      .make(toplayout, it.toString(), Snackbar.LENGTH_SHORT)
                  snackbar.view.setBackgroundColor(ContextCompat.getColor(activity!!,R.color.snackbarcolor))
                  snackbar.setActionTextColor(Color.WHITE)
                  snackbar.show()*/
            }
        })
        return  binding.root
    }

   /* fun validateEditText(s:Editable)
    {
        if (!TextUtils.isEmpty(s)) {

 x       }
            layoutEdtPhone.setError(null);
    }*/


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





    fun toast(text:String,teddxt:String)
    {
      /*  val inflater = layoutInflater
        var layout: View  = inflater.inflate(R.layout.custom_toast,context.findViewById(R.id.custom_toast_container));
   *//*     val layout: View = inflater.inflate(
            R.layout.custom_toast,R.id.custom_toast_container as ViewGroup?)*//*
        val tv = layout.findViewById<View>(R.id.txtvw) as TextView
        tv.text = "Custom Toast Notification"*/



       // Toast.makeText(activity,it.toString(), Toast.LENGTH_SHORT).show()
       var toast: Toast = Toast.makeText(activity, text.toString(), Toast.LENGTH_LONG);
         var toastView :View= toast.getView(); // This'll return the default View of the Toast.

/* And now you can get the TextView of the default View of the Toast. */
        var toastMessage:TextView =  toastView.findViewById(android.R.id.message);
        toastMessage.setTextSize(10f);
        toastMessage.setTextColor(Color.BLACK);
        toastMessage.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_launcher, 0, 0, 0);
        toastMessage.setGravity(Gravity.CENTER);
        toastMessage.setCompoundDrawablePadding(18);
        toastView.setBackgroundColor(Color.GREEN);
        toast.show();

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loader = ProgressDialog(view.context)
    }

}
