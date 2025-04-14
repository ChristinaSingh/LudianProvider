package com.ludianseller.models

data class PropertyPriceModel(
    val result: List<Property>,
    val message: String,
    val status: String
){
    data class Property(val title:String,val price:String,val latitude:String,val longitude :String)

}
