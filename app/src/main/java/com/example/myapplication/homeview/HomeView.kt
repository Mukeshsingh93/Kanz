package com.example.myapplication.homeview

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.example.myapplication.R
import com.example.myapplication.databinding.HomeviewBinding
import com.example.myapplication.network.ImageModel
import com.example.myapplication.network.OfferProducts
import com.example.myapplication.network.Offers
import com.example.myapplication.network.SliderImages
import com.example.myapplication.util.*
import com.google.android.material.snackbar.Snackbar
import com.raywenderlich.favoritemovies.AdvertiserBannerAdapter
import kotlinx.android.synthetic.main.addtocartscreen.*
import kotlinx.android.synthetic.main.custom_toast_layout.view.*
import kotlinx.android.synthetic.main.homeview.*

class HomeView : Fragment()
{

    lateinit var binding: HomeviewBinding
    private lateinit var viewModel : HomeViewModel
    private lateinit var vi: View
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var pagerAdapter: AdvertiserBannerAdapter
    var imageModel: MutableList<ImageModel>? =  mutableListOf()
    private var dotscount: Int = 0
    lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var loader: ProgressDialog


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
        ): View? {
        binding = DataBindingUtil.inflate<HomeviewBinding>(
            inflater, R.layout.homeview, container, false)
        sharedPreferences =  (activity as AppCompatActivity).getSharedPreferences("dd", Context.MODE_PRIVATE)
        val application = requireNotNull(this.activity).application
        val viewModelFactory = HomeViewModelFactory(sharedPreferences, application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)

        imageModel!!.add(ImageModel("dd","ddd"))
        imageModel!!.add(ImageModel("dd","ddd"))
        imageModel!!.add(ImageModel("dd","ddd"))
        imageModel!!.add(ImageModel("dd","ddd"))
        imageModel!!.add(ImageModel("dd","ddd"))

        viewModel.sliderimg.observe(viewLifecycleOwner, Observer {
            pagerAdapter = AdvertiserBannerAdapter(childFragmentManager, it as  MutableList<SliderImages> )
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

       binding.offerList.adapter = CampainsListAdapter(CampainsListAdapter.OnClickListener
        { offers: Offers, i: Int, i1: Int ->
            Log.e("SELECTED","observer seleted is called");
          //  viewModel.displayProductDetails(it)
            offerProduct(offers)
        })

     /*   val offeradapter = CampainsListAdapter(CampainsListAdapter.OnClickListener { data, type, position ->
            //  Toast.makeText(context, "${nightId}", Toast.LENGTH_LONG).show()
            Log.e("OFFERPRODUCTDETAILS", "click is....$position !! type...$type")
            if (type == 1) {
                offerProduct(data)
            }

        })


        binding.offerList.adapter = offeradapter*/


        binding.viewbtn.setOnClickListener {
//            val action = HomeViewDirections.actionHomeViewToVerificationView()
//            NavHostFragment.findNavController(this).navigate(action)
        }




        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.completerecyclerview.setLayoutManager(linearLayoutManager)
        val adapter = OfferProductAdapter(OnClick { data, type, position ->
            //  Toast.makeText(context, "${nightId}", Toast.LENGTH_LONG).show()
            Log.e("ADDTOCART","click is....$position !! type...$type")
            if(type==1)
            {
                viewModel.addToCart(data,position)
            }
            else if(type==2)
            {
                viewModel.addQuantity(data,position)
            }
            else if(type==3)
            {
                viewModel.subtract(data,position)
            }
            else if(type==4)
            {
                viewModel.addtoWhishList(data,position)
            }
            else if(type==5)
            {
                mainActivity(data)
            }
            else if(type==6)
            {
                    offerProduct(data.offers!!)
            }
        })

        binding.completerecyclerview.adapter = adapter
        viewModel.notifyItem.observe(viewLifecycleOwner, Observer {
            Log.e("ADDQUANTITY","Notify item is called")
            binding.completerecyclerview.adapter?.notifyItemChanged(it)
        })
        viewModel.offerProduct.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        })
        viewModel.addtoCart.observe(viewLifecycleOwner, Observer {
            Log.e("SECONDREG","regist api is called....")
            it?.let {
                if(it==1)
                {
                    val action = HomeViewDirections.actionHomeViewToActivityLogin()
                    NavHostFragment.findNavController(this).navigate(action)
                    viewModel.complete()
               }
            }
        })
       viewModel.navigateRegister.observe(viewLifecycleOwner, Observer {
            Log.e("SECONDREG","regist api is called....")
            it?.let {
                if(it==1)
                {
                    Log.e("CHECKDATA","ondata is called")
                    binding.viewModel = viewModel
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

        binding.drawer.setOnClickListener {
         (activity as HomeViewActivity).slideUp()
        }

        viewModel.message.observe(viewLifecycleOwner, Observer {
            it?.let {
          //    Toast.makeText(activity,it.toString(), Toast.LENGTH_SHORT).show()
                Log.e("ADDTOCART","addtocart screen is called before before....."+it)
                ValidationUtil.toast(context as AppCompatActivity,it.toString())
            }
        })

        viewModel.sessionEx.observe(viewLifecycleOwner,androidx.lifecycle.Observer {
            it?.let {
                if (it == 5) {
                    (activity as HomeViewActivity).logOut()
                }
            }
        })

        viewModel.notifyView.observe(viewLifecycleOwner, Observer {
            binding.viewModel = viewModel
        })


        /*     binding.addcart.setOnClickListener {
                 (activity as HomeViewActivity).addToCart()
             }*/
        binding.viewModel = viewModel
        return  binding.root
    }

    fun mainActivity(offerProduct: OfferProducts)
    {
        val intent = Intent(activity as AppCompatActivity, MainActivity::class.java)
        intent.putExtra("viewType", Constant.PRODUCTDETAIL)
        intent.putExtra(Constant.PRODUCTDETAIL,offerProduct)
        startActivity(intent)
    }

    fun offerProduct(offers: Offers)
    {
        val intent = Intent(activity as AppCompatActivity, MainActivity::class.java)
        intent.putExtra("viewType", Constant.OFFERDETAIL)
        intent.putExtra(Constant.OFFERDETAIL,offers)
        startActivity(intent)
    }

    fun tt()
    {
        var toast: Toast = Toast.makeText(activity, "HELLO OF THE TOAST of the value if tgeer of the value of the new of the gans of the jdfjkjkjdjkfj jkjjhf jkjdhdfh  123456789 nine of the value of the seeen value of the data", Toast.LENGTH_LONG);
        var toastView :View= toast.getView(); // This'll return the default View of the Toast.
        val drawable = ContextCompat.getDrawable(context as AppCompatActivity, R.drawable.toast_round_background)
        var toastMessage: TextView =  toastView.findViewById(android.R.id.message);
        toastMessage.setTextSize(14f);
        toastMessage.setTextColor(Color.WHITE);
        toastMessage.background = drawable
        toastMessage.setGravity(Gravity.CENTER);
        toastMessage.setCompoundDrawablePadding(28);
        toastView.setBackgroundColor(Color.WHITE);
        toast.show();
    }

    fun toast(text:String)
    {
        Log.e("ADDTOCART","addtocart screen is called....."+text)
        val inflater = LayoutInflater.from(context as AppCompatActivity)
        val layout = inflater.inflate(R.layout.custom_toast_layout, (context as AppCompatActivity).findViewById(R.id.custom_toast_layout))
        layout.custom_toast_image.setImageDrawable(ContextCompat.getDrawable(context as AppCompatActivity, R.drawable.ic_home))
        val drawable = ContextCompat.getDrawable(context as AppCompatActivity, R.drawable.toast_round_background)
        drawable?.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(context as AppCompatActivity, R.color.toast),PorterDuff.Mode.MULTIPLY)
        layout.background = drawable
        layout.custom_toast_message.setTextColor(Color.WHITE)
        layout.custom_toast_message.setInputType( InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        layout.custom_toast_message.text = "text is coming of the count of the error is calledd"

        /* font?.let {
             layout.custom_toast_message.typeface = font
         }*/

        val toast = Toast(context as AppCompatActivity)
        toast.duration = Toast.LENGTH_SHORT

       /* if (80 == 80) {
            toast.setGravity(80, 0, 80)
        } else {
            toast.setGravity(80, 0, 0)
        }*/

        toast.view = layout//setting the view of custom toast layout
        toast.show()
        Log.e("ADDTOCART","addtocart screen show show is called....."+text)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loader = ProgressDialog(view.context)
    }
}


