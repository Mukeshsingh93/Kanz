package com.example.myapplication.wishlist

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
import com.example.myapplication.databinding.WishlistviewBinding
import com.example.myapplication.util.ApiStatus
import com.example.myapplication.util.MainActivity
import com.example.myapplication.util.ProgressDialog
import com.example.myapplication.util.ValidationUtil
import kotlinx.android.synthetic.main.custom_toast_layout.view.*

class WishlistView : Fragment() {

    lateinit var binding: WishlistviewBinding
    private lateinit var viewModel : WishlistViewModel
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
        binding = DataBindingUtil.inflate<WishlistviewBinding>(
            inflater, R.layout.wishlistview, container, false)
        Log.e("WISHLISTVIEW","product view is called onData wishlist view......")
        sharedPreferences =   (activity as AppCompatActivity).getSharedPreferences("dd", Context.MODE_PRIVATE)
        val application = requireNotNull(this.activity).application
        val viewModelFactory = WishlistViewModelFactory(sharedPreferences, application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(WishlistViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.message.observe(viewLifecycleOwner, Observer {
            it?.let {
              //  Toast.makeText(activity,it.toString(), Toast.LENGTH_SHORT).show()
           //     toast(it)
                ValidationUtil.toast(context as AppCompatActivity,it.toString())
            }
        })

        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.addtocartrecyclerview.setLayoutManager(linearLayoutManager)
        val adapter = WishListAdapter(OnClick { data, type, position ->
            //  Toast.makeText(context, "${nightId}", Toast.LENGTH_LONG).show()
            Log.e("CARTLIST","click is....$position !! type...$type")
            if(type==2)
            {
                Log.e("DELETEITEM","delete item is....."+position)
                viewModel.delete(data,position)
            }

        })

        viewModel.wishlist.observe(viewLifecycleOwner, Observer {
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

        viewModel.notifyView.observe(viewLifecycleOwner, Observer {
            binding.viewModel = viewModel
        })

        viewModel.notifyItemRemove.observe(viewLifecycleOwner, Observer {
            Log.e("ADDIMAGE","notifyItem observer is calledd....")
            binding.addtocartrecyclerview.adapter?.notifyItemRemoved(it!!)
            binding.addtocartrecyclerview.adapter?.notifyItemRangeChanged(it!!,viewModel.wishlist.value!!.size)
            binding.addtocartrecyclerview.setItemAnimator(null);

            //   viewModel.updateposition()
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
