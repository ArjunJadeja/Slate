package com.arjun.slate.view

import android.content.Context
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.arjun.slate.R
import com.arjun.slate.data.Text
import com.arjun.slate.databinding.FragmentAddTextBinding
import com.arjun.slate.viewmodel.SharedViewModel

class AddTextFragment : Fragment(R.layout.fragment_add_text) {

    private lateinit var binding: FragmentAddTextBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddTextBinding.bind(view)

        showSoftKeyboard(binding.textInputEditText)

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.save -> {
                    if (binding.textInputEditText.text.toString().trim() == "") {
                        Toast.makeText(context, "Nothing to save", Toast.LENGTH_SHORT).show()
                    } else {
                        submitData()
                        hapticFeedback()
                        Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                        findNavController().navigateUp()
                    }
                    true
                }
                else -> false
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onPause() {
        super.onPause()
        if (binding.textInputEditText.text.toString().trim() == "") {
            sharedViewModel.saveTextToDataStore("")
        } else {
            sharedViewModel.saveTextToDataStore(binding.textInputEditText.text.toString().trimEnd())
        }
    }

    override fun onResume() {
        super.onResume()
        sharedViewModel.readTextFromDataStore.observe(viewLifecycleOwner) {
            if (it != "") {
                binding.textInputEditText.setText(it)
                binding.textInputEditText.setSelection(it.length)
            }
        }
    }

    private fun submitData() {
        val textInput = binding.textInputEditText.text.toString().trimEnd()
        sharedViewModel.insertText(Text(textInput, System.currentTimeMillis()))
        binding.textInputEditText.text = null
    }

    private fun showSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun hapticFeedback() {
        activity?.window?.decorView?.performHapticFeedback(
            HapticFeedbackConstants.CONFIRM,
            HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
        )
    }
}