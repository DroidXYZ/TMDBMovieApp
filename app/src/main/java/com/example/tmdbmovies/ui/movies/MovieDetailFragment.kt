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
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.tmdbmovies.R
import com.example.tmdbmovies.base.BaseFragment
import com.example.tmdbmovies.extensions.showToast
import com.example.tmdbmovies.models.moviedetail.MovieDetailResponse
import com.example.tmdbmovies.tmdbutils.TMDBConstants
import javax.inject.Inject

class MovieDetailFragment : BaseFragment() {

    private lateinit var movieListActivity: MoviesActivity
    private lateinit var tvMovieTitle: TextView
    private lateinit var tvMovieDetail: TextView
    private lateinit var tvMoviePopularity: TextView
    private lateinit var tvMovieReleaseDate: TextView
    private lateinit var ivMovieDetailPoster: ImageView
    private lateinit var progressBar: ProgressBar
    private var imagePath:String=""

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
        tvMovieTitle= view.findViewById(R.id.tvMovieTitle)
        tvMovieDetail=  view.findViewById(R.id.tvMovieDetail)
        tvMoviePopularity= view.findViewById(R.id.tvMoviePopularity)
        tvMovieReleaseDate=  view.findViewById(R.id.tvMovieReleaseDate)
        ivMovieDetailPoster=  view.findViewById(R.id.ivMovieDetailPoster)
        progressBar=  view.findViewById(R.id.progressBar)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movieID = requireArguments().getInt(TMDBConstants.EXTRA_MOVIE_ID)
        imagePath = requireArguments().getString(TMDBConstants.EXTRA_MOVIE_IMAGE_PATH)?:""
        viewModel.getMoviesDetail(movieID,TMDBConstants.API_KEY,TMDBConstants.APP_LANGUAGE)
        subscribeToViewModel()
    }
    private fun subscribeToViewModel(){
        viewModel.movieDetail.observe(viewLifecycleOwner) {
            updateUI(it)
        }
        viewModel.errorMessage.observe(viewLifecycleOwner){
            context?.showToast("Error Message $it")
        }
        viewModel.loading.observe(viewLifecycleOwner){
            showLoadingIndicator(it)
        }
    }
    private fun updateUI(movieDetailResponse: MovieDetailResponse){
        tvMovieTitle.text= movieDetailResponse.original_title
        tvMovieDetail.text=  movieDetailResponse.overview
        tvMoviePopularity.text= "${getString(R.string.movie_popularity)} : ${movieDetailResponse.popularity}"
        tvMovieReleaseDate.text =  "${getString(R.string.movie_release_date)} : ${movieDetailResponse.release_date}"
        val options = RequestOptions()
            .fitCenter()
            .error(R.drawable.no_image_available)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH)
            .placeholder(R.drawable.no_image_available)
        context?.let {
            Glide.with(it)
                .load(imagePath)
                .apply(options)
                .into(ivMovieDetailPoster)
        }
    }

    private fun showLoadingIndicator(isVisible: Boolean){
        if (!isVisible)
            progressBar.visibility = View.GONE
        else
            progressBar.visibility = View.VISIBLE
    }



}