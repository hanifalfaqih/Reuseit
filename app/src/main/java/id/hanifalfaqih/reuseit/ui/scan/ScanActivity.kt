package id.hanifalfaqih.reuseit.ui.scan

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import id.hanifalfaqih.reuseit.R
import id.hanifalfaqih.reuseit.databinding.ActivityScanBinding
import id.hanifalfaqih.reuseit.helper.rotateBitmapIfRequired
import id.hanifalfaqih.reuseit.ml.ImageClassifier
import id.hanifalfaqih.reuseit.ml.wasteProcessingInfo
import id.hanifalfaqih.reuseit.ui.scan.resultscan.ResultScanFragment
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ScanActivity : AppCompatActivity(), ImageCapture.OnImageSavedCallback {

    private var _binding: ActivityScanBinding? = null
    private val binding get() = _binding!!


    private lateinit var classifier: ImageClassifier
    private lateinit var imageCapture: ImageCapture
    private var imageUri: Uri? = null
    private var cameraExecutor: ExecutorService? = null
    private var cameraProvider: ProcessCameraProvider? = null

    private val backCameraSelector by lazy {
        CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
    }

    private val cameraPermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) startCamera()
            else Toast.makeText(this, "Camera permission not granted.", Toast.LENGTH_SHORT).show()
        }

    private val pickImage = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            imageUri = uri
            displayImageFromGallery(uri)
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityScanBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inisialisasi classifier
        classifier = ImageClassifier(
            this, R.raw.model_sampah, R.raw.labels
        )

        cameraExecutor = Executors.newSingleThreadExecutor()

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            cameraPermissionResult.launch(Manifest.permission.CAMERA)
        } else {
            startCamera()
        }

        binding.btnGallery.setOnClickListener {
            pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.btnReset.setOnClickListener {
            resetPreview()
        }

        binding.btnCamera.setOnClickListener {
            takePicture()
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            bindCameraUseCases()
        }, ContextCompat.getMainExecutor(this))
    }

    private fun bindCameraUseCases() {
        val preview = Preview.Builder().build().also {
            it.surfaceProvider = binding.imageView.surfaceProvider
        }

        imageCapture = ImageCapture.Builder().build()

        try {
            cameraProvider?.unbindAll()
            cameraProvider?.bindToLifecycle(
                this,
                backCameraSelector,
                preview,
                imageCapture
            )
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to bind camera use cases", Toast.LENGTH_SHORT).show()
        }
    }

    private fun resetPreview() {
        binding.imageViewPicker.visibility = View.GONE
        binding.imageView.visibility = View.VISIBLE
        imageUri = null
        cameraProvider?.unbindAll()
        startCamera()
        Toast.makeText(this, "Preview reset successfully", Toast.LENGTH_SHORT).show()
    }

    private fun takePicture() {
        if (!this::imageCapture.isInitialized) return

        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            this.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "IMG_${System.currentTimeMillis()}")
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            }
        ).build()

        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(this), this)
    }

    private fun displayImageFromCamera(uri: Uri) {
        val bitmap = BitmapFactory.decodeStream(this.contentResolver.openInputStream(uri))
        val rotatedBitmap = rotateBitmapIfRequired(this, uri, bitmap) // Perbaiki rotasi
        classifyAndDisplayResult(rotatedBitmap)
    }

    private fun displayImageFromGallery(uri: Uri) {
        val bitmap = BitmapFactory.decodeStream(this.contentResolver.openInputStream(uri))
        val rotatedBitmap = rotateBitmapIfRequired(this, uri, bitmap) // Perbaiki rotasi
        classifyAndDisplayResult(rotatedBitmap)
    }

    private fun classifyAndDisplayResult(bitmap: Bitmap) {
        binding.imageView.visibility = View.GONE
        binding.imageViewPicker.visibility = View.VISIBLE
        binding.imageViewPicker.setImageBitmap(bitmap)

        // Jalankan klasifikasi
        val result = classifier.classify(bitmap)

        ResultScanFragment.newInstance(result).show(supportFragmentManager, "ResultScanFragment")

        // Tampilkan hasil
//        Toast.makeText(this, "Classification Result: $result", Toast.LENGTH_LONG).show()
    }

    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
        imageUri = outputFileResults.savedUri
        imageUri?.let {
            displayImageFromCamera(it)
        } ?: Toast.makeText(this, "Image saved but URI is null", Toast.LENGTH_SHORT).show()
    }

    override fun onError(exception: ImageCaptureException) {
        Toast.makeText(this, "Failed to save image: ${exception.message}", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor?.shutdown()
        _binding = null
    }
}
