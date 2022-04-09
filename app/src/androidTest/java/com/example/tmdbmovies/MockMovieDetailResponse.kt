package com.example.tmdbmovies

import com.example.tmdbmovies.models.moviedetail.*

object MockMovieDetailResponse {

    fun getMovieDetailModel() :MovieDetailResponse{

         return MovieDetailResponse(
             adult = false,
            backdrop_path = null,
             belongs_to_collection =null,
             budget= 0,
             genres= emptyList(),
             homepage= null,
             id= 0,
             imdb_id= null,
             original_language= "English",
             original_title= "Title",
             overview= null,
              popularity= 0.0,
             poster_path= null,
             production_companies= emptyList(),
             production_countries= emptyList(),
             release_date= "",
             revenue= 0,
             runtime= 0,
             spoken_languages = emptyList(),
             status= "",
             tagline= null,
             title= "",
             video =false,
             vote_average= 0.0,
             vote_count =0
        )
    }
}