package com.arjun.slate.view

import android.os.Build
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.arjun.slate.R
import com.arjun.slate.databinding.FragmentSetPinBinding
import com.arjun.slate.viewmodel.SharedViewModel

class SetPinFragment : Fragment(R.layout.fragment_set_pin) {

    private lateinit var binding: FragmentSetPinBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var pin: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSetPinBinding.bind(view)

        sharedViewModel.readPinFromDataStore.observe(viewLifecycleOwner) {
            if (it == "none") {
                userValidated()
            } else {
                validateUser()
            }
            pin = it
        }

        binding.submitValidationButton.setOnClickListener {
            checkPin()
        }

        binding.validateUserEditText.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                checkPin()
                return@OnKeyListener true
            }
            false
        })

        binding.disablePinButton.setOnClickListener {
            sharedViewModel.savePinToDataStore("none")
            hapticFeedback()
            Toast.makeText(context, "Pin disabled", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }

        binding.setPinButton.setOnClickListener {
            savePin()
        }

        binding.confirmPinEditText.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                savePin()
                return@OnKeyListener true
            }
            false
        })

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun validateUser() {
        binding.lockIconImageView.visibility = View.VISIBLE
        binding.validateUserTextView.visibility = View.VISIBLE
        binding.validateUserEditText.visibility = View.VISIBLE
        binding.submitValidationButton.visibility = View.VISIBLE
    }

    private fun userValidated() {
        binding.setPinTextView.visibility = View.VISIBLE
        binding.setPinTextEditText.visibility = View.VISIBLE
        binding.confirmPinEditText.visibility = View.VISIBLE
        binding.setPinButton.visibility = View.VISIBLE
        binding.setPinTextEditText.requestFocus()
    }

    private fun checkPin() {
        if (binding.validateUserEditText.text.toString() == pin) {
            hapticFeedback()
            binding.lockIconImageView.visibility = View.GONE
            binding.validateUserTextView.visibility = View.GONE
            binding.validateUserEditText.visibility = View.GONE
            binding.submitValidationButton.visibility = View.GONE
            binding.disablePinButton.visibility = View.VISIBLE
            userValidated()
        } else if (binding.validateUserEditText.text.toString() == "") {
            Toast.makeText(context, "Enter pin", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Incorrect Pin", Toast.LENGTH_SHORT).show()
        }
    }

    private fun savePin() {
        if (binding.setPinTextEditText.text.toString() == binding.confirmPinEditText.text.toString()) {
            if (binding.setPinTextEditText.text.toString() == "") {
                Toast.makeText(context, "Enter pin", Toast.LENGTH_SHORT).show()
            } else if (binding.setPinTextEditText.text.toString() == pin) {
                Toast.makeText(context, "Same as old pin", Toast.LENGTH_SHORT).show()
            } else {
                sharedViewModel.savePinToDataStore(binding.confirmPinEditText.text.toString())
                hapticFeedback()
                Toast.makeText(context, "Pin set successfully", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        } else {
            Toast.makeText(context, "Check input", Toast.LENGTH_SHORT).show()
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