package com.example.sma.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import com.example.sma.ui.theme.AppTheme
import com.example.sma.data.multimediaList

//Class that represents a media piece
data class Multimedia(
    val index: Int,
    val imageRes: Int,
    val title: String,
    val seen: Boolean,
    val favorite: Boolean,
    val score: String,
    val type: String,
    val type2: String,
    val time: String,
    val aired: Int,
    val gender: String,
    val synopsis: String
)

//Menu screen
@Composable
fun StartMenuScreen(onItemClick: (Multimedia) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            var searchText by remember { mutableStateOf("") }

            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, bottom = 20.dp, start = 60.dp),
                placeholder = { Text("Search...") },
                singleLine = true,
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.Gray
                    )
                }
            )

            Text(
                text = "Catalog",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            MultimediaGrid(searchText = searchText, onItemClick = onItemClick)
        }
    }
}

// Grid of cards
@Composable
fun MultimediaGrid(searchText: String, onItemClick: (Multimedia) -> Unit) {
    val filteredMultimedia = multimediaList.filter {
        it.title.contains(searchText, ignoreCase = true)
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(filteredMultimedia) { multimedia ->
            MultimediaCard(
                imageRes = multimedia.imageRes,
                title = multimedia.title,
                type = multimedia.type,
                onClick = { onItemClick(multimedia) }
            )
        }
    }
}

// Card UI
@Composable
fun MultimediaCard(
    imageRes: Int,
    title: String,
    type: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(27f / 40f)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = type,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}


@Preview()
@Composable
fun MenuScreenPreview() {
    AppTheme {
        StartMenuScreen(onItemClick = {})
    }
}
