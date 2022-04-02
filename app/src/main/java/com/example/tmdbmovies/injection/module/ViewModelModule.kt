package com.example.tmdbmovies.injection.module

import androidx.lifecycle.ViewModel
import com.example.tmdbmovies.ui.movies.MoviesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MoviesViewModel::class)
    internal abstract fun bindMoviesViewModel(moviesViewModel: MoviesViewModel): ViewModel


}