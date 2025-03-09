package com.example.practice2_3

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.commit
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val database = Firebase.database
    private val moviesRef = database.getReference("movies")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Setup toolbar
        findViewById<Toolbar>(R.id.toolbar).apply {
            setSupportActionBar(this)
        }

        // Load initial fragment
        supportFragmentManager.commit {
            replace(R.id.fragment_container, MovieListFragment())
            addToBackStack(null)
        }

        // FAB Click Listener. HERE IS WHERE I NEED TO CREATE THE MOVIE
        findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener {
            val fragment = EditFragment().apply {
                arguments = Bundle().apply {
                    putString("id", "")
                }
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack("New Movie")
                .commit()

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                auth.signOut()
                startActivity(Intent(this, Login::class.java))
                finish()
            }
            R.id.profile -> Toast.makeText(this, "Go to profile", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }
}