package eu.tutorials.myapplicationmoodvie

import eu.tutorials.myapplicationmoodvie.MoodChecker
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val nextButton = findViewById<Button>(R.id.button)
        nextButton.setOnClickListener {
            val intent = Intent(this, MoodChecker::class.java)
            startActivity(intent) // Starts Mood Checker with the Clicklistener once the button gets presssed
        }
    }
}