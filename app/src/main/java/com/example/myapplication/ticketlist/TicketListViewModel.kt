package com.example.myapplication.ticketlist

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.R
import com.example.myapplication.network.CouponList
import com.example.myapplication.util.ApiStatus
import com.example.myapplication.util.CarzApi
import com.example.myapplication.util.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TicketListViewModel (val sharedPreferences: SharedPreferences,
                           application: Application
) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _navigateRegister = MutableLiveData<Int>()
    val navigateRegister: LiveData<Int?>
        get() = _navigateRegister


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

    private val _productlist = MutableLiveData<List<CouponList>>()
    val productlist: LiveData<List<CouponList>>
        get() = _productlist

    private val _norecordfoundStatus = MutableLiveData<Int>()
    val norecordfound : LiveData<Int?>
        get() = _norecordfoundStatus



    var Product: MutableList<CouponList>? =  mutableListOf()


    init {
        _norecordfoundStatus.value=0
        _navigateRegister.value = 0
         getproductlistingListing()
    }


    fun complete() {
        _navigateRegister.value = 0
    }


    fun getproductlistingListing()
    {
        Log.e("APIRESPONSE","ticket listing product listing is called")

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
                var getPropertiesDeferred = CarzApi.retrofitService.getCoupon("bearer "+token.toString())
                try {
                    val response = getPropertiesDeferred.await()
                    Log.e(Constant.APIRESPONSE,"coupon list api response is......"+response.toString())
                    _status.value = ApiStatus.DONE
                    _loadingStatus.value=0
                      if(response.status== Constant.SUCCEESSSTATUS)
                       {
                    Log.e("RESPONSE","email.......pin..."+response.toString())
                    _productlist.value =  response.Coupons!! as MutableList<CouponList>
                    //  Log.e("RESPONSE","runningproperties....."+ (_runningproperties.value as MutableList<Offers>).size)
                    Product!!.addAll(_productlist.value as MutableList<CouponList>)
                    _productlist.value=Product
                    //   _message.value= response.message
                    _navigateRegister.value = 1
                           if(_productlist.value!!.size<=0)
                           {
                               _norecordfoundStatus.value=1
                           }
                         }
                      else if(response.status ==404)
                      {
                          _norecordfoundStatus.value=1
                      }
                      else
                     {
                     _message.value= response.message
                     }
                    _notifyItem.value=0

                } catch (e: Exception) {

                    Log.e(Constant.APIRESPONSE,"coupon api response is......"+e.toString())

                    _loadingStatus.value=0
                    _status.value = ApiStatus.ERROR
                    _message.value= "Api Failure "+e.message
                }
            }
        }
    }




}