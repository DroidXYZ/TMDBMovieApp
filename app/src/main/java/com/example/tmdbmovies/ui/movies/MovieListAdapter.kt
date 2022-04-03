package com.example.tmdbmovies.ui.movies

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdbmovies.R
import com.example.tmdbmovies.databinding.RowItemMovieLayoutBinding
import com.example.tmdbmovies.models.movielist.Result


class MovieListAdapter (val context: Context?): RecyclerView.Adapter<MovieListAdapter.BaseViewHolder<*>>(){

    private var movieList: ArrayList<Result> = arrayListOf()
    fun setMovieList(movieList: ArrayList<Result> ){
        this.movieList = movieList
        notifyDataSetChanged()
    }
    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: Result)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val bindingComponent: RowItemMovieLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_item_movie_layout, parent, false)
         return ViewHolderInstalledApp(bindingComponent)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val installedAppInfo = movieList[position]
        holder.bind(installedAppInfo)

    }
    inner class ViewHolderInstalledApp(var binding: RowItemMovieLayoutBinding) :
        BaseViewHolder<Result>(binding.root) {
        override fun bind(item: Result) {
            binding.clMainLayout.setOnClickListener {


            }
//            binding.ivMoviePoster.setImageDrawable(item.appIcon)

        }
    }

    override fun getItemCount(): Int {
        return  movieList.size
    }
}