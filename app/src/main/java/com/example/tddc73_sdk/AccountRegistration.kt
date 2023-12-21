package com.example.tddc73_sdk

import android.widget.PopupWindow
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

interface AccountField{
    var text : String
    var required : Boolean
    var description : String?
}

@OptIn(ExperimentalMaterial3Api::class)
class AccountRegistration(){
    var birthday by mutableStateOf("")
    var fields = mutableMapOf<String, MutableState<String>>()
    @Composable
    fun AddField(nameOfField : String, typeOfField: FieldType, required: Boolean){
        fields[nameOfField] = remember {mutableStateOf("")}
        val text : MutableState<String>? = fields[nameOfField]
        if (text != null)
        ExtendedTextField(nameOfField,text,required,null)
    }

    @Composable
    private fun ExtendedTextField(
        nameOfField: String,
        text: MutableState<String>,
        required: Boolean,
        description: String?,
    ) {
        Column {
                TextField(
                    value = text.value,
                    onValueChange = {text.value = it},
                    label = { Text(description ?: "") },
                    placeholder = { Text("Enter text") },

                    )

        }

    }
}

@Composable
fun ShowCard(){
    Card() {
        Text(text = "Hello, world!")
    }
}


enum class FieldType{
    DropDown,
    Password,
    Date,
    NormalTextField,
    NameTextField,
    NumberTextField,
    AddressTextField,
}