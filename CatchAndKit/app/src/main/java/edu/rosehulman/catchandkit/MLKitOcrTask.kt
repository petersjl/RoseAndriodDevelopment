package edu.rosehulman.catchandkit

import android.graphics.Bitmap
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage

class MLKitOcrTask: MLKitTask {

    override fun execute(downloadUri: String, bitmap: Bitmap, thumbnailRef: CollectionReference) {
        val image = FirebaseVisionImage.fromBitmap(bitmap)
        val detector = FirebaseVision.getInstance().onDeviceTextRecognizer
        val result = detector.processImage(image).addOnSuccessListener { result ->
            thumbnailRef.add(Thumbnail(downloadUri, result.text))
        }
    }
}
