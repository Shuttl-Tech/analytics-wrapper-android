package com.shuttl.android.analyticswrapper.models

import android.content.Context
import com.clevertap.android.sdk.CleverTapAPI
import com.google.firebase.analytics.FirebaseAnalytics

data class LoggerPayload(
        var eventName: String = "",
        var eventMessage: String? = null,
        var eventData: Map<String, Any?>? = null,
        var sentryPayload: SentryPayload? = null,
        var exception: Exception? = null
)

data class SentryPayload(
        var eventData: Map<String, String?>? = null,
        var category: String? = null,
        var type: String? = null
)

data class InitSDKs(
        var context: Context? = null,
        var isDebug: Boolean? = null,
        var buildType: String? = null,
        var sentryDsn: String? = null,
        var intercomAPIKey: String? = null,
        var intercomAppId: String? = null
)

data class InitSDKsDirectly(
        var cleverTap: CleverTapAPI? = null,
        var firebaseAnalytics: FirebaseAnalytics? = null
)