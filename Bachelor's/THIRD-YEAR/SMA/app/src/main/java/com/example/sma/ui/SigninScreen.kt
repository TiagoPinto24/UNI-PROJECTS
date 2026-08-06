package com.example.sma.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledTonalButton
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.sma.ui.theme.AppTheme
import com.example.sma.R

//Sign In screen
@Composable
fun StartSignInScreen () {
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
                .offset(y = 450.dp)
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
    )  {
        TextFieldsAndButton(onClickConfirm = {})
    }
}

//Text fields and button UI
@Composable
fun TextFieldsAndButton (onClickConfirm:() -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }

    val allFieldsFilled = username.isNotBlank() && password.isNotBlank() &&
            confirmPassword.isNotBlank() && firstName.isNotBlank() &&
            lastName.isNotBlank()

    Row(
        modifier = Modifier
            .fillMaxWidth(0.8f),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First Name") },
            singleLine = true,
            modifier = Modifier.weight(1f)
        )

        TextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name") },
            singleLine = true,
            modifier = Modifier.weight(1f)
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

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

    TextField(
        value = confirmPassword,
        onValueChange = { confirmPassword = it },
        label = { Text("Confirm Password") },
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(0.8f)
    )

    Spacer(modifier = Modifier.height(16.dp))


    FilledTonalButton(
            onClick = { onClickConfirm() },
    enabled = allFieldsFilled,
    modifier = Modifier.fillMaxWidth(0.8f)
    ) {
        Text("Sign in")
    }

}

@Preview(showBackground = true)
@Composable
fun SignInScreenPreview() {
    AppTheme {
        StartSignInScreen()
    }
}
