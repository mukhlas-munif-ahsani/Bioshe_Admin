package com.munifahsan.biosheadmin.ui.editProduk

class EditProdukPresenter(val mView: EditProdukContract.View): EditProdukContract.Presenter, EditProdukContract.Listener {
    val mRepo: EditProdukContract.Repository

    init {
        mRepo = EditProdukRepository(this)
    }

    override fun getProduk(produkId: String){
        mRepo.getProduk(produkId)
    }

    override fun simpan(produkId: String){
        mRepo.simpan(produkId)
    }

    override fun deleteDraft(){
        mRepo.deleteDraft()
    }

    override fun updateShow(show: Boolean, produkId: String){
        mRepo.updateShow(show, produkId)
    }

    override fun updateNama(nama: String, produkId: String){
        mRepo.updateNama(nama, produkId)
        mView.showNamaProgress()
    }

    override fun updateHarga(harga: Int, produkId: String){
        mRepo.updateHarga(harga, produkId)
        mView.showHargaProgress()
    }

    override fun updateStok(stok: Int, produkId: String){
        mRepo.updateStok(stok, produkId)
        mView.showStokProgress()
    }

    override fun updateKeterangan(keterangan: String, produkId: String){
        mRepo.updateKeterangan(keterangan, produkId)
        mView.showDeskripsiProgress()
    }

    override fun updateBerat(berat: Int, produkId: String){
        mRepo.updateBerat(berat, produkId)
        mView.showBeratProgress()
    }

    override fun updatePromo(produkId: String, promoId: String, diskon: Int){
        mRepo.updatePromo(produkId, promoId, diskon)
    }

    override fun deletePromo(produkId: String){
        mRepo.deletePromo(produkId)
    }

    override fun updateNamaListener(){
        mView.hideNamaProgress()
    }

    override fun updateHargaListener(){
        mView.hideHargaProgress()
    }

    override fun updateStokListener(){
        mView.hideStokProgress()
    }

    override fun updateKeteranganListener(){
        mView.hideDeskripsiProgress()
    }

    override fun updateBeratListener(){
        mView.hideBeratProgress()
    }

    override fun updateDiskonListener(image: String, nama: String, diskon: Int){
        mView.showPromo(image, nama, diskon)
    }

    override fun draftProdukExist(
        id: String,
        nama: String,
        keterangan: String,
        harga: Int,
        stok: Int,
        berat: Int,
        show: Boolean
    ) {
        mView.produkId(id)
        mView.setNama(nama)
        if (harga == 0){
            mView.setHarga("")
        } else {
            mView.setHarga(harga.toString())
        }

        if (stok == 0){
            mView.setStok("")
        } else {
            mView.setStok(stok.toString())
        }

        mView.setDeskripsi(keterangan)

        if (berat == 0){
            mView.setBerat("")
        } else {
            mView.setBerat(berat.toString())
        }

        mView.setShow(show)
    }

    override fun selesaiListener() {
        mView.selesai()
    }

    override fun createProdukListener(produkId: String){
        //mView.showMessage(produkId)
        mView.produkId(produkId)
    }

    override fun deletePromoListener() {
        mView.hidePromo()
    }
}