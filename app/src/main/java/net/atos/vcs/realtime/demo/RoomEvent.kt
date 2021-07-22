package net.atos.vcs.realtime.demo

import net.atos.vcs.realtime.sdk.LocalParticipant
import net.atos.vcs.realtime.sdk.RemoteParticipant
import net.atos.vcs.realtime.sdk.Room

sealed class RoomEvent {
    data class roomJoined(val roomName: String,
                          val localParticipant: LocalParticipant,
                          val remoteParticipants: List<RemoteParticipant>,
                          val video: Boolean) : RoomEvent()
    data class roomJoinError(val error: String): RoomEvent()
    data class roomLeft(val roomName: String?, val localParticipant: LocalParticipant?): RoomEvent()
    data class participantJoined(val remoteParticipant: RemoteParticipant): RoomEvent()
    data class participantLeft(val remoteParticipant: RemoteParticipant): RoomEvent()
    data class remoteStreamUpdated(val remoteParticipant: RemoteParticipant): RoomEvent()
    data class registrationRejected(val roomName: String, val reason: String): RoomEvent()
    data class muted(val muted: Boolean) : RoomEvent()
    data class videoEnabled(val video: Boolean) : RoomEvent()
    data class speakerOn(val speaker: Boolean) : RoomEvent()
    data class error(val error: String) : RoomEvent()
}
