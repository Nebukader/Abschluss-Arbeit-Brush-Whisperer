package com.example.brush_wisperer.ui.ColourFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.brush_wisperer.R


class CategorieFragment : Fragment() {

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View? {
      return inflater.inflate(R.layout.fragment_categorie, container, false)
   }
}