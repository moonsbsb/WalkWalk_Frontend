package com.example.pet_walk.data.remote.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("response")
    val response: Response?
)
data class Response (
    @SerializedName("header") var header : Header? = Header(),
    @SerializedName("body"   ) var body   : Body?   = Body()
)
data class Header (
    @SerializedName("resultCode") var resultCode : String? = null,
    @SerializedName("resultMsg" ) var resultMsg  : String? = null
)
data class Body (
    @SerializedName("dataType"   ) var dataType   : String? = null,
    @SerializedName("items"      ) var items      : Items?  = Items(),
    @SerializedName("pageNo"     ) var pageNo     : Int?    = null,
    @SerializedName("numOfRows"  ) var numOfRows  : Int?    = null,
    @SerializedName("totalCount" ) var totalCount : Int?    = null
)
data class Items (
    @SerializedName("item" ) var item : ArrayList<Item> = arrayListOf()
)
data class Item (
    @SerializedName("baseDate"  ) var baseDate  : String? = null,
    @SerializedName("baseTime"  ) var baseTime  : String? = null,
    @SerializedName("category"  ) var category  : String? = null,
    @SerializedName("nx"        ) var nx        : Int?    = null,
    @SerializedName("ny"        ) var ny        : Int?    = null,
    @SerializedName("obsrValue" ) var obsrValue : String? = null
)
