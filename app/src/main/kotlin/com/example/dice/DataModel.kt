package com.example.dice

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

