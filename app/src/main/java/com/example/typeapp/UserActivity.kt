package com.example.typeapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.typeapp.image.ImageUpload
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class UserActivity : AppCompatActivity() {

    lateinit var img : ImageView
    lateinit var name : EditText
    lateinit var login :Button

    private val firestore = FirebaseFirestore.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val imageRef = storage.reference.child("images")
    private var selectedImageUri: Uri? = null



    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data != null && data.data != null) {
                    // Get the selected image URI
                    selectedImageUri = data.data
                    // Display the selected image in the ImageView
                    img.setImageURI(selectedImageUri)
                }
            }
        }

    private fun openImageChooser() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        getContent.launch(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        img = findViewById(R.id.image)
        name = findViewById(R.id.name)
        login = findViewById(R.id.login)


        img.setOnClickListener {
            openImageChooser()
        }
        login.setOnClickListener {
            val username = name.text.toString()
            if (username.isNotEmpty() && selectedImageUri != null){

                val imageRef = imageRef.child(selectedImageUri!!.lastPathSegment!!)
                val uploadTask = imageRef.putFile(selectedImageUri!!)

                uploadTask.addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        imageRef.downloadUrl.addOnSuccessListener { uri->

                            val imageupload = ImageUpload(uri.toString(),username)
                            firestore.collection("images").add(imageupload)

                            val databaseRef = database.reference.child("images")
                            val key = databaseRef.push().key

                            if(key != null){
                                databaseRef.child(key).setValue(imageupload)
                            }
                            Toast.makeText(this, "Upload successful", Toast.LENGTH_SHORT).show()
                            val i = Intent(this@UserActivity,DatabaseActivity::class.java)
                            startActivity(i)
                        }
                    }else {
                        Toast.makeText(this, "Upload failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }else {
                Toast.makeText(this, "Name and image selection required", Toast.LENGTH_SHORT).show()
            }
        }
    }
}