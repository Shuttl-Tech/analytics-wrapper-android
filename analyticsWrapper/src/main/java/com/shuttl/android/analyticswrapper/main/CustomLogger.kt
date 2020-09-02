package com.shuttl.android.analyticswrapper.main

import com.shuttl.android.analyticswrapper.androidlog.NativeLogWrapper
import com.shuttl.android.analyticswrapper.clevertap.ClevertapWrapper
import com.shuttl.android.analyticswrapper.firebase.FirebaseWrapper
import com.shuttl.android.analyticswrapper.intercom.IntercomWrapper
import com.shuttl.android.analyticswrapper.interfaces.SDKTracker
import com.shuttl.android.analyticswrapper.models.*
import com.shuttl.android.analyticswrapper.sentry.SentryWrapper
import java.lang.Exception

object CustomLogger {

    private fun translateLogType(logType: List<LogType>): List<SDKTracker> {
        var list = ArrayList<SDKTracker>()
        for (sdk in logType) {
            when (sdk) {
                LogType.Firebase -> list.add(FirebaseWrapper)
                LogType.CleverTap -> list.add(ClevertapWrapper)
                LogType.Intercom -> list.add(IntercomWrapper)
                LogType.Sentry -> list.add(SentryWrapper)
                else -> {
                }
            }
        }
        return list
    }

    fun pushExceptionEvent(e: Exception) {
        NativeLogWrapper.sLogE(msg = e.toString())

        val payload = loggerPayload { exception = e }
        FirebaseWrapper.pushEvent(payload)
        SentryWrapper.pushEvent(payload)
    }

    fun loggerPayload(block: LoggerPayload.() -> Unit): LoggerPayload = LoggerPayload().apply(block)

    fun initSDKs(block: InitSDKs.() -> Unit): InitSDKs = InitSDKs().apply(block)

    fun userMetadata(block: UserMetadata.() -> Unit): UserMetadata = UserMetadata().apply(block)

    fun initSDKsDirectly(block: InitSDKsDirectly.() -> Unit): InitSDKsDirectly = InitSDKsDirectly().apply(block)

    fun sentryPayload(block: SentryPayload.() -> Unit): SentryPayload = SentryPayload().apply(block)

    fun init(sdksList: List<LogType>, initSDK: InitSDKs) {
        for (sdk in translateLogType(sdksList)) {
            try {
                sdk.initSDK(initSDK)
            } catch (e: Exception) {
                pushExceptionEvent(e)
            }
        }
    }

    fun initDirectly(sdksList: List<LogType>, initSDKsDirectly: InitSDKsDirectly) {
        for (sdk in translateLogType(sdksList)) {
            try {
                sdk.initSDKDirectly(initSDKsDirectly)
            } catch (e: Exception) {
                pushExceptionEvent(e)
            }
        }
    }

    fun pushEvent(sdksList: List<LogType>, loggerPayload: LoggerPayload) {
        NativeLogWrapper.sLogI(msg = loggerPayload.toString(), customTag = "Pushing Events for: $sdksList")
        for (sdk in translateLogType(sdksList)) {
            try {
                sdk.pushEvent(loggerPayload)
            } catch (e: Exception) {
                pushExceptionEvent(e)
            }
        }
    }

    fun pushUserMetadata(userMetadata: UserMetadata) {
        ClevertapWrapper.pushUserMetadata(userMetadata)
        FirebaseWrapper.pushUserMetadata(userMetadata)
        IntercomWrapper.pushUserMetadata(userMetadata)
        SentryWrapper.pushUserMetadata(userMetadata)
    }

}