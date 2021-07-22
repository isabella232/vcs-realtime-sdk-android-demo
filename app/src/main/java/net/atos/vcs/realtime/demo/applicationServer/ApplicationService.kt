package net.atos.vcs.realtime.demo.applicationServer

import retrofit2.http.*

interface ApplicationService {
    /**
     * @GET declares an HTTP GET request to retrieve configuration data
     */
    @GET("/api/config")
    suspend fun getConfig(): Config

    /**
     * @GET declares an HTTP GET request
     * @Query("name") annotation to add query data to the path e.g., /api/room/name=<room name>
     */
    @GET("/api/room")
    suspend fun getRoom(@Query("name") roomName: String): Room

    @Headers(
        "Accept: */*",
        "Content-Type: application/json"
    )
    @POST("/api/room")
    suspend fun createRoom(@Body body: RoomName): Room
}
