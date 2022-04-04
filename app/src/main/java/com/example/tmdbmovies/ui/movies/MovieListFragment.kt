package com.example.tmdbmovies.ui.movies

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdbmovies.R
import com.example.tmdbmovies.TMDBMovieApplication
import com.example.tmdbmovies.base.BaseFragment
import com.example.tmdbmovies.databinding.MovieDetailFragmentLayoutBinding
import com.example.tmdbmovies.databinding.MovieFragmentLayoutBinding
import com.example.tmdbmovies.extensions.showToast
import com.example.tmdbmovies.models.movielist.Result
import com.example.tmdbmovies.tmdbutils.TMDBConstants
import com.google.gson.Gson
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class MovieListFragment : BaseFragment(),MovieListAdapter.OnMovieItemClick {

    private lateinit var movieListActivity: MoviesActivity
    private lateinit var bindingComponent: MovieFragmentLayoutBinding

    private lateinit var movieListAdapter: MovieListAdapter
    private var imagePath:String =""

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    val viewModel by lazy{
        ViewModelProvider(this,  viewModelFactory)[MoviesViewModel::class.java]
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        movieListActivity = activity as MoviesActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getImageConfiguration(TMDBConstants.API_KEY)
        subscribeToViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingComponent = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.movie_fragment_layout, container, false)

        return bindingComponent.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindingComponent.rvMovieList.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context) as androidx.recyclerview.widget.RecyclerView.LayoutManager?
        val dividerItemDecoration = DividerItemDecoration(
            bindingComponent.rvMovieList.context,
            1
        )
        bindingComponent.rvMovieList.addItemDecoration(dividerItemDecoration)
        movieListAdapter= MovieListAdapter(context)
        movieListAdapter.setMovieItemClick(this)
        bindingComponent.rvMovieList.adapter = movieListAdapter
        viewModel.getMoviesList(TMDBConstants.API_KEY,TMDBConstants.APP_LANGUAGE,1)

    }
    private fun subscribeToViewModel(){
        viewModel.imageConfiguration.observe(movieListActivity) {
            var imageWidth = "original"
            val posterArraySize = it.images.poster_sizes.size
           if (posterArraySize>3){
                imageWidth= it.images.poster_sizes[posterArraySize-3]
           }
            imagePath=  "${it.images.secure_base_url}${imageWidth}"
        }
        viewModel.movieList.observe(movieListActivity) {
            context?.showToast("Movie List Size${it.results.size}")
            movieListAdapter.setMovieList(it.results as ArrayList<Result>,imagePath)
        }
        viewModel.errorMessage.observe(movieListActivity){
            context?.showToast("Error Message $it")
        }
        viewModel.loading.observe(movieListActivity){
            showLoadingIndicator(it)
        }
    }
    private fun showLoadingIndicator(isVisible: Boolean){
        if (!isVisible)
            bindingComponent.progressBar.visibility = View.GONE
        else
            bindingComponent.progressBar.visibility = View.VISIBLE
    }

    override fun onMovieItemClick(movieId: Int,posterPath:String?) {
        val bundle = Bundle()
        bundle.putInt(TMDBConstants.EXTRA_MOVIE_ID, movieId)
        posterPath?.let {
            bundle.putString(TMDBConstants.EXTRA_MOVIE_IMAGE_PATH, "$imagePath$it")
        }
        val navController = Navigation.findNavController(movieListActivity, R.id.movie_main_nav_host_fragment)
        if (navController.currentDestination?.id == R.id.MovieListFragment) {
            navController.navigate(R.id.action_list_to_detail, bundle)
        }
    }

}