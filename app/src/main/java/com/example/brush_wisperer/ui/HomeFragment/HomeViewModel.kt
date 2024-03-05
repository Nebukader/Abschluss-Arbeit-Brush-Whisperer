package com.example.brush_wisperer.ui.HomeFragment


import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.brush_wisperer.Data.Local.Model.BlogPostEntity
import com.example.brush_wisperer.Data.Local.Model.Database.BlogPostDatabaseInstance
import com.example.brush_wisperer.Data.Model.FirestoreColour
import com.example.brush_wisperer.Data.RepositoryBlogPostNews
import com.example.brush_wisperer.Data.RepositoryFirebase
import com.example.brush_wisperer.ui.Adapter.HomeLastWishedAdapter
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel (application: Application): AndroidViewModel(application) {

    private val repository =
        RepositoryBlogPostNews(BlogPostDatabaseInstance.getDatabase(application))
    private val firebaseRepo = RepositoryFirebase()

    val news: LiveData<List<BlogPostEntity>> = repository.allNews

    private val _wishlistArrayList = MutableLiveData<List<FirestoreColour>>()
    val wishlistArrayList: LiveData<List<FirestoreColour>> get() = _wishlistArrayList
    private var wishlistListener: ListenerRegistration? = null

    init {
        getWishlistColours(HomeLastWishedAdapter(wishlistArrayList.value ?: emptyList()))
    }

    private suspend fun firebaseCurrentUserID(): String? {
        return withContext(Dispatchers.IO) {
            firebaseRepo.getCurrentUserID()
        }
    }

    private fun currentUserDB(): FirebaseFirestore {
        return firebaseRepo.getDBInstance()
    }

    fun updateNews() {
        viewModelScope.launch(Dispatchers.IO) {
            val exitstingNews = repository.allNews.value ?: emptyList()
            Log.d("news", "Existing News: $exitstingNews")
            val fetchedNews = repository.scrapeBlogPost("/blogs/explore/tagged/news")
            Log.d("news", "Fetched News: $fetchedNews")

            for (fetchedNew in fetchedNews) {
                var isNew = true
                for (existingNew in exitstingNews) {
                    if (fetchedNew.title == existingNew.title) {
                        isNew = false
                        break
                    }
                }
                if (isNew) {
                    repository.insertNews(fetchedNew)
                }
            }
        }
    }

    fun getNews() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.scrapeWebPage()
            val news = repository.scrapeBlogPost("/blogs/explore/tagged/news")
            Log.d("TAG", "News: $news")
        }
    }

    private fun getWishlistColours(adapter: HomeLastWishedAdapter) {
        viewModelScope.launch {
            val currentUserId = firebaseCurrentUserID()
            val db = currentUserDB()
            wishlistListener = db.collection("users").document(currentUserId!!).collection("wishlist").addSnapshotListener(object :
                EventListener<QuerySnapshot> {
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {
                    if (error != null) {
                        Log.e("Firestore Error", error.message.toString())
                        return
                    }
                    val tempList = mutableListOf<FirestoreColour>()
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            tempList.add(dc.document.toObject(FirestoreColour::class.java))
                        }else if (dc.type == DocumentChange.Type.REMOVED) {
                            tempList.remove(dc.document.toObject(FirestoreColour::class.java))
                        }
                    }
                    _wishlistArrayList.value = tempList
                    adapter.notifyDataSetChanged()
                }
            })
        }
    }
}


