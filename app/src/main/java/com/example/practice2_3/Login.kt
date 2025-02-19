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
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val emailKey = "example@gmail.com"
        val passwordKey = "12345678"


        val btnGoMain = findViewById<Button>(R.id.login)
        btnGoMain.setOnClickListener{

            var email = findViewById<EditText>(R.id.email).text.toString()
            var password = findViewById<EditText>(R.id.password).text.toString()
            println(email+"   "+password)

            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{
                result ->
                if (result.isSuccessful){
                    Toast.makeText(this, "User authenticated successfully", Toast.LENGTH_LONG).show()
                    goMain()
                }else{
                    Toast.makeText(this, "Error: "+result.exception?.message ?: "", Toast.LENGTH_LONG).show()
                }
            }

        }

        auth = FirebaseAuth.getInstance()
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser

        if(currentUser==null){
            Toast.makeText(this, "There is no authenticated users", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "Hello"+currentUser.email.toString(), Toast.LENGTH_LONG).show()
            goMain()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun goMain(){
        startActivity(Intent(this, MainActivity::class.java).putExtra("idUser",auth.currentUser!!.uid.toString()))
    }

}