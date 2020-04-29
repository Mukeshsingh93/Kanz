package com.example.myapplication.homeview

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.R
import com.example.myapplication.network.*
import com.example.myapplication.util.ApiStatus
import com.example.myapplication.util.CarzApi
import com.example.myapplication.util.Constant
import com.example.myapplication.util.ValidationUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeViewModel (val sharedPreferences: SharedPreferences,
                     application: Application
) : AndroidViewModel(application)
{

    private val context = getApplication<Application>().applicationContext

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _navigateRegister = MutableLiveData<Int>()
    val navigateRegister : LiveData<Int?>
        get() = _navigateRegister

    // runnning out adapter
    private val _runningproperties = MutableLiveData<List<Offers>>()
    val runningproperties: LiveData<List<Offers>>
        get() = _runningproperties

    // runnning out adapter
    private val _sliderimg = MutableLiveData<List<SliderImages>>()
    val sliderimg: LiveData<List<SliderImages>>
        get() = _sliderimg

    private val _offerProduct = MutableLiveData<List<OfferProducts>>()
    val offerProduct: LiveData<List<OfferProducts>>
        get() = _offerProduct

    var imageModel: MutableList<Offers>? =  mutableListOf()
    var productModel: MutableList<HomeProductModel>? =  mutableListOf()


    private val _message= MutableLiveData<String>()
    val message : LiveData<String?>
        get() = _message

    private val _status = MutableLiveData<ApiStatus>()
    val status : LiveData<ApiStatus?>
        get() = _status

    private val _loadingStatus = MutableLiveData<Int>()
    val loadingStatus : LiveData<Int?>
        get() = _loadingStatus

    private val _norecordfoundStatus = MutableLiveData<Int>()
    val norecordfound : LiveData<Int?>
        get() = _norecordfoundStatus

    private val _addtoCart = MutableLiveData<Int>()
    val addtoCart : LiveData<Int?>
        get() = _addtoCart

    private var _notifyItem = MutableLiveData<Int>()
    val notifyItem: LiveData<Int>
        get() = _notifyItem

    private val _sessionEx = MutableLiveData<Int?>()
    val sessionEx: LiveData<Int?>
        get() = _sessionEx


    private val _cartCount = MutableLiveData<String?>()
    val cartCount: LiveData<String?>
        get() = _cartCount

    private val _notifyView = MutableLiveData<Int>()
    val notifyView : LiveData<Int?>
        get() = _notifyView


    init {
        imageModel!!.add(Offers(1,"ddd"))
        imageModel!!.add(Offers(1,"ddd"))
        imageModel!!.add(Offers(1,"ddd"))
        imageModel!!.add(Offers(1,"ddd"))
        imageModel!!.add(Offers(1,"ddd"))
        productModel!!.add(HomeProductModel("dd"))
        productModel!!.add(HomeProductModel("dd"))
        productModel!!.add(HomeProductModel("dd"))
        productModel!!.add(HomeProductModel("dd"))
      //  _runningproperties.value =  imageModel
            // _product.value =  productModel
        _navigateRegister.value = 0
        _norecordfoundStatus.value=0
        _addtoCart.value=0
       _loadingStatus.value=0

        _cartCount.value="0"
        getHomeApi()
    }

    fun register()
    {
        Log.e("CHECKDATA","ondata is called called")
        _navigateRegister.value = 1
    }


    fun getHomeApi()
    {
        if(!Constant.connected(context))
        {
            _message.value= context.resources.getString(R.string.nointernet)
        }
        else
        {
            _status.value = ApiStatus.LOADING
            _loadingStatus.value=1
            coroutineScope.launch {
                // Get the Deferred object for our Retrofit request

                var token =sharedPreferences.getString(Constant.TOKEN,"")
                Log.e(Constant.APIRESPONSE,"home api token......."+token.toString())

                if(token.equals(""))
                {

                    var getPropertiesDeferred = CarzApi.retrofitService.getHomewitoutauth()
                    try {
                        val response = getPropertiesDeferred.await()

                        Log.e(Constant.APIRESPONSE,"home api response is......"+response.toString())


                        _status.value = ApiStatus.DONE
                        _loadingStatus.value=0
                        if(response.status== Constant.SUCCEESSSTATUS)
                        {
                            Log.e(Constant.APIRESPONSE,"home api response is......"+response.toString())

                            _runningproperties.value =  response.data!!.offers as MutableList<Offers>
                            _offerProduct.value =  response.data!!.offerProducts as MutableList<OfferProducts>
                            _sliderimg.value = response.data!!.sliders!!.get(0).Images as MutableList<SliderImages>
                            Log.e("RESPONSE","runningproperties....."+ (_runningproperties.value as MutableList<Offers>).size)

                            _cartCount.value = response.data!!.cart_count

                            //  _message.value= response.message
                            _navigateRegister.value = 1
                        }
                        else if(response.status== Constant.TOKENEXPIRED)
                        {
                            _message.value= response.message
                            _sessionEx.value = 5
                        }
                        else
                        {
                            _message.value= response.message
                        }

                    } catch (e: Exception) {
                        _loadingStatus.value=0
                        _status.value = ApiStatus.ERROR
                        _message.value= "Api Failure "+e.message
                        Log.e(Constant.APIRESPONSE,"home api response is......"+e.message)

                    }
                }
                else
                {
                    var getPropertiesDeferred = CarzApi.retrofitService.getHome("bearer "+token.toString())
                    try {
                        val response = getPropertiesDeferred.await()

                        Log.e(Constant.APIRESPONSE,"home api response is......"+response.toString())


                        _status.value = ApiStatus.DONE
                        _loadingStatus.value=0
                        if(response.status== Constant.SUCCEESSSTATUS)
                        {
                            Log.e(Constant.APIRESPONSE,"home api response is......"+response.toString())

                            _runningproperties.value =  response.data!!.offers as MutableList<Offers>
                            _offerProduct.value =  response.data!!.offerProducts as MutableList<OfferProducts>
                            _sliderimg.value = response.data!!.sliders!!.get(0).Images as MutableList<SliderImages>
                            Log.e("RESPONSE","runningproperties....."+ (_runningproperties.value as MutableList<Offers>).size)
                            _cartCount.value = response.data!!.cart_count
                            //  _message.value= response.message
                            _navigateRegister.value = 1
                        }
                        else if(response.status== Constant.TOKENEXPIRED)
                        {
                            _message.value= response.message
                            _sessionEx.value = 5
                        }
                        else
                        {
                            _message.value= response.message
                        }
                    } catch (e: Exception) {
                        _loadingStatus.value=0
                        _status.value = ApiStatus.ERROR
                        _message.value= "Api Failure "+e.message
                        Log.e(Constant.APIRESPONSE,"home api response is......"+e.message)

                    }
                }


            }
        }
    }


    fun complete()
    {
        _addtoCart.value = 0
    }

    fun addToCart(offerProduct:OfferProducts,index: Int)
    {
        var item =sharedPreferences.getString(Constant.TOKEN,"")
        var quantity: Int = offerProduct.quantity!!
        Log.e("LOGINVIEW","loginview is...."+item.toString())
        when {
            item!!.toString().equals("") -> _addtoCart.value =1
            else -> addToCartApi(item.toString(), offerProduct.id!!,quantity)
        }
    }

    fun addQuantity(product:OfferProducts,index: Int)
    {
//        val id: Int = product.id!!
//        var prodName: String = product.name!!
//        var minquantity: String = product.description!!
//        var image: String = product.image!!
//        var sider: Slider = product.product!!
        var quantity: Int = product.quantity!!
       Log.e("ADDQUANTITY","quantity is...."+quantity)
        if(quantity==0)
        {
            _message.value="Minimum Quantity to purchase of this product is 1"
            //HUAWEI  P30 Pro
        }
        else
        {
            quantity = quantity.plus(1)
            Log.e("ADDQUANTITY","udate quantity is...."+quantity)
            updatePrice( quantity,index)
            _notifyItem.value=index
        }
    }

    fun subtract(product:OfferProducts,index: Int)
    {
        var quantity: Int = product.quantity!!

        if(quantity==1)
        {
            _message.value="Minimum Quantity to purchase of this product is 1"
        }
        else
        {
            quantity = quantity.minus(1)
            Log.e("ADDQUANTITY","udate quantity is...."+quantity)
            updatePrice( quantity,index)
            _notifyItem.value=index
        }
    }

    fun addtoWhishList(offerProduct:OfferProducts,index: Int)
    {
        var item =sharedPreferences.getString(Constant.TOKEN,"")
        Log.e("LOGINVIEW","loginview is...."+item.toString())
        when {
            item!!.toString().equals("") -> _addtoCart.value =1
            else -> addToWhishListApi(offerProduct, index)
        }
    }

  fun  addToWhishListApi(offerProduct:OfferProducts,index: Int)
    {
        if(!Constant.connected(context))
        {
            _message.value= context.resources.getString(R.string.nointernet)
        }
        else
        {
            var token =sharedPreferences.getString(Constant.TOKEN,"")
            var type:Int=0
            if(offerProduct.in_wishlist==true)
            {
                type=0
            }
            else
            {
                type=1

            }


            _status.value = ApiStatus.LOADING
            _loadingStatus.value=1
            coroutineScope.launch {
                // Get the Deferred object for our Retrofit request

                var getPropertiesDeferred = CarzApi.retrofitService.addtoWishListApi("bearer "+token,
                    offerProduct.id!!.toString(),type)
                try {
                    val response = getPropertiesDeferred.await()

                    Log.e(Constant.APIRESPONSE,"addToWhishListApi api response is......"+response.toString())
                    _status.value = ApiStatus.DONE
                    _loadingStatus.value=0
                    if(response.status== Constant.SUCCEESSSTATUS)
                    {
                        Log.e("RESPONSE","email.......pin..."+response.toString())

                        if(type==1)
                        {
                            updateQuantity(true,index)
                        }
                        else
                        {
                            updateQuantity(false,index)
                        }
                        _notifyItem.value = index
                        _message.value= response.message

                    }
                    else if(response.status== Constant.TOKENEXPIRED)
                    {
                        _message.value= response.message
                        _sessionEx.value = 5
                    }
                    else
                    {
                        _message.value= response.message
                    }

                } catch (e: Exception) {

                    Log.e(Constant.APIRESPONSE,"addToWhishListApi api response is......"+e.message)
                    _loadingStatus.value=0
                    _status.value = ApiStatus.ERROR
                    _message.value= "Api Failure "+e.message
                }
            }
        }
    }

    fun updatePrice( quantity: Int,index:Int)
    {
        _offerProduct.value!!.get(index).quantity=quantity
        _offerProduct.value=  _offerProduct.value
    }


    fun addToCartApi(token:String,id:Int,quantity:Int)
    {
        if(!Constant.connected(context))
        {
            _message.value= context.resources.getString(R.string.nointernet)
        }
        else
        {
            _status.value = ApiStatus.LOADING
            _loadingStatus.value=1
            coroutineScope.launch {
                // Get the Deferred object for our Retrofit request
                Log.e("APIRESPONSE","email.......pin..."+token)

                var getPropertiesDeferred = CarzApi.retrofitService.addtoCartApi("bearer "+token,
                    id.toString(),quantity.toString())
                try {
                    val response = getPropertiesDeferred.await()

                    Log.e(Constant.APIRESPONSE,"addtocart api response is......"+response.toString())
                    _status.value = ApiStatus.DONE
                    _loadingStatus.value=0

                    if(response.status== Constant.SUCCEESSSTATUS)
                    {
                        Log.e("RESPONSE","email.......pin..."+response.toString())
                         _message.value= response.message
                        _cartCount.value = response!!.cart_count
                        Log.e("NOTIFYVALUE","notifyvalue...."+ response!!.cart_count)
                        _notifyView.value=0
                    }
                    else if(response.status== Constant.TOKENEXPIRED)
                    {
                        _message.value= response.message
                        _sessionEx.value = 5
                    }
                    else
                    {
                        _message.value= response.message
                    }
                } catch (e: Exception) {
                    _loadingStatus.value=0
                    _status.value = ApiStatus.ERROR
                    _message.value= "Api Failure "+e.message
                    Log.e(Constant.APIRESPONSE,"addtocart api response is......"+e.message.toString())
               }
            }
        }
    }

    fun updateQuantity(like:Boolean, index: Int)
    {
        _offerProduct.value!!.get(index).in_wishlist=like
        _offerProduct.value=  _offerProduct.value
    }



}