package com.example.reddittest.ui.main.utils.workers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.reddittest.ui.main.utils.KEY_IMAGE_URL
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
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
            val resourceUrl = inputData.getString(KEY_IMAGE_URL)

            val bitmap: Bitmap?
            try {
                val inputStream: InputStream = URL(resourceUrl).openStream()
                bitmap = BitmapFactory.decodeStream(inputStream)
            } catch (e: IOException) {
                throw IllegalArgumentException("Invalid input url")
            }

            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)

            val byteArray = stream.toByteArray()

            // Finally, return the compressed bitmap
            val compressedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

            val imageUrl = MediaStore.Images.Media.insertImage(
                resolver, compressedBitmap, Title, dateFormatter.format(Date())
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
