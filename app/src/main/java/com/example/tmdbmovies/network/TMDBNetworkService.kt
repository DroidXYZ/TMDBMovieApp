package com.example.tmdbmovies.network

import com.example.tmdbmovies.models.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

abstract class TMDBNetworkService {

    @GET("4/list/1")
    abstract suspend fun getMovieList(@Query("apikey") apikey:String ):Response<MovieResponse>
}