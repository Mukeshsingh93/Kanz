package com.example.myapplication.wishlist

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.R
import com.example.myapplication.network.WishListModel
import com.example.myapplication.util.ApiStatus
import com.example.myapplication.util.CarzApi
import com.example.myapplication.util.Constant
import com.example.myapplication.util.ValidationUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class WishlistViewModel (val sharedPreferences: SharedPreferences,
                         application: Application
) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

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

    private val _notifyView = MutableLiveData<Int>()
    val notifyView : LiveData<Int?>
        get() = _notifyView

    private val _notifyItemRemove = MutableLiveData<Int>()
    val notifyItemRemove : LiveData<Int?>
        get() = _notifyItemRemove

    private val _wishlist = MutableLiveData<List<WishListModel>>()
    val wishlist: LiveData<List<WishListModel>>
        get() = _wishlist

    private val _sessionEx = MutableLiveData<Int?>()
    val sessionEx: LiveData<Int?>
        get() = _sessionEx

    private val _norecordfoundStatus = MutableLiveData<Int>()
    val norecordfound : LiveData<Int?>
        get() = _norecordfoundStatus






    var productModel: MutableList<WishListModel>? =  mutableListOf()


    init {

        _sessionEx.value =0
        _norecordfoundStatus.value=0
        _notifyView.value=0

        Log.e("APIRESPONSE","wishlist api is called...")
        getWishListingListing()

    }


    fun complete() {
    }




    fun delete(product: WishListModel, index: Int)
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
            coroutineScope.launch {
                // Get the Deferred object for our Retrofit request
                Log.e("RESPONSE","email.......pin...")
                var getPropertiesDeferred = CarzApi.retrofitService.deleteWishList("bearer "+token,cartId)
                try {
                    val response = getPropertiesDeferred.await()
                    _status.value = ApiStatus.DONE

                    Log.e(Constant.APIRESPONSE,"WishListView Delete api response is......"+response.toString())

                    if(response.status== Constant.SUCCEESSSTATUS)
                    {
                        Log.e("RESPONSE","email.......pin..."+response.toString())
                        //  Log.e("RESPONSE","runningproperties....."+ (_runningproperties.value as MutableList<Offers>).size)
                        _message.value= response.message
                        Log.e("DELETEITEM", "before product size product position..." + productModel!!.size)
                        var positon: Int = 0
                        for (i in 0 until wishlist!!.value!!.size) {

                            Log.e("DELETEITEM", "product position..." + product.id.toString())
                            Log.e("DELETEITEM", "for for image position..." + wishlist!!.value!!.get(i).id)
                            if (wishlist!!.value!!.get(i).id!!.equals(product.id)) {
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
                    Log.e(Constant.APIRESPONSE,"WishListView Delete api failure is......"+e.toString())
                   _status.value = ApiStatus.ERROR
                    _message.value= "Api Failure "+e.message
                }
            }
        }
    }

    fun updateList()
    {
        if(_wishlist.value!!.size<=0)
        {
            _norecordfoundStatus.value=1
            _notifyView.value=0

        }
    }




    fun getWishListingListing()
    {
        if(!Constant.connected(context))
        {
            _message.value= context.resources.getString(R.string.nointernet)
        }
        else
        {

            var token =sharedPreferences.getString(Constant.TOKEN,"")
            //_status.value = ApiStatus.LOADING
            _loadingStatus.value=1
            coroutineScope.launch {
                // Get the Deferred object for our Retrofit request
                Log.e("RESPONSE","email.......pin...")
                var getPropertiesDeferred = CarzApi.retrofitService.getWishList("bearer "+token)

                try {
                    val response = getPropertiesDeferred.await()
                 //   _status.value = ApiStatus.DONE

                    Log.e(Constant.APIRESPONSE,"wishlistapi api response is......"+response.toString())
                    _loadingStatus.value=0

                    if(response.status== Constant.SUCCEESSSTATUS)
                    {
                        _wishlist.value =  response.wish_lists!! as MutableList<WishListModel>
                        //  Log.e("RESPONSE","runningproperties....."+ (_runningproperties.value as MutableList<Offers>).size)
                        productModel!!.addAll(_wishlist.value as MutableList<WishListModel>)
                        _wishlist.value=productModel

                        if(_wishlist.value!!.size<=0)
                        {
                            _norecordfoundStatus.value=1
                        }
                    }
                    else if(response.status ==404)
                    {
                        _norecordfoundStatus.value=1
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
                    _notifyView.value=0
                } catch (e: Exception) {

                    Log.e(Constant.APIRESPONSE,"wishlistapi api response is......"+e.toString())
                    _loadingStatus.value=0
                   // _status.value = ApiStatus.ERROR
                    _message.value= "Api Failure "+e.message
                }
            }
        }
    }

}