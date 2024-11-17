package com.example.gardenguide

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainDash : AppCompatActivity() {
    private lateinit var viewFlipper: ViewFlipper
    private lateinit var nextButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        viewFlipper = findViewById(R.id.viewFlipper)
        nextButton = findViewById(R.id.button)

        nextButton.setOnClickListener {
            viewFlipper.showNext()
        }

        val login: TextView = findViewById(R.id.textLogin)
        login.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

    }
}