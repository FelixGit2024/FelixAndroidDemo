package com.firstproject.androiddemofx.provider

interface ProviderInfo {
    val authorities:List<String>
    val directBootAware:Boolean
    val enabled:Boolean
    val exported:Boolean
    val grantUriPermissions:Boolean
    val initOrder:Int
    val label:String
    val multiprocess:Boolean
    val name:String
    val permission:String
    val process:String
    val syncable:Boolean

    val readPermission:String
    val writePermission:String
    val protectionLevel:String
}