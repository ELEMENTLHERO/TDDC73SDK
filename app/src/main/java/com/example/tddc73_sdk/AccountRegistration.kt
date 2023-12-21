package com.example.tddc73_sdk

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
class AccountRegistration(){
    var fields = mutableMapOf<String, MutableState<String>>()

    /**
     * Function to add an account form field with customizable properties such as name, type, and requirement.
     *
     * @param nameOfField The displayed name for the user. NOTE: Two fields that share the same name will be treated as the same field.
     * @param typeOfField The type of the field, such as FieldType.EmailTextField which affects KeyboardOptions, isValid/isError and more.
     * @param required Specifies whether the field is required. Default is false.
     *
     * @throws IllegalArgumentException if the specified field type is not supported.
     *
     * @see FieldType
     * @see PasswordRequirement
     * @see Password
     * @see Password.CreatePasswordField
     * @see ExtendedTextField
     * @see KeyboardOptions
     */
    @Composable
    fun AddField(nameOfField : String, typeOfField: FieldType, required: Boolean = false){
        fields[nameOfField] = remember {mutableStateOf("")}
        val text : MutableState<String>? = fields[nameOfField]
        if(typeOfField == FieldType.StandardPassword){
            val reqs: MutableList<PasswordRequirement> = mutableListOf()
            fun req1(text:String) : Boolean{ return text.length >= 6 }
            reqs.add(PasswordRequirement(::req1,"Must have at least 6 length",required))
            Password(reqs)
            val password = Password(reqs)
            password.CreatePasswordField(DisplayType.Both,nameOfField)
            return
        }
        if (text != null)
            ExtendedTextField(nameOfField,typeOfField,text,required,nameOfField)
    }

    /**
     * Function to add a dropdown menu (ExposedDropdownMenu) as a field with customizable properties.
     *
     * @param nameOfField The displayed name for the user. NOTE: Two fields that share the same name will be treated as the same field.
     * @param options The list of options to be displayed in the dropdown menu.
     * @param required Specifies whether the field is required. Default is false.
     *
     * @see ExposedDropdownMenuBox
     * @see TextField
     * @see ExposedDropdownMenu
     * @see Options
     */
    @Composable
    fun AddDropDown(nameOfField : String, options: List<String>, required: Boolean = false){
        fields[nameOfField] = remember {mutableStateOf("")}
        val text : MutableState<String> = fields[nameOfField] ?: return
        val expanded = remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded.value,
            onExpandedChange = { expanded.value = !expanded.value },
            modifier = Modifier.padding(5.dp)
        ) {
            TextField(
                readOnly = true,
                value = text.value,
                onValueChange = { },
                label = { Text(if(required) "$nameOfField* " else nameOfField) },
                modifier = Modifier.menuAnchor(),
                isError = if(required) text.value.isBlank() else false,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded.value
                    )
                },
            )
            ExposedDropdownMenu(
                expanded = expanded.value,
                onDismissRequest = {
                    expanded.value = false
                }
            ) {
                Options(expanded,options,nameOfField)
            }

        }
    }
    @Composable
    private fun Options(
        expanded: MutableState<Boolean>,
        options: List<String>,
        nameOfField : String
    ) {
        options.forEachIndexed { index, option ->
            DropdownMenuItem(
                text = { Text(option) },
                onClick = {
                    fields[nameOfField]?.value = option
                    expanded.value = false
                },
            )
        }
    }

    private fun isValid(typeOfField: FieldType, text: String) : Boolean{
        when(typeOfField){
            FieldType.NameTextField -> {
                return text.contains(regex = Regex("[A-Za-z0-9]+"))
            }
            FieldType.AddressTextField -> {
                return text.contains(regex = Regex("[A-Za-z0-9]+"))
            }
            FieldType.NormalTextField -> {
                return text.isNotBlank()
            }
            FieldType.NumberTextField -> {
                return text.isNotBlank()
            }
            FieldType.EmailTextField -> {
                return text.contains(regex = Regex("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$"))
            }

            else -> {
                throw Exception("isValid is not set")
                return false
            }
        }
    }

    private fun getKeyBoardType(typeOfField: FieldType): KeyboardOptions{
        return when(typeOfField){
            FieldType.NumberTextField -> {
                KeyboardOptions(keyboardType = KeyboardType.Number)
            }

            FieldType.EmailTextField -> {
                KeyboardOptions(keyboardType = KeyboardType.Email)
            }

            else -> {
                KeyboardOptions.Default
            }
        }
    }

    @Composable
    private fun ExtendedTextField(
        nameOfField: String,
        typeOfField: FieldType,
        text: MutableState<String>,
        required: Boolean,
        description: String,

    ) {
        Box(modifier = Modifier.padding(5.dp)) {
            Row {
                TextField(
                    value = text.value,
                    onValueChange = {text.value = it},
                    label = { Text(if(required) "$description* " else description) },
                    placeholder = { Text("Enter text") },
                    isError = if(required) !isValid(typeOfField,text.value) else false,
                    keyboardOptions = getKeyBoardType(typeOfField)
                )
            }

        }

    }
}

/**
 * The type of the field, such as FieldType.EmailTextField which affects KeyboardOptions, isValid/isError and more.
 *
 * @see KeyboardOptions
 */
enum class FieldType{
    StandardPassword,
    NormalTextField,
    NameTextField,
    EmailTextField,
    NumberTextField,
    AddressTextField,
}