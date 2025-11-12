package org.freedu.basicchatappb6.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import org.freedu.basicchatappb6.Model.Message
import org.freedu.basicchatappb6.databinding.ItemMessageBinding
import java.text.SimpleDateFormat
import java.util.*

class MessageAdapter(
    private val messages: MutableList<Message>,
    private val currentUserId: String
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    inner class MessageViewHolder(val binding: ItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = ItemMessageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MessageViewHolder(binding)
    }

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]

        // Set message text and sender
        holder.binding.tvMessage.text = message.messageText
        holder.binding.tvSender.text = message.senderName

        // Format timestamp
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        holder.binding.tvTime.text = sdf.format(Date(message.timestamp))

        // Align message left/right
        val params = holder.binding.cardMessage.layoutParams as FrameLayout.LayoutParams
        if (message.senderId == currentUserId) {
            params.gravity = android.view.Gravity.END
            holder.binding.cardMessage.setCardBackgroundColor(Color.parseColor("#DCF8C6")) // sender color
        } else {
            params.gravity = android.view.Gravity.START
            holder.binding.cardMessage.setCardBackgroundColor(Color.WHITE) // receiver color
        }
        holder.binding.cardMessage.layoutParams = params
    }

    // Helper function to add new messages
    fun addMessage(msg: Message) {
        messages.add(msg)
        notifyItemInserted(messages.size - 1)
    }
}
