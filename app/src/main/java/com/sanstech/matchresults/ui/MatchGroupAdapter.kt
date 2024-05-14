package com.sanstech.matchresults.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sanstech.matchresults.data.Match
import com.sanstech.matchresults.data.Tournament
import com.sanstech.matchresults.databinding.ItemHeaderBinding
import com.sanstech.matchresults.databinding.ItemMatchResultsBinding
import javax.inject.Inject

class MatchGroupAdapter @Inject constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var filteredItems: ArrayList<RecyclerViewItem> = arrayListOf()
    private var allitems: List<RecyclerViewItem> = arrayListOf()
    private var items: List<RecyclerViewItem> = arrayListOf()
    private var clickInterface: ClickInterface<Match>? = null

    companion object {
        private const val TOURNAMENT_VIEW_TYPE = 0
        private const val MATCH_VIEW_TYPE = 1
    }

    fun updateMatchGroups(items: List<RecyclerViewItem>) {
        this.items = items
        this.allitems = items
        notifyItemRangeInserted(0, items.size)
    }

    fun filterGroups(groupedMatches: Map<Tournament, List<Match>> = mutableMapOf(),filter: Boolean) {
        if (filter) {
            filteredItems.clear()
            groupedMatches.forEach { (tournament, matchList) ->
                val count = matchList.count { it.sc.abbr.contains("Bitti") || it.sc.abbr.contains("MS") }
                if (count > 0) {
                    filteredItems.add(
                        RecyclerViewItem.TournamentItem(
                            tournament
                        )
                    )
                    matchList.forEach { match ->
                        if (match.sc.abbr.contains("Bitti") || match.sc.abbr.contains("MS"))
                            filteredItems.add(RecyclerViewItem.MatchItem(match))
                    }
                }
            }
            items = filteredItems
        } else
            items = allitems

        notifyDataSetChanged()

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TOURNAMENT_VIEW_TYPE -> LeagueHeaderViewHolder(ItemHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            MATCH_VIEW_TYPE -> MatchViewHolder(ItemMatchResultsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LeagueHeaderViewHolder -> {
                val tournamentItem = items[position] as RecyclerViewItem.TournamentItem
                holder.bind(tournamentItem.tournament)
            }
            is MatchViewHolder -> {
                val matchItem = items[position] as RecyclerViewItem.MatchItem
                holder.bind(matchItem.match)

            }
        }
    }


    override fun getItemCount(): Int {
        return items.size
    }

    fun setItemClick(clickInterface: ClickInterface<Match>) {
        this.clickInterface = clickInterface
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is RecyclerViewItem.TournamentItem -> TOURNAMENT_VIEW_TYPE
            is RecyclerViewItem.MatchItem -> MATCH_VIEW_TYPE
        }
    }

    inner class LeagueHeaderViewHolder(private val binding: ItemHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tournament: Tournament) {
            binding.leagueName = tournament.n

            if (tournament.flag.contains("https://"))
                Glide.with(binding.root.context)
                    .load(tournament.flag)
                    .circleCrop()
                    .into(binding.imageViewFlag)

        }
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

            if (match.sc.abbr == "SCHEDULED") {
                binding.textViewScore.text = "  -  "
            } else {
                binding.textViewScore.text = "${match.sc.ht.r}  -  ${match.sc.at.r}"
                binding.textViewHalfTime.text = "${match.sc.ht.r}  -  ${match.sc.at.r}"
                binding.textViewAbbr.text = match.sc.abbr
            }

        }
    }

    interface ClickInterface<T> {
        fun onClick(data: T)
    }

    sealed class RecyclerViewItem {
        data class TournamentItem(val tournament: Tournament) : RecyclerViewItem()
        data class MatchItem(val match: Match) : RecyclerViewItem()
    }

}

