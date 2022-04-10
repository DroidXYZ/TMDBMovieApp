package com.example.tmdbmovies

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.tmdbmovies.BuildConfig.API_KEY
import com.example.tmdbmovies.models.moviedetail.MovieDetailResponse
import com.example.tmdbmovies.models.movielist.MovieResponse
import com.example.tmdbmovies.tmdbutils.TMDBConstants
import com.example.tmdbmovies.ui.movies.MovieRepository
import com.example.tmdbmovies.ui.movies.MoviesViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

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

    @Mock
    private lateinit var repository: MovieRepository

    @Mock
    private lateinit var apiMovieListObserver: Observer<MovieResponse>

    @Mock
    private lateinit var apiMovieDetailObserver: Observer<MovieDetailResponse>

    @Mock
    private lateinit var apiErrorObserver: Observer<String>

    @Before
    fun setup() {
    }

    @Test
    fun givenMovieDetailResponse_WhenFetchMovieDetail_ShouldUpdateLiveData() =
        mainCoroutineRule.runBlockingTest {
            //Given
            val movieDetailResponse = mock(MovieDetailResponse::class.java)
            val viewModel = MoviesViewModel(repository)
            viewModel.movieDetail.observeForever(apiMovieDetailObserver)
            viewModel.getMoviesDetail(53455, API_KEY, TMDBConstants.APP_LANGUAGE)
            viewModel.movieDetail.postValue(movieDetailResponse)

            //When
            doReturn(movieDetailResponse)
                .`when`(repository).getMovieDetail(53455, API_KEY, TMDBConstants.APP_LANGUAGE)

            //Than
            verify(repository).getMovieDetail(53455, API_KEY, TMDBConstants.APP_LANGUAGE)
            verify(apiMovieDetailObserver).onChanged(movieDetailResponse)
            viewModel.movieDetail.removeObserver(apiMovieDetailObserver)
        }


    @Test
    fun givenServerResponseError_whenFetchDetail_shouldReturnError() =
        mainCoroutineRule.runBlockingTest {
            //Given
            val errorMessage = "Error While Fetching Movie Detail"
            val viewModel = MoviesViewModel(repository)
            viewModel.errorMessage.observeForever(apiErrorObserver)
            viewModel.getMoviesDetail(45455, API_KEY, TMDBConstants.APP_LANGUAGE)
            viewModel.errorMessage.postValue(errorMessage)

            //When
            doThrow(RuntimeException(errorMessage))
                .`when`(repository)
                .getMovieDetail(45455, API_KEY, TMDBConstants.APP_LANGUAGE)

            //Than
            verify(repository).getMovieDetail(45455, API_KEY, TMDBConstants.APP_LANGUAGE)
            verify(apiErrorObserver).onChanged(RuntimeException(errorMessage).localizedMessage)
            viewModel.errorMessage.removeObserver(apiErrorObserver)
        }

    @Test
    fun givenMovieListResponse_WhenFetchMovieList_ShouldUpdateLiveData() =
        mainCoroutineRule.runBlockingTest {
            //Given
            val movieResponse = mock(MovieResponse::class.java)
            val viewModel = MoviesViewModel(repository)
            viewModel.movieList.observeForever(apiMovieListObserver)
            viewModel.getMoviesAndConfiguration(API_KEY, TMDBConstants.APP_LANGUAGE, 1)
            viewModel.movieList.postValue(movieResponse)

            //When
            doReturn(movieResponse)
                .`when`(repository).getMovieList(API_KEY, TMDBConstants.APP_LANGUAGE, 1)

            //Than
            verify(repository).getImageConfiguration(API_KEY)
            verify(repository).getMovieList(API_KEY, TMDBConstants.APP_LANGUAGE, 1)
            verify(apiMovieListObserver).onChanged(movieResponse)

            viewModel.movieList.removeObserver(apiMovieListObserver)
        }

    @Test
    fun givenServerResponseError_whenFetchMovieList_shouldReturnError() =
        mainCoroutineRule.runBlockingTest {
            //Given
            val errorMessage = "Error While Fetching Movie List"
            val viewModel = MoviesViewModel(repository)
            viewModel.errorMessage.observeForever(apiErrorObserver)
            viewModel.getMoviesAndConfiguration(API_KEY, TMDBConstants.APP_LANGUAGE, 1)
            viewModel.errorMessage.postValue(errorMessage)

            //When
            doThrow(RuntimeException(errorMessage)).`when`(repository)
                .getMovieList(API_KEY, TMDBConstants.APP_LANGUAGE, 1)

            //Than
            verify(repository).getImageConfiguration(API_KEY)
            verify(repository).getMovieList(API_KEY, TMDBConstants.APP_LANGUAGE, 1)
            verify(apiErrorObserver).onChanged(RuntimeException(errorMessage).localizedMessage)
            viewModel.errorMessage.removeObserver(apiErrorObserver)
        }


}
