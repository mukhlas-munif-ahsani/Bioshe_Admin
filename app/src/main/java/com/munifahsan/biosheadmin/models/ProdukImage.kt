package com.munifahsan.biosheadmin.models

import com.google.firebase.firestore.DocumentId

data class ProdukImage(
    @DocumentId
    val id: String = "",
    val image: String = "",
    val nomor: Int = 0
)
