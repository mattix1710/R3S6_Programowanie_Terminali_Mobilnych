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
import com.example.poznanbike.database.BikeStationDAO
import com.example.poznanbike.database.BikeStationDatabase
import com.example.poznanbike.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), Animator.AnimatorListener {
    // view binding variable that allows for easy access to the Activities' Views
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
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
            appBarConfiguration = AppBarConfiguration(navController.graph)          //TODO: Maciek tu wrzucił
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
                // If the currently displayed fragment is the DetailFragment find it in the navHostFragment
                val detailFragment: DetailFragment? =
                    navHostFragment?.childFragmentManager?.primaryNavigationFragment as DetailFragment?
                // call the saveToDatabase method to store or update currently displayed bike station
                detailFragment?.saveToDatabase()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // set the firstStart value to false when we enter the resumed state
        // this will allow to hide the fab when we change destinations
        // see navController.addOnDestinationChangeListener in onCreate
        firstStart = false
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
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if (navController.currentDestination?.id == R.id.listFragment) {
            val navHostFragment: NavHostFragment? =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment?
            // Perform actions for different menu options

            // The options menu is available only when a ListFragment is displayed.
            // Get the Hold to ListFragment from navigation component
            val listFragment: ListFragment? =
                navHostFragment?.childFragmentManager?.primaryNavigationFragment as ListFragment?

            // Clear the list displayed in the fragment
            listFragment?.clearList()

            val queryId: BikeStationDatabase.QUERYID
            // Set the query based on menu item id
            when (item.itemId) {
                R.id.get_all -> {
                    queryId = BikeStationDatabase.QUERYID.GET_ALL
                }
                R.id.get_with_bikes -> {
                    queryId = BikeStationDatabase.QUERYID.GET_STATIONS_WITH_BIKES
                }
                R.id.get_with_racks -> {
                    queryId = BikeStationDatabase.QUERYID.GET_STATIONS_WITH_FREE_RACKS
                }
                R.id.clear -> {
                    // If clear is selected from the menu, call the clearDB method and return
                    clearDB()
                    return true
                }
                else -> return true
            }
            // Get the data for the list with a given query id
            listFragment?.populateListFromDB(queryId)
        }
        return  super.onOptionsItemSelected(item)
    }

    private fun clearDB() {
        // Get a hold to the BikeStationDatabase DAO
        val bikeStationDAO =
            BikeStationDatabase.getInstance(applicationContext).bikeStationDatabaseDao
        // In a coroutine perform the clear operation
        GlobalScope.launch {
            withContext(Dispatchers.IO){
                bikeStationDAO.clear()
            }
        }
    }

    override fun onAnimationStart(p0: Animator?) {
    }

    override fun onAnimationEnd(p0: Animator?) {
        // get the current destination in the navGraph
        val currentDestination =
            findNavController(R.id.nav_host_fragment_content_main).currentDestination
        if(currentDestination?.id == R.id.listFragment){
            // If the current destination is the ListFragment, change the icon of the FAB to "update"
            binding.fab.setImageDrawable(getDrawable(R.drawable.ic_baseline_update_24))
        } else if (currentDestination?.id == R.id.detailFragment){
            // If the current destination is the DetailFragment, change the icon of the FAB to "save"
            binding.fab.setImageDrawable(getDrawable(R.drawable.ic_baseline_save_24))
        }
        // Show the fab again with a new icon
        binding.fab.show()
    }

    override fun onAnimationCancel(p0: Animator?) {
    }

    override fun onAnimationRepeat(p0: Animator?) {
    }

    override fun onSupportNavigateUp(): Boolean {
        // Find the navController
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // call either the onSupportNavigateUp from the parent class or one specified
        // in the appBarConfiguration variable
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}