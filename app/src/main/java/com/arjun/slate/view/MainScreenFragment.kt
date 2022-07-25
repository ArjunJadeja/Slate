package com.arjun.slate.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arjun.slate.R
import com.arjun.slate.data.Text
import com.arjun.slate.databinding.FragmentMainScreenBinding
import com.arjun.slate.utils.INotesRVAdapter
import com.arjun.slate.utils.TextRVAdapter
import com.arjun.slate.viewmodel.SharedViewModel
import kotlin.system.exitProcess

class MainScreenFragment : Fragment(R.layout.fragment_main_screen), INotesRVAdapter {

    private lateinit var binding: FragmentMainScreenBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var adapter: TextRVAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainScreenBinding.bind(view)

        binding.textsListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = TextRVAdapter(requireContext(), this)
        binding.textsListRecyclerView.adapter = adapter

        sharedViewModel.allTexts.observe(viewLifecycleOwner) { list ->
            list?.let {
                adapter.updateList(it)
            }
        }

        binding.addTextButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainScreenFragment_to_addTextFragment)
        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.set_pin -> {
                    findNavController().navigate(R.id.action_mainScreenFragment_to_setPinFragment)
                    true
                }
                R.id.about_app -> {
                    findNavController().navigate(R.id.action_mainScreenFragment_to_aboutAppFragment)
                    true
                }
                else -> false
            }
        }

        // Hiding FAB on scrolling
        binding.textsListRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    if (binding.addTextButton.isShown) {
                        binding.addTextButton.hide()
                    }
                } else if (dy < 0) {
                    if (!binding.addTextButton.isShown) {
                        binding.addTextButton.show()
                    }
                }
            }
        })

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    exitProcess(0)
                }
            }
        )
    }

    override fun onItemClicked(text: Text) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text.text)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }
}