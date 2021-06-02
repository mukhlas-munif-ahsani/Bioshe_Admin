package com.munifahsan.biosheadmin.ui.tambahPromo

import android.net.Uri

interface TambahPromoContract {
    interface View{

        fun promoId(promoId: String)
        fun showMessage(s: String)
        fun showImage(image: String)
        fun showUploadImageProgress(progress: Int)
        fun showNama(nama: String)
        fun showDiskon(diskon: String)
        fun showKeterangan(ketereangan: String)
        fun setShow(show: Boolean)
        fun showNamaProgress()
        fun showDiskonProgress()
        fun showKeteranganProgress()
        fun hideNamaProgress()
        fun hideDiskonProgress()
        fun hideKeteranganProgress()
        fun selesai()
    }
    interface Presenter{

        fun createPromo()
        fun updateImage(promoId: String, imageUri: Uri)

        fun updateNama(promoId: String, judul: String)
        fun updateDiskon(promoId: String, diskon: Int)
        fun updateShow(promoId: String, show: Boolean?)

        fun updateKeterangan(promoId: String, keterangann: String)
        fun updateSelesai(promoId: String)
    }
    interface Repository{

        fun createPromo()
        fun updateImage(promoId: String, imgareUri: Uri)
        fun updateJudul(promoId: String, judul: String)
        fun updateDiskon(promoId: String, diskon: Int)
        fun updateShow(promoId: String, show: Boolean)
        fun updateSelesai(produkId: String)
        fun updateKetarangan(promoId: String, kertarangan: String)
    }
    interface Listener{
        fun createPromoListener(s: String)
        fun draftPromoExist(s: String, image: String, judul: String, ketereangan: String, diskon: Int, show: Boolean?)
        fun selesaiListener()
        fun updateImageListener(image: String)

        fun uploadImageProgressListener(progress: Int)
        fun updateDiskonListener()
        fun updateNamaListener()
        fun updateShowListener()
        fun updateKeranganListener()
    }
}