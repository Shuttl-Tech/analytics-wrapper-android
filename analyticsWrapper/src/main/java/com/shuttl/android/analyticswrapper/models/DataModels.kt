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
        val context: Context? = null,
        val isDebug: Boolean? = null,
        val buildType: String? = null,
        val sentryDsn: String? = null,
        val intercomAPIKey: String? = null,
        val intercomAppId: String? = null
)

data class InitSDKsDirectly(
        val cleverTap: CleverTapAPI? = null,
        val firebaseAnalytics: FirebaseAnalytics? = null
)