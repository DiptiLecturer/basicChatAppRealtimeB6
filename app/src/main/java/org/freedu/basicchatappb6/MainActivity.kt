package org.freedu.basicchatappb6

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    //private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        /*  auth = FirebaseAuth.getInstance()
          val user = auth.currentUser
          if (user != null) {
              // already signed in -> go to ChatActivity
              startActivity(Intent(this, ChatScreenActivity::class.java))
          } else {
              // not signed in -> go to SignInActivity
              startActivity(Intent(this, SignInActivity::class.java))
          }
          finish()
      }*/

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        },2000)
    }
}