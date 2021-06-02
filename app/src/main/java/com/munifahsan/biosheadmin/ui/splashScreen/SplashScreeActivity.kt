package com.munifahsan.biosheadmin.ui.splashScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.munifahsan.biosheadmin.MainActivity
import com.munifahsan.biosheadmin.databinding.ActivitySplashScreeBinding
import com.munifahsan.biosheadmin.ui.login.LoginActivity
import com.munifahsan.biosheadmin.utils.Constants

class SplashScreeActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreeBinding
    private lateinit var auth: FirebaseAuth
    val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        val userid: String? = currentUser?.uid
        if (currentUser != null){
            currentUser.getIdToken(true).addOnCompleteListener {
//                if (it.isSuccessful){
//                    Constants.USER_DB.
//                }
            }
            Constants.ADMIN_DB.document().get().addOnSuccessListener {

            }
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}