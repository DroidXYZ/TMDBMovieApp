package com.example.tmdbmovies.injection.module

import android.content.Context
import androidx.annotation.NonNull
import com.example.tmdbmovies.network.TMDBNetworkService
import com.example.tmdbmovies.tmdbutils.TMDBEndPoint
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkBuilderModule {

    @Singleton
    @Named("OkHttpClient")
    @Provides
    fun provideHttpClient(context: Context): OkHttpClient {
        return  OkHttpClient.Builder()
            .build()
    }

    @Singleton
    @Named("Retrofit")
    @Provides
    fun provideRetrofit(@NonNull @Named("OkHttpClient")okHttpClient: OkHttpClient): Retrofit {

        val gson =  GsonBuilder()
            .setLenient()
            .create()
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(TMDBEndPoint.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Singleton
    @Provides
    fun provideService(@NonNull @Named("Retrofit") retrofit: Retrofit): TMDBNetworkService {
        return retrofit.create(TMDBNetworkService::class.java)
    }
}