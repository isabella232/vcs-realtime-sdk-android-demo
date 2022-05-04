# VCS Realtime SDK Sample App for Android
![SignIn](https://user-images.githubusercontent.com/4389724/130239263-c3e598be-6d57-464a-b59d-bf02bd297ff7.png)

This sample app uses the Virtual Care Service (VCS) Realtime Android SDK to demonstrate how to join virtual rooms and interact with other participants via audio and/or video. In addition to audio and video, data-channel messaging is also available.
## Build and Run

The demo app can be built to run on a device or the simulator. Note that the simulator does not provide video.

The root gradle file contains the references to the SDK and WebRTC images.
```xml
...
allprojects {
    repositories {
        ...
        maven { url 'https://raw.github.com/ATOS-VIRTUAL-CARE/vcs-realtime-sdk-android/repo/' }
        maven { url 'https://raw.github.com/ATOS-VIRTUAL-CARE/webrtc-android/repo/' 
    }
}
```

The dependencies are also added to the app module (such as app/build.gradle). The version of SDK and WebRTC dependencies should be updated to the latest released version. Also, the apollo runtime dependency should match that noted in the SDK [README](https://github.com/ATOS-VIRTUAL-CARE/vcs-realtime-sdk-android/blob/master/README.md#integrate-into-android-project)
```xml
dependencies {
    ...
    implementation 'atos.virtual.care:vcs-realtime-sdk:x.x.x'
    implementation 'atos.virtual.care:libwebrtc:M98'
    implementation 'com.apollographql.apollo3:apollo-runtime:x.x.x'
}
```

## Running the application server locally

For development purposes the demo application server may be run locally to provide access to a test system. See the sample application for web clients [Running application locally](https://github.com/ATOS-VIRTUAL-CARE/vcs-realtime-sdk-web-demo#running-application-locally) for instructions. The application server address to be configured in the mobile client will be the localhost's IP address and the port the application server is listening on. Also, "http://" must prefix the address when not using TLS.
Example address: http://192.168.1.232:3001

To permit a client application to use clear text (http) trafic for debug purposes, the following placeholders should be added to the application's build.gradle file.
```xml
...
android {
    . . .
    buildTypes {
        release {
            . . .
            manifestPlaceholders = [usesCleartextTraffic:"false"]
        }
        debug {
            . . .
            manifestPlaceholders = [usesCleartextTraffic:"true"]
        }
    }
    . . .
}
```

Then, the application section of the AndroidManifest.xml file should be updated to include the usesCleartextTraffic property.
```xml
android:usesCleartextTraffic="${usesCleartextTraffic}"
```


## Configuration

The application server address needs to be configured from within the demo application's settings. Basic authentication credentials to create a room may be configured in settings. The demo application will prompt for these credentials when creating a room and they can be entered and saved at that time as well.

![Settings](https://user-images.githubusercontent.com/4389724/130242609-d993f59b-8115-4343-a21d-4d56b9508f97.png)

## More Information

Where to find more information about the VCS Realtime SDKs and APIs.

* For more information on the VCS SDK family, see the [VCS realtime SDK page](https://sdk.virtualcareservices.net/)
* For more information on the VCS Android SDK, see the [guide for Android realtime SDK](https://sdk.virtualcareservices.net/sdks/android)
* A list of all APIs for the Android SDK is available at the [reference API for Android realtime SDK](https://sdk.virtualcareservices.net/reference/android)
