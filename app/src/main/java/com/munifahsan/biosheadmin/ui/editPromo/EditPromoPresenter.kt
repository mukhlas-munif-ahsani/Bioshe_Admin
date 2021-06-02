package com.munifahsan.biosheadmin.ui.editPromo

import android.net.Uri

class EditPromoPresenter(val mView: EditPromoContract.View): EditPromoContract.Presenter, EditPromoContract.Listener {
    val mRepo: EditPromoContract.Repository
    init {
        mRepo = EditPromoRepository(this)
    }

    override fun getPromosi(promoId: String){
        mRepo.getPromosi(promoId)
    }

    override fun deleteDraft(){
        mRepo.deleteDraft()
    }

    override fun updateImage(promoId: String, image: Uri) {
        mRepo.updateImage(promoId, image)
    }

    override fun updateSelesai(promoId: String) {
        mRepo.simpan(promoId)
    }

    override fun updateShow(promoId: String, show: Boolean) {
        mRepo.updateShow(show, promoId)
    }

    override fun updateKeterangan(promoId: String, keterangan: String) {
        mRepo.updateKeterangan(keterangan, promoId)
        mView.showKeteranganProgress()
    }

    override fun updateDiskon(promoId: String, diskon: Int) {
       mRepo.updateDiskon(diskon, promoId)
        mView.showDiskonProgress()
    }

    override fun updateNama(promoId: String, nama: String) {
        mRepo.updateNama(nama, promoId)
        mView.showNamaProgress()
    }

    override fun draftPromoExist(
        id: String,
        nama: String,
        thumbnail: String?,
        keterangan: String,
        diskon: Int,
        show: Boolean,
    ) {
        mView.showNama(nama)
        mView.showImage(thumbnail!!)
        if (diskon == 0){
            mView.showDiskon("")
            mView.showKeterangan(keterangan)
            mView.setShow(show)
        } else {
            mView.showDiskon(diskon.toString())
            mView.showKeterangan(keterangan)
            mView.setShow(show)
        }

    }

    override fun updateNamaListener(){
        mView.hideNamaProgress()
    }

    override fun updateKeteranganListener(){
        mView.hideKeteranganProgress()
    }

    override fun updateDiskonListener(){
        mView.hideDiskonProgress()
    }

    override fun updateImageListener(image: String) {
        mView.showImage(image)
    }

    override fun uploadImageProgressListener(progress: Int) {
        mView.showUploadImageProgress(progress)
    }

    override fun selesai(){
        mView.selesai()
    }
}