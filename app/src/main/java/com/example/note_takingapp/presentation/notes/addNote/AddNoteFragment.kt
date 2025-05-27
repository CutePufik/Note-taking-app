package com.example.note_takingapp.presentation.notes.addNote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.note_takingapp.R
import com.example.note_takingapp.databinding.FragmentAddNoteBinding
import com.example.note_takingapp.di.App
import com.example.note_takingapp.presentation.ViewModelFactory
import com.example.note_takingapp.presentation.addnote.AddNoteViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject


class AddNoteFragment : Fragment() {

    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!

    private val component by lazy {
        (requireActivity().application as App).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this,viewModelFactory)[AddNoteViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        component.inject(this)
        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val content = binding.etContent.text.toString()

            if (title.isBlank() || content.isBlank()) {
                Toast.makeText(requireContext(), "Поля не должны быть пустыми", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId == null) {
                Toast.makeText(requireContext(), "Пользователь не авторизован", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.addNote(title, content, userId)
        }


        lifecycleScope.launchWhenStarted {
            viewModel.addNoteResult.collectLatest { result ->
                result?.onSuccess {
                    Toast.makeText(requireContext(), "Заметка добавлена", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_addNoteFragment_to_noteListFragment)
                    viewModel.clearResult()
                }?.onFailure {
                    Toast.makeText(requireContext(), "Ошибка: ${it.message}", Toast.LENGTH_SHORT).show()
                    viewModel.clearResult()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
