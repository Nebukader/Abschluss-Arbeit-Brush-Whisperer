package com.example.brush_wisperer

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.brush_wisperer.Data.Local.Model.Database.DeleteDatabaseWorker
import com.example.brush_wisperer.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.visibility = View.VISIBLE

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupActionBarWithNavController(this, navController)

        toolbar.inflateMenu(R.menu.main_menu)
        val menuIcon: ImageView = toolbar.findViewById(R.id.menuIcon)

        //menuIcon.visibility = View.GONE
        //binding.drawerLayout.visibility = View.GONE

        val deleteRequest = PeriodicWorkRequestBuilder<DeleteDatabaseWorker>(24,TimeUnit.HOURS,).build()
        WorkManager.getInstance(this).enqueue(deleteRequest)

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(deleteRequest.id)
            .observe(this, Observer { workInfo ->
                if (workInfo != null) {
                    Log.d("MainActivity", "Status: ${workInfo.state}")
                }
            })
    }

    fun showToolBarIcon(visibility: Int) {
        binding.drawerLayout.visibility = visibility
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val menuIcon: ImageView = toolbar.findViewById(R.id.menuIcon)
        menuIcon.visibility = visibility
    }
}
