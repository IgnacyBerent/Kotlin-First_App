package com.example.mad_l3.firestore

import com.google.protobuf.Internal.IntList

data class FireStoreData(
    var id: String = "",
    var selNumb: List<Int>?=null,
    var drawNumb: List<Int>?=null,
    var win: Double = 0.0
)