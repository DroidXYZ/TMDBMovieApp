package com.example.tmdbmovies.ui.movies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tmdbmovies.models.moviedetail.MovieDetailResponse
import com.example.tmdbmovies.models.movielist.MovieResponse
import kotlinx.coroutines.*
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

    fun getMoviesList(apiKey:String,language : String, pageNo: Int) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.getMovieList(apiKey,language,pageNo)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    movieList.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }

    }

    fun getMoviesDetail(path:Int, apiKey:String,language : String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.getMovieDetail(path,apiKey,language)
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

    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}