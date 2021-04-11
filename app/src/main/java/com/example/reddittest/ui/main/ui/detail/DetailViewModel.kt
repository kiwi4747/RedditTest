package com.example.reddittest.ui.main.ui.detail

import android.app.Application
import androidx.hilt.Assisted
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.work.*
import com.example.reddittest.ui.main.data.model.RedditQueryThread
import com.example.reddittest.ui.main.utils.IMAGE_MANIPULATION_WORK_NAME
import com.example.reddittest.ui.main.utils.KEY_IMAGE_URL
import com.example.reddittest.ui.main.utils.TAG_OUTPUT
import com.example.reddittest.ui.main.utils.workers.CleanupWorker
import com.example.reddittest.ui.main.utils.workers.CompressWorker
import com.example.reddittest.ui.main.utils.workers.SaveImageToFileWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    application: Application,
    private val workManager: WorkManager,
    @Assisted private val state: SavedStateHandle,
) :
    ViewModel() {
    internal var imageUrl: String? = null
    fun setImage(it: String?) {
        imageUrl = it
    }

    internal val outputWorkInfos: Flow<List<WorkInfo>>

    init {
        // This transformation makes sure that whenever the current work Id changes the WorkInfo
        // the UI is listening to changes
        outputWorkInfos = workManager.getWorkInfosByTagLiveData(TAG_OUTPUT).asFlow()
    }

    fun saveImage() {
        imageUrl?.let {

            var continuation = workManager
                .beginUniqueWork(
                    IMAGE_MANIPULATION_WORK_NAME,
                    ExistingWorkPolicy.REPLACE,
                    OneTimeWorkRequest.from(CleanupWorker::class.java)
                )

            val blurBuilder = OneTimeWorkRequestBuilder<CompressWorker>()

            blurBuilder.setInputData(createInputDataForUri())

            continuation = continuation.then(blurBuilder.build())

            // Create charging constraint
            val constraints = Constraints.Builder()
                //  .setRequiresCharging(true)
                .build()

            // Add WorkRequest to save the image to the filesystem
            val save = OneTimeWorkRequestBuilder<SaveImageToFileWorker>()
                .setConstraints(constraints)
                .addTag(TAG_OUTPUT)
                .build()
            continuation = continuation.then(save)

            // Actually start the work
            continuation.enqueue()
        }

    }

    /**
     * Creates the input data bundle which includes the Uri to operate on
     * @return Data which contains the Image Uri as a String
     */
    private fun createInputDataForUri(): Data {
        val builder = Data.Builder()
        imageUrl?.let {
            builder.putString(KEY_IMAGE_URL, imageUrl)
        }
        return builder.build()
    }

    val redditThread = state.get<RedditQueryThread>("reddit_thread")
}