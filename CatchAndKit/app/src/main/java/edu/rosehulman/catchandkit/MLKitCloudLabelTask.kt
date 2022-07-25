package edu.rosehulman.catchandkit

import android.graphics.Bitmap
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage

class MLKitCloudLabelTask: MLKitTask {
    override fun execute(downloadUri: String, bitmap: Bitmap, thumbnailRef: CollectionReference) {
        val image = FirebaseVisionImage.fromBitmap(bitmap)
        val labeler = FirebaseVision.getInstance().cloudImageLabeler

        labeler.processImage(image)
            .addOnSuccessListener { result ->
                val labels = result.joinToString { it.text }
                thumbnailRef.add(Thumbnail(downloadUri, labels))
            }
    }
}