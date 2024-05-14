package com.sanstech.matchresults.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sanstech.matchresults.R
import com.sanstech.matchresults.utils.ErrorPopupView
import com.sanstech.matchresults.utils.LottiePopupView
import com.sanstech.matchresults.data.Match
import com.sanstech.matchresults.data.Tournament
import com.sanstech.matchresults.databinding.FragmentMatchResultsBinding
import com.sanstech.matchresults.network.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MatchResults : Fragment() {

    private val mainViewModel: MainViewModel by viewModels()
    private var _binding: FragmentMatchResultsBinding? = null
    private lateinit var lottiePopupView : LottiePopupView
    private lateinit var errorPopupView: ErrorPopupView
    @Inject
    lateinit var matchAdapter: MatchGroupAdapter
    private var groupedMatches: Map<Tournament, List<Match>> = mutableMapOf()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMatchResultsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        initObserver()

    }

    private fun initUI() {
        lottiePopupView = LottiePopupView(requireContext())
        errorPopupView = ErrorPopupView(requireContext())

        binding.recyclerViewMatches.setLayoutManager(LinearLayoutManager(requireContext()))
        binding.recyclerViewMatches.adapter = matchAdapter

        matchAdapter.setItemClick(object : MatchGroupAdapter.ClickInterface<Match> {
            override fun onClick(data: Match) {
                findNavController().navigate(
                    MatchResultsDirections.actionMatchResultsToMatchDetail(
                        data
                    )
                )
            }
        })
    }
    private fun initObserver() {
        mainViewModel.matchResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Loading -> {
                    lottiePopupView.show()
                }

                is NetworkResult.Failure -> {
                    lottiePopupView.dismiss()
                    errorPopupView.showError(it.errorMessage)
                }

                is NetworkResult.Success -> {
                    lottiePopupView.dismiss()

                    groupedMatches = it.data.sortedBy { it1 -> it1.d }.groupBy { it1 -> it1.to }
                    val items: ArrayList<MatchGroupAdapter.RecyclerViewItem> = arrayListOf()

                    groupedMatches.forEach { (tournament, matchList) ->
                        items.add(
                            MatchGroupAdapter.RecyclerViewItem.TournamentItem(
                                tournament
                            )
                        )
                        matchList.forEach { match ->
                            items.add(MatchGroupAdapter.RecyclerViewItem.MatchItem(match))
                        }
                    }

                    matchAdapter.updateMatchGroups(items)

                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_ms -> {
                matchAdapter.filterGroups(groupedMatches,true)
                true
            }
            R.id.action_all -> {
                matchAdapter.filterGroups(groupedMatches,false)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}