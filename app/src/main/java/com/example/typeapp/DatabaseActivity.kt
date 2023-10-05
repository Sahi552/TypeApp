package com.example.typeapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.typeapp.Adopter.Myadopter
import com.example.typeapp.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DatabaseActivity : AppCompatActivity() {

    lateinit var RecyclerView : RecyclerView
    lateinit var dref : DatabaseReference
    lateinit var userArray: ArrayList<User>
    lateinit var btn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database)

        RecyclerView = findViewById(R.id.recyclerView)
        RecyclerView.layoutManager = LinearLayoutManager(this)
        RecyclerView.setHasFixedSize(true)
        btn = findViewById(R.id.nxtbtn2)

        userArray = arrayListOf<User>()
        getuserdata()

        btn.setOnClickListener {
            finish() // Finish the current activity
            moveTaskToBack(true) // Move the app to the background
            android.os.Process.killProcess(android.os.Process.myPid()) //kill the app process
        }


    }
    private fun getuserdata(){

        dref = FirebaseDatabase.getInstance().getReference("images")

        dref.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){

                    for (userSnapshot in snapshot.children){

                        val user = userSnapshot.getValue(User::class.java)
                        userArray.add(user!!)

                    }
                    RecyclerView.adapter = Myadopter(userArray)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext,"Failed to fetch data",Toast.LENGTH_SHORT).show()
            }

        })
    }
}