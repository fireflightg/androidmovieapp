package eu.tutorials.myapplicationmoodvie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.net.Uri
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.VideoView

class HappyMovies : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_happy_movies)

        val videoView = findViewById<VideoView>(R.id.videoViewTrailer)

        // The path of my video file
        val videoPath = "android.resource://" + packageName + "/" + R.raw.norbittrailer

        // Set the video URI
        val uri = Uri.parse(videoPath)
        videoView.setVideoURI(uri)

        // This starts playing the video
        videoView.start()

        // Find the ImageView
        val homeAloneButton = findViewById<ImageView>(R.id.homeAloneButton)

        // Set OnClickListener to navigate to HomeAloneActivity
        homeAloneButton.setOnClickListener {
            val intent = Intent(this, HomeAloneActivity::class.java)
            startActivity(intent)
        }
    }
}
