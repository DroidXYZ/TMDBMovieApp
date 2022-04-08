package com.example.tmdbmovies.models

data class ErrorMessage (
    val success:String,
    val status_code:String,
    val status_message:String
    )