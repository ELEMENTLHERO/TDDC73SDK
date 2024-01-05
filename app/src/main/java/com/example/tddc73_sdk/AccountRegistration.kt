package com.example.tddc73_sdk

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
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
    private var requiredFieldsInErrorState: MutableList<MutableState<Boolean>> = mutableListOf()

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
     * @see PasswordField
     * @see PasswordField.CreatePasswordField
     * @see ExtendedTextField
     * @see KeyboardOptions
     */
    @Composable
    fun AddField(nameOfField : String, typeOfField: FieldType, modifier: Modifier = Modifier, required: Boolean = false){
        fields[nameOfField] = remember {mutableStateOf("")}
        val text : MutableState<String>? = fields[nameOfField]
        if(typeOfField == FieldType.StandardPassword){

            val reqs: MutableList<PasswordRequirement> = mutableListOf()
            fun req1(text:String) : Boolean{ return text.length >= 6 }
            reqs.add(PasswordRequirement(::req1,"Must have at least 6 length",required))
            val password = PasswordField(reqs,required)
            password.CreatePasswordField(DisplayType.Both,nameOfField)
            return
        }
        if (text != null)
            ExtendedTextField(nameOfField,typeOfField,text,required,nameOfField, modifier = modifier)
    }

    private fun areAllRequiredFulfilled() : Boolean{
        for (req in requiredFieldsInErrorState){
            if (req.value) return false //if any are in errorState
        }
        return true
    }
    /**
     * Composable function to add a submit button with customizable properties.
     * The button, when clicked, triggers a function handling data operations.
     *
     * @param functionHandlingData The function that will be executed on button click. It takes a MutableMap of String to MutableState of String as an input parameter.
     * @param modifier Modifier used to modify the appearance and layout of the button. Default is Modifier.
     * @param textOnButton Text to be displayed on the button. Default is "Submit".
     *
     * @see Button
     * @see Text
     */
    @Composable
    fun AddSubmitButton(functionHandlingData : (MutableMap<String, MutableState<String>>)->Unit,modifier: Modifier = Modifier,textOnButton : String = "Submit") {
        Button(modifier = modifier,onClick = { functionHandlingData(fields) }, enabled = areAllRequiredFulfilled()) {
            Text(text = textOnButton)
        }
    }
    /**
     * Composable function to add a custom password field with various features like display type, password requirements, and an optional custom display.
     *
     * @param nameOfField The name of the password field. Used for identification and display purposes.
     * @param display The type of display for the password field, can be of type DisplayType. Default is DisplayType.Both.
     * @param reqs A list of password requirements to be enforced on the password field. Default is an empty MutableList.
     * @param customDisplay A composable function that allows for customizing the display of the password field. Default is an empty composable function.
     * @param required A Boolean indicating whether the password field is required. Default is true.
     *
     * @see PasswordField
     */
    @Composable
    fun AddCustomPassword(nameOfField : String, display : DisplayType = DisplayType.Both, reqs: MutableList<PasswordRequirement> = mutableListOf(),customDisplay : @Composable ()->Unit = {}, required: Boolean = true){
        val password = PasswordField(reqs,required)
        password.CreatePasswordField(display,nameOfField,customDisplay)
        requiredFieldsInErrorState.add(password.isErrorState)
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
     */
    @Composable
    fun AddDropDown(nameOfField : String, options: List<String>, required: Boolean = false) {
        fields[nameOfField] = remember { mutableStateOf("") }
        val text: MutableState<String> = fields[nameOfField] ?: return
        val expanded = remember { mutableStateOf(false) }
        val isErrorState = remember { mutableStateOf(required) }
        ExposedDropdownMenuBox(
            expanded = expanded.value,
            onExpandedChange = { expanded.value = !expanded.value },
            modifier = Modifier.padding(5.dp)
        ) {
            TextField(
                readOnly = true,
                value = text.value,
                onValueChange = {},
                label = { Text(if (required) "$nameOfField* " else nameOfField) },
                modifier = Modifier.menuAnchor(),
                isError = isErrorState.value,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded.value
                    )
                },
            )
            requiredFieldsInErrorState.add(isErrorState)
            ExposedDropdownMenu(
                expanded = expanded.value,
                onDismissRequest = {
                    expanded.value = false
                }
            ) {
                options.forEachIndexed { index, option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            fields[nameOfField]?.value = option
                            expanded.value = false
                            isErrorState.value = if (required) text.value.isBlank() else false
                        },
                    )
                }
            }

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
        nameOfField: String, //this only exists to give all functions the same signature and since this is private no one else should have to be confused over this.
        typeOfField: FieldType,
        text: MutableState<String>,
        required: Boolean,
        description: String,
        modifier: Modifier = Modifier,
        isValidOverride : (typeOfField: FieldType, text: String)->Boolean = { fieldType: FieldType, s: String -> isValid(fieldType, s) },
    ) {
        val isErrorState = remember { mutableStateOf(required) }
        Box(modifier = Modifier.padding(5.dp)) {
            TextField(
                modifier = modifier,
                value = text.value,
                onValueChange = {
                    text.value = it
                    isErrorState.value = if(required) !isValidOverride(typeOfField,text.value) else false},
                label = { Text(if(required) "$description* " else description) },
                placeholder = { Text("Enter text") },
                isError = isErrorState.value,
                keyboardOptions = getKeyBoardType(typeOfField)
            )

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