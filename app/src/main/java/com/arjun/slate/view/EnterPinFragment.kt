package com.arjun.slate.view

import android.os.Build
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.arjun.slate.R
import com.arjun.slate.databinding.FragmentEnterPinBinding
import com.arjun.slate.viewmodel.SharedViewModel
import kotlin.system.exitProcess

class EnterPinFragment : Fragment(R.layout.fragment_enter_pin) {

    private lateinit var binding: FragmentEnterPinBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var pin: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEnterPinBinding.bind(view)

        sharedViewModel.readPinFromDataStore.observe(viewLifecycleOwner) {
            pin = it
        }

        binding.pinInputEditText.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                submitPin()
                return@OnKeyListener true
            }
            false
        })

        binding.submitPinButton.setOnClickListener {
            submitPin()
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    exitProcess(0)
                }
            }
        )
    }

    private fun submitPin() {
        if (binding.pinInputEditText.text.toString() == pin) {
            hapticFeedback()
            findNavController().navigate(R.id.action_enterPinFragment_to_mainScreenFragment)
        } else if (binding.pinInputEditText.text.toString() == "") {
            Toast.makeText(context, "Enter pin", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Incorrect Pin", Toast.LENGTH_SHORT).show()
        }
    }

    private fun hapticFeedback() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity?.window?.decorView?.performHapticFeedback(
                HapticFeedbackConstants.CONFIRM,
                HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
            )
        }
    }
}