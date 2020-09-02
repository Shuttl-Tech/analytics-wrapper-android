# Analytic Logger

[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-orange.svg)](http://makeapullrequest.com)

A wrapper over various SDKs, libs used in Consumer/Driver App for Analytics

* [Getting Started](#getting-started-)
* [Features](#features-)
* [Configure](#configure-)
* [FAQ](#faq-)
* [Contributing](#contributing-)

## Getting Started 👣

AnalyticWrapper is distributed through JitPack. To use it you need to add the following **Gradle dependency** to your `build.gradle` file of your android app module (NOT the root file).

```groovy
repositories {
    maven { url "https://jitpack.io" }
}
```

```groovy
dependencies {
  implementation 'com.github.Shuttl-Tech:analytics-wrapper-android:Tag'
}
```

Added this to your root:gradle.properties file. This will enable all the supported SDKs
```groovy
    customLogger.enableClevertap = true
    customLogger.enableFirebase = true
    customLogger.enableSentry = true
    customLogger.enableIntercom = true
```

To start using AnalyticsWrapper, just consume CustomLogger public APIs:

```kotlin
CustomLogger.init(sdkList, initSdkDirectlyPayload)
CustomLogger.initDirectly(sdkList, initSdkPayload)
CustomLogger.pushEvent(sdkList, loggerPayload)
CustomLogger.pushExceptionEvent(exception)
CustomLogger.pushUserMetadata(userMetadata)
```

**That's it!** 🎉

## Features 🧰

Don't forget to check the [changelog](https://github.com/Shuttl-Tech/analytics-wrapper-android/releases) to have a look at all the changes in the latest version of AnalyticWrapper.

* **API >= 16** compatible
* Easy to integrate (just a 2 gradle implementation line)
* Highly Configurable

## Configure 🎨
```kotlin
var sdkList = listOf<LogType>(LogType.CleverTap, LogType.Sentry)

var loggerPayload = CustomLogger.loggerPayload {
        eventName = "TestEvent"
        eventMessage = "TestMessage"
        eventData = HashMap()
        sentryPayload = CustomLogger.sentryPayload {
            eventData = HashMap()
            category = "Category"
            type = "Type"
        }
        exception = null
    }

var initSdkPayload = CustomLogger.initSDKs {
        context = null
        isDebug = null
        buildType = null
        sentryDsn = null
        intercomAPIKey = null
        intercomAppId = null
    }

 var initDirectly = CustomLogger.initSDKsDirectly {
         cleverTap = null
         firebaseAnalytics = null
     }

var userMetadata = CustomLogger.userMetadata {
        name = "Name"
        email = "Email"
        phone = "123"
        /*
        Add other required params
         */
    }
```

## FAQ ❓

* **Why is it not working for me?** - dependency issue maybe, create an issue if it doesn't work

## Contributing 🤝

**We're looking for contributors! Don't be shy.** 😁 Feel free to open issues/pull requests to help me improve this project.