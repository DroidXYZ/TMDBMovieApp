package com.example.tmdbmovies.ui.movies

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.tmdbmovies.R
import com.example.tmdbmovies.databinding.RowItemMovieLayoutBinding
import com.example.tmdbmovies.models.movielist.Result


class MovieListAdapter(val context: Context?) :
    RecyclerView.Adapter<MovieListAdapter.BaseViewHolder<*>>() {

    private var movieList: ArrayList<Result> = arrayListOf()
    private lateinit var onMovieItemClick: OnMovieItemClick
    fun setMovieList(movieList: ArrayList<Result>) {
        this.movieList = movieList
        notifyDataSetChanged()
    }

    fun setMovieItemClick(onmovieItemClick: OnMovieItemClick) {
        this.onMovieItemClick = onmovieItemClick
    }

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: Result)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val bindingComponent: RowItemMovieLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_item_movie_layout, parent, false
        )
        return ViewHolderInstalledApp(bindingComponent)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val movieList = movieList[position]
        holder.bind(movieList)

    }

    inner class ViewHolderInstalledApp(var binding: RowItemMovieLayoutBinding) :
        BaseViewHolder<Result>(binding.root) {
        override fun bind(item: Result) {
            binding.clRowMainLayout.setOnClickListener {
                onMovieItemClick.onMovieItemClick(item.id, item.poster_path, adapterPosition)
            }
            val options = RequestOptions()
                .fitCenter()
                .error(R.drawable.no_image_available_big)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .placeholder(R.drawable.image_loading)
            context?.let {
                Glide.with(it)
                    .load("${item.poster_path}")
                    .apply(options)
                    .into(binding.ivMoviePoster)
            }

        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    interface OnMovieItemClick {
        fun onMovieItemClick(movieId: Int, posterPath: String?, position: Int)
    }
}