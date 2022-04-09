package com.example.tmdbmovies

import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tmdbmovies.models.moviedetail.MovieDetailResponse
import com.example.tmdbmovies.models.movielist.MovieResponse
import com.example.tmdbmovies.models.movielist.Result
import com.example.tmdbmovies.tmdbutils.TMDBConstants
import com.example.tmdbmovies.ui.movies.MovieDetailFragment
import com.example.tmdbmovies.ui.movies.MovieListFragment
import com.example.tmdbmovies.ui.movies.MoviesViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.runners.model.TestClass
import org.mockito.Mockito.mock

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class MovieDetailFragmentTest {

    private lateinit var scenario: FragmentScenario<MovieDetailFragment>

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.tmdbmovies", appContext.packageName)
    }

    @Before
    fun setUp() {

        scenario = launchFragmentInContainer(fragmentArgs = targetBundle(), themeResId = R.style.Theme_TMDBMovies)
        scenario.moveToState(newState = Lifecycle.State.STARTED)
    }

    private fun targetBundle(): Bundle? {
        val bundle = Bundle()
        bundle.putInt(TMDBConstants.EXTRA_MOVIE_ID, 54545)
        bundle.putString(TMDBConstants.EXTRA_MOVIE_IMAGE_PATH, "posterpath")
        return bundle
    }
    @Test
    fun assert_Movie_Detail_Fragment_Render_The_UI_According_The_Loading_State() {
        scenario.onFragment(){ fragment->
            fragment.showLoadingIndicator(true)

        }
        onView(withId(R.id.progressBar)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

    }

    @Test
    fun assert_Movie_Detail_Fragment_Render_The_UI_According_The_Empty_Movie_Detail_State() {
        scenario.onFragment(){ fragment->
            val movieResponse = MockMovieDetailResponse.getMovieDetailModel()
            fragment.updateUI(movieResponse)
        }
        onView(withId(R.id.clMainLayout)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

    @Test
    fun assert_Movie_Detail_Fragment_Render_The_UI_According_The_Error_Movie_Detail_State() {
        scenario.onFragment(){ fragment->
            fragment.showEmptyState(true,"error message")
        }
        onView(withId(R.id.clMainLayout)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        onView(withId(R.id.tvEmptyView)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

}