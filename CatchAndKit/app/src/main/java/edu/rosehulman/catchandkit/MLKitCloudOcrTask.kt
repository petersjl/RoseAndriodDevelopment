package edu.rosehulman.catchandkit

import android.graphics.Bitmap
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions
import java.util.*

class MLKitCloudOcrTask: MLKitTask {

    override fun execute(downloadUri: String, bitmap: Bitmap, thumbnailRef: CollectionReference) {
        val image = FirebaseVisionImage.fromBitmap(bitmap)
        val options = FirebaseVisionCloudTextRecognizerOptions.Builder()
            .setLanguageHints(Arrays.asList("en", "hi"))
            .build()
        val detector = FirebaseVision.getInstance().getCloudTextRecognizer(options)
        val result = detector.processImage(image).addOnSuccessListener { result ->
            thumbnailRef.add(Thumbnail(downloadUri, result.text))
        }
    }
}