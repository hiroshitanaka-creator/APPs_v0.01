package com.apps.android.feature.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.android.core.model.Post
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

sealed interface PostDetailUiState {
    data object Loading : PostDetailUiState
    data class Success(val post: Post) : PostDetailUiState
    data class Error(val message: String) : PostDetailUiState
}

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
) : ViewModel() {

    private val _uiState = MutableStateFlow<PostDetailUiState>(PostDetailUiState.Loading)
    val uiState: StateFlow<PostDetailUiState> = _uiState.asStateFlow()

    fun loadPost(postId: String) {
        viewModelScope.launch {
            runCatching {
                firestore.collection("posts").document(postId).get().await()
                    .toObject(Post::class.java) ?: error("Post not found")
            }.onSuccess { post ->
                _uiState.value = PostDetailUiState.Success(post)
            }.onFailure { e ->
                _uiState.value = PostDetailUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
