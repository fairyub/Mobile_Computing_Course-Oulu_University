package com.fairyub.composetutorial.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairyub.composetutorial.data.Attachment
import com.fairyub.composetutorial.data.AttachmentDao
import com.fairyub.composetutorial.data.ConversationDao
import com.fairyub.composetutorial.data.Message
import com.fairyub.composetutorial.data.MessageDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val messageDao: MessageDao,
    private val attachmentDao: AttachmentDao
) : ViewModel() {
    private val _messages = MutableStateFlow<List<Message?>>(arrayListOf())
    val messages get() = _messages.asStateFlow()

    fun messageBelongConversationFromDB(conversationId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _messages.value = messageDao.getAll(conversationId)
        }
    }

    fun addMessageToDB(message: Message) {
        viewModelScope.launch(Dispatchers.IO) {
            messageDao.insert(message)
            messageBelongConversationFromDB(message.conversationId)
        }
    }

    fun sendMessage(
        conversationId: Int,
        userId: Int,
        text: String?,
        attachmentUris: List<String>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            if (text.isNullOrBlank() && attachmentUris.isEmpty()) return@launch

            val newId = messageDao.insert(
                Message(
                    uid = 0,
                    conversationId = conversationId,
                    userId = userId,
                    text = text?.takeIf { it.isNotBlank() }
                )
            ).toInt()

            attachmentUris.forEach { path ->
                attachmentDao.insert(
                    Attachment(
                        uid = 0,
                        messageId = newId,
                        uri = path,
                        imageRes = null
                    )
                )
            }

            _messages.value = messageDao.getAll(conversationId)
        }
    }
}