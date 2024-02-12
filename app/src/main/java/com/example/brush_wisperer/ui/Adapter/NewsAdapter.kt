package com.example.brush_wisperer.ui.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.brush_wisperer.Data.Local.Model.BlogPostEntity
import com.example.brush_wisperer.databinding.NewsItemBinding
import com.example.brush_wisperer.ui.HomeFragment.HomeFragmentDirections
import com.example.brush_wisperer.ui.HomeFragment.HomeViewModel

class NewsAdapter(
    private val blogPosts: List<BlogPostEntity>,


) :
    RecyclerView.Adapter<NewsAdapter.ItemViewHolder>() {

    class ItemViewHolder(val binding: NewsItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val newsData = blogPosts
        val binding = holder.binding

        binding.newsTitleTV.text = newsData[position].title
        binding.newsImageIV.load(blogPosts[position].image)
        val postLink = newsData[position].postLink

        binding.newsCV.setOnClickListener{
            it.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToHomeDetailFragment(postLink))
        }
    }


    override fun getItemCount() = blogPosts.size
}



