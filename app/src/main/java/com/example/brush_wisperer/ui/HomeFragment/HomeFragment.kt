package com.example.brush_wisperer.ui.HomeFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.brush_wisperer.Data.RepositoryFirebase
import com.example.brush_wisperer.R
import com.example.brush_wisperer.databinding.FragmentHomeBinding
import com.example.brush_wisperer.ui.Adapter.NewsAdapter
import com.google.firebase.auth.FirebaseAuth


class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentHomeBinding.bind(view)


        binding.dataBT.setOnClickListener {
            //viewModel.insertNews()
            //testViewModel.loadData()

        }

        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val name = user.displayName
            binding.usernameTV.text = "$name"
        }


        viewModel.news.observe(viewLifecycleOwner) { news ->
            news?.let {
                binding.newsRV.adapter = NewsAdapter(it)
            }
        }
    }

}