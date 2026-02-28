package com.example.sma.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sma.ui.theme.AppTheme
import com.example.sma.R

//Login screen
@Composable
fun StartLoginScreen(onClickLogin: () -> Unit, onClickSignIn: () -> Unit) {
        Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 25.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
            Image(
                painter = painterResource(id = R.drawable.appicon1),
                contentDescription = null,
                modifier = Modifier
                    .offset(y = 400.dp)
                .clip(RoundedCornerShape(12)),
                contentScale = ContentScale.Crop
            )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp),  // space from bottom edge
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Login(onClickLogin = onClickLogin )
        Spacer(modifier = Modifier.height(16.dp))
        SignInButton(onClickSignIn = onClickSignIn)
    }
}

//Make the sign in button
@Composable
fun SignInButton(onClickSignIn: () -> Unit) {
    ElevatedButton(
        onClick = { onClickSignIn() },
        modifier = Modifier.fillMaxWidth(0.8f)
    ) {
        Text(stringResource(R.string.Signin))
    }
}

//Make the login button and fields
@Composable
fun Login(onClickLogin: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val allFieldsFilled = username.isNotBlank() && password.isNotBlank()

    TextField(
        value = username,
        onValueChange = { username = it },
        label = { Text("Username") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(0.8f)
    )

    Spacer(modifier = Modifier.height(16.dp))

    TextField(
        value = password,
        onValueChange = { password = it },
        label = { Text("Password") },
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(0.8f)
    )

    Spacer(modifier = Modifier.height(16.dp))

    FilledTonalButton(
        onClick = { onClickLogin() },
        enabled = allFieldsFilled,
        modifier = Modifier.fillMaxWidth(0.8f)
    ) {
        Text("Login")
    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    AppTheme {
        StartLoginScreen({},{})
    }
}