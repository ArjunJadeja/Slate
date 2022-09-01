package com.arjun.slate.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat.finishAffinity
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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
            if (list.isEmpty()) {
                binding.textsListRecyclerView.visibility = View.GONE
                binding.emptyListLayout.visibility = View.VISIBLE
            } else {
                list?.let {
                    adapter.updateList(it)
                }
                binding.emptyListLayout.visibility = View.GONE
                binding.textsListRecyclerView.visibility = View.VISIBLE
            }
        }

        binding.addTextButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainScreenFragment_to_addTextFragment)
        }

        binding.addTextFAB.setOnClickListener {
            findNavController().navigate(R.id.action_mainScreenFragment_to_addTextFragment)
        }

        binding.scrollToTopButton.setOnClickListener {
            binding.textsListRecyclerView.smoothScrollToPosition(0)
            binding.scrollToTopButton.visibility = View.GONE
        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.lock -> {
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

        // Recyclerview scrolling
        binding.textsListRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    if (binding.addTextFAB.isShown) {
                        binding.addTextFAB.hide()
                        binding.scrollToTopButton.visibility = View.VISIBLE
                    }
                } else if (dy < 0) {
                    if (!binding.addTextFAB.isShown) {
                        binding.addTextFAB.show()
                        binding.scrollToTopButton.visibility = View.GONE
                    }
                }
            }
        })

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    finishAffinity(requireActivity())
                }
            }
        )
    }

    override fun shareTextClicked(text: Text) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text.text)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    override fun deleteTextClicked(text: Text) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.delete_text))
            .setMessage(resources.getString(R.string.delete_message))
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.delete)) { dialog, _ ->
                sharedViewModel.deleteText(text)
                dialog.dismiss()
            }
            .show()
    }
}