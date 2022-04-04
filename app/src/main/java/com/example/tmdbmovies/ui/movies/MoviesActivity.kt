package com.example.tmdbmovies.ui.movies

import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.Navigation
import com.example.tmdbmovies.R
import com.example.tmdbmovies.base.BaseActivity
import dagger.android.support.DaggerAppCompatActivity

class MoviesActivity : BaseActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movie_activity_layout)
        val navController = Navigation.findNavController(this, R.id.movie_main_nav_host_fragment)
        val navInflater = navController.navInflater
        val graph = navInflater.inflate(R.navigation.movie_navigation_graph)
        navController.graph = graph
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
