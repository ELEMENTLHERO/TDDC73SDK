package com.example.tddc73_sdk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    private fun req1(text:String) : Boolean{
        return text.length >= 6
    }
    private fun req2(text:String) : Boolean{
        return text.contains(regex = Regex("[^A-Za-z0-9]"))
    }
    fun handelData(data : (MutableMap<String, MutableState<String>>)){
        //Do something
    }

    var formData = mutableMapOf<String, MutableState<String>>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val reqs: MutableList<PasswordRequirement> = mutableListOf()
            reqs.add(PasswordRequirement(::req1,"Must have at least 6 length",true))
            reqs.add(PasswordRequirement(::req2,"Include special character",false))

            val accountReg : AccountRegistration = AccountRegistration()

            Column( modifier = Modifier
                .verticalScroll(rememberScrollState())) {
                accountReg.AddField("Name",FieldType.NameTextField,required =true)
                accountReg.AddField("Email",FieldType.EmailTextField,required =false)
                accountReg.AddField("Telephone",FieldType.NumberTextField,required =false)
                accountReg.AddField("Address",FieldType.AddressTextField,Modifier.fillMaxWidth().size(100.dp),required =false)
                accountReg.AddField("Non Mandatory Password",FieldType.StandardPassword, required =  false)


                accountReg.AddDropDown("Country of birth", listOf("Sweden","EU excluding Sweden","Non EU"),true)

                accountReg.AddCustomPassword("Strong password",DisplayType.Both,reqs)
                accountReg.AddSubmitButton({ handelData(formData) })
            }

        }

    }
}