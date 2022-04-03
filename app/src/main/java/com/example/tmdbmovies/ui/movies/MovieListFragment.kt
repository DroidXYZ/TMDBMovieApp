package com.example.tmdbmovies.ui.movies

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdbmovies.R
import com.example.tmdbmovies.extensions.showToast
import com.example.tmdbmovies.models.movielist.Result
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class MovieListFragment : DaggerFragment() {

    private lateinit var movieListActivity: MoviesActivity
    private lateinit var rvMovieList: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var movieListAdapter: MovieListAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    val viewModel by lazy{
        ViewModelProvider(this,  viewModelFactory)[MoviesViewModel::class.java]
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        movieListActivity = activity as MoviesActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.movie_fragment_layout, container, false)
        rvMovieList = view.findViewById(R.id.rvMovieList)
        progressBar = view.findViewById(R.id.progressBar)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvMovieList.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context) as androidx.recyclerview.widget.RecyclerView.LayoutManager?
        val dividerItemDecoration = DividerItemDecoration(
            rvMovieList.context,
            1
        )
        rvMovieList.addItemDecoration(dividerItemDecoration)
        movieListAdapter= MovieListAdapter(context)
        rvMovieList.adapter = movieListAdapter
        viewModel.getMoviesList("cb59747c962dcea1ec650918bf749348","en-US",2)
        subscribeToViewModel()
    }
    private fun subscribeToViewModel(){
        viewModel.movieList.observe(viewLifecycleOwner) {
           context?.showToast("Movie List Size${it.results.size}")
           movieListAdapter.setMovieList(it.results as ArrayList<Result>)
        }
        viewModel.errorMessage.observe(viewLifecycleOwner){
            context?.showToast("Error Message $it")
        }
    }

}