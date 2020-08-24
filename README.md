# analytics-wrapper-android [WIP]

A wrapper over AndroidLog and Analytics Libraries/SDKs: Firebase, Sentry, Intercom, Clevertap

## Implementation

Add this to your app:build.gradle file - implementation 'com.github.Shuttl-Tech:analytics-wrapper-android:tag'

## Usage

 - Add enablers to your root:gradle.properties file

    customLogger.enableClevertap = true
    customLogger.enableFirebase = true
    customLogger.enableSentry = true
    customLogger.enableIntercom = true

 - Wrapper Functions:
    Use NativeLogWrapper for Android.Log functions
    Use CustomLogger public functions to log the events
