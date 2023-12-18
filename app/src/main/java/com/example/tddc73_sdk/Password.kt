package com.example.tddc73_sdk

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.RemoveCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

enum class displayType{
    Text,
    Meter,
    Custom
}

interface IPassword {
    var maxStrength : Int
    // display type
    var display : @Composable ()->Unit
    // req for one more strength level
    //var reqForStrength : List<(password:String)->Boolean>

}

class PasswordRequirement(val condition : (String) -> Boolean = { true }, val description : String = ""){}

@OptIn(ExperimentalMaterial3Api::class)
class Password(var reqForStrength: List<PasswordRequirement>) {
    var display : displayType = displayType.Text

    @Composable fun CreatePasswordField(customDisplay : @Composable ()->Unit = {}){
        var text by remember { mutableStateOf("") }
        var passwordVisible by rememberSaveable { mutableStateOf(false) }
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Label") },
            placeholder = {
                Text("Enter Password")
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff
                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = {passwordVisible = !passwordVisible}){
                    Icon(imageVector  = image, description)
                }
            }
        )
        when(display){
            displayType.Meter -> {
                //add meter

            }
            displayType.Text -> {
                Column(){
                    for (req in this@Password.reqForStrength){
                        Row {
                            Icon(imageVector = if(req.condition(text)) Icons.Filled.CheckCircle else Icons.Filled.RemoveCircle, contentDescription = "Check OK", tint = if(req.condition(text)) Color(0,200,0) else Color.Red)
                            Text(text = req.description)
                        }

                    }
                }
            }
            displayType.Custom -> {
                if (customDisplay == {}){
                    throw Exception("displayType.Custom chosen but no customDisplay was specified")
                }
                customDisplay()
            }

            else -> {}
        }
    }
    /*
    @Composable fun CreateDisplayStrength(display : displayType){
        when(display){
            displayType.Meter -> {
                //add meter
            }
            displayType.Text -> {

            }
            displayType.Custom -> {
                customDisplay()
            }

            else -> {}
        }
    }
     */
}