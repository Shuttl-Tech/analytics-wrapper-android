package com.shuttl.android.analyticswrapper.sentry

import android.content.Context
import com.shuttl.android.analyticswrapper.BuildConfig
import com.shuttl.android.analyticswrapper.interfaces.SDKTracker
import com.shuttl.android.analyticswrapper.models.InitSDKs
import com.shuttl.android.analyticswrapper.models.InitSDKsDirectly
import com.shuttl.android.analyticswrapper.models.LoggerPayload
import com.shuttl.android.analyticswrapper.models.UserMetadata
import io.sentry.Sentry
import io.sentry.android.AndroidSentryClientFactory
import io.sentry.event.Breadcrumb
import io.sentry.event.BreadcrumbBuilder
import io.sentry.event.UserBuilder
import java.lang.Exception
import java.util.*

object SentryWrapper : SDKTracker {

    val enableSentry = BuildConfig.enableSentry

    /**
     * Initializes Sentry
     */
    private fun init(context: Context, sentryDsn: String, buildType: String) {

        Sentry.init(sentryDsn, AndroidSentryClientFactory(context))
        Sentry.getStoredClient().environment = buildType
    }


    /**
     * Records a [Breadcrumb] to the Sentry client. Breadcrumbs are sent with message or
     * exception events to Sentry which could provide context about the exception.
     *
     *
     * This method accepts a message {@param message} and a custom data map {@param dataMap} which
     * can be stored in the Breadcrumb to store the relevant information at that stage of the app.
     * The {@param type} is a custom type, where the values are converted to [Breadcrumb.Type].
     *
     *
     * Note: Only last 100 Breadcrumbs are available with an event or exception on Sentry.
     * The Breadcrumbs are reset when the application is reopened.
     *
     * @param message  the message to be added to the Breadcrumb
     * @param dataMap  a <String></String>, String> map containing the custom key-value pairs with the relevant
     * information for the stage of the app
     * @param category the category to be added to the [Breadcrumb]
     * @param type     the custom type to be added to the [Breadcrumb]
     */
    private fun recordSentryBreadcrumb(message: String, dataMap: Map<String, String?>?,
                                       category: String?, type: String?) {

        if (dataMap == null && category == null && type == null) {
            Sentry.capture(message)
        } else {
            val breadcrumbBuilder = BreadcrumbBuilder()
            breadcrumbBuilder.setMessage(message)
            if (dataMap != null) {
                breadcrumbBuilder.setData(dataMap)
            }
            if (type != null) {
                breadcrumbBuilder.setType(Breadcrumb.Type.valueOf(type))
            }
            breadcrumbBuilder
                    .setLevel(Breadcrumb.Level.INFO)
                    .setCategory(category)
                    .setTimestamp(Date())
            Sentry.getContext().recordBreadcrumb(breadcrumbBuilder.build())
        }

    }


    /**
     * Sets the User object for Sentry client.
     * Note: should be called whenever the [User] object is synced with server.
     */
    private fun setSentryUser(userMetadata: UserMetadata) {
        val userBuilder = UserBuilder()
        userBuilder.setId(userMetadata.id)
                .setUsername(userMetadata.name)
                .setEmail(userMetadata.email)
                .withData(SentryConstants.phone, userMetadata.phone)
                .withData(SentryConstants.referralId, userMetadata.referralId) //TransferId
                .withData(SentryConstants.referredBy, userMetadata.referredBy) //SignupReferralCode
                .withData(SentryConstants.appVersion, userMetadata.appVersion)
        Sentry.getContext().setUser(userBuilder.build())
    }

    /**
     * Records exception in Sentry
     */
    private fun recordException(e: Exception) {
        Sentry.capture(e)
    }

    override fun initSDK(initSDK: InitSDKs) {
        if (!enableSentry) return
        init(initSDK.context!!, initSDK.sentryDsn!!, initSDK.buildType!!)
    }

    override fun initSDKDirectly(initSDKsDirectly: InitSDKsDirectly) {
        //ignored
    }

    override fun pushEvent(loggerPayload: LoggerPayload) {
        if (!enableSentry) return
        if (loggerPayload.exception != null) recordException(loggerPayload.exception!!)
        recordSentryBreadcrumb(loggerPayload.eventName, loggerPayload.sentryPayload?.eventData, loggerPayload.sentryPayload?.category, loggerPayload.sentryPayload?.type)
    }

    override fun pushUserMetadata(userMetadata: UserMetadata) {
        if (!enableSentry) return
        setSentryUser(userMetadata)
    }
}