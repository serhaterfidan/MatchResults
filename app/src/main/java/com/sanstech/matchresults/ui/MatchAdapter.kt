package com.sanstech.matchresults.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sanstech.matchresults.data.Match
import com.sanstech.matchresults.databinding.ItemMatchResultsBinding
import javax.inject.Inject

// MatchAdapter.kt

class MatchAdapter @Inject constructor() : RecyclerView.Adapter<MatchAdapter.MatchViewHolder>() {

    private var matchResults = mutableListOf<Match>()
    private var clickInterface: ClickInterface<Match>? = null

    fun updateMovies(movies: List<Match>) {
        this.matchResults = movies.toMutableList()
        notifyItemRangeInserted(0, movies.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val binding = ItemMatchResultsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MatchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        holder.bind(matchResults[position])
    }

    override fun getItemCount(): Int {
        return matchResults.size
    }

    fun setItemClick(clickInterface: ClickInterface<Match>) {
        this.clickInterface = clickInterface
    }

    inner class MatchViewHolder(private val binding: ItemMatchResultsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(match: Match) {
            binding.match = match

            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    clickInterface?.onClick(match)
                }
            }

            BindingAdapters.setMatchScore(binding.textScore, match)

            binding.executePendingBindings()
        }
    }

}

interface ClickInterface<T> {
    fun onClick(data: T)
}

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("matchScore")
    fun setMatchScore(textView: TextView, match: Match) {
        val abbreviation = match.sc.abbr

        val scoreText = if (abbreviation == "SCHEDULED") {
            abbreviation
        } else {
            "${match.sc.abbr} | ${match.sc.ht.r} - ${match.sc.at.r}"
        }

        textView.text = scoreText
    }
}
