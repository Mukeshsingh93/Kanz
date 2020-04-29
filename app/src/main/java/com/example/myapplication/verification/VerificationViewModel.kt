package com.example.myapplication.verification

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.R
import com.example.myapplication.util.ApiStatus
import com.example.myapplication.util.CarzApi
import com.example.myapplication.util.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class VerificationViewModel (val sharedPreferences: SharedPreferences,
                             val otp: String,
                             application: Application
) : AndroidViewModel(application)
{


    private val context = getApplication<Application>().applicationContext

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    private val _editTextOne = MutableLiveData<String>()
    val editTextOne : LiveData<String?>
        get() = _editTextOne

    private val _editTextTwo = MutableLiveData<String>()
    val editTextTwo : LiveData<String?>
        get() = _editTextTwo

    private val _editTextThree = MutableLiveData<String>()
    val editTextThree : LiveData<String?>
        get() = _editTextThree

    private val _editTextFour = MutableLiveData<String>()
    val editTextFour : LiveData<String?>
        get() = _editTextFour

    private val _message= MutableLiveData<String>()
    val message : LiveData<String?>
        get() = _message


    private val _navigateToScreen = MutableLiveData<Int>()
    val navigateToScreen : LiveData<Int?>
        get() = _navigateToScreen

    private val _status = MutableLiveData<ApiStatus>()
    val status : LiveData<ApiStatus?>
        get() = _status

    init {
         _editTextOne.value=""
        _editTextTwo.value=""
        _editTextThree.value=""
        _editTextFour.value=""
        _navigateToScreen.value=0
    }

    fun onTextChangedEditTextOne(s: CharSequence, start: Int, before: Int, count: Int) {
        if(!s.toString().isEmpty())
        {
            _editTextOne.value= s.toString()
        }
        else
        {
            _editTextOne.value=""
        }
    }

    fun onTextChangedEditTextTwo(s: CharSequence, start: Int, before: Int, count: Int) {
        if(!s.toString().isEmpty())
        {
            _editTextTwo.value= s.toString()
        }
        else
        {
            _editTextTwo.value=""
        }
    }

    fun onTextChangedEditTextThree(s: CharSequence, start: Int, before: Int, count: Int) {
        if(!s.toString().isEmpty())
        {
            _editTextThree.value= s.toString()
        }
        else
        {
            _editTextThree.value=""
        }
    }

    fun onTextChangedEditTextFour(s: CharSequence, start: Int, before: Int, count: Int) {
        if(!s.toString().isEmpty())
        {
            _editTextFour.value= s.toString()
        }
        else
        {
            _editTextFour.value=""
        }
    }

    fun complete()
    {
        _navigateToScreen.value = 0
    }

    fun checkOtp()
    {
        var otpone = _editTextOne.value.toString()
        var otptwo = _editTextTwo.value.toString()
        var otpthree = _editTextThree.value.toString()
        var otpfour = _editTextFour.value.toString()
        var otpvalue = otpone.toString()+otptwo.toString()+otpthree.toString()+otpfour.toString()

        Log.e("CHECKDATA","otp..."+otpvalue.toString())
        Log.e("CHECKDATA","otp value..."+ otp.toString())

        val matchPassword : Boolean =(otp == otpvalue)

        when
        {
           !matchPassword ->  _message.value="You have entered wrong otp"
            else -> verficationApi()
        }
    }

    fun verficationApi()
    {
        if(!Constant.connected(context))
        {
            _message.value= context.resources.getString(R.string.nointernet)
        }
        else
        {
            _status.value = ApiStatus.LOADING
            coroutineScope.launch {
                // Get the Deferred object for our Retrofit request
                var getPropertiesDeferred = CarzApi.retrofitService.verify(otp.toString())
                try {
                    val response = getPropertiesDeferred.await()

                    Log.e(Constant.APIRESPONSE,"verification api response is......"+response.toString())

                    _status.value = ApiStatus.DONE
                    if(response.status== Constant.SUCCEESSSTATUS)
                    {
                        _message.value= response.message
                        sharedPreferences.edit { putString(Constant.TOKEN,response.token)}
                        _navigateToScreen.value=1
                    }
                    else
                    {
                        _message.value= response.message
                    }
                } catch (e: Exception) {
                    _status.value = ApiStatus.ERROR
                    _message.value= "Api Failure "+e.message
                    Log.e(Constant.APIRESPONSE,"verification api failure is......"+e.toString())
                }
            }
        }
    }



}