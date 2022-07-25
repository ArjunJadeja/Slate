package com.arjun.slate.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.arjun.slate.databinding.ActivityMainBinding
import com.arjun.slate.viewmodel.SharedViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(com.arjun.slate.R.style.Theme_Slate)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost =
            supportFragmentManager.findFragmentById(com.arjun.slate.R.id.fragment_navigation) as NavHostFragment?
        val navController = navHost!!.navController

        val navInflater = navController.navInflater
        val graph = navInflater.inflate(com.arjun.slate.R.navigation.app_navigation)

        sharedViewModel.readPinFromDataStore.observe(this) {
            if (it == "none") {
                graph.setStartDestination(com.arjun.slate.R.id.mainScreenFragment)
            } else {
                graph.setStartDestination(com.arjun.slate.R.id.enterPinFragment)
            }
            navController.graph = graph
        }
    }
}