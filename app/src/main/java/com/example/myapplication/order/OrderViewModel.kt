package com.example.myapplication.order

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.R
import com.example.myapplication.network.GetCartList
import com.example.myapplication.util.ApiStatus
import com.example.myapplication.util.CarzApi
import com.example.myapplication.util.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class OrderViewModel (val sharedPreferences: SharedPreferences,
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

    private val _GetCartListlist = MutableLiveData<List<GetCartList>>()
    val GetCartListlist: LiveData<List<GetCartList>>
        get() = _GetCartListlist


  /*  private val _offerGetCartList = MutableLiveData<List<GetCartList>>()
    val offerGetCartList: LiveData<List<GetCartList>>
        get() = _offerGetCartList*/



    var GetCartList: MutableList<GetCartList>? =  mutableListOf()


    init {

        _navigateRegister.value = 0
        getGetCartListlistingListing()
    }


    fun complete() {
        _navigateRegister.value = 0
    }


    fun getGetCartListlistingListing()
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
                Log.e("RESPONSE","email.......pin...")
                var getPropertiesDeferred = CarzApi.retrofitService.getOrder("bearer "+token)
                try {
                    val response = getPropertiesDeferred.await()

                    Log.e(Constant.APIRESPONSE,"OrderList api response is......"+response.toString())

                    _status.value = ApiStatus.DONE
                    _loadingStatus.value=0
                    //  if(response.status== Constant.SUCCEESSSTATUS)
                    //   {
                    Log.e("RESPONSE","email.......pin..."+response.toString())
                    _GetCartListlist.value =  response.cartlist!! as MutableList<GetCartList>
                    //  Log.e("RESPONSE","runningproperties....."+ (_runningproperties.value as MutableList<Offers>).size)
                    GetCartList!!.addAll(_GetCartListlist.value as MutableList<GetCartList>)
                    _GetCartListlist.value=GetCartList
                    //   _message.value= response.message
                    _navigateRegister.value = 1

                } catch (e: Exception) {
                    Log.e(Constant.APIRESPONSE,"OrderList api response is......"+e.toString())
                    _loadingStatus.value=0
                    _status.value = ApiStatus.ERROR
                    _message.value= "Api Failure "+e.message
                }
            }
        }
    }




}