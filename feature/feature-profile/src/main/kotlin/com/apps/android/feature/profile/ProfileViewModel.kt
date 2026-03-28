package com.apps.android.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.android.core.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

sealed interface ProfileUiState {
    data object Loading : ProfileUiState
    data class Success(
        val user: User,
        val isCurrentUser: Boolean,
        val isFollowing: Boolean,
    ) : ProfileUiState
    data class Error(val message: String) : ProfileUiState
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun loadProfile(userId: String) {
        viewModelScope.launch {
            runCatching {
                val doc = firestore.collection("users").document(userId).get().await()
                val user = doc.toObject(User::class.java) ?: User()
                val currentUserId = auth.currentUser?.uid
                val isCurrentUser = currentUserId == userId
                val isFollowing = if (!isCurrentUser && currentUserId != null) {
                    firestore.collection("users")
                        .document(currentUserId)
                        .collection("following")
                        .document(userId)
                        .get()
                        .await()
                        .exists()
                } else false
                Triple(user, isCurrentUser, isFollowing)
            }.onSuccess { (user, isCurrentUser, isFollowing) ->
                _uiState.value = ProfileUiState.Success(user, isCurrentUser, isFollowing)
            }.onFailure { e ->
                _uiState.value = ProfileUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun toggleFollow(targetUserId: String, onComplete: () -> Unit) {
        val currentUserId = auth.currentUser?.uid ?: return
        val state = _uiState.value as? ProfileUiState.Success ?: return
        viewModelScope.launch {
            runCatching {
                val followingRef = firestore.collection("users")
                    .document(currentUserId)
                    .collection("following")
                    .document(targetUserId)
                if (state.isFollowing) {
                    followingRef.delete().await()
                } else {
                    followingRef.set(mapOf("followedAt" to System.currentTimeMillis())).await()
                }
            }.onSuccess {
                _uiState.value = state.copy(isFollowing = !state.isFollowing)
                onComplete()
            }
        }
    }
}
