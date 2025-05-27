package com.example.note_takingapp.presentation.login.ui.forgetPassword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.note_takingapp.R
import com.example.note_takingapp.databinding.FragmentForgetPasswordBinding
import com.example.note_takingapp.databinding.FragmentRegisterBinding
import com.example.note_takingapp.di.App
import com.example.note_takingapp.presentation.ViewModelFactory
import com.example.note_takingapp.presentation.login.state.AuthState
import com.example.note_takingapp.presentation.login.ui.viewModels.ForgetPasswordViewModel
import com.example.note_takingapp.presentation.login.ui.viewModels.RegisterViewModel
import javax.inject.Inject


class ForgetPasswordFragment : Fragment() {

    private var _binding: FragmentForgetPasswordBinding? = null

    private val binding get() = _binding!!

    private val component by lazy {
        (requireActivity().application as App).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this,viewModelFactory)[ForgetPasswordViewModel::class.java]
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val usernameEditText = binding.username
        val loginButton = binding.forgetPasswordButton



        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.authState.collect { state ->
                when(state) {
                    is AuthState.Idle -> {
                        loginButton.isEnabled = true
                    }
                    is AuthState.Loading -> {
                        loginButton.isEnabled = false
                    }
                    is AuthState.Success -> {
                        loginButton.isEnabled = true
                        Toast.makeText(context, "Welcome, ${state.user.email}", Toast.LENGTH_SHORT).show()
                    }
                    is AuthState.Error -> {
                        loginButton.isEnabled = true
                        Toast.makeText(context, "Error: ${state.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.forgetPasswordButton.setOnClickListener {
            viewModel.sendPasswordResetEmail(usernameEditText.text.toString())
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForgetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}