package com.example.tmdbmovies.injection.module

import androidx.annotation.NonNull
import com.example.tmdbmovies.network.TMDBNetworkService
import com.example.tmdbmovies.ui.movies.MovieListFragment
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkBuilderModule {
    @Provides
    @Singleton
    @Named("Retrofit")
    fun provideRetrofit(@NonNull @Named("OkHttpClient") okHttpClient: OkHttpClient): Retrofit {

        val gson =  GsonBuilder()
            .setLenient()
            .create()
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://api.themoviedb.org/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideService(@NonNull @Named("Retrofit") retrofit: Retrofit): TMDBNetworkService {
        return retrofit.create(TMDBNetworkService::class.java)
    }
}