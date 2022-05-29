package com.example.poznanbike

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.poznanbike.bikestations.BikeStation
import com.example.poznanbike.database.BikeStationDatabase
import com.example.poznanbike.databinding.FragmentDetailBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DetailFragment : Fragment() {

    // view binding variable that allows for easy access to the Fragments' Views
    lateinit var binding: FragmentDetailBinding

    val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDetailBinding.inflate(inflater, container, false) // setup the binding variable
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bikeStation: BikeStation = args.bikeStation
        val numBikes: Int = bikeStation.properties.bikes.toInt()
        // Display the details of the selected BikeStation - access to all Views is made with
        // View Binding via binding variable
        binding.bikeNumberDetail.text = bikeStation.properties.bikes

        when(numBikes){
            0 -> binding.bikeImgDetail.setColorFilter(Helpers.getColor(requireContext(), R.color.my_red))
            in 1..5 -> binding.bikeImgDetail.setColorFilter(Helpers.getColor(requireContext(), R.color.my_orange))
            else -> binding.bikeImgDetail.setColorFilter(Helpers.getColor(requireContext(), R.color.my_green))
        }
        val numRacks: Int = bikeStation.properties.freeRacks.toInt()
        binding.rackNumberDetail.text = bikeStation.properties.freeRacks
        when(numRacks){
            0 -> binding.parkImgDetail.setColorFilter(Helpers.getColor(requireContext(), R.color.my_red))
            in 1..5 -> binding.parkImgDetail.setColorFilter(Helpers.getColor(requireContext(), R.color.my_orange))
            else -> binding.parkImgDetail.setColorFilter(Helpers.getColor(requireContext(), R.color.my_green))
        }
        binding.stationNameDetail.text = bikeStation.properties.label
        binding.updateTimeDetail.text = bikeStation.properties.updated
        val longitude = bikeStation.geometry.coordinates[0]
        val latitude = bikeStation.geometry.coordinates[1]

        binding.coordinatesDetail.text = getString(R.string.coordinates).format(
            "Lat:" , latitude, "Lng:", longitude)
    }

    fun saveToDatabase(){
        // Get a hold to a BikeStationDatabase DAO
        val bikeStationDAO =
            BikeStationDatabase.getInstance(requireContext()).bikeStationDatabaseDao

        // In the background task created with coroutine (GlobalScope and IO dispatcher) run the following code
        GlobalScope.launch {
            withContext(Dispatchers.IO){
                // Check whether a bike station with a given label already exists in the database
                val byLabel = bikeStationDAO.getByLabel(args.bikeStation.properties.label)
                val op = if (byLabel.isEmpty()){
                    // If the bike station isn't in the database insert it and set the message (op variable)
                    // to "Saved"
                    bikeStationDAO.insert(Helpers.createBikeStationDB(args.bikeStation))
                    "Saved"
                } else{
                    // if the bike station is already in the database, update it and set the message
                    // to "Updated"
                    "Updated"
                }
                // Display the status of the operation to the user with a snackbar
                Snackbar.make(
                    binding.root,
                    "$op ${args.bikeStation.properties.label}",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }


}