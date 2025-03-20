package com.streamliners.lipu.ui

import android.app.Application
import com.streamliners.lipu.di.koinSetup

class LIpuApp: Application() {
    override fun onCreate() {
        super.onCreate()
        koinSetup()
    }
}