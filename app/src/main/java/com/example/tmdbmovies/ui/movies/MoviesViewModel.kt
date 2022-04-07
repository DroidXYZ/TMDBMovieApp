package com.example.tmdbmovies.ui.movies

import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.tmdbmovies.models.configuration.ConfigurationResponse
import com.example.tmdbmovies.models.moviedetail.MovieDetailResponse
import com.example.tmdbmovies.models.movielist.MovieResponse
import kotlinx.coroutines.*
import retrofit2.Response
import javax.inject.Inject

class MoviesViewModel @Inject constructor(private val repository: MovieRepository) : ViewModel() {



     val errorMessage = MutableLiveData<String>()
     val movieList = MutableLiveData<MovieResponse>()
     val movieDetail = MutableLiveData<MovieDetailResponse>()
     private var job: Job? = null
     private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
     }
     val loading = MutableLiveData<Boolean>()


    fun getMoviesDetail(movieID:Int, apiKey:String,language : String) {
        loading.value = true
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.getMovieDetail(movieID,apiKey,language)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    movieDetail.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }

    }


    fun getMoviesAndConfiguration(apiKey:String,language : String, pageNo: Int) {
        loading.value = true
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            // get configuration first
            val configurationAsync = async {repository.getImageConfiguration(apiKey) }
            // than movie list
            val movieAsync = async {repository.getMovieList(apiKey,language,pageNo)}
            val configurationResponse = configurationAsync.await()
            val movieListResponse= movieAsync.await()
            withContext(Dispatchers.Main) {
                if (configurationResponse.isSuccessful) {
                    bindConfigurationWithMovieList(configurationResponse,movieListResponse)
                } else {
                    onError("Error : ${configurationResponse.message()} ")
                    if(movieListResponse.isSuccessful){
                        movieList.postValue(movieListResponse.body())
                        loading.value = false
                    }else{
                        onError("Error : ${movieListResponse.message()} ")
                    }
                }
            }
        }


    }
    private fun bindConfigurationWithMovieList(configurationResponse:Response<ConfigurationResponse>,movieListResponse:Response<MovieResponse>){
        var imageWidth = "original"
        val configurationResponseBody= configurationResponse.body()
        val posterArraySize = configurationResponseBody?.images?.poster_sizes?.size?:0
        if (posterArraySize>3){
            imageWidth= configurationResponseBody?.images?.poster_sizes?.get(posterArraySize-3)!!
        }
        val imagePath=  "${configurationResponseBody?.images?.secure_base_url}${imageWidth}"
        if(movieListResponse.isSuccessful){
            val movieListResponseBody = movieListResponse.body()
            movieListResponseBody?.results?.forEach {
                it.poster_path = "$imagePath${it.poster_path}"
            }
            movieList.postValue(movieListResponse.body())
            loading.value = false
        }else{
            onError("Error : ${movieListResponse.message()} ")
        }
    }


    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}