package com.example.tmdbmovies.extensions

import android.content.Context
import android.widget.Toast

fun Context.showToast(text: CharSequence) {
    val duration = Toast.LENGTH_SHORT
    Toast.makeText(this, text, duration).show()
}

fun Context.showToastLong(text: CharSequence) {
    val duration = Toast.LENGTH_LONG
    Toast.makeText(this, text, duration).show()
}