package net.atos.vcs.realtime.demo

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomViewModule {

    @Provides
    @Singleton
    fun providesRoomManager(
        @ApplicationContext context: Context
    ): RoomManager = RoomManager(context)
}