package com.example.brush_wisperer.ui.ColourFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.brush_wisperer.R
import com.example.brush_wisperer.databinding.FragmentCategorieBinding


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

      viewModel.loadColourData()

      binding.theArmyPainterCV.setOnClickListener {
         findNavController().navigate(CategorieFragmentDirections.actionCategorieFragmentToColourRangesAndColourListFragment("The Army Painter","Speed Paint"))
      }
      binding.gwCV.setOnClickListener {
         findNavController().navigate(CategorieFragmentDirections.actionCategorieFragmentToColourRangesAndColourListFragment("Citadel Colour","Base"))
      }
      binding.vallejoCV.setOnClickListener {
         findNavController().navigate(CategorieFragmentDirections.actionCategorieFragmentToColourRangesAndColourListFragment("Vallejo","GameColor"))
      }
      binding.akCV.setOnClickListener {
         findNavController().navigate(CategorieFragmentDirections.actionCategorieFragmentToColourRangesAndColourListFragment("AK Interactive","3Gen Acrylics"))
      }

    }
}