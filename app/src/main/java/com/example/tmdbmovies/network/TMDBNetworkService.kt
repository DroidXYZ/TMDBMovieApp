package com.example.tmdbmovies.network

import com.example.tmdbmovies.models.configuration.ConfigurationResponse
import com.example.tmdbmovies.models.moviedetail.MovieDetailResponse
import com.example.tmdbmovies.models.movielist.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBNetworkService {

    /**
     * [Get Movie Popular List](https://developers.themoviedb.org/3/movies/get-popular-movies)
     *
     *  Get movies by Popular list
     *
     *  @param [api_key] Specify the API key.
     *  @param [language] Specify the language current default is en-US.
     *  @param [page] Specify the page no.
     *
     *  @return [MovieResponse] response
     */
    @GET("3/movie/popular")
    suspend fun getMovieList(
        @Query("api_key") api_key: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<MovieResponse>

    /**
     * [Get Movie Popular Detail](https://developers.themoviedb.org/3/movies/get-movie-details)
     *
     *  Get movies by Popular detail
     *
     *  @param [api_key] Specify the API key.
     *  @param [language] Specify the language current default is en-US.
     *  @param [movie_id] Specify the movie ID which is return in list.
     *
     *  @return [MovieDetailResponse] response
     */
    @GET("3/movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String,
        @Query("language") language: String
    ): Response<MovieDetailResponse>


    /**
     * [Get Movie Poster Image Path](https://developers.themoviedb.org/3/configuration/get-api-configuration)
     *
     *  Get Movie Poster Image Path
     *
     *  @param [api_key] Specify the API key.
     *
     *  @return [ConfigurationResponse] response
     */
    @GET("3/configuration")
    suspend fun getImageConfiguration(
        @Query("api_key") api_key: String
    ): Response<ConfigurationResponse>
}