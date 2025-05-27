package com.example.note_takingapp.presentation.notes.updateNote

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
import com.example.note_takingapp.databinding.FragmentUpdateNoteBinding
import com.example.note_takingapp.di.App
import com.example.note_takingapp.domain.Notes.model.Note
import com.example.note_takingapp.presentation.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

class UpdateNoteFragment : Fragment() {

    private var _binding: FragmentUpdateNoteBinding? = null
    private val binding get() = _binding!!

    private val component by lazy {
        (requireActivity().application as App).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[UpdateNoteViewModel::class.java]
    }

    private var currentNoteId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateNoteBinding.inflate(inflater, container, false)
        component.inject(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentNoteId = arguments?.getString("id") ?: ""
        Log.d("UpdateNoteFragment", "Note ID: $currentNoteId")

        viewModel.loadNoteById(currentNoteId)

        lifecycleScope.launch {
            viewModel.note.collectLatest { note ->
                note?.let {
                    binding.etNoteTitle.setText(it.title)
                    binding.etNoteContent.setText(it.text)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.error.collectLatest { errorMsg ->
                errorMsg?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnSaveNote.setOnClickListener {
            val updatedTitle = binding.etNoteTitle.text.toString().trim()
            val updatedContent = binding.etNoteContent.text.toString().trim()

            if (updatedTitle.isEmpty() || updatedContent.isEmpty()) {
                Toast.makeText(requireContext(), "Поля не должны быть пустыми", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedNote = Note(
                id = viewModel.note.value!!.id,
                title = updatedTitle,
                text = updatedContent,
                createdAt = Date(),
                userId = viewModel.note.value!!.userId
            )

            viewModel.saveNote(updatedNote)

            Toast.makeText(requireContext(), "Заметка обновлена", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateNoteFragment_to_noteListFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
