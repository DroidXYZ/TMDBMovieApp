package com.example.tmdbmovies.injection.module

import androidx.lifecycle.ViewModelProvider
import com.example.tmdbmovies.injection.component.ViewModelFactory
import dagger.Binds
import dagger.Module


@Module
abstract class ViewModelFactoryModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}