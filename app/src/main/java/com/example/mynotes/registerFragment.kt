package com.example.mynotes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.mynotes.Utils.NetworkResult
import com.example.mynotes.Utils.TokenManager
import com.example.mynotes.databinding.FragmentRegisterBinding
import com.example.mynotes.models.UserRequest
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class registerFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()
    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false);



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnSignUp.setOnClickListener {
            val validationResult = validateUser()
            if (validationResult.first){
                authViewModel.registerUser(getUserRequest())
            }else{
                binding.txtError.text = validationResult.second
            }

        }

        binding.btnLogin.setOnClickListener {
             findNavController().navigate(R.id.action_registerFragment_to_loginFragment)

        }

        bindObserver()
    }

    private fun getUserRequest() : UserRequest{
        val emailAddr = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()
        val userName = binding.txtUsername.text.toString()

        return UserRequest(userName, emailAddr,password)
    }

    private fun validateUser():Pair<Boolean, String>{
        val userRequest = getUserRequest()
        return authViewModel.validateCredentials(userRequest.username, userRequest.email, userRequest.password,false)
    }

    private fun bindObserver() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    //token
                    tokenManager.saveToken(it.data!!.token)
                    findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
                }
                is NetworkResult.Error -> {
                    binding.txtError.text = it.message
                }

                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }

            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}