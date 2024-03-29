package com.example.brush_wisperer.ui.WorkshopFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.brush_wisperer.Data.Model.FirestoreColour
import com.example.brush_wisperer.databinding.FragmentWorkshopMyColourCollectionBinding
import com.example.brush_wisperer.ui.Adapter.WorkshopColourCollectionAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class WorkshopMyColourCollection : Fragment() {

    private val viewModel: WorkshopViewModel by activityViewModels()
    private lateinit var binding: FragmentWorkshopMyColourCollectionBinding
    private lateinit var favouriteColoursArrayList: ArrayList<FirestoreColour>
    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: WorkshopColourCollectionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentWorkshopMyColourCollectionBinding.inflate(inflater)
        favouriteColoursArrayList = ArrayList()
        adapter = WorkshopColourCollectionAdapter(favouriteColoursArrayList)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addColourBtn.setOnClickListener {
            // Navigate to the add colour fragment
            findNavController().popBackStack()
            findNavController().navigate(WorkshopMyColourCollectionDirections.actionWorkshopMyColourCollectionToWorkshopColourList())
        }
        // get the user's favourite colours

        //TODO: Ins ViewModel auslagern

        fun getFavouriteColours() {
            val currentUserId = FirebaseAuth.getInstance().currentUser?.uid.toString()
            db = FirebaseFirestore.getInstance()
            db.collection("users").document(currentUserId).collection("colours").addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ){
                    if (error != null) {
                        Log.e("Firestore Error",  error.message.toString())
                        return
                    }
                    for (dc : DocumentChange in value?.documentChanges!!) {

                        if (dc.type == DocumentChange.Type.ADDED) {
                            favouriteColoursArrayList.add(dc.document.toObject(FirestoreColour::class.java))
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            })
        }
        getFavouriteColours()
        binding.myColourCollectionRecyclerView.adapter = adapter

        binding.addColourBtn.setOnClickListener {
            findNavController().navigate(WorkshopMyColourCollectionDirections.actionWorkshopMyColourCollectionToWorkshopColourList())
        }

        val searchView = binding.searchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {

                } else {
                    val searchList = ArrayList<FirestoreColour>()
                    val lowerCaseNewText = newText.lowercase()

                    for (item in favouriteColoursArrayList) {
                        if (item.colourName.lowercase().contains(lowerCaseNewText)) {
                            searchList.add(item)
                        }
                    }

                    if (searchList.isEmpty()) {
                        Toast.makeText(context, "No Colour found", Toast.LENGTH_SHORT).show()
                    } else {
                    }
                }
                return true
            }
        })
    }

}