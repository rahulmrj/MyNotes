package com.example.mynotes.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mynotes.Api.UserAPI
import com.example.mynotes.Utils.Constants.TAG
import com.example.mynotes.Utils.NetworkResult
import com.example.mynotes.models.UserRequest
import com.example.mynotes.models.UserResponce
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userAPI: UserAPI) {

    private val _userResponseLiveData = MutableLiveData<NetworkResult<UserResponce>>()
    val userResponseLiveData: LiveData<NetworkResult<UserResponce>> get() = _userResponseLiveData

    suspend fun registerUser(userRequest: UserRequest) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val responce = userAPI.signup(userRequest)
         Log.e(TAG, responce.body().toString() )
        handleResponse(responce)

    }

    suspend fun loginUser(userRequest: UserRequest) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val responce = userAPI.signin(userRequest)
         Log.e(TAG, responce.body().toString() )
        handleResponse(responce)

    }

    private fun handleResponse(responce: Response<UserResponce>) {
        if (responce.isSuccessful && responce.body() != null) {
            _userResponseLiveData.postValue(NetworkResult.Success(responce.body()))
        } else if (responce.errorBody() != null) {
            //     val errorOBJ = JSONObject(responce.errorBody()!!.charStream().toString())
            //  _userResponseLiveData.postValue(NetworkResult.Error(errorOBJ.getString("message")))
            _userResponseLiveData.postValue(NetworkResult.Error("message"+ responce.message()))

        } else {
            _userResponseLiveData.postValue(NetworkResult.Error("Some went wrong"))

        }
    }


}