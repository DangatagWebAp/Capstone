package com.example.gardenguide

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.gardenguide.databinding.ActivityLoginBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

        binding.loginButton.setOnClickListener{
            val loginPassword = binding.loginPassword.text.toString()
            val loginUsernameOrEmail = binding.loginEmail.text.toString()

            if (loginPassword.isNotEmpty() && loginUsernameOrEmail.isNotEmpty()) {
                loginUser(loginUsernameOrEmail, loginPassword)
            } else {
                Toast.makeText(this@Login, "Fill all Fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.textRegister.setOnClickListener{
            startActivity(Intent(this@Login, Signup::class.java))
            finish()
        }

    }

    private fun loginUser(usernameOrEmail: String, password: String) {
        databaseReference.orderByChild("username").equalTo(usernameOrEmail).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (userSnapshot in dataSnapshot.children) {
                        val userData = userSnapshot.getValue(UserData::class.java)

                        if (userData != null && userData.password == password) {
                            Toast.makeText(this@Login, "Login Successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@Login, MainActivity::class.java))
                            finish()
                            return
                        }
                    }
                    Toast.makeText(this@Login, "Invalid username or password", Toast.LENGTH_SHORT).show()
                } else {
                    databaseReference.orderByChild("email").equalTo(usernameOrEmail).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (userSnapshot in dataSnapshot.children) {
                                    val userData = userSnapshot.getValue(UserData::class.java)

                                    if (userData != null && userData.password == password) {
                                        Toast.makeText(this@Login, "Login Successful", Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this@Login, MainActivity::class.java))
                                        finish()
                                        return
                                    }
                                }
                            } else {
                                Toast.makeText(this@Login, "User does not exist", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            Toast.makeText(this@Login, "Database error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@Login, "Database error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}