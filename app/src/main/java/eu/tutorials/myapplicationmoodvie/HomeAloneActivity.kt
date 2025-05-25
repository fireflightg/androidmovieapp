package eu.tutorials.myapplicationmoodvie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


class HomeAloneActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_alone)

    }
    fun goBackToHappyMovies() {
        val intent = Intent(this, HappyMovies::class.java)
        startActivity(intent)
        finish() // Optional, if you want to close the current activity
    }



    /*val playAloneMovie = findViewById<Button>(R.id.playButton)
    playAlonemovie.setOnClickListener {
        val intent = Intent(this, HomeAloneMovie::class.java)
        startActivity(intent) // Starts the movie with the Clicklistener once the button gets presssed
    }*/

}