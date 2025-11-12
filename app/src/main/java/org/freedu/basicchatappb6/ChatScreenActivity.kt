package org.freedu.basicchatappb6

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.freedu.basicchatappb6.Model.Message
import org.freedu.basicchatappb6.adapter.MessageAdapter
import org.freedu.basicchatappb6.databinding.ActivityChatScreenBinding

class ChatScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatScreenBinding
    private var auth = FirebaseAuth.getInstance()
    private lateinit var dbRef: DatabaseReference
    private lateinit var adapter: MessageAdapter
    private val messages = mutableListOf<Message>()


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityChatScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = auth.currentUser
        if (user == null) {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
            return
        }
        binding.tvWelcome.text = "Hello:${user.email} "

        // RecyclerView setup
        adapter = MessageAdapter(messages, user.uid)
        binding.recyclerView.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true // scroll from Bottom
        }
        binding.recyclerView.adapter = adapter

        dbRef = FirebaseDatabase.getInstance().getReference("messages")

        dbRef.addChildEventListener(object : ChildEventListener {
            @SuppressLint("NotifyDataSetChanged")

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val msg = snapshot.getValue(Message::class.java)
                if (msg != null) {
                    messages.add(msg)
                    adapter.notifyDataSetChanged()
                    binding.recyclerView.scrollToPosition(adapter.itemCount - 1)
                }

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@ChatScreenActivity, "DB error: ${error.message}", Toast.LENGTH_SHORT
                ).show()
            }

        })


        // Send message
        binding.btnSend.setOnClickListener { sendMessage() }

        binding.etMessage.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendMessage()
                true
            } else false
        }

        binding.btnLogout.setOnClickListener {

            auth.signOut()
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
    }

    private fun sendMessage() {
        val text = binding.etMessage.text.toString().trim()
        if (text.isEmpty()) return

        val user = auth.currentUser ?: return
        // You can optionally fetch the user's display name from db; here we use email local part
        val displayName = user.email?.substringBefore("@") ?: "User"

        val msg = Message(
            senderId = user.uid,
            senderName = displayName,
            messageText = text,
            timestamp = System.currentTimeMillis()
        )

        dbRef.push().setValue(msg).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                binding.etMessage.setText("")
                binding.recyclerView.scrollToPosition(adapter.itemCount - 1)
            } else {
                Toast.makeText(
                    this, "Send failed: ${task.exception?.localizedMessage}", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}