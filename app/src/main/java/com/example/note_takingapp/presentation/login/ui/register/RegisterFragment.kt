package com.example.note_takingapp.presentation.login.ui.register

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
import com.example.note_takingapp.databinding.FragmentLoginBinding
import com.example.note_takingapp.databinding.FragmentRegisterBinding
import com.example.note_takingapp.di.App
import com.example.note_takingapp.presentation.ViewModelFactory
import com.example.note_takingapp.presentation.login.state.AuthState
import com.example.note_takingapp.presentation.login.ui.viewModels.LoginViewModel
import com.example.note_takingapp.presentation.login.ui.viewModels.RegisterViewModel
import javax.inject.Inject


class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null

    private val binding get() = _binding!!

    private val component by lazy {
        (requireActivity().application as App).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this,viewModelFactory)[RegisterViewModel::class.java]
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val usernameEditText = binding.username
        val passwordEditText = binding.password
        val loginButton = binding.register
        val loadingProgressBar = binding.loading


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.authState.collect { state ->
                when(state) {
                    is AuthState.Idle -> {
                        loadingProgressBar.visibility = View.GONE
                        loginButton.isEnabled = true
                    }
                    is AuthState.Loading -> {
                        loadingProgressBar.visibility = View.VISIBLE
                        loginButton.isEnabled = false
                    }
                    is AuthState.Success -> {
                        loadingProgressBar.visibility = View.GONE
                        loginButton.isEnabled = true
                        Toast.makeText(context, "Welcome, ${state.user.email}", Toast.LENGTH_SHORT).show()
                        // Навигация на следующий экран после успешного входа

                    }
                    is AuthState.Error -> {
                        loadingProgressBar.visibility = View.GONE
                        loginButton.isEnabled = true
                        Toast.makeText(context, "Error: ${state.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.register.setOnClickListener {
            viewModel.register(usernameEditText.text.toString(),passwordEditText.text.toString())
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
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}