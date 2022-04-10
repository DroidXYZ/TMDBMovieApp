package com.example.tmdbmovies.ui.movies

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.tmdbmovies.BuildConfig
import com.example.tmdbmovies.R
import com.example.tmdbmovies.base.BaseFragment
import com.example.tmdbmovies.databinding.MovieDetailFragmentLayoutBinding
import com.example.tmdbmovies.extensions.showToast
import com.example.tmdbmovies.models.moviedetail.MovieDetailResponse
import com.example.tmdbmovies.tmdbutils.InternetUtil
import com.example.tmdbmovies.tmdbutils.TMDBConstants
import javax.inject.Inject

class MovieDetailFragment : BaseFragment() {

    private var imagePath: String = ""
    private var movieID: Int = 0
    private lateinit var bindingComponent: MovieDetailFragmentLayoutBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[MoviesViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingComponent = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.movie_detail_fragment_layout, container, false
        )

        return bindingComponent.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieID = requireArguments().getInt(TMDBConstants.EXTRA_MOVIE_ID)
        imagePath = requireArguments().getString(TMDBConstants.EXTRA_MOVIE_IMAGE_PATH) ?: ""
        checkNetworkConnection()
        subscribeToViewModel()
    }

    private fun checkNetworkConnection() = if (InternetUtil.isInternetOn())
        viewModel.getMoviesDetail(movieID, BuildConfig.API_KEY, TMDBConstants.APP_LANGUAGE) else {
        showEmptyState(
            isShowEmptyState = true,
            getString(R.string.please_check_your_internet_connection_and_try_again)
        )
        InternetUtil.observe(viewLifecycleOwner) { status ->
            if (status) {
                showEmptyState(isShowEmptyState = false, "")
                viewModel.getMoviesDetail(movieID, BuildConfig.API_KEY, TMDBConstants.APP_LANGUAGE)
            }
        }
    }

    fun showEmptyState(isShowEmptyState: Boolean, errorMsg: String) {
        if (isShowEmptyState) {
            bindingComponent.tvEmptyView.visibility = View.VISIBLE
            bindingComponent.tvEmptyView.text = errorMsg
            bindingComponent.clMainLayout.visibility = View.GONE
        } else {
            bindingComponent.tvEmptyView.visibility = View.GONE
            bindingComponent.clMainLayout.visibility = View.VISIBLE
        }
    }

    private fun subscribeToViewModel() {
        viewModel.movieDetail.observe(viewLifecycleOwner) {
            updateUI(it)
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            context?.showToast("Error Message $it")
            showEmptyState(true, "Error Message $it")
        }
        viewModel.loading.observe(viewLifecycleOwner) {
            showLoadingIndicator(it)
        }
    }

    @SuppressLint("SetTextI18n")
    fun updateUI(movieDetailResponse: MovieDetailResponse) {
        bindingComponent.clMainLayout.visibility = View.VISIBLE
        bindingComponent.tvMovieTitle.text = movieDetailResponse.original_title
        bindingComponent.tvMovieDetail.text = movieDetailResponse.overview
        bindingComponent.tvMoviePopularity.text =
            "${getString(R.string.movie_popularity)} : ${movieDetailResponse.popularity}"
        bindingComponent.tvMovieReleaseDate.text =
            "${getString(R.string.movie_release_date)} : ${movieDetailResponse.release_date}"
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
                .into(bindingComponent.ivMovieDetailPoster)
        }
    }

    fun showLoadingIndicator(isVisible: Boolean) {
        if (!isVisible)
            bindingComponent.progressBar.visibility = View.GONE
        else
            bindingComponent.progressBar.visibility = View.VISIBLE
    }


}