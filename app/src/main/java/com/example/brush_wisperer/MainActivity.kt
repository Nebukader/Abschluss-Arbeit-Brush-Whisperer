package com.example.brush_wisperer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.brush_wisperer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
        private lateinit var binding: ActivityMainBinding

        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                binding = ActivityMainBinding.inflate(layoutInflater)
                setContentView(binding.root)

                binding.bottomNavigationView.visibility = View.GONE
        }
        fun showBottomNav(visibility: Int){
                binding.bottomNavigationView.visibility = visibility
        }
}