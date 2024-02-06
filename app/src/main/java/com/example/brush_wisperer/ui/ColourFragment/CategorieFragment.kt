package com.example.brush_wisperer.ui.ColourFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.brush_wisperer.R
import com.example.brush_wisperer.databinding.FragmentCategorieBinding
import com.example.brush_wisperer.databinding.FragmentHomeBinding
import com.example.brush_wisperer.ui.HomeFragment.HomeViewModel


class CategorieFragment : Fragment() {

   private val viewModel: ColourViewModel by activityViewModels()



   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View? {
      return inflater.inflate(R.layout.fragment_categorie, container, false)

   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      val binding = FragmentCategorieBinding.bind(view)

      binding.theArmyPainterCV.setOnClickListener {
         findNavController().navigate(CategorieFragmentDirections.actionCategorieFragmentToColourRangesAndColourListFragment())
      }

    }
}