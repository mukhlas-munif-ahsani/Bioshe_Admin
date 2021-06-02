package com.munifahsan.biosheadmin.ui.detailTransaksi

import java.util.*

class DetailTransaksiPresenter(val mView: DetailTransaksiContract.View) :
    DetailTransaksiContract.Presenter, DetailTransaksiContract.Listener {
    private val mRepo: DetailTransaksiContract.Repository

    init {
        this.mRepo = DetailTransaksiRepository(this)
    }

    override fun getData(orderId: String) {
        mRepo.getData(orderId)

        mView.hideStatus()
        mView.hideTanggal()
        mView.hideIdPesananBioshe()
        mView.hideIdPesananMidtrans()
        mView.hideKurir()
        mView.hideResi()
        mView.hideAlamat()
        mView.hideMetodePembayaran()
        mView.hideTotalBarangHargaJumlah()
        mView.hideTotalOngkir()
    }

    override fun getDataStatusListner(
        membayar: Boolean,
        dibayar: Boolean,
        diproses: Boolean,
        dikirim: Boolean,
        selesaiDikirim: Boolean,
        selesaiDiterima: Boolean,
    ) {
        if (membayar && !dibayar && !diproses && !dikirim && !selesaiDikirim && !selesaiDiterima) {
            mView.showStatus("PENDING")
        } else if (membayar && dibayar && !diproses && !dikirim && !selesaiDikirim && !selesaiDiterima) {
            mView.showStatus("DIBAYAR")
        } else if (membayar && dibayar && diproses && !dikirim && !selesaiDikirim && !selesaiDiterima) {
            mView.showStatus("DIPROSES")
        } else if (membayar && dibayar && diproses && dikirim && !selesaiDikirim && !selesaiDiterima) {
            mView.showStatus("DIKIRIM")
        } else if (membayar && dibayar && diproses && dikirim && selesaiDikirim && !selesaiDiterima) {
            mView.showStatus("SELESAI")
        } else if (membayar && dibayar && diproses && dikirim && !selesaiDikirim && selesaiDiterima) {
            mView.showStatus("SELESAI")
        } else if (membayar && dibayar && diproses && dikirim && selesaiDikirim && selesaiDiterima) {
            mView.showStatus("SELESAI")
        }
    }

    override fun getDataListener(
        status: String,
        tanggalPesanan: Date?,
        idPemesananBioshe: String,
        idPemesananMidtrans: String,
        kurirPengiriman: String,
        noResi: String,
        alamatPengiriman: String,
        nama: String,
        nomor: String,
        metodePembayaran: String,
        ongkir: Int,
    ) {
        mView.showTanggal(tanggalPesanan)
        mView.showIdPesananBioshe(idPemesananBioshe)
        if (idPemesananMidtrans == ""){
            mView.showIdPesananMidtrans("-")
        } else {
            mView.showIdPesananMidtrans(idPemesananMidtrans)
        }
        mView.showKurir(kurirPengiriman)
        if (noResi == ""){
            mView.showResi("-")
        } else{
            mView.showResi(noResi)
        }
        mView.showAlamat(nama, nomor, alamatPengiriman)
        mView.showMetodePembayaran(metodePembayaran)
        mView.showTotalOngkir(ongkir)
    }

    override fun getDataPelangganListener(nama: String, id:String){
        mView.showNamaPelanggan(nama, id)
    }

    override fun getDataSalesListener(nama: String, id: String){
        mView.showSales(nama, id)
    }

    override fun getDataDistributorListener(nama: String, id: String){
        mView.showDistributor(nama, id)
    }

    override fun totalHargaBarangListener(jumlah: Int, harga: Int, berat: Int, ongkir: Int) {
        mView.showTotalBarangHargaJumlah(jumlah, harga, berat, ongkir)
    }
}