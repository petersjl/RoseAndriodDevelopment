package edu.rosehulman.catchandkit

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import kotlin.math.abs
import kotlin.random.Random

class ThumbnailAdapter(
    val context: Context,
    private val listener: ThumbnailGridFragment.OnThumbnailListener?
) : RecyclerView.Adapter<ThumbnailViewHolder>() {
    private val thumbnails = ArrayList<Thumbnail>()


    val thumbnailRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.THUMBNAIL_COLLECTION)

    // TODO: Add storage ref.
    private val storageRef = FirebaseStorage.getInstance().reference.child("images")



    init {
        thumbnailRef.addSnapshotListener { snapshot: QuerySnapshot?, exception: FirebaseFirestoreException? ->
            Log.d(Constants.TAG, "Adding snapshot listener")
            if (exception != null) {
                Log.e(Constants.TAG, "Firebase error: $exception")
            }
            processThumbnailDiffs(snapshot!!)
        }
    }

    private fun processThumbnailDiffs(snapshot: QuerySnapshot) {
        for (documentChange in snapshot.documentChanges) {
            val thumbnail = Thumbnail.fromSnapshot(documentChange.document)
            when (documentChange.type) {
                DocumentChange.Type.ADDED -> {
                    if (thumbnail.url.isNotEmpty()) {
                        thumbnails.add(0, thumbnail)
                        notifyItemInserted(0)
                    }
                }
                DocumentChange.Type.REMOVED -> {
                    val index = thumbnails.indexOfFirst { it.id == thumbnail.id }
                    thumbnails.removeAt(index)
                    notifyItemRemoved(index)
                }
                DocumentChange.Type.MODIFIED -> {
                    val index = thumbnails.indexOfFirst { it.id == thumbnail.id }
                    thumbnails[index] = thumbnail
                    notifyItemChanged(index)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThumbnailViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.thumbnail_item, parent, false)
        return ThumbnailViewHolder(itemView, context, this)
    }

    override fun getItemCount() = thumbnails.size

    override fun onBindViewHolder(viewHolder: ThumbnailViewHolder, position: Int) {
        viewHolder.bind(thumbnails[position])
    }

    fun onThumbnailSelected(position: Int) {
        listener?.onThumbnailSelected(thumbnails[position])
    }

    fun onDeleteThumbnail(position: Int) {
        val thumb = thumbnails[position]
        val img = FirebaseStorage.getInstance().getReferenceFromUrl(thumb.url)
        img.delete()
        thumbnailRef.document(thumb.id).delete()
        notifyItemRemoved(position)
    }

    fun add(localPath: String) {
        // TODO: You'll want to wait to add this to Firetore until after you have a Storage download URL.
        // Move this line of code there.
        //thumbnailRef.add(Thumbnail(localPath))
        ImageRescaleTask(localPath).execute()
    }

    // Could save a smaller version to Storage to save time on the network.
    // But if too small, recognition accuracy can suffer.
    inner class ImageRescaleTask(val localPath: String) : AsyncTask<Void, Void, Bitmap>() {
        override fun doInBackground(vararg p0: Void?): Bitmap? {
            // Reduces length and width by a factor (currently 2).
            val ratio = 2
            return BitmapUtils.rotateAndScaleByRatio(context, localPath, ratio)
        }

        override fun onPostExecute(bitmap: Bitmap?) {
            // that uses Firebase storage.
            // https://firebase.google.com/docs/storage/android/upload-files
            storageAdd(localPath, bitmap)
        }
    }

    private fun storageAdd(localPath: String, bitmap: Bitmap?){
        val baos = ByteArrayOutputStream()
        bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100 , baos)
        val data = baos.toByteArray()
        val id = abs(Random.nextLong()).toString()
        var uploadTask = storageRef.child(id).putBytes(data)

        uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (!task.isSuccessful){
                task.exception?.let {
                    throw it
                }
            }
            return@Continuation storageRef.child(id).downloadUrl
        }).addOnCompleteListener() { task ->
            if (task.isSuccessful){
                val downloadUri = task.result
//                thumbnailRef.add(Thumbnail(downloadUri.toString()))
//                val task = MLKitOcrTask()
                val task :MLKitTask= when(listener!!.getTaskType()) {
                    MLKitTaskType.OCR -> MLKitOcrTask()
                    MLKitTaskType.OCR_CLOUD -> MLKitCloudOcrTask()
                    MLKitTaskType.LABEL -> MLKitLabelTask()
                    MLKitTaskType.LABEL_CLOUD -> MLKitCloudLabelTask()
                    MLKitTaskType.FACE -> MLKitFaceTask()
                }
                task.execute(downloadUri.toString(), bitmap!!, thumbnailRef)
            }
        }
    }

}