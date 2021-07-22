package net.atos.vcs.realtime.demo

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.annotation.UiThread
import net.atos.vcs.realtime.sdk.MediaStreamVideoView
import org.webrtc.EglBase
import org.webrtc.RendererCommon
import org.webrtc.VideoTrack

/**
 * Renders local video track on the surface view.
 */
@UiThread
class LocalVideoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : MediaStreamVideoView(context, attrs) {

    override fun init(
        sharedContext: EglBase.Context?,
        rendererEvents: RendererCommon.RendererEvents?
    ) {
        super.init(sharedContext, rendererEvents)
        setMirror(true)

        // Setting this eliminates the the need to call setZOrderOnTop() each time a remote view is added. It also
        // makes the switch camera icon remain on top of the local view.
        setZOrderMediaOverlay(true)
    }

    override fun setVideoSource(track: VideoTrack) {
        super.setVideoSource(track)
        setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_BALANCED)
    }
}
