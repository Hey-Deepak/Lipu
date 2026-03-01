package com.streamliners.lipu.di

import android.app.Application
import com.streamliners.lipu.android.helper.DataStoreUtil
import com.streamliners.lipu.android.helper.TTSHelper
import com.streamliners.lipu.data.local.LocalDB
import com.streamliners.lipu.data.local.LocalRepo
import com.streamliners.lipu.feature.chat.ChatViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun Application.koinSetup() {
    startKoin {
        androidLogger()
        androidContext(this@koinSetup)
        modules(appModule, viewModelModule)
    }
}

private val appModule = module {
    single {
        HttpClient(CIO) {
            expectSuccess = true
        }
    }
    single {
        LocalDB.create(androidApplication())
    }
    single {
        get<LocalDB>().chatHistoryDao()
    }
    single {
        get<LocalDB>().learningEntryDao()
    }
    single {
        get<LocalDB>().projectDao()
    }
    single { TTSHelper(androidApplication()) }
    single { DataStoreUtil.create(androidApplication()) }
    single { LocalRepo(get()) }
}

private val viewModelModule = module {
    viewModel { ChatViewModel(get(), get(), get()) }
}