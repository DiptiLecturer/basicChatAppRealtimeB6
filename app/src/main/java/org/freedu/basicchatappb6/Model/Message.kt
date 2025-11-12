package org.freedu.basicchatappb6.Model

data class Message(
    var senderId: String? = null,
    var senderName: String? = null,
    var messageText: String? = null,
    var timestamp: Long = 0L // use epoch millis
)

