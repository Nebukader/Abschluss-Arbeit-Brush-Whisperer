package com.example.brush_wisperer.ui.HomeFragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.brush_wisperer.R
import com.example.brush_wisperer.databinding.FragmentHomeBinding
import com.example.brush_wisperer.ui.Adapter.NewsAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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

        viewModel.updateNews()

        viewModel.news.observe(viewLifecycleOwner) { news ->
            news?.let {
                binding.newsRV.adapter = NewsAdapter(it)
            }
        }

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val docRef = FirebaseFirestore.getInstance().collection("users").document(userId!!)
            Log.d("TAG", "users: $docRef")
            docRef.get()
                ?.addOnSuccessListener { document ->
                    if (document != null) {
                        val name = document.getString("username")
                        binding.usernameTV?.let {
                            it.text = name
                        }
                    } else {
                        Log.d("TAG", "No such document")
                    }
                }
                ?.addOnFailureListener { exception ->
                    Log.d("TAG", "get failed with ", exception)
                }
        }else{
            Toast.makeText(context, "You are not logged in anymore", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRefresh() {
        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.updateNews()
            swipeRefreshLayout.isRefreshing = false
        }, 3000)
    }
}

