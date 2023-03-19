package com.example.mynotes

import android.provider.ContactsContract.CommonDataKinds.Email
import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.Utils.NetworkResult
import com.example.mynotes.models.UserRequest
import com.example.mynotes.models.UserResponce
import com.example.mynotes.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor( private val userRepository: UserRepository) :ViewModel() {

    val userResponseLiveData : LiveData<NetworkResult<UserResponce>>
    get() = userRepository.userResponseLiveData

    fun registerUser(userRequest: UserRequest){
        viewModelScope.launch {
            userRepository.registerUser(userRequest)
        }

    }

    fun loginUser(userRequest: UserRequest){
        viewModelScope.launch {
            userRepository.loginUser(userRequest)
        }
    }

    fun validateCredentials(username: String, email: String, password: String, isLogin : Boolean): Pair<Boolean, String> {
        var result = Pair(true,"")

        if(!isLogin && TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            result = Pair(false, "Please provide valid credentials")
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            result = Pair(false, "Please enter valid email")
        }else if(password.length <=5) {
            result = Pair(false, "Password length cannot less then 5")
        }
            return result
    }
}