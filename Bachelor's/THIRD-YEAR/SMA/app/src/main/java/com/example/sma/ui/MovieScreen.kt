package com.example.sma.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sma.ui.theme.AppTheme

import com.example.sma.data.multimediaList

//A Media screen
@Composable
fun StartMediaScreen(id: Int) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {

                //Selecting the correct media to display
                var multimedia: Multimedia = multimediaList[0]
                multimediaList.forEach { item ->
                    if (item.index == id)
                        multimedia=item
                }

                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = multimedia.imageRes),
                        contentDescription = "Image",
                        modifier = Modifier
                            .size(400.dp)
                            .padding(top = 50.dp, end = 100.dp)
                    )
                    Text(
                        text = "Score",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .padding(
                                top = 55.dp,
                                start = 314.dp
                            )
                    )
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Type:")
                            }
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
                                append("\n${multimedia.type}")
                            }
                            append("\n\n")

                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(multimedia.type2)
                            }
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
                                append("\n${multimedia.time}")
                            }
                            append("\n\n")

                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Aired:")
                            }
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
                                append("\n${multimedia.aired}")
                            }
                        },
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .padding(
                                top = 150.dp,
                                start = 290.dp
                            )
                    )
                    Text(
                        text = multimedia.score,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(
                                top = 80.dp,
                                start = 290.dp
                            )
                    )
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 30.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    var isFavorite by remember { mutableStateOf(multimedia.favorite) }
                    var seen by remember { mutableStateOf(multimedia.seen) }

                    IconButton(
                        onClick = { seen = !seen },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "Seen",
                            tint = if (seen) Color.Green else Color.Gray,
                            modifier = Modifier.size(35.dp)
                        )
                    }

                    IconButton(
                        onClick = { isFavorite = !isFavorite },

                        ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color.Red else Color.Gray,
                            modifier = Modifier.size(35.dp)
                        )
                    }
                }

                Text(
                    text = multimedia.title,
                    fontSize = 39.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.SansSerif,
                    textAlign = TextAlign.Center,
                    lineHeight = 40.sp, 
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 20.dp)
                )

                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
                            append(multimedia.gender)
                        }
                    },
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Serif,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 10.dp),
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle( textDecoration = TextDecoration.Underline)) {
                            append("Synopsis")
                        }},
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Serif,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 20.dp),
                )

                var expanded by remember { mutableStateOf(false) }

                Column(
                    modifier = Modifier
                        .padding(
                            top = 20.dp,
                            start = 10.dp,
                            end = 10.dp,
                            bottom = 30.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = multimedia.synopsis,
                        fontSize = 17.sp,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.SansSerif,
                        maxLines = if (expanded) Int.MAX_VALUE else 4,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = if (expanded) "△" else "▽",
                        modifier = Modifier
                            .clickable { expanded = !expanded }
                            .padding(top = 8.dp),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}


@Preview()
@Composable
fun MediaScreenPreview() {
    AppTheme {
        StartMediaScreen(1)
    }
}

