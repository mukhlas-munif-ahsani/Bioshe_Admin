package com.munifahsan.biosheadmin.ui.chatRoom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.munifahsan.biosheadmin.databinding.ActivityChatRoomBinding

class ChatRoomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatRoomBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatRoomBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}