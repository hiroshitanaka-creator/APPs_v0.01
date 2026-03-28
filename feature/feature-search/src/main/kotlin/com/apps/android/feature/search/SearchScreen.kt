package com.apps.android.feature.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.apps.android.core.model.Post

@Composable
fun SearchScreen(
    onPostClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val query by viewModel.query.collectAsState()
    val results by viewModel.searchResults.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        SearchBar(
            inputField = {
                androidx.compose.material3.SearchBarDefaults.InputField(
                    query = query,
                    onQueryChange = viewModel::onQueryChange,
                    onSearch = { viewModel.onQueryChange(it) },
                    expanded = false,
                    onExpandedChange = {},
                    placeholder = { Text("投稿・ユーザーを検索") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                )
            },
            expanded = false,
            onExpandedChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {}

        LazyColumn {
            items(results, key = { it.id }) { post ->
                SearchResultItem(post = post, onClick = { onPostClick(post.id) })
            }
        }
    }
}

@Composable
private fun SearchResultItem(
    post: Post,
    onClick: () -> Unit,
) {
    ListItem(
        headlineContent = { Text(post.title) },
        supportingContent = { Text("@${post.author.username}") },
        modifier = Modifier.padding(horizontal = 8.dp),
    )
}
