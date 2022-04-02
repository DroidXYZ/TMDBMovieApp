package com.example.tmdbmovies.injection.module

import com.example.tmdbmovies.ui.movies.MoviesActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {
    @ContributesAndroidInjector
    internal abstract fun contributeMoviesActivity():MoviesActivity
}