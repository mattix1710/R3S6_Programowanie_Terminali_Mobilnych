package com.example.poznanbike

import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.poznanbike.bikestations.BikeStation
import com.example.poznanbike.database.BikeStationDatabase
import com.example.poznanbike.databinding.FragmentListBinding
import com.example.poznanbike.network.BikeStationApi
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import java.lang.Exception

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ListFragment : Fragment() {

    private var bikeStations: List<BikeStation>? = null
    private lateinit var binding: FragmentListBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentListBinding.inflate(inflater, container, false)

        return binding.root

    }

    fun clearList() {
        val adapter = binding.bikeStationsList.adapter ?: return
        (adapter as BikeStationsListAdapter).clearList()
        binding.updateInfo.visibility = View.VISIBLE
        binding.progressBar.visibility = View.INVISIBLE
    }




    fun showList(){
        // SHows the list and sets the item click listener
        binding.updateInfo.visibility = View.INVISIBLE
        binding.progressBar.visibility = View.INVISIBLE
        with(binding.bikeStationsList) {
            layoutManager = LinearLayoutManager(context)
            adapter = BikeStationsListAdapter(ArrayList(bikeStations), object :
                BikeStationListEventListener {
                override fun clickListener(
                    position: Int,
                    bikeStation: BikeStation
                ) {
                    val actionListFragmentToDetailFragment =
                        ListFragmentDirections.actionListFragmentToDetailFragment(
                            bikeStation
                        ) // safe args are used to pass data between fragemnts
                    findNavController().navigate(actionListFragmentToDetailFragment)
                }

            }
            )
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(bikeStations != null)
            showList()
    }

    fun populateListFromInternet(){
        // Change the visibility of the progress bar (visible) and TextView that displays the message (invisible)
        binding.updateInfo.visibility = View.INVISIBLE
        binding.progressBar.visibility = View.VISIBLE

        // run the following code in a background thread by using Coroutines in GlobalScope
        GlobalScope.launch {
            // Call a suspend method that will retrieve the bike station list from the remote
            // service using retrofit
            getBikeStationsFromRetrofit()

            // Alternative version of the way of obtaining bike stations list
            // getBikeStationsFromRetrofitWithQuery()

            // When the list is obtained display a snackbar indicating the size of bikeStations list
            // and display the list with the showList method
            withContext(Dispatchers.Main){
                Snackbar.make(
                    binding.root,
                    "${bikeStations?.size}" + "bike stations",
                    Snackbar.LENGTH_LONG
                ).show()
                showList()
            }
        }
    }

    private suspend fun getBikeStationsFromRetrofit() {
        try {
            // Use a globalScope coroutine with IO dispatcher to get the bikeStations form
            // the remote service. async method is used to store the list from the results to
            // the bikeStations variable of the ListFragment class
            bikeStations = GlobalScope.async {
                withContext(Dispatchers.IO){
                    // Access the retrofit service with the BikeStationAPI object. The getBikeStations
                    // method from the BikeStationAPIService interface is used to fetch the data
                    BikeStationApi.retrofitService.getBikeStations().bikeStations
                }
            }.await()
        } catch (e: Exception){
            Log.e(activity?.localClassName, e.message.toString())
        }
    }

    private suspend fun getBikeStationsFromRetrofitWithQuery(){
        try {
            // Use a GlobalScope coroutine with IO dispatcher to get the bikeStations form
            // the remote service. async method is used to store the list from the results to
            // the bikeStations variable of the ListFragment class
            bikeStations = GlobalScope.async {
                withContext(Dispatchers.IO){
                    // Access the retrofit service with the BikeStationApi object. The getBikeStations(String, String)
                    // method from the BikeStationApiService interface is used to fetch the data with a query
                    // specified by the arguments of the method
                    BikeStationApi.retrofitService.getBikeStations(
                        "pub_transport",
                        "stacje_rowerowe"
                    ).bikeStations
                }
            }.await()
        } catch (e: Exception){
            Log.e(activity?.localClassName, e.message.toString())
        }
    }

    fun populateListFromDB(queryId: BikeStationDatabase.QUERYID){
        // Change the visibility of the progress bar (visible) and TextView that displays the message (invisible)
        binding.updateInfo.visibility = View.INVISIBLE
        binding.progressBar.visibility = View.VISIBLE

        // run the following code in a background thread by using Coroutines in GlobalScope
        GlobalScope.launch {
            // Call a suspend method that will retrieve the bike station list from the database
            // with a query given by queryId
            getBikeStationsFromRoom(queryId)

            // When the list is obtained display a snackbar indicating the size of bikeStations list
            // and display the list with the showList method.
            withContext(Dispatchers.Main){
                Snackbar.make(
                    binding.root,
                    "${bikeStations?.size} bike stations",
                    Snackbar.LENGTH_LONG
                ).show()
            }

            if(bikeStations?.isNotEmpty() == true){
                // If the database is not empty show the list
                showList()
            } else{
                // Hide the progress bar and show the message otherwise
                binding.progressBar.visibility = View.INVISIBLE
                binding.updateInfo.visibility = View.VISIBLE
            }
        }

    }

    private suspend fun getBikeStationsFromRoom(queryId: BikeStationDatabase.QUERYID) {
        // Get a hold to a BikeStationDatabase DAO
        val bikeStationDAO =
            BikeStationDatabase.getInstance(requireContext()).bikeStationDatabaseDao
        // In the background task perform the database query and store the result in async variable
        val async = GlobalScope.async {
            withContext(Dispatchers.IO){
                when(queryId){
                    BikeStationDatabase.QUERYID.GET_STATIONS_WITH_BIKES -> {
                        // Get all bike stations with bikes
                        bikeStationDAO.getStationsWithBikes(0)
                    }
                    BikeStationDatabase.QUERYID.GET_STATIONS_WITH_FREE_RACKS -> {
                        // Get all bike stations with free racks
                        bikeStationDAO.getStationsWithFreeRacks(0)
                    }
                    BikeStationDatabase.QUERYID.GET_ALL -> {
                        // Get all bike stations
                        bikeStationDAO.getAll()
                    }
                    else -> bikeStationDAO.getAll()
                }
            }
        }
        // Since the object stored in the database are of type BikeStationDB and the bikeStations
        // list stores objects of type BikeStation we use a helper method from the Helpers class
        // to convert the database list to list that can be displayed by the ListFragments'
        // RecyclerView Adapter
        bikeStations = Helpers.createBikeStationList(async.await()).toList()
    }

}
