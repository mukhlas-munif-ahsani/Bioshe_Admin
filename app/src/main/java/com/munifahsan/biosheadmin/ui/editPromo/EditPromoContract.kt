package com.munifahsan.biosheadmin.ui.editPromo

import android.net.Uri

interface EditPromoContract {
    interface View{

        fun showMessage(s: String)
        fun showImage(image: String)
        fun showNama(nama: String)
        fun showNamaProgress()
        fun hideNamaProgress()
        fun showDiskon(diskon: String)
        fun showDiskonProgress()
        fun hideDiskonProgress()
        fun showKeterangan(ketereangan: String)
        fun showKeteranganProgress()
        fun hideKeteranganProgress()
        fun setShow(show: Boolean)
        fun showUploadImageProgress(progress: Int)
        fun selesai()
    }
    interface Presenter{
        fun updateImage(promoId: String, image: Uri)
        fun updateSelesai(promoId: String)
        fun updateShow(promoId: String, show: Boolean)
        fun updateKeterangan(promoId: String, keterangan: String)
        fun updateDiskon(promoId: String, diskon: Int)
        fun updateNama(promoId: String, nama: String)

        fun getPromosi(promoId: String)
        fun deleteDraft()
    }
    interface Repository{

        fun getPromosi(promoId: String)
        fun simpan(promoId: String)
        fun deleteDraft()
        fun updateShow(show: Boolean, produkId: String)
        fun updateNama(nama: String, produkId: String)
        fun updateDiskon(diskon: Int, produkId: String)
        fun updateKeterangan(keterangan: String, produkId: String)
        fun updateImage(promoId: String, image: Uri)
    }
    interface Listener{
        fun draftPromoExist(
            id: String,
            nama: String,
            thumbnail: String?,
            keterangan: String,
            diskon: Int,
            show: Boolean,
        )

        fun updateNamaListener()
        fun updateKeteranganListener()
        fun updateDiskonListener()
        fun updateImageListener(image: String)
        fun uploadImageProgressListener(progress: Int)
        fun selesai()
    }
}