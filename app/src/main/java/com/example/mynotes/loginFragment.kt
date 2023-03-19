package com.example.mynotes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.mynotes.Utils.NetworkResult
import com.example.mynotes.Utils.TokenManager
import com.example.mynotes.databinding.FragmentLoginBinding
import com.example.mynotes.models.UserRequest
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class loginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        if (tokenManager.getToken() != null) {
            findNavController().navigate(
                R.id.action_registerFragment_to_mainFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener {
            val validationResult = validateUser()
            if (validationResult.first) {
                authViewModel.loginUser(getUserRequest())
            } else {
                binding.txtError.text = validationResult.second
            }
        }

        binding.btnSignUp.setOnClickListener {
            findNavController().popBackStack()
        }

        bindObserver()
    }

    private fun getUserRequest(): UserRequest {
        val emailAddr = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()

        return UserRequest(emailAddr, password, "")
    }

    private fun validateUser(): Pair<Boolean, String> {
        val userRequest = getUserRequest()
        return authViewModel.validateCredentials(
            userRequest.username,
            userRequest.email,
            userRequest.password,
            true
        )
    }


    private fun bindObserver() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    //token
                    tokenManager.saveToken(it.data!!.token)
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
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