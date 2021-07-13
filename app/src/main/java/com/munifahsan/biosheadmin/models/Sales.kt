package com.munifahsan.biosheadmin.models

import com.google.firebase.firestore.DocumentId

data class Sales(
    @DocumentId
    val id: String = "",
    val nama: String = "",
    val email: String = "",
    val photo_url: String = "",
    val alamat: String = "",
    val nik: String = "",
    val noHp: String = ""
)
