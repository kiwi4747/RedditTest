package com.example.reddittest.ui.main.di

import android.content.Context
import com.example.reddittest.ui.main.data.DataSource
import com.example.reddittest.ui.main.data.IDataRepository
import com.example.reddittest.ui.main.data.RedditRepository
import com.example.reddittest.ui.main.data.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRepository(
        @ApplicationContext context: Context,
        dataSource: DataSource
    ): IDataRepository =
        RedditRepository(context, dataSource)

    @Singleton
    @Provides
    fun provideRemoteDataSource(): DataSource {
        return RemoteDataSource
    }

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope