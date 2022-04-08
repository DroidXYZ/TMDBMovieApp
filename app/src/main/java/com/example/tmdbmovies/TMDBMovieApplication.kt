package com.example.tmdbmovies

import com.example.tmdbmovies.injection.component.DaggerAppComponent
import com.example.tmdbmovies.tmdbutils.InternetUtil
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class TMDBMovieApplication:DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent
            .builder()
            .applicationContext(applicationContext)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        InternetUtil.init(this)
    }

}