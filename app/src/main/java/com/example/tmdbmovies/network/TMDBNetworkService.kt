package com.example.tmdbmovies.network

import com.example.tmdbmovies.models.moviedetail.MovieDetailResponse
import com.example.tmdbmovies.models.movielist.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBNetworkService {

    @GET("3/movie/popular")
    suspend fun getMovieList(
        @Query("api_key") apikey:String,
        @Query("language") language:String,
        @Query("page") page:Int
    ):Response<MovieResponse>

     @GET("3/movie/{movie_id}")
     suspend fun getMovieDetail(
         @Path("movie_id") movie_id:Int,
         @Query("api_key") apikey:String,
         @Query("language") language:String
     ):Response<MovieDetailResponse>
}