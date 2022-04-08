package com.example.tmdbmovies.ui.movies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tmdbmovies.models.ErrorMessage
import com.example.tmdbmovies.models.configuration.ConfigurationResponse
import com.example.tmdbmovies.models.moviedetail.MovieDetailResponse
import com.example.tmdbmovies.models.movielist.MovieResponse
import com.google.gson.Gson
import kotlinx.coroutines.*
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject


class MoviesViewModel @Inject constructor(private val repository: MovieRepository) : ViewModel() {



     val errorMessage = MutableLiveData<String>()
     val movieList = MutableLiveData<MovieResponse>()
     val movieDetail = MutableLiveData<MovieDetailResponse>()
     private var job: Job? = null
     private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
         errorMessage.postValue("${throwable.localizedMessage}")
         loading.postValue(false)
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
                    response.errorBody()?.let { onError(it) }
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
                    configurationResponse.errorBody()?.let { onError(it) }
                    if(movieListResponse.isSuccessful){
                        movieList.postValue(movieListResponse.body())
                        loading.value = false
                    }else{
                        movieListResponse.errorBody()?.let { onError(it) }
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
            movieListResponse.errorBody()?.let { onError(it) }
        }
    }


    private fun onError(message: ResponseBody) {
        val errorMessageResponse = Gson().fromJson(message.string(),ErrorMessage::class.java)
        errorMessage.value = errorMessageResponse.status_message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}