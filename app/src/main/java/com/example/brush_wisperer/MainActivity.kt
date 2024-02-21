package com.example.brush_wisperer

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.brush_wisperer.Data.Local.Model.Database.DeleteDatabaseWorker
import com.example.brush_wisperer.databinding.ActivityMainBinding
import com.example.brush_wisperer.ui.LoginFragment.LoginViewModel
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel : LoginViewModel
    private lateinit var toolbarTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = LoginViewModel(application)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        viewModel.user.observe( this, Observer { user ->
            if (user == null) {
                findNavController(R.id.fragmentContainerView).navigate(R.id.loginFragment)
            }
        })
        toolbarTitle = findViewById(R.id.toolbar_tv)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment))
        NavigationUI.setupActionBarWithNavController(this, navController,appBarConfiguration)

        val menuIcon: ImageButton = findViewById(R.id.menuIcon)

        menuIcon.setOnClickListener() {
            binding.drawerLayout.openDrawer(GravityCompat.END)
        }

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            // den backstack leeren
            navController.popBackStack(navController.graph.startDestinationId, false)
            // Um sicherzustellen, dass das Element ausgewählt ist, wenn wir darauf klicken
            // speichern wir die ID des aktuellen Elements und vergleichen sie mit der ID des ausgewählten Elements
            val handled = when (menuItem.itemId) {
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment)
                    updateToolbarTitle("Home")
                    true
                }
                R.id.categorieFragment -> {
                    navController.navigate(R.id.categorieFragment)
                    updateToolbarTitle("Brands")
                    true
                }
                R.id.workshop_categorie -> {
                    navController.navigate(R.id.workshop_categorie)
                    updateToolbarTitle("Workshop")
                    true
                }
                R.id.settingsFragment -> {
                    navController.navigate(R.id.settingsFragment)
                    updateToolbarTitle("Settings")
                    true
                }
                R.id.nav_logout -> {
                    viewModel.logOut()
                    showToolBar(View.GONE)
                    true
                }
                else -> false

            }
            // wenn das Element ausgewählt wurde, schließen wir den Drawer
            if (handled) {
                menuItem.isChecked = true
                binding.drawerLayout.closeDrawer(GravityCompat.END)
            }
            handled
        }
        var currentMenuItemId = R.id.homeFragment
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val menu = binding.navView.menu
            val menuItemId = when (destination.id) {
                R.id.homeFragment -> R.id.homeFragment
                R.id.homeDetailFragment -> R.id.homeFragment
                R.id.categorieFragment -> R.id.categorieFragment
                R.id.colourRangesAndColourListFragment -> R.id.categorieFragment
                R.id.workshop_categorie -> R.id.workshop_categorie
                R.id.workshop_colour_list -> R.id.workshop_categorie
                R.id.workshop_my_colour_collection -> R.id.workshop_categorie
                R.id.settingsFragment -> R.id.settingsFragment
                else -> currentMenuItemId
            }
            // wir stellen sicher das das item nicht schon ausgewählt ist
            if (menuItemId !=  currentMenuItemId) {
                // wir setzen das item als ausgewählt wenn es nicht das currrentMenuItem ist
                //somit kann ich das verhalten des Menüs was nach doppelt klick zb vorher in den else teil gesprungen ist stuern und verhindern
                //das, das falsche element im Drawer als ausgewählt markiert wird
                menu.findItem(menuItemId)?.isChecked = true
                currentMenuItemId = menuItemId
            }
        }

        toolbar.visibility = View.GONE


        val deleteRequest = PeriodicWorkRequestBuilder<DeleteDatabaseWorker>(24, TimeUnit.HOURS).build()
        WorkManager.getInstance(this).enqueue(deleteRequest)

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(deleteRequest.id)
            .observe(this, Observer { workInfo ->
                if (workInfo != null) {
                    Log.d("MainActivity", "Status: ${workInfo.state}")
                }
            })
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragmentContainerView)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    fun showToolBar(visibility: Int) {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.visibility = visibility

    }
    fun updateToolbarTitle(title: String) {
        toolbarTitle.text = title
    }
}
