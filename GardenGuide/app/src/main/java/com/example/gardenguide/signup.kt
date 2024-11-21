package com.example.gardenguide

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.gardenguide.databinding.ActivitySignupBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Signup : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

        binding.signupButton.setOnClickListener{
            val signupUsername = binding.signupUsername.text.toString()
            val signupPassword = binding.signupPassword.text.toString()
            val signupEmail = binding.signupEmail.text.toString()

            if (signupUsername.isNotEmpty() && signupPassword.isNotEmpty() && signupEmail.isNotEmpty()){
                signupUser(signupUsername, signupPassword, signupEmail)
            } else {
                Toast.makeText(this@Signup,"Fill all Fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.textLogin.setOnClickListener{
            startActivity(Intent(this@Signup, Login::class.java))
            finish()
        }

    }

    private fun signupUser(username: String, password: String, email: String){
        databaseReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object :ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.exists()){
                    val id = databaseReference.push().key
                    val userData = UserData(id, username, password, email)
                    databaseReference.child(id!!).setValue(userData)
                    Toast.makeText(this@Signup,"SignUp Successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@Signup, Login::class.java))
                    finish()
                } else {
                    Toast.makeText(this@Signup,"User Already exists", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@Signup,"Database Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

}