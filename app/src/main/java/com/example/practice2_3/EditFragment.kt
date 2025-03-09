package com.example.practice2_3

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import android.Manifest
import androidx.core.os.requestProfiling


class EditFragment : Fragment() {
    private lateinit var name: EditText
    private lateinit var genre: EditText
    private lateinit var year: EditText
    private lateinit var latitude: EditText
    private lateinit var longitude: EditText


    private val database = Firebase.database
    private val myRef = database.getReference("movies")

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLat: Double = 0.0
    private var currentLon: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Get location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        getLastLocation()


        // Initialize UI components
        name = view.findViewById(R.id.editName)
        genre = view.findViewById(R.id.editGenre)
        year = view.findViewById(R.id.editYear)
        val btnEditMovie = view.findViewById<Button>(R.id.editMovieBtn)
        val goBackMain = view.findViewById<Button>(R.id.Cancel)

        // Get arguments
        val args = arguments
        val movieId = args?.getString("id")
        val movieName = args?.getString("name")
        val movieGenre = args?.getString("genre")
        val movieYear = args?.getString("year")

        // Set initial values
        name.setText(movieName)
        genre.setText(movieGenre)
        year.setText(movieYear)

        // Set up button click listeners
        goBackMain.setOnClickListener {
            // Navigate back using FragmentManager
            parentFragmentManager.popBackStack()
        }

        btnEditMovie.setOnClickListener {
            getLocationAndSave()
        }
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(), // Changed here
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    currentLat = it.latitude
                    currentLon = it.longitude
                }
            }
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLocationAndSave()
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

    // EditFragment.kt
    private fun getLocationAndSave() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    saveMovie(it.latitude, it.longitude)
                } ?: run {
                    Toast.makeText(
                        requireContext(),
                        "Couldn't get location",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun saveMovie(lat: Double, lon: Double) {
        val movieId = arguments?.getString("id") ?: ""
        val movieRef = if (movieId.isEmpty()) {
            // Create new movie with generated ID
            myRef.push()
        } else {
            // Update existing movie
            myRef.child(movieId)
        }

        val movie = Movie(
            name.text.toString(),
            year.text.toString(),
            genre.text.toString(),
            lat,
            lon
        )

        movieRef.setValue(movie).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(requireContext(), "Movie saved", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            } else {
                Toast.makeText(requireContext(), "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

}



