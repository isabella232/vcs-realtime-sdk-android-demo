package net.atos.vcs.realtime.demo

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.UiThread
import net.atos.vcs.realtime.sdk.MediaStreamVideoView
import org.webrtc.RendererCommon
import org.webrtc.VideoTrack

/**
 * Renders remote video track on the surface view.
 */
@UiThread
class RemoteVideoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : MediaStreamVideoView(context, attrs) {

    override fun setVideoSource(track: VideoTrack) {
        super.setVideoSource(track)
        setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT)
    }
}