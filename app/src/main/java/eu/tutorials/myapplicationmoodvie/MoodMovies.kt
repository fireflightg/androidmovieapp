package eu.tutorials.myapplicationmoodvie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MoodMovies : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mood_movies)

        val nextButton = findViewById<Button>(R.id.buttonYes)
        nextButton.setOnClickListener {
            val intent = Intent(this, HappyMovies::class.java)
            startActivity(intent) // Starts Mood Checker with the Clicklistener once the button gets presssed

            val nextButton = findViewById<Button>(R.id.buttonNo)
            nextButton.setOnClickListener {
                val intent = Intent(this, SadMovies::class.java)
                startActivity(intent) // Starts Mood Checker with the Clicklistener once the button gets presssed
        }
    }
} }