package com.example.practice2_3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Edit : AppCompatActivity() {

    val database = Firebase.database
    val myRef = database.getReference("movies")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val goBackMain = findViewById<Button>(R.id.Cancel)

        goBackMain.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        val params = intent.extras

        val name = findViewById<EditText>(R.id.editName)
        val genre = findViewById<EditText>(R.id.editGenre)
        val year = findViewById<EditText>(R.id.editYear)

        name.setText(params?.getCharSequence("name").toString())
        genre.setText(params?.getCharSequence("genre").toString())
        year.setText(params?.getCharSequence("year").toString())


        val btnEditMovie = findViewById<Button>(R.id.editMovieBtn)
        btnEditMovie.setOnClickListener {
            var movieObj = Movie(name.text.toString(), year.text.toString(), genre.text.toString())
            myRef.child(params?.getCharSequence("id").toString()).setValue(movieObj)
                .addOnCompleteListener {
                task ->
                if (task.isSuccessful){
                    Toast.makeText(this,"Movie edited Successfully",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this,task.exception!!.message.toString(),Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}