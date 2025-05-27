package com.example.note_takingapp.presentation.login.ui.login


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.note_takingapp.R
import com.example.note_takingapp.databinding.FragmentLoginBinding
import com.example.note_takingapp.di.App
import com.example.note_takingapp.presentation.ViewModelFactory
import com.example.note_takingapp.presentation.login.state.AuthState
import com.example.note_takingapp.presentation.login.ui.viewModels.LoginViewModel
import javax.inject.Inject

class LoginFragment : Fragment() {


    private var _binding: FragmentLoginBinding? = null


    private val binding get() = _binding!!

    private val component by lazy {
        (requireActivity().application as App).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this,viewModelFactory)[LoginViewModel::class.java]
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        component.inject(this)
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val usernameEditText = binding.username
        val passwordEditText = binding.password
        val loginButton = binding.login
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
                        findNavController().navigate(R.id.action_loginFragment_to_noteListFragment)
                    }
                    is AuthState.Error -> {
                        loadingProgressBar.visibility = View.GONE
                        loginButton.isEnabled = true
                        Toast.makeText(context, "Error: ${state.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.login.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            viewModel.login(username, password)
            Toast.makeText(context, username + password, Toast.LENGTH_SHORT).show()
            Log.d("LOGINFRAGMENT", username + password)
        }

        binding.buttonToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_logUpFragment)
        }

        binding.buttonToForget.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}