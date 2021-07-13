package com.munifahsan.biosheadmin.ui.detailPelanggan

import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.munifahsan.biosheadmin.utils.Constants
import org.joda.time.DateTime
import org.joda.time.Months
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DetailPelangganRepository(private val mListener: DetailPelangganContract.Listener) :
    DetailPelangganContract.Repository {

    override fun getData(pelangganId: String) {
        Constants.USERS_DB.document(pelangganId).addSnapshotListener { value, error ->
            if (value != null) {
                mListener.getPointsListener(value.getLong("bioshePoints")!!.toInt())
                mListener.getLoyaltiListener(value.getString("loyalti").toString(), value.id)
                mListener.getFotoListener(value.getString("photo_url").toString())
                mListener.getEmailListener(value.getString("email").toString())
                mListener.getNamaListener(value.getString("nama").toString())
                mListener.getNikListener(value.getString("nik").toString())
                mListener.getGenderListener(value.getString("gender").toString())
                mListener.getNohpListener(value.getString("noHp").toString())
                mListener.getNowaListener(value.getString("noWa").toString())
                mListener.getAlamatRumahListener(value.getString("alamatRumah").toString())
                mListener.getAhliWarisListener(value.getString("ahliWaris").toString())
                mListener.getNamaOutletListener(value.getString("namaOutlet").toString())
                mListener.getAlamatOutletListener(value.getString("alamatOutlet").toString())

                getDataTagihan(value.id)
                getDataTotalBelanja(value.id)
                getDataSales(value.getString("salesId").toString())
                if (value.getString("distributorId") != null){
                    getDataDistributor(value.getString("distributorId").toString())
                } else {
                    mListener.getDataDistributorListener("null", "","","", "")
                }
            }
        }
    }

    private fun getDataSales(id: String) {
        Constants.SALES_DB.document(id).get().addOnSuccessListener {
            if (it.exists()) {
                mListener.getDataSalesListener(it.getString("nama").toString(),
                    it.id,
                    it.getString("photo_url").toString(),
                    it.getString("alamat").toString(),
                    "")
            }
        }
    }

    private fun getDataDistributor(id: String) {
        if (id != ""){
            Constants.DISTRIBUTOR_DB.document(id).get().addOnSuccessListener {
                if (it.exists()) {
                    mListener.getDataDistributorListener(it.getString("nama").toString(),
                        it.id,
                        it.getString("photo_url").toString(),
                        it.getString("alamat").toString(),
                        "")
                }
            }
        } else {
            mListener.getDataDistributorListener("null", "","","", "")
        }
    }

    private fun getDataTagihan(id: String) {

        Constants.USERS_DB.document(id).collection("TAGIHAN").whereEqualTo("dibayar", false)
            .addSnapshotListener { value, error ->
                val tagihan = ArrayList<Int>()
                if (value != null) {
                    for (field in value) {
                        val a = field.getLong("jumlahPembayaran")
                        val b = field.getDate("deadLinePembayaran")
                        val c = field.getLong("bunga")
                        val d = field.getLong("jumlahPembayaranTelat")

                        if (a != null && b != null && c != null) {
                            if (Date().after(b)) {
//                                val bunga = (a.toInt() * c.toInt() / 100) * getBulanTelat(b)
                                tagihan.add(d!!.toInt())
                            } else {
                                tagihan.add(a.toInt())
                            }
                        }
                    }
                }
                mListener.getTagihanListener(tagihan.sum())
            }
    }

    private fun getDataTotalBelanja(id: String) {

        Constants.ORDERS_DB.whereEqualTo("userId", id).whereEqualTo("selesaiDikirim", true)
            .addSnapshotListener { value, error ->
                if (value != null) {
                    for (field in value) {
                        val rev = Constants.ORDERS_DB.document(field.id).collection("PRODUCT")
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
                                berat += d!!.toInt() * b.toInt()
                                val disconNum: Int = c!!.toInt()
                                val disconHarga: Int = a!!.toInt() * disconNum / 100
                                val harga = a - disconHarga
                                jumlah.add(harga.toInt() * b.toInt())
                            }

                            mListener.getTotalBelanjaListener(jumlah.sum())
                        }
                    }
                }
            }
    }
}