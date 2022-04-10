package com.example.tmdbmovies

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tmdbmovies.models.movielist.Result
import com.example.tmdbmovies.ui.movies.MovieListFragment
import com.example.tmdbmovies.ui.movies.MoviesActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class MovieListFragmentTest {

    @get:Rule
    var activityRule = ActivityScenarioRule(MoviesActivity::class.java)

    private lateinit var scenario: FragmentScenario<MovieListFragment>

    @Before
    fun setUp() {

        scenario = launchFragmentInContainer(themeResId = R.style.Theme_TMDBMovies)
        scenario.moveToState(newState = Lifecycle.State.STARTED)
    }

    @Test
    fun assert_Movie_List_Fragment_Render_The_UI_According_The_Loading_State() {
        scenario.onFragment() { fragment ->
            fragment.showLoadingIndicator(true)

        }
        onView(withId(R.id.progressBar)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        onView(withId(R.id.tvEmptyView)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))

    }

    @Test
    fun assert_Movie_List_Fragment_Render_The_UI_According_The_Empty_Movie_List_State() {
        scenario.onFragment() { fragment ->
            fragment.renderMovieListUI(arrayListOf<Result>())
        }
        onView(withId(R.id.rvMovieList)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

    @Test
    fun assert_Movie_List_Fragment_Render_The_UI_According_The_Error_Movie_List_State() {
        scenario.onFragment() { fragment ->
            fragment.showEmptyState(true, "error message")
        }
        onView(withId(R.id.rvMovieList)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        onView(withId(R.id.tvEmptyView)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

    @Test
    fun assert_Movie_List_Fragment_Render_The_UI_According_The_Movie_List_State() {
        scenario.onFragment() {
        }
        onView(withId(R.id.rvMovieList)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

    @Test
    fun test_isSelectListItem_detailFragmentVisible() {
        // Create a TestNavHostController
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )

        // Create a graphical FragmentScenario for the MovieListFragment
        scenario.onFragment { fragment ->

            // Set the graph on the TestNavHostController
            navController.setGraph(R.navigation.movie_navigation_graph)

            // Make the NavController available via the findNavController() APIs
            Navigation.setViewNavController(fragment.requireView(), navController)

        }

        onView(withId(R.id.rvMovieList)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        assertEquals(navController.currentDestination?.id, R.id.MovieDetailFragment)
    }


}