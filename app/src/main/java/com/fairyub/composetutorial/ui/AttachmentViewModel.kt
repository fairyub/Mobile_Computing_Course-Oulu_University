package com.fairyub.composetutorial.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairyub.composetutorial.data.Attachment
import com.fairyub.composetutorial.data.AttachmentDao
import com.fairyub.composetutorial.data.Message
import com.fairyub.composetutorial.data.MessageDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AttachmentViewModel @Inject constructor(
    private val attachmentDao: AttachmentDao,
) : ViewModel() {
    private val _attachments = MutableStateFlow<Map<Int, List<Attachment>>>(emptyMap())
    val attachments get() = _attachments.asStateFlow()

    fun attachmentBelongMessageFromDB(messageId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = attachmentDao.getAll(messageId)
            _attachments.update { old ->
                old + (messageId to list)
            }
        }
    }

    fun addAttachmentToDB(attachment: Attachment) {
        viewModelScope.launch(Dispatchers.IO) {
            attachmentDao.insert(attachment)
            attachmentBelongMessageFromDB(attachment.messageId)
        }
    }
}