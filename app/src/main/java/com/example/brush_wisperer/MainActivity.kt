package com.example.brush_wisperer

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.brush_wisperer.Data.Local.Model.Database.DeleteDatabaseWorker
import com.example.brush_wisperer.databinding.ActivityMainBinding
import com.example.brush_wisperer.ui.LoginFragment.LoginViewModel
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = LoginViewModel(application)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        viewModel.user.observe(this, Observer { user ->
            if (user == null) {
                findNavController(R.id.fragmentContainerView).navigate(R.id.loginFragment)
            }
        })

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        val toolbarTitle: TextView = findViewById(R.id.toolbar_title)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            toolbarTitle.text = destination.label
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)


        val toogle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toogle)
        toogle.syncState()

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.loginFragment,
                R.id.homeFragment,
                R.id.homeDetailFragment,
                R.id.categorieFragment,
                R.id.colourRangesAndColourListFragment,
                R.id.settingsFragment,
                R.id.workshop_categorie,
                R.id.workshop_my_colour_collection,
                R.id.workshop_colour_list,
                R.id.workshop_new_project,
                R.id.workshop_project_miniatures,
                R.id.workshop_miniature_colourlist,
                R.id.workshopWishlist,
                R.id.tutorialCategorie,
                R.id.tutorialBeginner,
                R.id.tutorialAirbrush,
                R.id.tutorialAdvanced,
                R.id.tutorialSpeedPaint,
                R.id.tutorialDetail,
                R.id.tutorialCategorie,
            ), drawerLayout
        )

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        setupActionBarWithNavController(navController, appBarConfiguration)

        val headerView: View = binding.navView.getHeaderView(0)
        val navUsername: TextView = headerView.findViewById(R.id.header_user_nameTV)
        viewModel.user.observe(this, Observer { user ->
            if (user != null) {
                val userNameShort = user.displayName?.substringBefore(" ")
                navUsername.text = userNameShort
            } else {
                navUsername.text = getString(R.string.guest)
            }
        })
// region Navigation Drawer
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            //Empty the back stack
            navController.popBackStack(navController.graph.startDestinationId, false)
            // To make sure the item gets selected
            // we safe the the id of the selected item and compare it with the current item
            val handled = when (menuItem.itemId) {
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }

                R.id.categorieFragment -> {
                    navController.navigate(R.id.categorieFragment)
                    true
                }

                R.id.workshop_categorie -> {
                    navController.navigate(R.id.workshop_categorie)
                    true
                }

                R.id.tutorialCategorie -> {
                    navController.navigate(R.id.tutorialCategorie)
                    true
                }

                R.id.settingsFragment -> {
                    navController.navigate(R.id.settingsFragment)
                    true
                }

                R.id.nav_logout -> {
                    viewModel.logOut()
                    toolbar.visibility = View.GONE
                    true
                }

                else -> false

            }
            // We close the drawer if the item was handled
            if (handled) {
                menuItem.isChecked = true
                binding.drawerLayout.closeDrawer(GravityCompat.START)
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
                R.id.workshop_new_project -> R.id.workshop_categorie
                R.id.workshop_project_miniatures -> R.id.workshop_categorie
                R.id.workshop_miniature_colourlist -> R.id.workshop_categorie
                R.id.workshopWishlist -> R.id.workshop_categorie
                R.id.tutorialCategorie -> R.id.tutorialCategorie
                R.id.tutorialBeginner -> R.id.tutorialCategorie
                R.id.tutorialAirbrush -> R.id.tutorialCategorie
                R.id.tutorialAdvanced -> R.id.tutorialCategorie
                R.id.tutorialSpeedPaint -> R.id.tutorialCategorie
                R.id.tutorialDetail -> R.id.tutorialCategorie
                R.id.settingsFragment -> R.id.settingsFragment
                else -> currentMenuItemId
            }
            // We make sure the item is not checked if it is already checked
            if (menuItemId != currentMenuItemId) {
                // we set the item to be checked if it is not checked
                // So I can control the behavior of the menu, which previously jumped into the else part after a double click, and prevent it.
                menu.findItem(menuItemId)?.isChecked = true
                currentMenuItemId = menuItemId
            }
        }


        // endregion

        val deleteRequest =
            PeriodicWorkRequestBuilder<DeleteDatabaseWorker>(24, TimeUnit.HOURS).build()
        WorkManager.getInstance(this).enqueue(deleteRequest)
        // This closing brace marks the end of the `onCreate` method in the `MainActivity` class.
        // The `onCreate` method is where you initialize your activity. Most importantly, it is where you usually inflate your layout.
        // Any code within this method is executed as soon as the activity is created.
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(deleteRequest.id)
            .observe(this, Observer { workInfo ->
                if (workInfo != null) {
                }
            })
        toolbar.visibility = View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragmentContainerView)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    fun showToolBar(visibility: Int) {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.visibility = visibility
    }
}
