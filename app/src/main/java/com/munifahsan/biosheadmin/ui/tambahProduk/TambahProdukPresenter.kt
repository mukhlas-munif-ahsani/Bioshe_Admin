package com.munifahsan.biosheadmin.ui.tambahProduk

class TambahProdukPresenter(val mView: TambahProdukContract.View) : TambahProdukContract.Presenter,
    TambahProdukContract.Listener {
    private val mRepo: TambahProdukContract.Repository

    init {
        mRepo = TambahProdukRepository(this)
    }

    override fun createProduk(){
        mRepo.createProduk()
    }

    override fun updateSelesai(produkId: String){
        mRepo.updateSelesai(produkId)
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

    override fun updateDiskonListener(image: String, nama: String, diskon: Int){
        mView.showPromo(image, nama, diskon)
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

    override fun updateDiskonListener(){

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
}