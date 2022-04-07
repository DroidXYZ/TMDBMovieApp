package com.example.tmdbmovies.ui.movies

import com.example.tmdbmovies.network.TMDBNetworkService
import javax.inject.Inject

class MovieRepository @Inject  constructor(private val tmdbNetworkService: TMDBNetworkService){

   suspend fun getMovieList(apiKey:String,language : String, pageNo: Int) = tmdbNetworkService.getMovieList(apiKey,language,pageNo)
   suspend fun getMovieDetail(movieID:Int, apiKey:String,language : String) = tmdbNetworkService.getMovieDetail(movieID,apiKey,language)
   suspend fun getImageConfiguration(apiKey:String) = tmdbNetworkService.getImageConfiguration(apiKey)
}