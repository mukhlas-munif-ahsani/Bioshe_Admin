package com.munifahsan.biosheadmin.models

import com.google.firebase.firestore.DocumentId

data class Promo(
    @DocumentId
    val id: String = "",
    val imageThumbnail: String = "",
    val diskon: Int = 0,
    val judul: String = "",
    val show: Boolean = false,
    val selesai: Boolean = false,
    val keterangan: String = ""
)
