package com.example.tmdbmovies

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide.init
import com.bumptech.glide.load.engine.Resource
import com.example.tmdbmovies.BuildConfig.API_KEY
import com.example.tmdbmovies.models.moviedetail.MovieDetailResponse
import com.example.tmdbmovies.models.movielist.MovieResponse
import com.example.tmdbmovies.models.movielist.Result
import com.example.tmdbmovies.network.TMDBNetworkService
import com.example.tmdbmovies.tmdbutils.TMDBConstants
import com.example.tmdbmovies.ui.movies.MovieRepository
import com.example.tmdbmovies.ui.movies.MoviesViewModel
import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.Response
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response.error
import retrofit2.Response.success

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MovieViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = CoroutineRule()

//    // Subject under test
//    private lateinit var viewModel: MoviesViewModel

    @Mock
    private lateinit var repository: MovieRepository

    @Mock
    private lateinit var apiUsersObserver: Observer<MovieResponse>

    @Mock
    private lateinit var apiErrorObserver: Observer<String>

    @Before
    fun setup() {
//        MockitoAnnotations.initMocks(this)
//        repository= mock(MovieRepository::class.java)
////        viewModel= mock(MoviesViewModel::class.java)


    }

    @Test
    fun givenServerResponse200_whenFetch_shouldReturnSuccess() =
        mainCoroutineRule.runBlockingTest {

            val movieResponse = mock(MovieResponse::class.java)
//           `when`(repository.getMovieList(TMDBConstants.API_KEY,TMDBConstants.APP_LANGUAGE,1)).thenReturn(
//               success(movieResponse))

            doReturn(emptyList<Result>())
                .`when`(repository).getMovieList(API_KEY,TMDBConstants.APP_LANGUAGE,1)

            val viewModel = MoviesViewModel(repository)
            viewModel.movieList.observeForever(apiUsersObserver)
            verify(repository).getMovieList(API_KEY,TMDBConstants.APP_LANGUAGE,1)
//            verify(apiUsersObserver).onChanged(movieResponse)
            viewModel.movieList.removeObserver(apiUsersObserver)

        }

    @Test
    fun givenServerResponseError_whenFetch_shouldReturnError() {
        mainCoroutineRule.runBlockingTest {
            val errorMessage = "Error Message"
            doThrow(RuntimeException(errorMessage))
                .`when`(repository)
                .getMovieList(API_KEY,TMDBConstants.APP_LANGUAGE,1)
            val viewModel = MoviesViewModel(repository)
            viewModel.errorMessage.observeForever(apiErrorObserver)
            verify(repository).getMovieList(API_KEY,TMDBConstants.APP_LANGUAGE,1)
            verify(apiErrorObserver).onChanged(RuntimeException(errorMessage).toString())
            viewModel.movieList.removeObserver(apiUsersObserver)
        }
    }
}
