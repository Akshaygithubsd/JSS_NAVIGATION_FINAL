package com.mappls.sdk.demo

import android.app.Application
import com.mappls.sdk.maps.Mappls
import com.mappls.sdk.services.account.MapplsAccountManager
import com.mappls.sdk.services.api.autosuggest.MapplsAutosuggestManager

class MapApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        MapplsAccountManager.getInstance().mapSDKKey = " "
        MapplsAccountManager.getInstance().restAPIKey = " "
        MapplsAccountManager.getInstance().atlasClientId = " "
        MapplsAccountManager.getInstance().atlasClientSecret = " "
        Mappls.getInstance(this)
    }
}