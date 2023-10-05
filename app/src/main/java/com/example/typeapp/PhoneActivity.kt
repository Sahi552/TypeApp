package com.example.typeapp

import android.app.AlertDialog
import android.app.Application
import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.hbb20.CountryCodePicker
import java.util.concurrent.TimeUnit


class PhoneActivity : AppCompatActivity() {
    lateinit var otp: Button
    lateinit var phoneNumber: EditText
    lateinit var countryCodePicker: CountryCodePicker
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone)

        otp = findViewById(R.id.sendotpbtn)
        phoneNumber = findViewById(R.id.name)
        countryCodePicker = findViewById(R.id.ccp)
        auth = FirebaseAuth.getInstance()

        // Connect phone number with country code picker
        countryCodePicker.registerCarrierNumberEditText(phoneNumber)

        otp.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val number = phoneNumber.text.toString().trim()

        if (number.isNotEmpty()) {
            val fullNumber = countryCodePicker.fullNumberWithPlus
            sendVerificationCode(fullNumber)
        } else {
            Toast.makeText(this, "Enter a mobile number", Toast.LENGTH_LONG).show()
        }
    }

    private fun sendVerificationCode(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    startActivity(Intent(applicationContext, UserActivity::class.java))
                    finish()
                    Toast.makeText(this@PhoneActivity, "Phone number verified", Toast.LENGTH_LONG).show()
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    // Log or display the actual error message for debugging
                    Toast.makeText(this@PhoneActivity, "Verification failed: ${e.message}", Toast.LENGTH_LONG).show()
                }

                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    val storedVerificationId = verificationId
                    val resendToken = token

                    val intent = Intent(applicationContext, OtpActivity::class.java)
                    intent.putExtra("storedVerificationId", storedVerificationId)
                    startActivity(intent)
                    finish()
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }

}






