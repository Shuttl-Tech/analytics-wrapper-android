package com.shuttl.android.analyticswrapper.models

import android.location.Location
import com.shuttl.android.analyticswrapper.clevertap.ClevertapWrapper

class UserMetadata {

    var email: String? = null
    set(value) {
        if(value != null)
            ClevertapWrapper.pushClevertapProfileEmail(value)
        field = value
    }

    var id: String? = null
    set(value) {
        if(value != null)
            ClevertapWrapper.identifyUser(value)
        field = value
    }

    var location: Location? = null
    set(value) {
        if(value != null)
            ClevertapWrapper.updateUserLocation(value.latitude, value.longitude, value.provider)
        field = value
    }

    var name: String? = null
    var phone: String? = null
    var birthDate: String? = null
    var referralId: String? = null
    var referredBy: String? = null
    var facebook_Connected: String? = null
    var appVersion: String? = null
    var userBlob: Map<String, Any?>? = null

}