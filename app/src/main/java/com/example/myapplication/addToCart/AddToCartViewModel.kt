package com.example.myapplication.addToCart

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
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

class AddToCartViewModel (val sharedPreferences: SharedPreferences,
                          application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _navigateRegister = MutableLiveData<Int>()
    val navigateRegister: LiveData<Int?>
        get() = _navigateRegister

    private val _email = MutableLiveData<String>()
    val email: LiveData<String?>
        get() = _email

    private val _pin = MutableLiveData<String>()
    val pin: LiveData<String?>
        get() = _pin

    private val _message = MutableLiveData<String>()
    val message: LiveData<String?>
        get() = _message

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus?>
        get() = _status


    private val _loadingStatus = MutableLiveData<Int>()
    val loadingStatus : LiveData<Int?>
        get() = _loadingStatus

    private val _notifyItem = MutableLiveData<Int>()
    val notifyItem : LiveData<Int?>
        get() = _notifyItem

    private val _notifyItemRemove = MutableLiveData<Int>()
    val notifyItemRemove : LiveData<Int?>
        get() = _notifyItemRemove

    private val _offerProduct = MutableLiveData<List<GetCartList>>()
    val offerProduct: LiveData<List<GetCartList>>
        get() = _offerProduct


    private val _notifyView = MutableLiveData<Int>()
    val notifyView : LiveData<Int?>
        get() = _notifyView

    /*private val _viewVisible = MutableLiveData<String?>()
    val viewVisible: LiveData<String?>
        get() = _viewVisible*/

    private val _totalquantity = MutableLiveData<String?>()
    val totalquantity: LiveData<String?>
        get() = _totalquantity

    private val _totalticket = MutableLiveData<String?>()
    val totalticket: LiveData<String?>
        get() = _totalticket

    private val _totalprice = MutableLiveData<String?>()
    val totalprice: LiveData<String?>
        get() = _totalprice


    private val _promoCode = MutableLiveData<String>()
    val promoCode : LiveData<String?>
        get() = _promoCode

    private val _sessionEx = MutableLiveData<Int?>()
    val sessionEx: LiveData<Int?>
        get() = _sessionEx

    var productModel: MutableList<GetCartList>? =  mutableListOf()

    private val _norecordfoundStatus = MutableLiveData<Int>()
    val norecordfound : LiveData<Int?>
        get() = _norecordfoundStatus

    private val _cartView = MutableLiveData<Int>()
    val cartView : LiveData<Int?>
        get() = _cartView

    init {
        _navigateRegister.value = 0
        _email.value = ""
        _pin.value = ""
        _totalquantity.value=""
        _totalticket.value=""
        _totalprice.value=""
        _promoCode.value=""

        _sessionEx.value =0
        _norecordfoundStatus.value=0
        _cartView.value=0

//        productModel!!.add(GetCartList(1,"dd"))
//        productModel!!.add(GetCartList(2,"dd"))
//        productModel!!.add(GetCartList(3,"dd"))
//        productModel!!.add(GetCartList(4,"dd"))
//       _offerProduct.value =  productModel
        getCartListing()
    }


    fun promoCode()
    {
        when {
            _promoCode.value!!.isEmpty() -> _message.value = "The promo code field is required."
            else -> promocodeApi()
        }
    }


    fun onTextChangedPromoCode(s: CharSequence, start: Int, before: Int, count: Int) {
        if(!s.toString().isEmpty())
        {
            _promoCode.value= s.toString()
        }
        else
        {
            _promoCode.value=""
        }
    }



    fun promocodeApi()
    {
        if(!Constant.connected(context))
        {
            _message.value= context.resources.getString(R.string.nointernet)
        }
        else
        {
            var token =sharedPreferences.getString(Constant.TOKEN,"")
            _status.value = ApiStatus.LOADING
            _loadingStatus.value=1
            coroutineScope.launch {
                // Get the Deferred object for our Retrofit request
                Log.e("CARTLIST","token..."+token)
                var getPropertiesDeferred = CarzApi.retrofitService.promocode("bearer "+token,_promoCode.value.toString())

                try {
                    val response = getPropertiesDeferred.await()
                    _status.value = ApiStatus.DONE
                    _loadingStatus.value=0
                    Log.e(Constant.APIRESPONSE,"cartlist api response is......"+response.toString())
                    if(response.status== Constant.SUCCEESSSTATUS)
                    {
                        Log.e("RESPONSE","email.......pin..."+response.toString())
                        _totalticket.value = response.tickets_numbers
                        _notifyView.value=1
                    }
                    else
                    {
                        _message.value= response.message
                    }
                } catch (e: Exception) {
                    Log.e(Constant.APIRESPONSE,"cartlist api failure is......"+e.toString())
                    _loadingStatus.value=0
                    _status.value = ApiStatus.ERROR
                    _message.value= "Api Failure "+e.message
                }
            }
        }
    }

    fun donate(type:Int)
    {
        if(!Constant.connected(context))
        {
            _message.value= context.resources.getString(R.string.nointernet)
        }
        else
        {
            var token =sharedPreferences.getString(Constant.TOKEN,"")
            _status.value = ApiStatus.LOADING
            _loadingStatus.value=1
            coroutineScope.launch {
                // Get the Deferred object for our Retrofit request
                Log.e("CARTLIST","token..."+token)

                var getPropertiesDeferred = CarzApi.retrofitService.donate("bearer "+token,type)
                try {
                    val response = getPropertiesDeferred.await()
                    _status.value = ApiStatus.DONE
                    _loadingStatus.value=0
                    Log.e(Constant.APIRESPONSE,"cartlist api response is......"+response.toString())
                    if(response.status== Constant.SUCCEESSSTATUS)
                    {
                        Log.e("RESPONSE","email.......pin..."+response.toString())
                       _totalticket.value = response.tickets_numbers
                       _notifyView.value=1

                    }
                    else
                    {
                        _message.value= response.message
                    }
                } catch (e: Exception) {
                    Log.e(Constant.APIRESPONSE,"cartlist api failure is......"+e.toString())
                    _loadingStatus.value=0
                    _status.value = ApiStatus.ERROR
                    _message.value= "Api Failure "+e.message
                }
            }
        }


    }


    fun complete() {
        _navigateRegister.value = 0
    }




    fun delete(product:GetCartList,index: Int)
    {
        var cartId:String= product.id.toString()

        if(!Constant.connected(context))
        {
            _message.value= context.resources.getString(R.string.nointernet)
        }
        else
        {
            var token =sharedPreferences.getString(Constant.TOKEN,"")
            _status.value = ApiStatus.LOADING
            _loadingStatus.value=1
            coroutineScope.launch {
                // Get the Deferred object for our Retrofit request
                Log.e("RESPONSE","email.......pin...")
                var getPropertiesDeferred = CarzApi.retrofitService.deleteCart("bearer "+token,cartId)
                try {
                    val response = getPropertiesDeferred.await()
                    _status.value = ApiStatus.DONE
                    _loadingStatus.value=0

                    Log.e("RESPONSE","email.......pin..."+response.toString())

                    if(response.status== Constant.SUCCEESSSTATUS)
                    {
                        Log.e("RESPONSE","email.......pin..."+response.toString())
                        //  Log.e("RESPONSE","runningproperties....."+ (_runningproperties.value as MutableList<Offers>).size)
                        _message.value= response.message
                        _navigateRegister.value = 1



                        Log.e("DELETEITEM", "before product size product position..." + productModel!!.size)


                        var positon: Int = 0
                        for (i in 0 until offerProduct!!.value!!.size) {
                            Log.e("DELETEITEM", "product position..." + product.id.toString())
                            Log.e("DELETEITEM", "for for image position..." + offerProduct!!.value!!.get(i).id)
                            if (offerProduct!!.value!!.get(i).id!!.equals(product.id)) {
                                positon = i
                                Log.e("DELETEITEM", "before product position..." + positon)

                            }
                        }
                        Log.e("DELETEITEM", "final product position..." + positon)
                        productModel!!.removeAt(positon)
                        Log.e("DELETEITEM", "after product size product position..." + productModel!!.size)
                        _notifyItemRemove.value = positon

                        updateList()
                    }
                    else
                    {
                        _message.value= response.message
                    }
                } catch (e: Exception) {

                    Log.e("RESPONSE","email.......pin..."+e.toString())
                    _loadingStatus.value=0
                    _status.value = ApiStatus.ERROR
                    _message.value= "Api Failure "+e.message
                }
            }
        }
    }

    fun updateList()
    {
        if(_offerProduct.value!!.size<=0)
        {
            _norecordfoundStatus.value=1
            _cartView.value =0
            _notifyView.value=1
        }
    }


    fun updateQuantity(product:GetCartList,index: Int,type:Int)
    {

        var quantity:String= product.quantity.toString()
        var updateQuantity:Int= 0

        Log.e("CARTLIST","quantity before....."+product.quantity.toString())
        Log.e("CARTLIST","quantity before....."+quantity.toString())

        if(type==0)
        {
            updateQuantity=  quantity.toInt().minus(1)
            Log.e("CARTLIST","after minus quantity..."+updateQuantity)
        }
        else if(type==1)
        {
            updateQuantity=   quantity.toInt().plus(1)
            Log.e("CARTLIST","after quantity..."+updateQuantity)
        }
        Log.e("RESPONSE","quantity....."+quantity.toString())
        Log.e("CARTLIST","after quantity..."+quantity)
        var productId:String= product!!.id.toString()

        if(!Constant.connected(context))
        {
            _message.value= context.resources.getString(R.string.nointernet)
        }
        else
        {
            var token =sharedPreferences.getString(Constant.TOKEN,"")
            _status.value = ApiStatus.LOADING
            _loadingStatus.value=1
            coroutineScope.launch {
                // Get the Deferred object for our Retrofit request
                Log.e("CARTLIST","token..."+token)
                Log.e("CARTLIST","productid..."+productId)
                Log.e("CARTLIST","quantity..."+quantity)

                var getPropertiesDeferred = CarzApi.retrofitService.updateQuantity("bearer "+token,productId,updateQuantity )
                try {
                    val response = getPropertiesDeferred.await()
                    _status.value = ApiStatus.DONE
                    _loadingStatus.value=0

                    Log.e("RESPONSE","email.......pin..."+response.toString())

                    if(response.status== Constant.SUCCEESSSTATUS)
                    {
                        Log.e("RESPONSE","email.......pin..."+response.toString())
                        //  Log.e("RESPONSE","runningproperties....."+ (_runningproperties.value as MutableList<Offers>).size)
                        _message.value= response.message

                        updateQuantity(updateQuantity.toString(),index)
                        _notifyItem.value = index
                        _notifyView.value=1
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

                    Log.e(Constant.APIRESPONSE,"Updatequantity api response is......"+e.toString())

                    _loadingStatus.value=0
                    _status.value = ApiStatus.ERROR
                    _message.value= "Api Failure "+e.message
                }
            }
        }
    }

    fun updateQuantity(quantity:String, index: Int)
    {
        _offerProduct.value!!.get(index).quantity=quantity
        _offerProduct.value!!.get(index).coupon_numbers=quantity
        _offerProduct.value=  _offerProduct.value

        _totalquantity.value=   totalQuantity(_offerProduct.value!!)
        _totalticket.value=   totalCoupon(_offerProduct.value!!)
        _totalprice.value=   totalPrice(_offerProduct.value!!)

    }



    fun getCartListing()
    {
        if(!Constant.connected(context))
        {
            _message.value= context.resources.getString(R.string.nointernet)
        }
        else
        {

            var token =sharedPreferences.getString(Constant.TOKEN,"")
            _loadingStatus.value=1
            coroutineScope.launch {
                // Get the Deferred object for our Retrofit request
                Log.e("RESPONSE","email.......pin...")
                var getPropertiesDeferred = CarzApi.retrofitService.getCart("bearer "+token)

                try {
                    val response = getPropertiesDeferred.await()
                    _loadingStatus.value=0

                    Log.e(Constant.APIRESPONSE,"GetCart api response is......"+response.toString())

                    if(response.status== Constant.SUCCEESSSTATUS)
                    {
                        _offerProduct.value =  response.cartlist!! as MutableList<GetCartList>
                               //  Log.e("RESPONSE","runningproperties....."+ (_runningproperties.value as MutableList<Offers>).size)

                        Log.e(Constant.APIRESPONSE,"GetCart size is api response is......"+_offerProduct.value!!.size)

                        if(_offerProduct.value!!.size<=0)
                        {
                            _norecordfoundStatus.value=1
                            _cartView.value =0

                        }
                         if(_offerProduct.value!!.size>0)
                         {
                             _cartView.value =1
                             _totalquantity.value=   totalQuantity(_offerProduct.value!!)
                             _totalticket.value = totalCoupon(_offerProduct.value!!)
                             _totalprice.value=   totalPrice(_offerProduct.value!!)
                         }
                        else
                         {
                         }
                        productModel!!.addAll(_offerProduct.value as MutableList<GetCartList>)
                        _offerProduct.value=productModel
                     //  _message.value= response.message
                        _navigateRegister.value = 1
                        _notifyView.value=1
                    }
                    else if(response.status== Constant.TOKENEXPIRED)
                    {
                        _sessionEx.value = 5
                    }
                    else
                    {
                        _message.value= response.message
                    }
                } catch (e: Exception) {
                   Log.e(Constant.APIRESPONSE,"GetCart api failure is......"+e.toString())
                    _loadingStatus.value=0
                    _message.value= "Api Failure "+e.message
                }
            }
        }
    }

    fun totalQuantity(list: List<GetCartList>):String
    {
        var totalquantity:Int=0
        for (name in list!!.indices) {
            var quantity = list?.get(name).quantity!!.toInt()

            Log.e("ADDTOPANDU", "quanity..." + quantity)

            totalquantity = totalquantity.plus(quantity)

            Log.e("ADDTOPANDU", "totalquantity...." + totalquantity)

        }

        Log.e("ADDTOPANDU", "name" + totalquantity)

        return totalquantity.toString()
    }

    fun totalCoupon(list: List<GetCartList>):String
    {
        var totalCoupon:Int=0
        for (name in list!!.indices) {
            var coupon = list?.get(name).coupon_numbers!!.toInt()
            Log.e("ADDTOPANDU", "quanity..." + coupon)
            totalCoupon = totalCoupon.plus(coupon)
            Log.e("ADDTOPANDU", "totalquantity...." + totalCoupon)
        }
        Log.e("ADDTOPANDU", "name" + totalCoupon)
        return totalCoupon.toString()
    }

    fun totalPrice(list: List<GetCartList>):String
    {
        var totalPrice:Int=0
        for (name in list!!.indices) {

            var coupon = list?.get(name).coupon_numbers!!.toInt()
            Log.e("ADDTOPANDU", "quanity..." + coupon)
            var quantity=  list?.get(name).quantity!!.toInt()
            var price=  list?.get(name).products!!.price_per_unit!!.toDouble()
            var savePrice:Float = (quantity*price).toFloat()
            totalPrice = totalPrice.plus(savePrice).toInt()
            Log.e("ADDTOGANDU","total price is"+totalPrice)
        }
        return totalPrice.toString()
    }

    fun register() {
        _navigateRegister.value = 1
    }
}