package com.example.typeapp.models

data class User (
    val name : String,
    val imageUrl : String){

    constructor():this("","")
}