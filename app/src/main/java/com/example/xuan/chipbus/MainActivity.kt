package com.example.xuan.chipbus

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        register_account_btn.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }

        login_button.setOnClickListener {
            val email = email_editted_login.text.toString()
            val password = password_editted_login.text.toString()

            Log.d("Login","Attempt login with email and password: $email")

            if(!email.isEmpty() && !password.isEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener {
                            if(it.isSuccessful) {
                                val intent = Intent(this,LatestProductActivity::class.java)
                                startActivity(intent)
                                Log.d("LoginActivity","Successfully to Login Account")
                            }
                            else {
                                Log.d("LoginActivity","Failed to Login Account")
                            }
                        }

            }
        }

    }
}
