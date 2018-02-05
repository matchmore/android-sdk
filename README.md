# Alps Android SDK

`AlpsSDK` is a contextualized publish/subscribe model which can be used to model any geolocated or proximity based mobile application. Save time and make development easier by using our SDK. We are built on Android Location services and we also provide iBeacons compatibility.

## Versioning

SDK is written using Kotlin 1.2.

`AlpsSDK` requires Android 4.4+

## Installation

Gradle

Add it in your root build.gradle at the end of repositories:

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
}
```

SDK

```groovy
implementation 'com.github.matchmore.alps-android-sdk:sdk:<latest version>'
```

Rx Wrapper

```groovy
implementation 'com.github.matchmore.alps-android-sdk:rx:<latest version>'
```



Maven

```
<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
</repositories>
```

SDK

```
<dependency>
    <groupId>com.github.matchmore.alps-android-sdk</groupId>
    <artifactId>sdk</artifactId>
    <version>latest version</version>
</dependency>
```

Rx Wrapper

```
<dependency>
    <groupId>com.github.matchmore.alps-android-sdk</groupId>
    <artifactId>rx</artifactId>
    <version>latest version</version>
</dependency>
```

## Technical overview

The `MatchMore` is a static wrapper that provides you all the functions you need to use our SDK.

## Usage

Setup application API key and world, get it for free from [http://matchmore.io/](http://matchmore.io/).

```kotlin
override fun onCreate() {
        super.onCreate()
        MatchMore.config(MatchMoreConfig(this, SdkConfigTest.API_KEY, SdkConfigTest.WORLD_ID, debugLog = true))
    }
}
```

Create first device, publication and subscription. Please note that we're not caring about errors right now.

```kotlin
MatchMore.instance.apply {
    startUsingMainDevice({ device ->
        Log.i(TAG, "start using device ${device.name}")

        // Create publication
        val publication = Publication("Test Topic", 1.0, 0.0)
        publication.properties = hashMapOf("test" to "true")
        createPublication(publication, { result ->
            Log.i(TAG, "Publication created ${result.topic}")
        }, Throwable::printStackTrace)

        // Create subscription
        val subscription = Subscription("Test Topic", 1.0, 0.0)
        subscription.selector = "test = 'true'"
        createSubscription(subscription, { result ->
            Log.i(TAG, "Subscription created ${result.topic}")
        }, Throwable::printStackTrace)

        // Start getting matches
        matchMonitor.addOnMatchListener { matches, _ ->
            Log.i(TAG, "Matches found: ${matches.size}")
        }
        matchMonitor.startPollingMatches()
    }, Throwable::printStackTrace)
}
```

## Example

in `alps-android-sdk/example/` you will find working simple example.

## Documentation

See the [http://matchmore.io/documentation/api](http://matchmore.io/documentation/api) or consult our website for further information [http://matchmore.io/](http://matchmore.io/)

## Authors

- @kubatatami, kubatatami@gmail.com
- @maciejburda, maciej.burda@matchmore.com


## License

`AlpsSDK` is available under the MIT license. See the LICENSE file for more info.
