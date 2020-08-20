package com.shuttl.android.analyticswrapper.interfaces

import com.shuttl.android.analyticswrapper.models.InitSDKs
import com.shuttl.android.analyticswrapper.models.InitSDKsDirectly
import com.shuttl.android.analyticswrapper.models.LoggerPayload
import com.shuttl.android.analyticswrapper.models.UserMetadata

interface SDKTracker {
    fun pushEvent(loggerPayload: LoggerPayload)
    fun pushUserMetadata(userMetadata: UserMetadata)
    fun initSDK(initSDK: InitSDKs)
    fun initSDKDirectly(initSDKsDirectly: InitSDKsDirectly)
}