package com.apps.android.core.model

data class Comment(
    val id: String = "",
    val postId: String = "",
    val authorId: String = "",
    val author: User = User(),
    val body: String = "",
    val createdAt: Long = 0L,
)
