package com.example.poznanbike

import android.animation.Animator
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.poznanbike.bikestations.BikeStation
import com.example.poznanbike.database.BikeStationDatabase
import com.example.poznanbike.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private lateinit var appBarConfiguration: AppBarConfiguration

class MainActivity : AppCompatActivity(), Animator.AnimatorListener {
    // view binding variable that allows for easy access to the Activities' Views
    private lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController
    private var firstStart: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = findNavController(R.id.nav_host_fragment_content_main)
        navController.addOnDestinationChangedListener(NavController.OnDestinationChangedListener { controller, destination, arguments ->
            if (!firstStart) {
                binding.fab.hide()
            }
            // Each time we change the navigation destination the menu will be invalidated
            invalidateOptionsMenu()
        })

        binding.fab.addOnHideAnimationListener(this)

        binding.fab.setOnClickListener {
            // placeholder for the FAB click event implementation
            appBarConfiguration = AppBarConfiguration(navController.graph)
            setupActionBarWithNavController(navController, appBarConfiguration)
            val navHostFragment: NavHostFragment? =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment?
            if (navController.currentDestination?.id == R.id.listFragment) {
                // If the currently displayed fragment is the ListFragment find the ListFragment in the navHostFragment
                val listFragment: ListFragment? =
                    navHostFragment?.childFragmentManager?.primaryNavigationFragment as ListFragment?
                // Clear the list if listFragment exists
                listFragment?.clearList()
                // Populate the list with new data from Internet
                listFragment?.populateListFromInternet()
            } else if (navController.currentDestination?.id == R.id.detailFragment) {
                // If th ecurrently displayed fragment is the DetailFragment find it in the navHostFragment
                val detailFragment: DetailFragment? =
                    navHostFragment?.childFragmentManager?.primaryNavigationFragment as DetailFragment?
                // call the savetoDatabse method to store or update currently displayed bike station
                detailFragment?.saveToDatabase()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val navDestination =
            navController.currentDestination ?: return super.onPrepareOptionsMenu(menu)
        return if (navDestination!!.id == R.id.detailFragment) {
            false // the menu disappears when the detailFragment is displayed
        } else {
            super.onPrepareOptionsMenu(menu)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        // Find the navController
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // call either the onSupportNavigateUp from the parent class or one specified
        // in the appBarConfiguration variable
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if (navController.currentDestination?.id == R.id.listFragment) {
            val navHostFragment: NavHostFragment? =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment?
            // Perform actions for different menu options

            // The options menu is available only when a ListFragment is displayed
            // Get the Hold to ListFragment from navigation component
            val listFragment: ListFragment? =
                navHostFragment?.childFragmentManager?.primaryNavigationFragment as ListFragment?

            // Clear the list displayed in the fragment
            listFragment?.clearList()

            val queryid: BikeStationDatabase.QUERYID
            // Set the query based on menu item id
            when (item.itemId) {
                R.id.get_all -> {
                    queryid = BikeStationDatabase.QUERYID.GET_ALL
                }
                R.id.get_with_bikes -> {
                    queryid = BikeStationDatabase.QUERYID.GET_STATIONS_WITH_BIKES
                }
                R.id.get_with_racks -> {
                    queryid = BikeStationDatabase.QUERYID.GET_STATIONS_WITH_FREE_RACKS
                }
                R.id.clear -> {
                    // If clear is selected from menu call the clearDB method and return
                    clearDB()
                    return true
                }
                else -> return true
            }
            // Get the data for the list with a given query id
            listFragment?.populateListFromDB(queryid)
        }
        return  super.onOptionsItemSelected(item)
    }

    private fun clearDB() {
        // Get a hold to the BikeStationDatabase DAO
        val bikeStationDao =
            BikeStationDatabase.getInstance(applicationContext).bikeStationDatabaseDao
        // In a a coroutine perform the clear operation
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                bikeStationDao.clear()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // set the firstStart value to false when we enter the resumed state
        // this will allow to hide the fab when we change destinations
        // see navController.addOnDestinationChangeListener in OnCreate
        firstStart = false
    }

    override fun onAnimationStart(p0: Animator?) {
    }

    override fun onAnimationEnd(p0: Animator?) {
        val currentDestination =
            findNavController(R.id.nav_host_fragment_content_main).currentDestination
        if(currentDestination?.id == R.id.listFragment) {
            // If the current destination is the ListFragment change the icon of the Fab to "update"
            binding.fab.setImageDrawable(getDrawable(R.drawable.ic_baseline_update_24))
        } else if (currentDestination?.id == R.id.detailFragment) {
            // If the current destination is the DetailFragment change the icon of the Fab to "save"
            binding.fab.setImageDrawable(getDrawable(R.drawable.ic_baseline_save_24))
        }
        // Show the fab again with a new icon
        binding.fab.show()
    }

    override fun onAnimationCancel(p0: Animator?) {
    }

    override fun onAnimationRepeat(p0: Animator?) {

    }

}