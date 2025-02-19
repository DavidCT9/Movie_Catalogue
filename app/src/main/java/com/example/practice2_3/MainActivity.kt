package com.example.practice2_3

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth;

    lateinit var data : ArrayList<Movies>

    val database = Firebase.database
    val myRef = database.getReference("movies")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

//        fillList()

//        val btnGoEdit = findViewById<Button>(R.id.gotoEdit)
//        btnGoEdit.setOnClickListener{
//            startActivity(Intent(this, Edit::class.java))
//        }

        auth = FirebaseAuth.getInstance()

        val params = intent.extras

//        val btnLogout = findViewById<Button>(R.id.logout)
//        btnLogout.text = params?.getString("idUser").toString()
//
//        btnLogout.setOnClickListener{
//            auth.signOut()
//            startActivity(Intent(this, Login::class.java))
//            finish()
//        }

        readDB()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId== R.id.logout){
            auth.signOut()
            startActivity(Intent(this, Login::class.java))
            finish()
        }else if(item.itemId == R.id.profile){
            Toast.makeText(this, "Go to profile", Toast.LENGTH_SHORT).show()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun fillList(){
//        data = ArrayList<Movies>()
//        var movie = Movies("Duro de matar", "1995", "Acción", "1")
//
//        data.add(movie)


        val list = findViewById<ListView>(R.id.list)
        list.adapter = MovieAdapter(this, data)
    }

    private fun readDB() {
        myRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = snapshot.value
                Log.d(TAG, "Value is: " + value)
                data = ArrayList<Movies>()

                snapshot.children.forEach{
                    son ->
                        var movie = Movies(son.child("name").value.toString() ?: "", son.child("year").value.toString() ?: "", son.child("genre").value.toString() ?: "",son.key.toString())
                        data.add(movie)
                }

                fillList()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }

        })
    }

}