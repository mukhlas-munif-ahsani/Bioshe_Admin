package com.munifahsan.biosheadmin.ui.tambahPromo

import android.net.Uri

class TambahPromoPresenter(val mView: TambahPromoContract.View): TambahPromoContract.Presenter, TambahPromoContract.Listener {
    val mRepo: TambahPromoContract.Repository
    init {
        mRepo = TambahPromoRepository(this)
    }

    override fun createPromo(){
        mRepo.createPromo()
        mView.showNamaProgress()
        mView.showDiskonProgress()
    }

    override fun updateSelesai(promoId: String){
        mRepo.updateSelesai(promoId)
    }

    override fun updateImage(promoId: String, imageUri: Uri){
        mRepo.updateImage(promoId, imageUri)
    }

    override fun updateNama(promoId: String, judul: String){
        mRepo.updateJudul(promoId, judul)
        mView.showNamaProgress()
    }

    override fun updateKeterangan(promoId: String, keterangann: String){
        mRepo.updateKetarangan(promoId, keterangann)
    }

    override fun updateDiskon(promoId: String, diskon: Int){
        mRepo.updateDiskon(promoId, diskon)
        mView.showDiskonProgress()
    }

    override fun updateShow(promoId: String, show: Boolean?){
        mRepo.updateShow(promoId, show!!)
    }

    override fun draftPromoExist(
        promoId: String,
        image: String,
        judul: String,
        keterangann: String,
        diskon: Int,
        show: Boolean?,
    ) {
        mView.promoId(promoId)
        mView.showImage(image)
        mView.showNama(judul)
        mView.showDiskon(diskon.toString())
        mView.setShow(show!!)
        mView.showKeterangan(keterangann)
        //mView.showDataPromo(image, judul, diskon, show)
    }

    override fun updateImageListener(image: String){
        mView.showImage(image)
    }

    override fun uploadImageProgressListener(progress: Int){
        mView.showUploadImageProgress(progress)
    }

    override fun updateNamaListener(){
        mView.hideNamaProgress()
    }

    override fun updateDiskonListener(){
        mView.hideDiskonProgress()
    }

    override fun updateShowListener(){

    }

    override fun updateKeranganListener() {
        mView.hideKeteranganProgress()
    }

    override fun selesaiListener() {
        mView.selesai()
    }

    override fun createPromoListener(promoId: String) {
        mView.promoId(promoId)
    }
}