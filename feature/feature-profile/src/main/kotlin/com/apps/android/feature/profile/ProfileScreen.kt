package com.apps.android.feature.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage

@Composable
fun ProfileScreen(
    userId: String,
    onFollowClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is ProfileUiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is ProfileUiState.Success -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = state.user.profileImageUrl,
                        contentDescription = "プロフィール画像",
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape),
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = state.user.displayName,
                            style = MaterialTheme.typography.titleLarge,
                        )
                        Text(
                            text = "@${state.user.username}",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(text = state.user.bio)

                Spacer(modifier = Modifier.height(12.dp))

                Row {
                    Text(text = "${state.user.followerCount} フォロワー")
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = "${state.user.followingCount} フォロー中")
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (state.isCurrentUser) {
                    OutlinedButton(
                        onClick = { },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("プロフィールを編集")
                    }
                } else {
                    Button(
                        onClick = { viewModel.toggleFollow(userId, onFollowClick) },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(if (state.isFollowing) "フォロー中" else "フォローする")
                    }
                }
            }
        }
        is ProfileUiState.Error -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "エラーが発生しました")
            }
        }
    }
}
