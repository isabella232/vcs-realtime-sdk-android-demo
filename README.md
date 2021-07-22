# VCS Realtime SDK Sample App for Android

This sample app uses the Virtual Care Service (VCS) Realtime Android SDK to demonstrate how to join virtual rooms and interact with other participants via audio and/or video.
### Build and Run

The demo app can be built to run on a device or the simulator. Note that the simulator does not provide video.

The root gradle file contains the references to the SDK and WebRTC images
```xml
...
allprojects {
    repositories {
        ...
        maven { url 'https://raw.github.com/ATOS-VIRTUAL-CARE/vcs-realtime-sdk-android/repo/' }
        maven { url 'https://raw.github.com/ATOS-VIRTUAL-CARE/webrtc-android/repo/' }
    }
}
```

The dependencies are also added to the app module (such as app/build.gradle):
```xml
dependencies {
    implementation 'atos.virtual.care:vcs-reatime-sdk:0.2.0'
    implementation 'atos.virtual.care:libwebrtc:M90'
}
```

### More Information

Where to find more information about the VCS Realtime SDKs and APIs.

* For more information on the VCS SDK family, see the [VCS realtime SDK page](https://sdk.virtualcareservices.net/)
* For more information on the VCS iOS SDK, see the [guide for iOS realtime SDK](https://sdk.virtualcareservices.net/sdks/android)
* A list of all APIs for the iOS SDK is available at the [reference API for iOS realtime SDK](https://sdk.virtualcareservices.net/reference/android)