package com.example.tddc73_sdk

interface AccountField{
    var text : String
    var required : Boolean
    var description : String?
}

enum class FieldTypes{
    DropDown,
    Password,
    Date,
    NormalTextField,
    NameTextField,
    NumberTextField,
    AddressTextField,
}

class AccountRegistration {
    lateinit var fields : List<AccountField>
}