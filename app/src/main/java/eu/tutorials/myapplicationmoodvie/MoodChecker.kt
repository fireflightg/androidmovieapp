package eu.tutorials.myapplicationmoodvie
import androidx.camera.view.PreviewView


import android.content.Intent

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Size
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MoodChecker : AppCompatActivity() {
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var emotionTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mood_checker)

        emotionTextView = findViewById(R.id.emotion_text)
        cameraExecutor = Executors.newSingleThreadExecutor()

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(findViewById<PreviewView>(R.id.captureImageView).surfaceProvider)
                }

            val highAccuracyOpts = FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .build()

            val imageAnalysis = ImageAnalysis.Builder()
                .setTargetResolution(Size(640, 480))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, ImageAnalysis.Analyzer { imageProxy ->
                        val rotationDegrees = imageProxy.imageInfo.rotationDegrees
                        val image = InputImage.fromMediaImage(imageProxy.image!!, rotationDegrees)

                        val detector = FaceDetection.getClient(highAccuracyOpts)
                        detector.process(image)
                            .addOnSuccessListener { faces ->
                                for (face in faces) {
                                    val emotion = when {
                                        face.smilingProbability ?: 0f > 0.5f -> "Smiling"
                                        face.smilingProbability ?: 1f < 0.3f && (face.rightEyeOpenProbability ?: 1f < 0.5f || face.leftEyeOpenProbability ?: 1f < 0.5f) -> "Sad"
                                        face.smilingProbability ?: 1f < 0.3f -> "Frowning"
                                        else -> "Sad"
                                    }
                                    val nextActivityClass = when (emotion) {
                                        "Smiling" -> HappyMovies::class.java
                                        "Sad" -> SadMovies::class.java
                                        "Frowning" -> MoodMovies::class.java
                                        else -> null
                                    }
                                    runOnUiThread {
                                        emotionTextView.text = emotion
                                        findViewById<com.google.android.material.button.MaterialButton>(R.id.capture_btn).setOnClickListener {
                                            nextActivityClass?.let { cls ->
                                                val intent = Intent(this@MoodChecker, cls)
                                                startActivity(intent)
                                            }
                                        }
                                    }
                                }
                            }
                            .addOnFailureListener { e ->
                                // Handle exception
                            }
                            .addOnCompleteListener {
                                imageProxy.close()
                            }
                    })
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, preview, imageAnalysis)
            } catch (exc: Exception) {
                // Handle exception
            }
        }, ContextCompat.getMainExecutor(this))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                // Handle permission not granted
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
