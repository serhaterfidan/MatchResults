package com.sanstech.matchresults.ui

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.sanstech.matchresults.R
import com.sanstech.matchresults.databinding.FragmentMatchDetailBinding
import com.sanstech.matchresults.utils.SharedPreferencesHelper
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

        val isFavorite = SharedPreferencesHelper.getMatchList(requireContext()).any { it.i == match.i }
        if (isFavorite) {
            binding.favouriteButton.setImageResource(R.drawable.ic_star)
            binding.favouriteText.text = getString(R.string.favorilerden_sil)
        } else {
            binding.favouriteButton.setImageResource(R.drawable.ic_starborder)
            binding.favouriteButton.setColorFilter(Color.WHITE)
            binding.favouriteText.text = getString(R.string.favorilere_ekle)
        }

        binding.favoriteLayout.setOnClickListener {
            if (isFavorite) {
                binding.favouriteButton.setImageResource(R.drawable.ic_starborder)
                binding.favouriteButton.setColorFilter(Color.WHITE)
                binding.favouriteText.text = getString(R.string.favorilere_ekle)
                SharedPreferencesHelper.removeMatch(requireContext(),match.i)

            } else {
                binding.favouriteButton.setImageResource(R.drawable.ic_star)
                binding.favouriteText.text = getString(R.string.favorilerden_sil)
                SharedPreferencesHelper.addMatch(requireContext(),match)
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}