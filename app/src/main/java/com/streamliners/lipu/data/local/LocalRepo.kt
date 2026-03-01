package com.streamliners.lipu.data.local

import com.streamliners.lipu.android.helper.DataStoreUtil

class LocalRepo(
    private val dataStoreUtil: DataStoreUtil
) {

    companion object {
        private const val KEY_CONTENT_PREFERENCES = "contentPreferences"
    }

}