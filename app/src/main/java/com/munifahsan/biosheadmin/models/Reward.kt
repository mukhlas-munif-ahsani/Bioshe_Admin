package com.munifahsan.biosheadmin.models

import com.google.firebase.firestore.DocumentId

class Reward(
    @DocumentId
    val id: String = "",
    val nama: String = "",
    val thumbnail: String = "",
    val points: Int = 0,
    val stok: Int = 0,
    val berat: Int = 0,
    val sold: Int = 0,
    val selesai: Boolean = false,
    val show: Boolean = false
)
