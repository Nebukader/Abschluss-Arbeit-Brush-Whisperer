package com.example.brush_wisperer.ui.HomeFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.brush_wisperer.R
import com.example.brush_wisperer.databinding.FragmentHomeBinding
import com.example.brush_wisperer.ui.Adapter.HomeLastWishedAdapter
import com.example.brush_wisperer.ui.Adapter.NewsAdapter

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
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.updateNews()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        viewModel.wishlistArrayList.observe(viewLifecycleOwner) { wishlist ->
            binding.lastWishedColoursRV.adapter = wishlist?.takeLast(5)
                ?.let { HomeLastWishedAdapter(it) }
        }

        viewModel.news.observe(viewLifecycleOwner) { news ->
            if (news?.isEmpty() == true) {
                viewModel.updateNews()
            }
            binding.newsRV.adapter = NewsAdapter(news)
        }
    }
}

