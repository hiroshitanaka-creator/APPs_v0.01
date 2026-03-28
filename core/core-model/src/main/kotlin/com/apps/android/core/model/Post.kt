package com.apps.android.core.model

data class Post(
    val id: String = "",
    val authorId: String = "",
    val author: User = User(),
    val title: String = "",
    val body: String = "",
    val imageUrls: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val likeCount: Int = 0,
    val commentCount: Int = 0,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
)
