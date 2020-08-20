package com.shuttl.android.analyticswrapper.firebase

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.shuttl.android.analyticswrapper.BuildConfig
import com.shuttl.android.analyticswrapper.interfaces.SDKTracker
import com.shuttl.android.analyticswrapper.models.InitSDKs
import com.shuttl.android.analyticswrapper.models.InitSDKsDirectly
import com.shuttl.android.analyticswrapper.models.LoggerPayload
import com.shuttl.android.analyticswrapper.models.UserMetadata

object FirebaseWrapper : SDKTracker {

    private var firebaseAnalytics: FirebaseAnalytics? = null
    private val enableFirebase = BuildConfig.enableFirebase

    /**
     * Initializes firebase analytics
     */
    @SuppressLint("MissingPermission")
    private fun init(context: Context) {
        FirebaseApp.initializeApp(context)
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        firebaseAnalytics = FirebaseAnalytics.getInstance(context)
    }

    /**
     * Initializes firebase analytics directly from the application
     */
    private fun initDirectly(firebaseAnalytics: FirebaseAnalytics) {
        this.firebaseAnalytics = firebaseAnalytics
    }

    /**
     * Save user related properties in firebase dashboard
     */
    private fun saveFAUserProperties(userMetadata: UserMetadata) {
        firebaseAnalytics?.setUserProperty(FirebaseConstants.userId, userMetadata.id)
        firebaseAnalytics?.setUserProperty(FirebaseConstants.name, userMetadata.name)
        firebaseAnalytics?.setUserProperty(FirebaseConstants.email, userMetadata.email)
        firebaseAnalytics?.setUserProperty(FirebaseConstants.phone, userMetadata.phone)
        firebaseAnalytics?.setUserProperty(FirebaseConstants.birthDate, userMetadata.birthDate)
        firebaseAnalytics?.setUserProperty(FirebaseConstants.referralId, userMetadata.referralId)
        firebaseAnalytics?.setUserProperty(FirebaseConstants.referredBy, userMetadata.referredBy)
        firebaseAnalytics?.setUserProperty(FirebaseConstants.facebook_Connected, userMetadata.facebook_Connected)
        firebaseAnalytics?.setUserProperty(FirebaseConstants.appVersion, userMetadata.appVersion)
    }

    /**
     * Logs the user track event in firebase
     */
    private fun logFirebaseEvent(eventName: String, dataMap: Map<String, Any?>? = null) {
        var bundle = Bundle()
        if (dataMap != null) {
            bundle = getBundleFromMap(dataMap)
        }
        bundle.putLong(FirebaseConstants.DEVICE_TIME, System.currentTimeMillis())
        firebaseAnalytics?.logEvent(eventName, bundle)
    }

    /**
     * Internal helper function to generate the bundle from map
     */
    private fun getBundleFromMap(propertiesMap: Map<String, Any?>?): Bundle {
        val bundle = Bundle()
        if (propertiesMap != null) {
            for ((key, value) in propertiesMap) {
                try {
                    if (value is String) {
                        bundle.putString(key, value.toString())
                    } else if (value is Double) {
                        bundle.putDouble(key, java.lang.Double.valueOf(value.toString()))
                    } else if (value is Boolean) {
                        bundle.putBoolean(key, java.lang.Boolean.valueOf(value.toString()))
                    } else if (value is Long) {
                        bundle.putLong(key, java.lang.Long.valueOf(value.toString()))
                    } else if (value is Int) {
                        bundle.putInt(key, Integer.valueOf(value.toString()))
                    } else if (value is Float) {
                        bundle.putFloat(key, java.lang.Float.valueOf(value.toString()))
                    } else {
                        bundle.putString(key, value.toString())
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }
        return bundle
    }

    /**
     * Records exception in firebase dashboard
     */
    private fun recordException(e: Exception) {
        FirebaseCrashlytics.getInstance().recordException(e)
    }

    /**
     * Sets user specific details for crashlytics, helps in debugging an issue
     */
    fun setCrashlyticsDebugValues(userMetadata: UserMetadata) {
        FirebaseCrashlytics.getInstance().setCustomKey(FirebaseConstants.email, userMetadata.email
                ?: "")
        FirebaseCrashlytics.getInstance().setCustomKey(FirebaseConstants.phone, userMetadata.phone
                ?: "")
        FirebaseCrashlytics.getInstance().setCustomKey(FirebaseConstants.name, userMetadata.name
                ?: "")
    }

    override fun initSDK(initSDK: InitSDKs) {
        if (!enableFirebase) return
        init(initSDK.context!!)
    }

    override fun initSDKDirectly(initSDKsDirectly: InitSDKsDirectly) {
        if (!enableFirebase) return
        initDirectly(initSDKsDirectly.firebaseAnalytics!!)
    }

    override fun pushEvent(loggerPayload: LoggerPayload) {
        if (!enableFirebase) return
        if (loggerPayload.exception != null) recordException(loggerPayload.exception!!)
        else logFirebaseEvent(loggerPayload.eventName, dataMap = loggerPayload.eventData)
    }

    override fun pushUserMetadata(userMetadata: UserMetadata) {
        if (!enableFirebase) return
        saveFAUserProperties(userMetadata)
        setCrashlyticsDebugValues(userMetadata)
    }


}