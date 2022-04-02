package com.example.tmdbmovies.ui.movies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tmdbmovies.models.MovieResponse
import kotlinx.coroutines.*
import javax.inject.Inject

class MoviesViewModel @Inject constructor(private val repository: MovieRepository) : ViewModel() {
    private val errorMessage = MutableLiveData<String>()
    private val movieList = MutableLiveData<MovieResponse>()
    private var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val loading = MutableLiveData<Boolean>()

    fun getMoviesList(apiKey:String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.getMovieList(apiKey)
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

    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}