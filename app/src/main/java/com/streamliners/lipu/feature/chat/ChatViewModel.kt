package com.streamliners.lipu.feature.chat

import androidx.compose.runtime.mutableStateOf
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import com.streamliners.base.ext.hideLoader
import com.streamliners.base.ext.showLoader
import com.streamliners.base.taskState.taskStateOf
import com.streamliners.base.taskState.update
import com.streamliners.lipu.android.helper.TTSHelper
import com.streamliners.lipu.data.local.dao.ChatHistoryDao
import com.streamliners.lipu.data.local.dao.LearningEntryDao
import com.streamliners.lipu.domain.model.ChatHistoryItem
import com.streamliners.lipu.feature.chat.viewModelExt.toContentList
import com.streamliners.lipu.feature.chat.viewModelExt.toUIItems
import com.streamliners.lipu.feature.genAI.GeminiModel
import com.streamliners.lipu.other.ext.send
import com.streamliners.utils.DateTimeUtils
import com.streamliners.utils.DateTimeUtils.formatTime
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ChatViewModel(
    private val chatHistoryDao: ChatHistoryDao,
    val learningEntryDao: LearningEntryDao,
    val ttsHelper: TTSHelper
) : BaseViewModel() {

    data class ChatHistoryUIItem(
        val content: Content,
        val time: Long,
        val formattedTime: String,
        val date: String,
        val role: Role
    ) {
        enum class Role { User, Model }
    }

    class Data(
        val chatHistoryUIItems: List<ChatHistoryUIItem>
    )

    enum class ContentMode { Chat, QuickContent }

    enum class Mode { Text, Voice }

    private lateinit var generativeModel: GenerativeModel

    val contentMode = mutableStateOf(ContentMode.Chat)
    val mode = mutableStateOf(Mode.Text)
    val data = taskStateOf<Data>()

    private val currentDate = formatTime(DateTimeUtils.Format("yyyy/MM/dd"))

    lateinit var chat: Chat

    private var collectJob: Job? = null

    fun loadChat() {
        execute(false) {
            collectJob?.cancel()

            collectJob = launch {
                var isFirstFetch = true
                chatHistoryDao.getList(currentDate, contentMode.value.name).collectLatest { chatHistory ->
                    if (isFirstFetch) {
                        isFirstFetch = false

                        generativeModel = GeminiModel.get(contentMode.value)

                        chat = generativeModel.startChat(
                            chatHistory.toContentList()
                        )
                    }
                    data.update(
                        Data(chatHistory.toUIItems())
                    )
                }
            }
        }
    }

    fun sendPrompt(
        prompt: String,
        onSuccess: () -> Unit,
        takeNextVoicePrompt: () -> Unit
    ) {
        execute(false) {
            showLoader()

            val response = chat.send(prompt)

            chatHistoryDao.add(
                ChatHistoryItem(
                    type = contentMode.value.name,
                    role = "user",
                    message = prompt
                )
            )

            chatHistoryDao.add(
                ChatHistoryItem(
                    type = contentMode.value.name,
                    role = "model",
                    message = response
                )
            )

            hideLoader()
            onSuccess()

            if (mode.value == Mode.Voice) {
                ttsHelper.speak(response)
                takeNextVoicePrompt()
            }
        }
    }

}