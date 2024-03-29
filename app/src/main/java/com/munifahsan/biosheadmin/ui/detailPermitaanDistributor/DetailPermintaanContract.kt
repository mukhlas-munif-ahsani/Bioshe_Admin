package com.munifahsan.biosheadmin.ui.detailPermitaanDistributor

import java.util.*

interface DetailPermintaanContract {
    interface View{

        fun showStatus(status: String)
        fun hideStatus()
        fun showTanggal(tanggal: Date?)
        fun hideTanggal()
        fun showIdPermintaan(id: String)
        fun hideIdPermintaan()
        fun showProdukImage(image: String)
        fun showNamaProduk(nama: String)
        fun hideNamaProduk()
        fun showJumlahPermintaan(jumlah: String)
        fun hideJumlahPermintaan()
        fun showKurir(kurir: String)
        fun hideKurir()
        fun showNoResi(resi: String)
        fun hiedNoResi()
        fun showAlamatPengiriman(alamat: String)
        fun hideAlamatPengiriman()
        fun showNamaPenerima(nama: String)
        fun hideNamaPenerima()
        fun showNoPenerima(no: String)
        fun hideNoPenerima()
        fun enableProsesButton()
        fun disableProsesButton()
        fun enableKirimButton()
        fun disableKirimButton()
    }
    interface Presenter{

        fun getDataPermintaan(idPermintaan: String)
        fun updateKonfirmasiDiproses(idPermintaan: String)
        fun updateKonfirmasiDikirim(idPermintaan: String, kurir: String, resi: String)
    }
    interface Repository{

        fun getDataPermintaan(idPermintaan: String)
        fun updateKonfirmasiDiproses(idPermintaan: String)
        fun updateKonfirmasiDikirim(idPermintaan: String, kurir: String, resi: String)
    }
    interface Listener{

        fun getDataTanggal(tanggal: Date?)
        fun getDataProdukImage(image: String)
        fun getDataNamaProduk(nama: String)
        fun getDataJumlahPermintaan(jumlah: Int)
        fun getDataKurir(kurir: String)
        fun getDataNoresi(resi: String)
        fun getDataAlamat(alamat: String)
        fun getDataStatusListener(diproses: Boolean, dikirim: Boolean, diterima: Boolean)
        fun getIdPermintaan(id: String)
        fun getDataNamaPenerima(nama: String)
        fun getDataNomorPenerima(no: String)
    }
}