package com.example.myapplication.profile

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.R
import com.example.myapplication.network.User
import com.example.myapplication.network.WishListModel
import com.example.myapplication.util.ApiStatus
import com.example.myapplication.util.CarzApi
import com.example.myapplication.util.Constant
import com.example.myapplication.util.ValidationUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ProfileViewModel (val sharedPreferences: SharedPreferences,
                        application: Application
) : AndroidViewModel(application) {


    private val context = getApplication<Application>().applicationContext

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _email = MutableLiveData<String>()
    val email: LiveData<String?>
        get() = _email


    private val _password = MutableLiveData<String>()
    val password: LiveData<String?>
        get() = _password

    private val _profile = MutableLiveData<User>()
    val profile: LiveData<User?>
        get() = _profile



    private val _username = MutableLiveData<String>()
    val username: LiveData<String?>
        get() = _username

    private val _mobileno = MutableLiveData<String>()
    val mobileno: LiveData<String?>
        get() = _mobileno

    private val _message = MutableLiveData<String>()
    val message: LiveData<String?>
        get() = _message

    private val _otp = MutableLiveData<String>()
    val otp: LiveData<String?>
        get() = _otp

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus?>
        get() = _status

    private val _navigateVerification = MutableLiveData<Int>()
    val navigateVerification: LiveData<Int?>
        get() = _navigateVerification

    private val _notifyView = MutableLiveData<Int>()
    val notifyView : LiveData<Int?>
        get() = _notifyView

    private val _confirmPassword = MutableLiveData<String>()
    val confirmPassword : LiveData<String?>
        get() = _confirmPassword


    private val _address = MutableLiveData<String>()
    val address : LiveData<String?>
        get() = _address

    private val _sessionEx = MutableLiveData<Int?>()
    val sessionEx: LiveData<Int?>
        get() = _sessionEx

    init {

        _username.value = ""
        _otp.value = ""
        _mobileno.value = ""
        _email.value = ""
        _password.value = ""
        _confirmPassword.value=""
        _address.value=""
        _navigateVerification.value = 0

        _sessionEx.value =0

        getProfile();

    }

    fun complete() {
        _navigateVerification.value = 0
    }

    fun onTextChangedUsername(s: CharSequence, start: Int, before: Int, count: Int) {
        if (!s.toString().isEmpty()) {
            _username.value = s.toString()
        } else {
            _username.value = ""
        }
    }

    fun onTextChangedMobileno(s: CharSequence, start: Int, before: Int, count: Int) {
        if (!s.toString().isEmpty()) {
            _mobileno.value = s.toString()
        } else {
            _mobileno.value = ""
        }
    }

    fun onTextChangedEmail(s: CharSequence, start: Int, before: Int, count: Int) {
        if (!s.toString().isEmpty()) {
            _email.value = s.toString()
        } else {
            _email.value = ""
        }
    }

    fun onTextChangedPassword(s: CharSequence, start: Int, before: Int, count: Int) {
        if (!s.toString().isEmpty()) {
            _password.value = s.toString()
        } else {
            _password.value = ""
        }
    }

    fun onTextChangedConfirmPassword(s: CharSequence, start: Int, before: Int, count: Int) {
        if(!s.toString().isEmpty())
        {
            _confirmPassword.value= s.toString()
        }
        else
        {
            _confirmPassword.value=""
        }
    }

    fun onTextChangedAddress(s: CharSequence, start: Int, before: Int, count: Int) {
        if(!s.toString().isEmpty())
        {
            _address.value= s.toString()
        }
        else
        {
            _address.value=""
        }
    }

    fun submit() {
        val matchPassword : Boolean =(password.value == confirmPassword.value)
        when {
            _username.value!!.isEmpty() -> _message.value = "The username field is required."
            _mobileno.value!!.isEmpty() -> _message.value = "The mobile no field is required."
            _password.value!!.isEmpty() -> _message.value="The new password field is required."
            _password.value!!.length<6 -> _message.value="The password is minimum 6 character is required."
            _confirmPassword.value!!.isEmpty() -> _message.value="The confirm password field is required."
            !matchPassword ->  _message.value="Confirm Password does not match"
            else -> updateProfileApi()
        }
    }


    fun updateProfileApi() {
        if (!Constant.connected(context)) {
            _message.value = context.resources.getString(R.string.nointernet)
        } else {
            var token =sharedPreferences.getString(Constant.TOKEN,"")


            _status.value = ApiStatus.LOADING
            coroutineScope.launch {
                // Get the Deferred object for our Retrofit request
                Log.e("RESPONSE", "email...." + email.value.toString() + "...pin...")
                var getPropertiesDeferred = CarzApi.retrofitService.updateProfile(

                    "bearer "+token,
                    username.value.toString(),
                    mobileno.value.toString(),
                    address.value.toString(),
                    password.value.toString()

                        )
                try {
                    val response = getPropertiesDeferred.await()
                    Log.e(
                        Constant.APIRESPONSE,"updateprofile api response is......" + response.toString()
                    )
                    _status.value = ApiStatus.DONE
                    if (response.status == Constant.SUCCEESSSTATUS) {
                        sharedPreferences.edit {
                            putString(Constant.TOKEN,response.token)
                        }
                    } else {
                        _message.value = response.message
                    }
                } catch (e: Exception) {
                    _status.value = ApiStatus.ERROR
                    _message.value = "Api Failure " + e.message
                    Log.e(Constant.APIRESPONSE, "updateprofile api failure is......" + e.toString())
                }
            }
        }
    }


    fun getProfile()
    {
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
                Log.e("TOKEN","toke is..."+token.toString())

                var getPropertiesDeferred = CarzApi.retrofitService.getProfile("bearer "+token)

                try {
                    val response = getPropertiesDeferred.await()
                    _status.value = ApiStatus.DONE
                    Log.e(Constant.APIRESPONSE,"getProfile api response is......"+response.toString())
                 if(response.status== Constant.SUCCEESSSTATUS)
                    {
                        _profile.value = response.user
                        _username.value = _profile.value!!.name
                        _mobileno.value = _profile.value!!.phone
                        _address.value = _profile.value!!.address
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
                    Log.e(Constant.APIRESPONSE,"getProfile api response is......"+e.toString())
                    _status.value = ApiStatus.ERROR
                    _message.value= "Api Failure "+e.message
                }
            }
        }
    }
}