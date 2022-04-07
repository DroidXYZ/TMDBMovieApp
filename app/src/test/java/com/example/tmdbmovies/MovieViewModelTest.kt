package com.example.tmdbmovies

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide.init
import com.bumptech.glide.load.engine.Resource
import com.example.tmdbmovies.models.moviedetail.MovieDetailResponse
import com.example.tmdbmovies.models.movielist.MovieResponse
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

    @Before
    fun setup() {
//        MockitoAnnotations.initMocks(this)
//        repository= mock(MovieRepository::class.java)
////        viewModel= mock(MoviesViewModel::class.java)


    }

    @Test
    fun `when movie list is called and update live data`() =
        mainCoroutineRule.runBlockingTest {

            val movieResponse = mock(MovieResponse::class.java)
//           `when`(repository.getMovieList(TMDBConstants.API_KEY,TMDBConstants.APP_LANGUAGE,1)).thenReturn(
//               success(movieResponse))

            doReturn(repository.getMovieList(TMDBConstants.API_KEY,TMDBConstants.APP_LANGUAGE,1))
                .`when`(repository).getMovieList(TMDBConstants.API_KEY,TMDBConstants.APP_LANGUAGE,1)

            val viewModel = MoviesViewModel(repository)
            viewModel.movieList.observeForever(apiUsersObserver)
            verify(repository).getMovieList(TMDBConstants.API_KEY,TMDBConstants.APP_LANGUAGE,1)
//            verify(apiUsersObserver).onChanged(movieResponse)
            viewModel.movieList.removeObserver(apiUsersObserver)

        }

//    @Test
//    fun givenServerResponseError_whenFetch_shouldReturnError() {
//        mainCoroutineRule.runBlockingTest {
//            val errorMessage = "Error Message"
//            doThrow(RuntimeException(errorMessage))
//                .`when`(repository)
//                .getMovieList(TMDBConstants.API_KEY,TMDBConstants.APP_LANGUAGE,1)
//            viewModel.movieList.observeForever(apiUsersObserver)
//            verify(repository).getMovieList(TMDBConstants.API_KEY,TMDBConstants.APP_LANGUAGE,1)
////            verify(apiUsersObserver).onChanged(RuntimeException(errorMessage).toString())
//            viewModel.movieList.removeObserver(apiUsersObserver)
//        }
//    }
}
