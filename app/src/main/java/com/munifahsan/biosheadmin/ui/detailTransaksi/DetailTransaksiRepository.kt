package com.munifahsan.biosheadmin.ui.detailTransaksi

import com.munifahsan.biosheadmin.models.Orders
import com.munifahsan.biosheadmin.models.Pelanggan
import com.munifahsan.biosheadmin.utils.Constants

class DetailTransaksiRepository(private val mListener: DetailTransaksiContract.Listener): DetailTransaksiContract.Repository {


    override fun getData(orderId:String){
        Constants.ORDERS_DB.document(orderId).addSnapshotListener { value, error ->
            val order = value?.toObject(Orders::class.java)

            Constants.USERS_DB.document(order!!.userId).addSnapshotListener { valueUsr, error ->
                val user = valueUsr?.toObject(Pelanggan::class.java)
                mListener.getDataListener(
                    order.orderStatus,
                    order.orderDate,
                    order.id,
                    order.midtransOrderId,
                    order.kurir,
                    order.resi,
                    order.alamatPengiriman,
                    user!!.nama,
                    user.noHp,
                    order.metodePembayaran,
                    order.ongkir
                )

                mListener.getDataPelangganListener(user.nama, user.id)

                getSales(user.salesId)

                getDistributor(user.distributorId)
            }

            mListener.getDataStatusListner(order.membayar, order.dibayar, order.diproses, order.dikirim, order.selesaiDikirim, order.selesaiDiterima)

        }
        showTotalBarangHarga(orderId)
    }

    private fun getSales(salesId: String) {
        Constants.SALES_DB.document(salesId).get().addOnSuccessListener {
            mListener.getDataSalesListener(it.getString("nama").toString(), salesId)
        }
    }

    private fun getDistributor(distributorId: String){
        Constants.DISTRIBUTOR_DB.document(distributorId).get().addOnSuccessListener {
            mListener.getDataDistributorListener(it.getString("nama").toString(), distributorId)
        }
    }

    private fun showTotalBarangHarga(orderId: String) {
        val rev = Constants.ORDERS_DB.document(orderId).collection("PRODUCT")
        rev.addSnapshotListener { value, error ->
            val jumlah = ArrayList<Int>()
            var jumlahItem = 0
            var berat = 0
            for (field in value!!) {
                val a = field.getLong("harga")
                val b = field.getLong("jumlahItem")
                val c = field.getLong("diskon")
                val d = field.getLong("berat")
                jumlahItem += b!!.toInt()
                berat += d!!.toInt()*b.toInt()
                val disconNum: Int = c!!.toInt()
                val disconHarga: Int = a!!.toInt() * disconNum / 100
                val harga = a - disconHarga
                jumlah.add(harga.toInt() * b.toInt())
            }

            Constants.ORDERS_DB.document(orderId).addSnapshotListener { valueOrdr, error ->
                mListener.totalHargaBarangListener( jumlahItem, jumlah.sum(), berat, valueOrdr!!.getLong("ongkir")!!.toInt())
            }
        }
    }
}