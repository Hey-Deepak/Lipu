package com.streamliners.lipu.feature.chat

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.streamliners.base.ext.showFailureMessage
import com.streamliners.base.taskState.comp.whenLoaded
import com.streamliners.compose.android.comp.appBar.TitleBarScaffold
import com.streamliners.lipu.feature.chat.ChatViewModel.Mode.Text
import com.streamliners.lipu.feature.chat.comp.MessagesList
import com.streamliners.lipu.feature.chat.comp.TextInput
import com.streamliners.lipu.feature.chat.comp.VoiceMode
import com.streamliners.lipu.feature.voice.SpeechRecognitionButton
import com.streamliners.lipu.ui.main.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavController,
    viewModel: ChatViewModel
) {
    val prompt = rememberSaveable { mutableStateOf("") }

    LaunchedEffect(key1 = Unit) {
        viewModel.loadChat()
    }

    TitleBarScaffold(
        title = "Lipu - Learn In Public",
        actions = {
            IconButton(
                onClick = {
                    navController.navigate(Screen.Projects.route)
                }
            ) {
                Icon(imageVector = Icons.Default.WorkspacePremium, contentDescription = "Projects")
            }

            IconButton(
                onClick = {
                    navController.navigate(Screen.ContentHistory.route)
                }
            ) {
                Icon(imageVector = Icons.Default.History, contentDescription = "Content History")
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
        ) {

            Column(
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                viewModel.data.whenLoaded { data ->
                    MessagesList(data)
                }
            }

            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SpeechRecognitionButton(
                    onInput = { input, nextInput ->
                        prompt.value = input
                        viewModel.mode.value = ChatViewModel.Mode.Voice
                        viewModel.sendPrompt(
                            prompt = prompt.value,
                            onSuccess = { prompt.value = "" },
                            takeNextVoicePrompt = nextInput
                        )
                    },
                    showError = viewModel::showFailureMessage,
                    onDismiss = { viewModel.mode.value = Text }
                )

                AnimatedContent(
                    modifier = Modifier.weight(1f),
                    targetState = viewModel.mode,
                    label = "Mode"
                ) { mode ->

                    if (viewModel.mode.value == Text) {
                        TextInput(prompt, viewModel)
                    } else {
                        VoiceMode(
                            modifier = Modifier.weight(1f),
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}