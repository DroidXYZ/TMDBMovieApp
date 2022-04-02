package com.example.tmdbmovies.ui.movies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tmdbmovies.R

class MoviesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MovieListFragment.newInstance())
                .commitNow()
        }
    }
}