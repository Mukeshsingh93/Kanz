package com.example.myapplication.verification

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.myapplication.util.HomeViewActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.VerificationviewBinding
import com.example.myapplication.util.ValidationUtil
import kotlinx.android.synthetic.main.custom_toast_layout.view.*

class VerificationView : Fragment(), TextWatcher
{


    lateinit var binding: VerificationviewBinding
    private lateinit var viewModel : VerificationViewModel
    private lateinit var vi: View
    lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //   vi = inflater.inflate(R.layout.fragment_registration_first,container,false)
        val otp = VerificationViewArgs.fromBundle(arguments!!).otp
        Log.e("CHECKDATA","get otp..."+otp.toString())
        binding = DataBindingUtil.inflate<VerificationviewBinding>(
            inflater, R.layout.verificationview, container, false)
        sharedPreferences =
            (activity as AppCompatActivity).getSharedPreferences("dd", Context.MODE_PRIVATE)
        val application = requireNotNull(this.activity).application
        val viewModelFactory = VerificationViewModelFactory(sharedPreferences, otp,application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(VerificationViewModel::class.java)

        binding.edittextone.addTextChangedListener(this);
        binding.edittexttwo.addTextChangedListener(this);
        binding.edittextthree.addTextChangedListener(this);
        binding.edittextfour.addTextChangedListener(this);


        viewModel.message.observe(viewLifecycleOwner, Observer {
            it?.let {
              //  Toast.makeText(activity,it.toString(), Toast.LENGTH_SHORT).show()
               // toast(it)
                ValidationUtil.toast(context as AppCompatActivity,it.toString())

            }
        })


        viewModel.navigateToScreen.observe(viewLifecycleOwner, Observer {
            Log.e("SECONDREG","regist api is called....")
            it?.let {
                if(it==1)
                {
                    /* val action = VerificationViewDirections.actionVerificationViewToHomeView()
                     NavHostFragment.findNavController(this).navigate(action)
                     viewModel.complete()*/

                    val intent = Intent(activity as AppCompatActivity, HomeViewActivity::class.java)
                    startActivity(intent)
                    (activity as AppCompatActivity).finish()

                }
            }
        })

        binding.backarrow.setOnClickListener {

        //   Toast.makeText(activity as AppCompatActivity,"Hello",Toast.LENGTH_SHORT).show()
            (activity as AppCompatActivity).onBackPressed()
        }

        binding.viewModel = viewModel
        return  binding.root
    }

    override fun afterTextChanged(s: Editable?) {

        if (s!!.length == 1) {
            if (binding.edittextone.length() == 1) {
                binding.edittexttwo.requestFocus();
            }

            if (binding.edittexttwo.length() == 1) {
                binding.edittextthree.requestFocus();
            }
            if (binding.edittextthree.length() == 1) {
                binding.edittextfour.requestFocus();
            }
        }

        else if(s!!.length==0) {
            if (binding.edittextfour.length() == 0) {
                binding.edittextthree.requestFocus();
            }
            if (binding.edittextthree.length() == 0) {
                binding.edittexttwo.requestFocus();
            }
            if (binding.edittexttwo.length() == 0) {
               binding.edittextone.requestFocus();
            }
        }

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {


    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


       /* if (binding.edittextone.length() == 1) {
            binding.edittexttwo.requestFocus();
        }

        if ( binding.edittexttwo.length() == 1) {
            binding.edittextthree.requestFocus();
        }
        if (binding.edittextthree.length() == 1) {
            binding.edittextfour.requestFocus();
        }

        if (binding.edittextfour.length() == 0) {
            binding.edittextthree.requestFocus();
        }
        if (binding.edittextthree.length() == 0) {
            binding.edittexttwo.requestFocus();
        }
        if ( binding.edittexttwo.length() == 0) {
            binding.edittextone.requestFocus();
        }*/

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