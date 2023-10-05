package com.example.typeapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider


class OtpActivity : AppCompatActivity() {

    lateinit var otptxt: EditText
    lateinit var proceed: Button
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        otptxt = findViewById(R.id.otp)
        proceed = findViewById(R.id.nxt_btn)
        val storedverificationid = intent.getStringExtra("storedVerificationId")
        auth = FirebaseAuth.getInstance()


        proceed.setOnClickListener {
            val otp = otptxt.text.trim().toString()
            if (otp.isNotEmpty()){
                val credential :PhoneAuthCredential = PhoneAuthProvider.getCredential(storedverificationid.toString(),otp)
                signinwithphoneauthcredential(credential)
            }else{
                Toast.makeText(this,"Enter OTP",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signinwithphoneauthcredential(credential: PhoneAuthCredential) {
        this.auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, UserActivity::class.java)
                    startActivity(intent)
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}