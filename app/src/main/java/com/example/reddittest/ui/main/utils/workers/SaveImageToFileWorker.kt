package com.example.reddittest.ui.main.utils.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.reddittest.ui.main.utils.KEY_IMAGE_URL
import java.text.SimpleDateFormat
import java.util.*

/**
 * Saves the image to a permanent file
 */
class SaveImageToFileWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    private val Title = "Image"
    private val dateFormatter = SimpleDateFormat(
        "yyyy.MM.dd 'at' HH:mm:ss z",
        Locale.getDefault()
    )

    override fun doWork(): Result {
        // Makes a notification when the work starts
        makeStatusNotification("Saving image", applicationContext)

        val resolver = applicationContext.contentResolver
        return try {
            val resourceUri = inputData.getString(KEY_IMAGE_URL)
            val bitmap = BitmapFactory.decodeStream(
                resolver.openInputStream(Uri.parse(resourceUri))
            )
            val imageUrl = MediaStore.Images.Media.insertImage(
                resolver, bitmap, Title, dateFormatter.format(Date())
            )
            if (!imageUrl.isNullOrEmpty()) {
                val output = workDataOf(KEY_IMAGE_URL to imageUrl)

                makeStatusNotification("Image saved!", applicationContext)
                Result.success(output)
            } else {
                Log.e("SaveImageToFileWorker", "failure saving image")
                Result.failure()
            }
        } catch (exception: Exception) {
            // Timber.e(exception)
            Log.e("SaveImageToFileWorker", exception.message ?: "exception")
            Result.failure()
        }
    }
}
