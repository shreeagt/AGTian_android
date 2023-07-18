package com.agt.videostream.domain.depndency

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.agt.videostream.data.api.AjantaApi
import com.agt.videostream.data.database.VideoDataBase
import com.agt.videostream.domain.repository.AjantaRepository
import com.agt.videostream.util.Const
import com.agt.videostream.util.UserPreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.internal.synchronized
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context): VideoDataBase {
        return Room.databaseBuilder(
            context,
            VideoDataBase::class.java,
            "VideoDatabase"
        ).build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Const.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    @Provides
    @Singleton
    fun provideAjantaApi(retrofit: Retrofit): AjantaApi {
        return retrofit.create(AjantaApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMessageFlow(): Flow<String> {
        return MutableSharedFlow<String>()
    }

    @Provides
    @Singleton
    fun provideSharePreference(@ApplicationContext context: Context): UserPreference {
        return UserPreference(context)
    }

    @Provides
    fun provideRepository(
        api: AjantaApi,
        dataBase: VideoDataBase,
        userPreference: UserPreference
    ): AjantaRepository {
        return AjantaRepository(api, dataBase, userPreference)
    }

}