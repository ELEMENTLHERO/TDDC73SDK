package com.example.tddc73_sdk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import com.example.tddc73_sdk.ui.theme.TDDC73SDKTheme

class MainActivity : ComponentActivity() {
    private fun req1(text:String) : Boolean{
        return text.length >= 6
    }
    private fun req2(text:String) : Boolean{
        return text.contains(regex = Regex("[^A-Za-z0-9]"))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val reqs: MutableList<PasswordRequirement> = mutableListOf()
            reqs.add(PasswordRequirement(::req1,"Must have at least 6 length",true))
            reqs.add(PasswordRequirement(::req2,"Include special character",false))
            val p = Password(reqs)
            TDDC73SDKTheme {
                Column (modifier = Modifier.fillMaxWidth()){
                    p.CreatePasswordField(DisplayType.Custom)
                }
                // A surface container using the 'background' color from the theme

            }
        }
    }
}