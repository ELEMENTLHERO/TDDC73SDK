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

            val accountReg : AccountRegistration = AccountRegistration()

            Column {
                accountReg.AddField("Name",FieldType.NameTextField,true)
                accountReg.AddField("Email",FieldType.EmailTextField,false)
                accountReg.AddField("Telephone",FieldType.NumberTextField,false)
                accountReg.AddField("Address",FieldType.AddressTextField,false)
                accountReg.AddField("Non Mandatory Password",FieldType.StandardPassword,false)

                accountReg.AddDropDown("Country of birth", listOf("Sweden","EU excluding Sweden","Non EU"),true)

                p.CreatePasswordField(DisplayType.Both,"Strong password")

            }


        }
    }
}