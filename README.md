# Alps Android SDK

`AlpsSDK` is a contextualized publish/subscribe model which can be used to model any geolocated or proximity based mobile application. Save time and make development easier by using our SDK. We are built on Android Location services and we also provide iBeacons compatibility.

## Versioning

SDK is written using Kotlin 1.2.

`AlpsSDK` requires Android 4.4+

## Technical overview

The `MatchMore` is a static wrapper that provides you all the functions you need to use our SDK.

Features of MatchMore iOS SDK is divided into two parts: Asynchronous calls and dynamic calls.

### Asynchronous calls

All the asynchronous functions calls our cloud service and return a completion for you.

### Dynamic calls

Everytime you call an asynchronous function and it succeeds, our SDK stores it. To gain speed, we allow you to get access(read only) to these stored values.

## Usage

`USAGE EXAMPLE`

## Example

in `alps-android-sdk/example/` you will find working simple example.

## Documentation

See the [http://matchmore.io/documentation/api](http://matchmore.io/documentation/api) or consult our website for further information [http://matchmore.io/](http://matchmore.io/)

## Authors

- @kubatatami, 
- @maciejburda, maciej.burda@matchmore.com


## License

`AlpsSDK` is available under the MIT license. See the LICENSE file for more info.
