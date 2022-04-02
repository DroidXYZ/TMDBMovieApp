package com.example.tmdbmovies.injection.component

import android.app.Application
import com.example.tmdbmovies.TMDBMovieApplication
import com.example.tmdbmovies.injection.module.ActivityBuilderModule
import com.example.tmdbmovies.injection.module.FragmentBuilderModule
import com.example.tmdbmovies.injection.module.NetworkBuilderModule
import com.example.tmdbmovies.injection.module.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Component(
    modules = [AndroidSupportInjectionModule::class,
                ActivityBuilderModule::class,
                FragmentBuilderModule::class,
                ViewModelModule::class,
                NetworkBuilderModule::class
    ]
)
interface AppComponent : AndroidInjector<TMDBMovieApplication> {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }
}