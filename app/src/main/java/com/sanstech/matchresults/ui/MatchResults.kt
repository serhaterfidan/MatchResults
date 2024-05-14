package com.sanstech.matchresults.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sanstech.matchresults.utils.ErrorPopupView
import com.sanstech.matchresults.utils.LottiePopupView
import com.sanstech.matchresults.data.Match
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
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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

                    val groupedMatches = it.data.sortedBy { it1 -> it1.d }.groupBy { it1 -> it1.to }
                    val recyclerViewList = mutableListOf<MatchGroupAdapter.RecyclerViewItem>()

                    groupedMatches.forEach { (tournament, matchList) ->
                        recyclerViewList.add(
                            MatchGroupAdapter.RecyclerViewItem.TournamentItem(
                                tournament
                            )
                        )
                        matchList.forEach { match ->
                            recyclerViewList.add(MatchGroupAdapter.RecyclerViewItem.MatchItem(match))
                        }
                    }

                    matchAdapter.updateMatchGroups(recyclerViewList)

                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}