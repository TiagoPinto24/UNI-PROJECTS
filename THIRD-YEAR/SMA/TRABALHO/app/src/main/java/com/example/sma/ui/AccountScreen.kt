package com.example.sma.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.ui.text.font.FontWeight

import com.example.sma.ui.theme.AppTheme
import com.example.sma.data.multimediaList

//Account screen
@Composable
fun StartAccountScreen(onClickSwitchAcc: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()) ) {
        // Background image
        Image(
            painter = painterResource(id = multimediaList[0].imageRes),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 300.dp),
            contentScale = ContentScale.Crop
        )

        //Account Page
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .wrapContentHeight()
                .padding(top = 300.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Account information
            AccountInfo(onClickSwitchAcc)

            //Last seen list
            Text(
                text = "Last seen:",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            MakeList(1)

            Spacer(modifier = Modifier.height(16.dp))

            //Favorites List
            Text(
                text = "Favorites",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            MakeList(2)

            //Space for design
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

//Function responsible to make the display of the user information
@Composable
fun AccountInfo(onClickSwitchAcc: () -> Unit) {
    Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "User",
                modifier = Modifier.size(124.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "Heisenberg",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(5.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Walter",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "White",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            FilledIconButton(
                onClick = onClickSwitchAcc,
                modifier = Modifier.size(60.dp)
                ) {
                Icon(
                    Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Close Navigation",
                    modifier = Modifier.fillMaxSize().padding(all = 10.dp)

                )
            }
        }
    }

//Function responsible to generate the lists
@Composable
fun MakeList (option:Int) {
    val filteredMultimedia: List<Multimedia> = if (option == 1 ) {
        multimediaList.filter { multimedia -> multimedia.seen
        }
    } else {
        multimediaList.filter {
                multimedia -> multimedia.favorite
        }
    }

    LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(175.dp),
    horizontalArrangement = Arrangement.spacedBy(12.dp),
    contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(filteredMultimedia) { item ->
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .width(100.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AccountScreenPreview() {
    AppTheme {
        StartAccountScreen {}
    }
}