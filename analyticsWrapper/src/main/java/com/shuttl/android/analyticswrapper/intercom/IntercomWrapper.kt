package com.shuttl.android.analyticswrapper.intercom

import android.app.Application
import android.content.Context
import com.shuttl.android.analyticswrapper.BuildConfig
import com.shuttl.android.analyticswrapper.interfaces.SDKTracker
import com.shuttl.android.analyticswrapper.models.InitSDKs
import com.shuttl.android.analyticswrapper.models.InitSDKsDirectly
import com.shuttl.android.analyticswrapper.models.LoggerPayload
import com.shuttl.android.analyticswrapper.models.UserMetadata
import io.intercom.android.sdk.Intercom

object IntercomWrapper : SDKTracker {

    val enableIntercom = BuildConfig.enableIntercom

    /**
     * Initializes Intercom
     */
    private fun init(context: Context, apiKey: String, appId: String) {
        Intercom.initialize(context as Application, apiKey, appId)
    }

    /**
     * Logs Intercom event with/without metadata/custom params.
     *
     * @param eventName the event name to be logged, preferably from [AnalyticsConstants.Event]
     * @param metaData  a map of custom properties/data to be logged with the event
     */

    private fun logIntercomEvent(eventName: String, metaData: Map<String, Any?>? = null) {
        if (metaData == null) Intercom.client().logEvent(eventName)
        else Intercom.client().logEvent(eventName, metaData)
    }

    override fun initSDK(initSDK: InitSDKs) {
        if (!enableIntercom) return
        init(initSDK.context!!, initSDK.intercomAPIKey!!, initSDK.intercomAppId!!)
    }

    override fun initSDKDirectly(initSDKsDirectly: InitSDKsDirectly) {
        //ignored
    }

    override fun pushEvent(loggerPayload: LoggerPayload) {
        if (!enableIntercom) return
        logIntercomEvent(loggerPayload.eventName, loggerPayload.eventData)
    }

    override fun pushUserMetadata(userMetadata: UserMetadata) {
        //ignored
    }
}