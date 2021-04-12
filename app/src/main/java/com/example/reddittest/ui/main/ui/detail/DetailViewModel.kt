package com.example.reddittest.ui.main.ui.detail

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.example.reddittest.ui.main.data.model.RedditQueryThread
import com.example.reddittest.ui.main.utils.IMAGE_MANIPULATION_WORK_NAME
import com.example.reddittest.ui.main.utils.TAG_OUTPUT
import com.example.reddittest.ui.main.utils.workers.CleanupWorker
import com.example.reddittest.ui.main.utils.workers.SaveImageToFileWorker
import com.example.reddittest.ui.main.utils.workers.createInputDataForUri
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    application: Application,
    private val workManager: WorkManager,
) :
    ViewModel() {

    var redditThread: RedditQueryThread? = null
    private set

    fun bindData(redditQueryThread: RedditQueryThread) {
        redditThread = redditQueryThread
    }

    private var imageUrl: String? = null

    fun setImage(it: String?) {
        imageUrl = it
    }

    // This transformation makes sure that whenever the current work Id changes the WorkInfo
    // the UI is listening to changes
    val outputWorkInfos: LiveData<List<WorkInfo>> =
        workManager.getWorkInfosByTagLiveData(TAG_OUTPUT)


    fun saveImage() {
        imageUrl?.let { url ->

            var continuation = workManager
                .beginUniqueWork(
                    IMAGE_MANIPULATION_WORK_NAME,
                    ExistingWorkPolicy.REPLACE,
                    OneTimeWorkRequest.from(CleanupWorker::class.java)
                )

            // Create charging constraint
            val constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build()

            // Add WorkRequest to save the image to the filesystem
            val save = OneTimeWorkRequestBuilder<SaveImageToFileWorker>()
                .setConstraints(constraints)
                .addTag(TAG_OUTPUT)
                .setInputData(createInputDataForUri(url))
                .build()
            continuation = continuation.then(save)

            // Actually start the work
            continuation.enqueue()
        }

    }


}