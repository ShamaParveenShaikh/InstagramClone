package com.example.instagramclone

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.instagramclone.Models.User
import com.example.instagramclone.Models.uploadImage
import com.example.instagramclone.databinding.ActivitySignUpBinding
import com.example.instagramclone.utils.USER_NODE
import com.example.instagramclone.utils.USER_PROFILE_FOLDER
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.squareup.picasso.Picasso


class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var user: User
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {

            uploadImage(uri, USER_PROFILE_FOLDER) {
                if (it == null) {

                } else {
                    user.image = it
                    binding.profileImage.setImageURI(uri)
                }
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val text = "<font color=#FF000000>Already have an Account</font> " +
                "<font color=#198BE6>login?</font>"
        binding.login.text = Html.fromHtml(text)
        user = User()
//        if (intent.hasExtra("MODE")) {
//            if (intent.getIntExtra("MODE", -1) == 1) {
//                binding.signUpBtn.text = "Update Profile"
//                Firebase.firestore.collection(USER_NODE)
//                    .document(Firebase.auth.currentUser!!.uid).get()
//                    .addOnSuccessListener {
//
//                        user = it.toObject<User>()!!
//                        if (!user.image.isNullOrEmpty()) {
//                            Picasso.get().load(user.image).into(binding.profileImage)
//                        }
//                        binding.name.editText?.setText(user.name)
//                        binding.email.editText?.setText(user.email)
//                        binding.password.editText?.setText(user.password)
//
//                    }
//            }
//        }



        binding.signUpBtn.setOnClickListener {

            if (intent.hasExtra("MODE")) {
                if (intent.getIntExtra("MODE", -1) == 1) {
                    Firebase.firestore.collection(USER_NODE)
                        .document(Firebase.auth.currentUser!!.uid).set(user)
                        .addOnSuccessListener {

                            startActivity(Intent(this@SignUpActivity, HomeActivity::class.java))
                            finish()
                        }
                }
            } else {


                if ((binding.name.editText?.text.toString() == "") or
                    (binding.email.editText?.text.toString() == "") or
                    (binding.password.editText?.text.toString() == "")
                ) {
                    Toast.makeText(this, "please fill your all information", Toast.LENGTH_SHORT)
                        .show()
                } else {

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        binding.email.editText?.text.toString(),
                        binding.password.editText?.text.toString()
                    ).addOnCompleteListener { result ->

                        if (result.isSuccessful) {
                            Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT)
                                .show()
                            user.name = binding.name.editText?.text.toString()
                            user.email = binding.email.editText?.text.toString()
                            user.password = binding.password.editText?.text.toString()
                            Firebase.firestore.collection(USER_NODE)
                                .document(Firebase.auth.currentUser!!.uid).set(user)
                                .addOnSuccessListener {

                                    startActivity(
                                        Intent(
                                            this@SignUpActivity,
                                            HomeActivity::class.java
                                        )
                                    )
                                    finish()
                                }

                        } else {
                            Toast.makeText(
                                this,
                                result.exception?.localizedMessage,
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }
                }
            }

        }
        binding.addImage.setOnClickListener {
            launcher.launch("image/*")
        }
        binding.login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }
}