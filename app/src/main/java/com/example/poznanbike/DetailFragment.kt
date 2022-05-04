package com.example.poznanbike

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.poznanbike.bikestations.BikeStation
import com.example.poznanbike.databinding.FragmentDetailBinding



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




}