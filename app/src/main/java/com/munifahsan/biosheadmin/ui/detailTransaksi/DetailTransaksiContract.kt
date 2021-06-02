package com.munifahsan.biosheadmin.ui.detailTransaksi

import java.util.*

interface DetailTransaksiContract {
    interface View{

        fun showStatus(status: String)
        fun hideStatus()
        fun showTanggal(tanggal: Date?)
        fun hideTanggal()
        fun showIdPesananBioshe(id: String)
        fun hideIdPesananBioshe()
        fun showIdPesananMidtrans(id: String)
        fun hideIdPesananMidtrans()
        fun showNamaPelanggan(nama: String, id: String)
        fun hideNamaPelanggan()
        fun showSales(sales: String, id: String)
        fun showDistributor(distributor: String, id: String)
        fun hideSales()
        fun hideDistributor()
        fun hideKurir()
        fun showKurir(kurir: String)
        fun showTotalBarangHargaJumlah(jumlah: Int, harga: Int, berat: Int, ongkir: Int)
        fun hideTotalBarangHargaJumlah()
        fun showResi(resi: String)
        fun showAlamat(nama: String, noHp: String, alamat: String)
        fun hideResi()
        fun hideAlamat()
        fun showMetodePembayaran(metode: String)
        fun hideMetodePembayaran()
        fun showTotalOngkir(ongkir: Int)
        fun hideTotalOngkir()
        fun showTotalBayar(bayar: Int)
        fun hideTotalBayar()
        fun showMessage(msg: String)
    }
    interface Presenter{

        fun getData(orderId: String)

    }
    interface Repository{

        fun getData(orderId: String)
    }
    interface Listener{

        fun getDataListener(
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
            ongkir: Int
        )
        fun totalHargaBarangListener(totalHarga: Int, totalBarang: Int, ongkir: Int, berat: Int)
        fun getDataStatusListner(
            membayar: Boolean,
            dibayar: Boolean,
            diproses: Boolean,
            dikirim: Boolean,
            selesaiDikirim: Boolean,
            selesaiDiterima: Boolean
        )

        fun getDataPelangganListener(nama: String, id: String)
        fun getDataSalesListener(nama: String, id: String)
        fun getDataDistributorListener(nama: String, id: String)
    }
}