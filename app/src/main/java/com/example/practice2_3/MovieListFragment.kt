package com.example.practice2_3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MovieListFragment : Fragment() {
    private lateinit var list: ListView
    private lateinit var data: ArrayList<Movies>
    private val database = Firebase.database
    private val myRef = database.getReference("movies")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list = view.findViewById(R.id.list)
        data = ArrayList()

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                data.clear()
                snapshot.children.forEach { child ->
                    try {
                        val movie = child.getValue(Movie::class.java)?.let {
                            Movies(
                                it.name,
                                it.year,
                                it.genre,
                                child.key ?: "",
                                it.latitude,
                                it.longitude
                            )
                        }
                        movie?.let { data.add(it) }
                    } catch (e: Exception) {
                        Toast.makeText(
                            requireContext(),
                            "Error parsing movie: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                list.adapter = MovieAdapter(requireActivity(), data)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Database error", Toast.LENGTH_SHORT).show()
            }
        })

        /* Firebase.database.reference.child("movies").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                data.clear()
                snapshot.children.forEach { child ->
                    val movie = Movies(
                        child.child("name").value.toString(),
                        child.child("year").value.toString(),
                        child.child("genre").value.toString(),
                        child.key.toString(),
                        child.child("latitude").getValue(Double::class.java) ?: 0.0,
                        child.child("longitude").getValue(Double::class.java) ?: 0.0
                    )
                    data.add(movie)
                }
                list.adapter = MovieAdapter(requireActivity(), data)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Database error", Toast.LENGTH_SHORT).show()
            }
        })

        */

        list.setOnItemClickListener { _, _, position, _ ->
            val movie = data[position]
            val fragment = EditFragment().apply {
                arguments = Bundle().apply {
                    putString("id", movie.id)
                    putString("name", movie.name)
                    putString("year", movie.year)
                    putString("genre", movie.genre)
                }
            }

//            println(data[position].latitude.toString())
//            println(data[position].longitude.toString())


            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack("edit_movie")
                .commit()
        }
    }
}