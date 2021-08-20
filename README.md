# VCS Realtime SDK Sample App for Android
![SignIn](https://user-images.githubusercontent.com/4389724/130239263-c3e598be-6d57-464a-b59d-bf02bd297ff7.png)

This sample app uses the Virtual Care Service (VCS) Realtime Android SDK to demonstrate how to join virtual rooms and interact with other participants via audio and/or video.
### Build and Run

The demo app can be built to run on a device or the simulator. Note that the simulator does not provide video.

The root gradle file contains the references to the SDK, WebRTC, and apollo-android images.
```xml
...
allprojects {
    repositories {
        ...
        maven { url 'https://raw.github.com/ATOS-VIRTUAL-CARE/vcs-realtime-sdk-android/repo/' }
        maven { url 'https://raw.github.com/ATOS-VIRTUAL-CARE/webrtc-android/repo/' 
        maven { url 'https://jitpack.io' } // Temporarily needed for apollo-android
    }
}
```

The dependencies are also added to the app module (such as app/build.gradle). The version of SDK and WebRTC dependencies should be updated to the latest released version.
```xml
dependencies {
    ...
    implementation 'atos.virtual.care:vcs-realtime-sdk:0.3.0'
    implementation 'atos.virtual.care:libwebrtc:M90'
    implementation 'com.github.ATOS-VIRTUAL-CARE.apollo-android:apollo-runtime:3.0.0-vcs01'
}
```

The application server address needs to be configured from within the demo application's settings. Basic authentication credentials to create a room may be configured in settings. The demo application will prompt for these credentials when creating a room and they can be entered and saved at that time as well.

![Settings](https://user-images.githubusercontent.com/4389724/130242609-d993f59b-8115-4343-a21d-4d56b9508f97.png)

### More Information

Where to find more information about the VCS Realtime SDKs and APIs.

* For more information on the VCS SDK family, see the [VCS realtime SDK page](https://sdk.virtualcareservices.net/)
* For more information on the VCS Android SDK, see the [guide for Android realtime SDK](https://sdk.virtualcareservices.net/sdks/android)
* A list of all APIs for the Android SDK is available at the [reference API for Android realtime SDK](https://sdk.virtualcareservices.net/reference/android)
