package com.apps.android.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth,
) : ViewModel() {

    fun signIn(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            runCatching {
                auth.signInWithEmailAndPassword(email, password).await()
            }.onSuccess {
                onSuccess()
            }
        }
    }

    fun signUp(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            runCatching {
                auth.createUserWithEmailAndPassword(email, password).await()
            }.onSuccess {
                onSuccess()
            }
        }
    }

    fun isSignedIn(): Boolean = auth.currentUser != null
}
