package com.example.tddc73_sdk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tddc73_sdk.ui.theme.TDDC73SDKTheme

class MainActivity : ComponentActivity() {
    fun req1(text:String) : Boolean{
        return text.length >= 6
    }
    fun req2(text:String) : Boolean{
        return text.contains(regex = Regex("[^A-Za-z0-9]"))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val reqs: MutableList<PasswordRequirement> = mutableListOf()
            reqs.add(PasswordRequirement(::req1,"Must have at least 6 length"))
            reqs.add(PasswordRequirement(::req2,"Must include special character"))
            val p = Password(reqs)
            TDDC73SDKTheme {
                Column {
                    p.CreatePasswordField()
                }
                // A surface container using the 'background' color from the theme

            }
        }
    }
}