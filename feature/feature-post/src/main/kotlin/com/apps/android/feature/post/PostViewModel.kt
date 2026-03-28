package com.apps.android.feature.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) : ViewModel() {

    fun createPost(title: String, body: String, onSuccess: () -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            runCatching {
                val post = hashMapOf(
                    "authorId" to userId,
                    "title" to title,
                    "body" to body,
                    "imageUrls" to emptyList<String>(),
                    "tags" to emptyList<String>(),
                    "likeCount" to 0,
                    "commentCount" to 0,
                    "createdAt" to System.currentTimeMillis(),
                    "updatedAt" to System.currentTimeMillis(),
                )
                firestore.collection("posts").add(post).await()
            }.onSuccess {
                onSuccess()
            }
        }
    }
}
