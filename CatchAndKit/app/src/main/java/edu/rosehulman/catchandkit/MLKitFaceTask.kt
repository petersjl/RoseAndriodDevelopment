package edu.rosehulman.catchandkit

import android.graphics.Bitmap
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions

class MLKitFaceTask: MLKitTask {
    override fun execute(downloadUri: String, bitmap: Bitmap, thumbnailRef: CollectionReference) {
        val options = FirebaseVisionFaceDetectorOptions.Builder()
            .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
            .build()
        val image = FirebaseVisionImage.fromBitmap(bitmap)
        val detector = FirebaseVision.getInstance().getVisionFaceDetector(options)
        val result = detector.detectInImage(image)
            .addOnSuccessListener { faces -> 
                var probs = mutableListOf<String>()
                for ((faceNumber, face) in faces.withIndex()){
                    probs.add("Face$faceNumber smile probability: ${if (face.smilingProbability != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) face.smilingProbability else "Not Computed"}")
                }
                thumbnailRef.add(Thumbnail(downloadUri, probs.toString()))
            }
    }
}