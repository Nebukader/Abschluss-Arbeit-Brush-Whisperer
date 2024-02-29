package com.example.brush_wisperer.ui.HomeFragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.brush_wisperer.R
import com.example.brush_wisperer.databinding.FragmentHomeBinding
import com.example.brush_wisperer.ui.Adapter.HomeLastWishedAdapter
import com.example.brush_wisperer.ui.Adapter.NewsAdapter
import com.example.brush_wisperer.ui.Adapter.WorkshopWishlistAdapter
import com.example.brush_wisperer.ui.WorkshopFragment.WorkshopViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

const val TAG = "HomeFragment"

class HomeFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private val viewModel: HomeViewModel by activityViewModels()
    lateinit var swipeRefreshLayout: SwipeRefreshLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentHomeBinding.bind(view)
        swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(this)

        viewModel.wishlistArrayList.observe(viewLifecycleOwner) { wishlist ->
            wishlist?.let {
                val lastFiveItems = it.takeLast(5)
                val adapter = HomeLastWishedAdapter(lastFiveItems)
                val recyclerView = binding.lastWishedColoursRV
                recyclerView.adapter = adapter
            }
        }


        viewModel.news.observe(viewLifecycleOwner) { news ->
            news?.let {
                if (it.isEmpty()) {
                    viewModel.updateNews()
                }
                binding.newsRV.adapter = NewsAdapter(it)
            }
        }
}

    override fun onRefresh() {
        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.updateNews()
            swipeRefreshLayout.isRefreshing = false
        }, 3000)
    }
}

