package com.example.tmdbmovies.ui.movies

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.tmdbmovies.BuildConfig.API_KEY
import com.example.tmdbmovies.R
import com.example.tmdbmovies.base.BaseFragment
import com.example.tmdbmovies.databinding.MovieFragmentLayoutBinding
import com.example.tmdbmovies.extensions.showToast
import com.example.tmdbmovies.models.movielist.Result
import com.example.tmdbmovies.tmdbutils.InternetUtil
import com.example.tmdbmovies.tmdbutils.TMDBConstants
import javax.inject.Inject

class MovieListFragment : BaseFragment(),MovieListAdapter.OnMovieItemClick {

    private lateinit var movieListActivity: MoviesActivity
    private lateinit var bindingComponent: MovieFragmentLayoutBinding

    private lateinit var movieListAdapter: MovieListAdapter
    private var lastSelectedPos= -1

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
        checkNetworkConnection()

    }

    private fun checkNetworkConnection() = if (InternetUtil.isInternetOn()) {
        viewModel.getMoviesAndConfiguration(API_KEY,TMDBConstants.APP_LANGUAGE,1)
    } else {
        showEmptyState(isShowEmptyState = true,getString(R.string.please_check_your_internet_connection_and_try_again))
        InternetUtil.observe(movieListActivity, Observer { status ->
            if(status) {
                showEmptyState(isShowEmptyState = false,"")
                viewModel.getMoviesAndConfiguration(API_KEY, TMDBConstants.APP_LANGUAGE, 1)
            }
        })
    }

    private fun showEmptyState(isShowEmptyState:Boolean,errorMsg:String){
        if(isShowEmptyState){
                bindingComponent.tvEmptyView.visibility = View.VISIBLE
                bindingComponent.tvEmptyView.text = errorMsg
                bindingComponent.rvMovieList.visibility = View.GONE
        }
        else{
            bindingComponent.tvEmptyView.visibility = View.GONE
            bindingComponent.rvMovieList.visibility = View.VISIBLE
        }
    }
    private fun subscribeToViewModel(){
        viewModel.movieList.observe(movieListActivity) {
            movieListAdapter.setMovieList(it.results as ArrayList<Result>)
            bindingComponent.rvMovieList.scrollToPosition(lastSelectedPos)
        }
        viewModel.errorMessage.observe(movieListActivity){
            context?.showToast("Error Message $it")
            showEmptyState(isShowEmptyState = true,"Error Message- $it")
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

    override fun onMovieItemClick(movieId: Int,posterPath:String?,position:Int) {
        val bundle = Bundle()
        bundle.putInt(TMDBConstants.EXTRA_MOVIE_ID, movieId)
        lastSelectedPos = position
        posterPath?.let {
            bundle.putString(TMDBConstants.EXTRA_MOVIE_IMAGE_PATH, "$it")
        }
        val navController = Navigation.findNavController(movieListActivity, R.id.movie_main_nav_host_fragment)
        if (navController.currentDestination?.id == R.id.MovieListFragment) {
            navController.navigate(R.id.action_list_to_detail, bundle)
        }
    }

}