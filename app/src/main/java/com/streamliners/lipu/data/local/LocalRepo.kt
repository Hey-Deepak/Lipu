package com.streamliners.lipu.data.local

import com.streamliners.timify.android.helper.DataStoreUtil

class LocalRepo(
    private val dataStoreUtil: DataStoreUtil
) {

    companion object {
        private const val KEY_SHEET_SYNC_STATE = "sheetSyncState"
    }

}