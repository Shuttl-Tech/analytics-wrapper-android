package com.shuttl.android.analyticswrapper.clevertap

import android.content.Context
import android.location.Location
import com.clevertap.android.sdk.CleverTapAPI
import com.shuttl.android.analyticswrapper.BuildConfig
import com.shuttl.android.analyticswrapper.interfaces.SDKTracker
import com.shuttl.android.analyticswrapper.models.InitSDKs
import com.shuttl.android.analyticswrapper.models.InitSDKsDirectly
import com.shuttl.android.analyticswrapper.models.LoggerPayload
import com.shuttl.android.analyticswrapper.models.UserMetadata
import java.util.*


import kotlin.collections.HashMap
object ClevertapWrapper : SDKTracker {

    private var cleverTap: CleverTapAPI? = null
    private val enableClevertap = BuildConfig.enableClevertap

    /**
    * Initializes cleverTap
     */
    private fun initCleverTap(context: Context, isDebug: Boolean) {
        cleverTap = CleverTapAPI.getDefaultInstance(context)
        if (cleverTap != null) {
            cleverTap?.enableDeviceNetworkInfoReporting(true)
        }
        if (isDebug) {
            CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.DEBUG)
        }
    }

    /**
     * Initializes cleverTap directly from Application instance
     */
    private fun initCleverTapDirectly(cleverTapAPI: CleverTapAPI) {
        cleverTap = cleverTapAPI
    }

    /**
     * Save user properties to track user specific details like phone, email etc.
     */
    private fun saveUserProperties(userMetadata: UserMetadata) {

        val peoplePropertiesMap = HashMap<String, Any?>()

        peoplePropertiesMap[ClevertapConstants.CT_IDENTITY] = userMetadata.id
        peoplePropertiesMap[ClevertapConstants.CT_ENCRYPTED_USER_ID] = userMetadata.id
        peoplePropertiesMap[ClevertapConstants.CT_NAME] = userMetadata.name
        peoplePropertiesMap[ClevertapConstants.CT_USER_GENDER] = userMetadata.gender
        peoplePropertiesMap[ClevertapConstants.CT_GENDER] = userMetadata.gender
        peoplePropertiesMap[ClevertapConstants.CT_EMAIL] = userMetadata.email
        peoplePropertiesMap[ClevertapConstants.CT_PHONE] = "+66" + userMetadata.phone
        peoplePropertiesMap[ClevertapConstants.CT_MOBILE_NUMBER] = "+66" + userMetadata.phone // duplicate property in order to allow segmentation in CleverTap
        peoplePropertiesMap[ClevertapConstants.CT_DATE_OF_BIRTH] = userMetadata.birthDate
        peoplePropertiesMap[ClevertapConstants.CT_DOB] = userMetadata.birthDate
        peoplePropertiesMap[ClevertapConstants.CT_HOME_LOCATION] = userMetadata.location

        peoplePropertiesMap.putAll(userMetadata.userBlob ?: emptyMap())

        cleverTap?.pushProfile(peoplePropertiesMap) //clevertap profile
    }

    /**
     * To log the clevertap event in the dashboard
     */
    private fun logClevertapEvent(eventName: String, propertiesMap: Map<String, Any?>? = null) {
        cleverTap?.pushEvent(eventName, propertiesMap)
    }

    /**
     * Add userId to identify a user in clevertap
     */
    fun identifyUser(userId: String) {
        if(!enableClevertap) return
        val idProp: MutableMap<String, Any> = HashMap()
        idProp[ClevertapConstants.CT_IDENTITY] = userId
        cleverTap?.onUserLogin(idProp)
    }

    /**
     * Update user email id
     */
    fun pushClevertapProfileEmail(email: String) {
        if(!enableClevertap) return
        val emailProp: MutableMap<String, Any> = HashMap()
        emailProp[ClevertapConstants.CT_EMAIL] = email
        cleverTap?.pushProfile(emailProp)
    }

    /**
     * Update user location
     */
    fun updateUserLocation(latitude: Double, longitude: Double, provider: String) {
        if(!enableClevertap) return
        val location = Location(provider)
        location.latitude = latitude
        location.longitude = longitude
        cleverTap?.setLocation(location)
    }

    fun updatePushNotificationId(pushNotificationId: String?) {
        if (cleverTap != null) {
            cleverTap?.pushFcmRegistrationId(pushNotificationId, true)
        }
    }

    override fun initSDK(initSDK: InitSDKs) {
        if(!enableClevertap) return
        initCleverTap(initSDK.context!!, initSDK.isDebug!!)
    }

    override fun initSDKDirectly(initSDKsDirectly: InitSDKsDirectly) {
        if(!enableClevertap) return
        initCleverTapDirectly(initSDKsDirectly.cleverTap!!)
    }

    override fun pushEvent(loggerPayload: LoggerPayload) {
        if(!enableClevertap) return
        logClevertapEvent(loggerPayload.eventName, loggerPayload.eventData)
    }

    override fun pushUserMetadata(userMetadata: UserMetadata) {
        if(!enableClevertap) return
        saveUserProperties(userMetadata)
    }

}