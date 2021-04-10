package com.example.reddittest.ui.main.utils.workers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.TextUtils
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.reddittest.ui.main.utils.KEY_IMAGE_URL
import java.io.IOException
import java.io.InputStream
import java.net.URL


class CompressWorker(var ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    override fun doWork(): Result {
        val appContext = applicationContext

        val resourceUri = inputData.getString(KEY_IMAGE_URL)

        makeStatusNotification("Blurring image", appContext)

        return try {
            if (TextUtils.isEmpty(resourceUri)) {
                throw IllegalArgumentException("Invalid input uri")
            }



            /*if it was an uri
            val resolver = appContext.contentResolver
            val picture = BitmapFactory.decodeStream(
                resolver.openInputStream(Uri.parse(resourceUri))
            )*/


            val picture: Bitmap?
            try {
                val inputStream: InputStream = URL(resourceUri).openStream()
                picture = BitmapFactory.decodeStream(inputStream)
            } catch (e: IOException) {
                throw IllegalArgumentException("Invalid input url")
            }

            val output = compressBitmap(picture, appContext)

            // Write bitmap to a temp file
            val outputUri = writeBitmapToFile(appContext, output)

            val outputData = workDataOf(KEY_IMAGE_URL to outputUri.toString())

            Result.success(outputData)
        } catch (throwable: Throwable) {
            Result.failure()
        }
    }
}
