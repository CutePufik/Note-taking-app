package com.example.note_takingapp.presentation.notes.noteList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.note_takingapp.R
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.note_takingapp.databinding.FragmentNoteListBinding
import com.example.note_takingapp.di.App
import com.example.note_takingapp.presentation.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


class NoteListFragment : Fragment() {


    var _binding : FragmentNoteListBinding? = null
    val binding get() = _binding ?: throw Exception("NoteListFragment is null")

    private lateinit var noteAdapter: NoteAdapter

    private val component by lazy {
        (requireActivity().application as App).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this,viewModelFactory)[NoteListViewModel::class.java]
    }

    val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean = false

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            val note = noteAdapter.currentList[position]
            noteAdapter.onItemSwiped?.invoke(note)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeNotes()
        setupClickListeners()
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.rvNotes)

        binding.fabAddNote.setOnClickListener {
            findNavController().navigate(R.id.action_noteListFragment_to_addNoteFragment)
        }
    }

    private fun setupRecyclerView() {
        noteAdapter = NoteAdapter().apply {
            onItemClick = { note ->
                // Обработка клика по заметке (например, открыть детали)
            }
            onItemSwiped = { note ->
                viewModel.deleteNote(note.id.toString())
            }

        }


        binding.rvNotes.apply {
            adapter = noteAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeNotes() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.notes.collectLatest { notes ->
                    noteAdapter.submitList(notes)
                }
            }
        }


    }

    private fun setupClickListeners() {
        binding.fabAddNote.setOnClickListener {
            // Открыть экран создания заметки
        }

        noteAdapter.onItemSwiped = { note ->
            viewModel.deleteNote(note.id.toString()) 
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteListBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}