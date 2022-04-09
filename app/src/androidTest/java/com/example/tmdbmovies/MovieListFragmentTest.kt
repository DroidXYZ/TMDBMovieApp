package com.example.tmdbmovies

import android.view.View
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tmdbmovies.models.movielist.Result
import com.example.tmdbmovies.ui.movies.MovieListFragment
import com.example.tmdbmovies.ui.movies.MoviesActivity
import com.example.tmdbmovies.ui.movies.MoviesViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matchers

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runners.model.TestClass

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

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.tmdbmovies", appContext.packageName)
    }

    @Before
    fun setUp() {

        scenario = launchFragmentInContainer(themeResId = R.style.Theme_TMDBMovies)
        scenario.moveToState(newState = Lifecycle.State.STARTED)
    }

    @Test
    fun assert_Movie_List_Fragment_Render_The_UI_According_The_Loading_State() {
        scenario.onFragment(){ fragment->
            fragment.showLoadingIndicator(true)

        }
        onView(withId(R.id.progressBar)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        onView(withId(R.id.tvEmptyView)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))

    }

    @Test
    fun assert_Movie_List_Fragment_Render_The_UI_According_The_Empty_Movie_List_State() {
        scenario.onFragment(){ fragment->
            fragment.renderMovieListUI(arrayListOf<Result>())
        }
        onView(withId(R.id.rvMovieList)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

    @Test
    fun assert_Movie_List_Fragment_Render_The_UI_According_The_Error_Movie_List_State() {
        scenario.onFragment(){ fragment->
            fragment.showEmptyState(true,"error message")
        }
        onView(withId(R.id.rvMovieList)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        onView(withId(R.id.tvEmptyView)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

    @Test
    fun assert_Movie_List_Fragment_Render_The_UI_According_The_Movie_List_State() {
        scenario.onFragment(){
        }
        onView(withId(R.id.rvMovieList)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

    @Test
    @Throws(InterruptedException::class)
    fun testVisibilityRecyclerView() {
        Thread.sleep(1000)
        var decorView : View?=null
        activityRule.scenario.onActivity {
           decorView= it.window.decorView
        }
        onView(withId(R.id.rvMovieList))
            .inRoot(
                RootMatchers.withDecorView(
                    Matchers.`is`(
                        decorView
                    )
                )
            )
            .check(matches(ViewMatchers.isDisplayed()))
    }

}