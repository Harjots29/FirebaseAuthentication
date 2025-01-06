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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.harjot.firebaseauthentication.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }
    var model = Model()
    private val collection = "User"
    private var auth: FirebaseAuth = Firebase.auth
    private var database = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding?.etPassword?.doOnTextChanged { text, _, _, _ ->
            if((text?.length ?:0) <6){
                binding?.etPassword?.error = "Atleast 6 characters"

            }else{
                binding?.etPassword?.error = null
            }
        }
        binding.btnRegister.setOnClickListener {
            if (binding.etName.text.toString().trim().isNullOrEmpty()) {
                binding.etName.error = "Enter Name"
                binding.etName.requestFocus()
            }else if(binding.etAddress.text.toString().trim().isNullOrEmpty()){
                binding.etAddress.error = "Enter Address"
                binding.etAddress.requestFocus()
            }else if(binding.etContact.text.toString().trim().isNullOrEmpty()){
                binding.etContact.error = "Enter Contact"
                binding.etContact.requestFocus()
            }else if(binding.etEmail.text.toString().trim().isNullOrEmpty()){
                binding.etEmail.error = "Enter Email"
                binding.etEmail.requestFocus()
            }else if(!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString().trim()).matches()){
                binding.etEmail.error = "Enter Valid Email"
                binding.etEmail.requestFocus()
            }else if(binding.etPassword.text.toString().trim().isNullOrEmpty()){
                binding.etPassword.error = "Enter Password"
                binding.etPassword.requestFocus()
            }else if(binding.etConfPassword.text.toString().trim().isNullOrEmpty()){
                binding.etConfPassword.error = "Enter Confirm Password"
                binding.etConfPassword.requestFocus()
            }else if(binding.etPassword.text.toString().length<6){
                binding.etPassword.error = "Enter Atleast 6 Characters"
                binding.etPassword.requestFocus()
            }else if(binding.etPassword.text.toString() != binding.etConfPassword.text.toString()){
                binding.etConfPassword.error = "Password Not Matched"
                binding.etConfPassword.requestFocus()
            }else{
                writeInDb()
            }
        }

    }
    private fun writeInDb() {
        model.name = binding?.etName?.text.toString()
        model.userId = auth.currentUser?.uid?:""
        model.address = binding?.etAddress?.text.toString()
        model.contact = binding?.etContact?.text.toString()
        model.email = binding?.etEmail?.text.toString()

        database.collection(collection).document(auth.currentUser?.uid?:"" )
            .set(model)
            .addOnSuccessListener {

                var intent = Intent(this, AfterLogin::class.java)
                Toast.makeText(
                    this,"Registration Successful",
                    Toast.LENGTH_LONG
                ).show()
                startActivity(intent)
                this.finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
    }
}