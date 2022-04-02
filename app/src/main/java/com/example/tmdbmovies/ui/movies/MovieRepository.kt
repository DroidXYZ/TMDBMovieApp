package com.example.tmdbmovies.ui.movies

import com.example.tmdbmovies.network.TMDBNetworkService
import javax.inject.Inject

class MovieRepository @Inject  constructor(private val tmdbNetworkService: TMDBNetworkService){

   suspend fun getMovieList(apiKey:String) = tmdbNetworkService.getMovieList(apiKey)
}