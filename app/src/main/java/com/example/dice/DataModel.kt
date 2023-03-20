package com.example.dice

import com.google.gson.JsonObject
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

    //data class TodoInfo2(val todo2 : TaskInfo)
    //data class TodoInfo3(val todo3 : TaskInfo)

