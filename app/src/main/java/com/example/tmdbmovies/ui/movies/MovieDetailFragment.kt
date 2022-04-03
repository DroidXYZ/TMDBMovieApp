package com.example.tmdbmovies.ui.movies

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdbmovies.R
import com.example.tmdbmovies.extensions.showToast
import com.example.tmdbmovies.models.movielist.Result
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class MovieDetailFragment : DaggerFragment() {

    private lateinit var movieListActivity: MoviesActivity
    private lateinit var movieListAdapter: MovieListAdapter
    private lateinit var tvMovieTitle: TextView
    private lateinit var tvMovieDetail: TextView
    private lateinit var tvMoviePopularity: TextView
    private lateinit var tvMovieReleaseDate: TextView
    private lateinit var ivMovieDetailPoster: ImageView

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
         val view = inflater.inflate(R.layout.movie_detail_fragment_layout, container, false)
        tvMovieTitle= view.findViewById<TextView>(R.id.tvMovieTitle)
        tvMovieDetail=  view.findViewById<TextView>(R.id.tvMovieDetail)
        tvMoviePopularity= view.findViewById<TextView>(R.id.tvMoviePopularity)
        tvMovieReleaseDate=  view.findViewById<TextView>(R.id.tvMovieReleaseDate)
        ivMovieDetailPoster=  view.findViewById<ImageView>(R.id.ivMovieDetailPoster)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getMoviesDetail(53345,"cb59747c962dcea1ec650918bf749348","en-US")
        subscribeToViewModel()
    }
    private fun subscribeToViewModel(){
        viewModel.movieDetail.observe(viewLifecycleOwner) {
           context?.showToast("Movie title${it.original_title}")
        }
        viewModel.errorMessage.observe(viewLifecycleOwner){
            context?.showToast("Error Message $it")
        }
    }

}