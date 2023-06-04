package com.example.dice.datamodel

import com.google.gson.annotations.SerializedName


data class DataModel (
        @SerializedName("sonic")
        val Sonic : String
    )
data class Loginserver (
        @SerializedName("id")
        val id : String
        )
data class NumInfo ( //회원번호
    @SerializedName("check")
    val check : String
)
data class milesInfo ( //회원번호
    @SerializedName("mile")
    val mile : String
)
data class reservers ( //회원번호
    @SerializedName("reserver")
    val reserver : String
)
data class finprice ( //최종결재 금액
    @SerializedName("finprices")
    val finprices : String
)

data class CarNumInfo ( //차량번호
    @SerializedName("carnum")
    val carnum : String
)
