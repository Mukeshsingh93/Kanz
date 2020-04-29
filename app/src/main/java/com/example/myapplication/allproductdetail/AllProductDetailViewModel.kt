package com.example.myapplication.allproductdetail

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.network.OfferProducts
import com.example.myapplication.network.Product
import com.example.myapplication.network.SliderImages
import com.example.myapplication.util.ApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class AllProductDetailViewModel (val sharedPreferences: SharedPreferences,
                                 val offerProducts: Product,
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


    // runnning out adapter
    private val _sliderimg = MutableLiveData<List<SliderImages>>()
    val sliderimg: LiveData<List<SliderImages>>
        get() = _sliderimg

    init {
        _navigateRegister.value = 0
        Log.e("APIRESPONSE","productdetail....."+offerProducts.toString())
        _sliderimg.value = offerProducts!!.images as MutableList<SliderImages>
    }

    fun complete() {
        _navigateRegister.value = 0
    }



}