package com.example.xuan.chipbus

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        register_button.setOnClickListener {
            val name = register_username_text_view.text.toString()
            val email = register_email_text_view.text.toString()
            val password = register_password_text_view.text.toString()
            val phone = register_phone_text_view.text.toString()
            val andress = register_andress_text_view.text.toString()

            if(name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty())
            {
                Toast.makeText(this,"Xin vui lòng nhập đầy đủ thông tin",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.d("RegisterActivity","name:"+ name )
            Log.d("RegisterActivity","email:"+ email)
            Log.d("RegisterActivity","password:"+ password)
            Log.d("RegisterActivity","phone:"+ phone)
            Log.d("RegisterActivity","andress:"+ andress)

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this) {
                        if (it.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("RegisterActivity", "signInWithEmail:success : ${it.result.user.uid}")

                              saveUserToFirebaseDatabase()

                        }else {
                            // If sign in fails, display a message to the user.
                            Log.w("RegisterActivity", "signInWithEmail:failure", it.exception)
                            Toast.makeText(this,"Please enter your email,username or password: ${it.exception}",Toast.LENGTH_SHORT).show()
                        }
                        // ...
                    }
        }
    }
    private fun saveUserToFirebaseDatabase() {

        val uid = FirebaseAuth.getInstance().uid ?: ""
        Log.d("RegisterActivity","fuck u :$uid")

        // database = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        Log.d("RegisterActivity","i want you action :$ref")
        val user = User(uid, register_username_text_view.text.toString(),register_andress_text_view.text.toString() )

        ref.setValue(user)

                .addOnSuccessListener {
                    Log.d("RegisterActivity", "Success save data to Firebase")

                    //open LatestMessegesActivity when you success save data to database
                    val intent = Intent(this,LatestProductActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                .addOnFailureListener {
                    Log.d("RegisterActivity", "Failed to set value to database :${it.message}")
                }

    }

}
class User (val uid: String,val username: String, val andress : String) {
    constructor() : this("","","")
}



