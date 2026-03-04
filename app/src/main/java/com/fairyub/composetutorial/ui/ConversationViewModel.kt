package com.fairyub.composetutorial.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairyub.composetutorial.data.AttachmentDao
import com.fairyub.composetutorial.data.ConversationDao
import com.fairyub.composetutorial.data.ConversationWithLastMessage
import com.fairyub.composetutorial.data.DataSource
import com.fairyub.composetutorial.data.MessageDao
import com.fairyub.composetutorial.data.UserDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversationViewModel @Inject constructor(
    private val conversationDao: ConversationDao,
    private val userDao: UserDao,
    private val messageDao: MessageDao,
    private val attachmentDao: AttachmentDao,
) : ViewModel() {
    private val _conversations = MutableStateFlow<List<ConversationWithLastMessage?>>(arrayListOf())
    val conversations get() = _conversations.asStateFlow()

    private val _currentConversation = MutableStateFlow<ConversationWithLastMessage?>(null)

    val currentConversation get() = _currentConversation.asStateFlow()

    fun conversationsFromDB() {
        viewModelScope.launch(Dispatchers.IO) {
            _conversations.value = conversationDao.getAllWithLastMessage()
        }
    }

    fun currentConversationFromDB(conversationId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _currentConversation.value = conversationDao.getAllWithLastMessage().find { it?.conversationId == conversationId }
        }
    }
}