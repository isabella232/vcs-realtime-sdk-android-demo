package net.atos.vcs.realtime.demo.applicationServer

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


data class Room(
    val room: RoomData,
    val domain: String
)

data class RoomData(
    val id: String,
    val name: String,
    val creationTime: String,
    val modificationTime: String,
    val maxParticipants: Int,
    val roomTokenTTLSeconds: Int,
    val roomTTLSeconds: Int,
    val endpointTTLSeconds: Int,
    val token: String
)

data class RoomName(
    val name: String
)

data class Config(
    val VCS_HOST: String
)

class ApplicationRepository {
    private val service: ApplicationService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://sdk-demo.virtualcareservices.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(ApplicationService::class.java)
    }

    suspend fun getConfig() = service.getConfig()

    suspend fun getRoom(roomName: String) = service.getRoom(roomName)

    suspend fun createRoom(roomName: String) = service.createRoom(RoomName(name = roomName))
}