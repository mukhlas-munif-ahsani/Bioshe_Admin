package com.munifahsan.biosheadmin.ui.tambahProduk

import com.google.firebase.firestore.FieldValue
import com.munifahsan.biosheadmin.models.Produk
import com.munifahsan.biosheadmin.utils.Constants

class TambahProdukRepository(val mListener: TambahProdukContract.Listener) :
    TambahProdukContract.Repository {

    override fun createProduk() {

        Constants.ADMIN_DB.document(Constants.CURRENT_USER_ID.toString()).get()
            .addOnSuccessListener {
                if (it.exists()) {
                    val nomorProduk = it.getLong("nomorProduk")

                   createProdukSecondStep(nomorProduk!!.toInt())
                }
            }
    }

    private fun createProdukSecondStep(nomorProduk: Int){
        val produk = Produk("", "", "", "","",0, 0, 0, 0, 0, selesai = false, show = false)
        Constants.PRODUCT_DB.document(Constants.CURRENT_USER_ID.toString() + nomorProduk.toString()).get().addOnSuccessListener {
            if (it.exists()){
                val id = Constants.CURRENT_USER_ID.toString() + nomorProduk.toString()
                val nama = it.getString("nama").toString()
//                val thumbnail = it.getString("thumbnail")
                val keterangan = it.getString("keterangan").toString()
                val harga = it.getLong("harga")!!.toInt()
//                val diskon = it.getLong("diskon")!!.toInt()
                val stok = it.getLong("stok")!!.toInt()
                val berat = it.getLong("berat")!!.toInt()
//                val sold = it.getLong("sold")
//                val selesai = it.getBoolean("selesai")
                val show = it.getBoolean("show")!!
                val promoId = it.getString("promoId").toString()

                mListener.draftProdukExist(id, nama, keterangan, harga, stok, berat, show)
                getPromo(promoId)
            } else {
                Constants.PRODUCT_DB.document(Constants.CURRENT_USER_ID.toString() + nomorProduk.toString())
                    .set(produk).addOnCompleteListener {
                        mListener.createProdukListener(Constants.CURRENT_USER_ID.toString() + nomorProduk.toString())
                    }
            }
        }
    }

    override fun updateSelesai(produkId: String){
        Constants.ADMIN_DB.document(Constants.CURRENT_USER_ID.toString()).update("nomorProduk", FieldValue.increment(1))
        Constants.PRODUCT_DB.document(produkId).update("selesai", true).addOnCompleteListener {
            mListener.selesaiListener()
        }
    }

    override fun updateShow(show: Boolean, produkId: String){
        Constants.PRODUCT_DB.document(produkId).update("show", show).addOnCompleteListener {
            mListener.updateNamaListener()
        }
    }

    override fun updateNama(nama: String, produkId: String){
        Constants.PRODUCT_DB.document(produkId).update("nama", nama).addOnCompleteListener {
            mListener.updateNamaListener()
        }
    }

    override fun updateHarga(harga: Int, produkId: String){
        Constants.PRODUCT_DB.document(produkId).update("harga", harga).addOnCompleteListener {
            mListener.updateHargaListener()
        }
    }

    override fun updateStok(stok: Int, produkId: String){
        Constants.PRODUCT_DB.document(produkId).update("stok", stok).addOnCompleteListener {
            mListener.updateStokListener()
        }
    }

    override fun updateKeterangan(keterangan: String, produkId: String){
        Constants.PRODUCT_DB.document(produkId).update("keterangan", keterangan).addOnCompleteListener {
            mListener.updateKeteranganListener()
        }
    }

    override fun updateBerat(berat: Int, produkId: String){
        Constants.PRODUCT_DB.document(produkId).update("berat", berat).addOnCompleteListener {
            mListener.updateBeratListener()
        }
    }

    override fun updateDiskon(diskon: Int, produkId: String){
        Constants.PRODUCT_DB.document(produkId).update("diskon", diskon).addOnCompleteListener {
            mListener.updateDiskonListener()
        }
    }

    override fun updatePromo(produkId: String, promoId: String, diskon: Int) {
        Constants.PRODUCT_DB.document(produkId).update("promoId", promoId, "diskon", diskon)
            .addOnSuccessListener {
                getPromo(promoId)
            }
    }

    fun getPromo(promoId: String){
        if (promoId != ""){
            Constants.PROMO_DB.document(promoId).get().addOnSuccessListener {
                if (it.exists()){
                    val image = it.getString("imageThumbnail").toString()
                    val nama = it.getString("judul").toString()
                    val diskon = it.getLong("diskon")!!.toInt()
                    mListener.updateDiskonListener(image, nama, diskon)
                }
            }
        }
    }
}