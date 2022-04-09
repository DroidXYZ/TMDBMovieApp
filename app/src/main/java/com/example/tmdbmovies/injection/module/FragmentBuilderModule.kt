package com.example.tmdbmovies.injection.module

import com.example.tmdbmovies.ui.movies.MovieDetailFragment
import com.example.tmdbmovies.ui.movies.MovieListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilderModule {
    @ContributesAndroidInjector
    internal abstract fun contributeMovieListFragment(): MovieListFragment

    @ContributesAndroidInjector
    internal abstract fun contributeMovieDetailFragment(): MovieDetailFragment
}