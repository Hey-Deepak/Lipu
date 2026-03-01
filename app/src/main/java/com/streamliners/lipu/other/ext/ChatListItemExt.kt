package com.streamliners.lipu.other.ext

import com.streamliners.lipu.feature.chat.ChatViewModel

fun ChatViewModel.ChatHistoryUIItem.message(): String {
    return content.asString()
}