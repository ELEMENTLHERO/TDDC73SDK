package com.example.tddc73_sdk

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

enum class DisplayType{
    Both,
    Text,
    Meter,
    Custom
}

class PasswordRequirement(val condition : (String) -> Boolean = { true }, val description : String = "", val required : Boolean){}

@OptIn(ExperimentalMaterial3Api::class)
class Password(var reqForStrength: List<PasswordRequirement>) {

    @Composable fun CreatePasswordField(display : DisplayType = DisplayType.Both ,customDisplay : @Composable ()->Unit = {}){
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
            DisplayType.Both -> {
                TextDisplay(text)
                MeterDisplay(text)
            }
            DisplayType.Meter -> {
                MeterDisplay(text)
            }
            DisplayType.Text -> {
                TextDisplay(text)
            }
            DisplayType.Custom -> {
                if (customDisplay == {}){
                    throw Exception("displayType.Custom chosen but no customDisplay was specified")
                }
                customDisplay()
            }

            else -> {}
        }
    }
    @Composable
    fun MeterDisplay(
        password: String,
        modifier: Modifier = Modifier,
    ) {
        val passwordStrength by remember(GetPasswordStrength(password)) {
            mutableIntStateOf(((GetPasswordStrength(password)*100f).toInt()))
        }

        LinearProgressIndicator(
            progress = passwordStrength / 100f,
            color = getPasswordStrengthColor(passwordStrength),
            modifier = modifier
                .height(8.dp)
        )
    }
    private fun getPasswordStrengthColor(strength: Int): Color {
        return when{
            strength < 30 -> Color.Red
            strength < 60 -> Color.Yellow
            else -> Color.Green
        }


    }

    private fun GetPasswordStrength(text: String) : Float{
        var okReqs : Int = 0
        for (req in this@Password.reqForStrength) {
            if (req.condition(text)){
                okReqs += 1
            }
        }
        return okReqs.toFloat()/this@Password.reqForStrength.count().toFloat()
    }

    @Composable
    fun TextDisplay(text: String) {
        Column() {
            for (req in this@Password.reqForStrength) {
                Row {
                    val icon: ImageVector =
                        if (req.condition(text)) Icons.Filled.CheckCircle else if (req.required) Icons.Filled.RemoveCircle else Icons.Filled.Info
                    val tintColor: Color =
                        if (req.condition(text)) Color(0, 200, 0) else if (req.required) Color(
                            200,
                            0,
                            0
                        ) else Color(0, 0, 200)
                    Icon(
                        imageVector = icon, contentDescription = "Requirement icon",
                        tint = tintColor
                    )
                    Text(text = req.description)
                }
            }
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