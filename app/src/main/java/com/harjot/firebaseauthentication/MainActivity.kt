package com.harjot.firebaseauthentication

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.harjot.firebaseauthentication.databinding.ActivityMainBinding
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private var auth: FirebaseAuth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.etPassword.doOnTextChanged { text, _, _, _ ->
            if((text?.length ?:0) <6){
                binding.etPassword.error = "Atleast 6 characters"
            }else{
                binding.etPassword.error = null
            }
        }
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding.btnLogin.setOnClickListener {
            if (binding.etEmail.text.toString().trim().isNullOrEmpty()){
                binding.etEmail.error = "Enter Email"
                binding.etEmail.requestFocus()
            }else if(Pattern.matches(Patterns.EMAIL_ADDRESS.toString(),binding.etEmail.text.toString().trim())==false){
                binding.etEmail.error = "Enter Valid Email"
                binding.etEmail.requestFocus()
            } else if (binding.etPassword.text.toString().trim().isNullOrEmpty())
            {
                binding.etPassword.error = "Enter Password"
                binding.etPassword.requestFocus()
            } else if(binding.etPassword.length()<6){
                binding.etPassword.error="Enter Atleast 6 Characters"
            }else{
                var email = binding.etEmail.text.toString().trim()
                var password = binding.etPassword.text.toString().trim()
                auth.signInWithEmailAndPassword(email,password)
                    .addOnSuccessListener {
                        var intent=Intent(this, AfterLogin::class.java)
                        Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show()
                        startActivity(intent)
                        this.finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Password or Email Incorrect", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}