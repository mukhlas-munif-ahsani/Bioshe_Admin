package com.munifahsan.biosheadmin.ui.tambahProduk

interface TambahProdukContract {
    interface View{

        fun showMessage(msg: String)
        fun produkId(produkId: String)
        fun showNamaProgress()
        fun hideNamaProgress()
        fun showHargaProgress()
        fun hideHargaProgress()
        fun showStokProgress()
        fun hideStokProgress()
        fun showDeskripsiProgress()
        fun hideDeskripsiProgress()
        fun showBeratProgress()
        fun hideBeratProgress()
        fun setNama(nama: String)
        fun setHarga(harga: String)
        fun setStok(stok: String)
        fun setDeskripsi(keterangan: String)
        fun setBerat(berat: String)
        fun selesai()
        fun showPromo(image: String, nama: String, diskon: Int)
        fun setShow(show: Boolean)
    }

    interface Presenter{

        fun createProduk()
        fun updateNama(nama: String, produkId: String)
        fun updateStok(stok: Int, produkId: String)
        fun updateHarga(harga: Int, produkId: String)
        fun updateKeterangan(keterangan: String, produkId: String)
        fun updateBerat(berat: Int, produkId: String)
        fun updateSelesai(produkId: String)
        fun updateShow(show: Boolean, produkId: String)
        fun updatePromo(produkId: String, promoId: String, diskon: Int)
    }

    interface Repository{

        fun createProduk()
        fun updateNama(nama: String, produkId: String)
        fun updateHarga(harga: Int, produkId: String)
        fun updateStok(stok: Int, produkId: String)
        fun updateKeterangan(keterangan: String, produkId: String)
        fun updateBerat(berat: Int, produkId: String)
        fun updateDiskon(diskon: Int, produkId: String)
        fun updateSelesai(produkId: String)
        fun updateShow(show: Boolean, produkId: String)
        fun updatePromo(produkId: String, promoId: String, diskon: Int)
    }

    interface Listener{

        fun createProdukListener(produkId: String)
        fun updateNamaListener()
        fun updateHargaListener()
        fun updateStokListener()
        fun updateKeteranganListener()
        fun updateBeratListener()
        fun updateDiskonListener()
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
        fun updateDiskonListener(image: String, nama: String, diskon: Int)
    }
}