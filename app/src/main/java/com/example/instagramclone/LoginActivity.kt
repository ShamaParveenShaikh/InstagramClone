package com.example.instagramclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.instagramclone.Models.User
import com.example.instagramclone.databinding.ActivityLogin2Binding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLogin2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogin2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginBtn.setOnClickListener {

            if ((binding.email.editText?.text.toString()=="" ) or
            (binding.password.editText?.text.toString()=="") ) {
                Toast.makeText( this,"Please fill all the details",Toast.LENGTH_SHORT).show()
            }else{
                val user = User(binding.email.editText?.text.toString(),
                    binding.password.editText?.text.toString())
                Firebase.auth.signInWithEmailAndPassword(user.email!!,user.password!!)
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            startActivity(Intent(this@LoginActivity ,HomeActivity::class.java))
                            finish()
                       }
                                                else
                        {
                            Toast.makeText(this , it.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                        }
                    }

            }
        }
    }
}