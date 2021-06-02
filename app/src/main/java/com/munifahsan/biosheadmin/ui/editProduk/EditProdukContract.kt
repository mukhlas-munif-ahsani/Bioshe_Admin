package com.munifahsan.biosheadmin.ui.editProduk

interface EditProdukContract {
    interface View{

        fun produkId(produkId: String)
        fun showNamaProgress()
        fun hideNamaProgress()
        fun setNama(nama: String)
        fun showHargaProgress()
        fun hideHargaProgress()
        fun setHarga(harga: String)
        fun showStokProgress()
        fun hideStokProgress()
        fun setStok(stok: String)
        fun showDeskripsiProgress()
        fun hideDeskripsiProgress()
        fun setDeskripsi(keterangan: String)
        fun showBeratProgress()
        fun hideBeratProgress()
        fun setBerat(berat: String)
        fun selesai()
        fun showMessage(msg: String)
        fun setShow(show: Boolean)
        fun showPromo(image: String, nama: String, diskon: Int)
        fun hidePromo()
    }
    interface Presenter{

        fun simpan(produkId: String)
        fun updateShow(show: Boolean, produkId: String)
        fun updateNama(nama: String, produkId: String)
        fun updateHarga(harga: Int, produkId: String)
        fun updateStok(stok: Int, produkId: String)
        fun updateKeterangan(keterangan: String, produkId: String)
        fun updateBerat(berat: Int, produkId: String)
        fun getProduk(produkId: String)
        fun deleteDraft()
        fun updatePromo(produkId: String, promoId: String, diskon: Int)
        fun deletePromo(produkId: String)
    }
    interface Repository{

        //fun updateDiskon(diskon: Int, produkId: String)
        fun updateBerat(berat: Int, produkId: String)
        fun updateKeterangan(keterangan: String, produkId: String)
        fun updateStok(stok: Int, produkId: String)
        fun updateHarga(harga: Int, produkId: String)
        fun updateNama(nama: String, produkId: String)
        fun updateShow(show: Boolean, produkId: String)
        fun simpan(produkId: String)
        fun getProduk(produkId: String)
        fun deleteDraft()
        fun updatePromo(produkId: String, promoId: String, diskon: Int)
        fun deletePromo(produkId: String)
    }
    interface Listener{

        fun updateNamaListener()
        fun updateHargaListener()
        fun updateStokListener()
        fun updateKeteranganListener()
        fun updateBeratListener()
        fun updateDiskonListener(image: String, nama: String, diskon: Int)
        fun draftProdukExist(
            id: String,
            nama: String,
            keterangan: String,
            harga: Int,
            stok: Int,
            berat: Int,
            show: Boolean
        )

        fun selesaiListener()
        fun createProdukListener(produkId: String)
        fun deletePromoListener()
    }
}