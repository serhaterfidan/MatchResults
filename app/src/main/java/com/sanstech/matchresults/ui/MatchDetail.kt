package com.sanstech.matchresults.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.sanstech.matchresults.databinding.FragmentMatchDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatchDetail : Fragment() {

    private var _binding: FragmentMatchDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val args: MatchDetailArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMatchDetailBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()

    }

    private fun initUI() {
        val match = args.selectedMatch
        binding.match = match

        if (match.to.flag.contains("https://"))
            Glide.with(binding.root.context)
                .load(match.to.flag)
                .circleCrop()
                .into(binding.imageViewFlag)

        if (match.sc.abbr == "SCHEDULED") {
            binding.textViewScore.text = "  -  "
        } else {
            binding.textViewScore.text = "${match.sc.ht.r}  -  ${match.sc.at.r}"
            binding.textViewHalfTime.text = "${match.sc.ht.r}  -  ${match.sc.at.r}"
            binding.textViewAbbr.text = match.sc.abbr
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}