package com.munifahsan.biosheadmin.ui.editProduk

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.munifahsan.biosheadmin.models.Produk
import com.munifahsan.biosheadmin.utils.Constants

class EditProdukRepository(val mListener: EditProdukContract.Listener) :
    EditProdukContract.Repository {

    override fun getProduk(produkId: String) {
        /*
        ambil data produk yang ingin di edit ke draft
         */
        Constants.PRODUCT_DB.document(produkId).get().addOnSuccessListener {
            if (it.exists()) {
                val id = it.id
                val nama = it.getString("nama").toString()
                val thumbnail = it.getString("thumbnail")
                val keterangan = it.getString("keterangan").toString()
                val harga = it.getLong("harga")!!.toInt()
                val diskon = it.getLong("diskon")!!.toInt()
                val stok = it.getLong("stok")!!.toInt()
                val berat = it.getLong("berat")!!.toInt()
                val sold = it.getLong("sold")
                val selesai = it.getBoolean("selesai")
                val show = it.getBoolean("show")!!
                val promoId = it.getString("promoId").toString()

                createDraftProduk(id,
                    nama,
                    thumbnail,
                    keterangan,
                    promoId,
                    harga,
                    diskon,
                    stok,
                    berat,
                    sold,
                    selesai,
                    show)

                Constants.PRODUK_DRAFT_DB.collection("IMAGES").get().addOnSuccessListener {
                    for (field in it) {
                        val id = field.id
                        Constants.PRODUK_DRAFT_DB.collection("IMAGES").document(id).delete()
                    }

                    /*
                     ambil dan input data yang ingin di edit ke draft
                    */
                    Constants.PRODUCT_DB.document(produkId).collection("IMAGES").get()
                        .addOnSuccessListener { result ->
                            for (field in result) {
                                val id = field.id
                                val image = field.getString("image").toString()
                                val nomor = field.getLong("nomor")!!.toInt()

                                val data = mapOf(
                                    "image" to image,
                                    "nomor" to nomor
                                )
                                Constants.PRODUK_DRAFT_DB.collection("IMAGES").document(id)
                                    .set(data)
                            }
                        }
                }

            }
        }
    }

    private fun createDraftProduk(
        id: String,
        nama: String,
        thumbnail: String?,
        keterangan: String,
        promoId: String,
        harga: Int,
        diskon: Int,
        stok: Int,
        berat: Int,
        sold: Long?,
        selesai: Boolean?,
        show: Boolean,
    ) {
        val data = mapOf(
            "produkId" to id,
            "nama" to nama,
            "thumbnail" to thumbnail,
            "keterangan" to keterangan,
            "promoId" to promoId,
            "harga" to harga,
            "diskon" to diskon,
            "stok" to stok,
            "berat" to berat,
            "sold" to sold,
            "selesai" to selesai,
            "show" to show
        )
        Constants.PRODUK_DRAFT_DB.set(data).addOnSuccessListener {
            Log.d("EditProdukRepository", "EditProdukRepository : masuk")

            Constants.PRODUK_DRAFT_DB.get().addOnSuccessListener {
                val id = it.getString("produkId").toString()
                val nama = it.getString("nama").toString()
                val thumbnail = it.getString("thumbnail")
                val keterangan = it.getString("keterangan").toString()
                val harga = it.getLong("harga")!!.toInt()
                val diskon = it.getLong("diskon")!!.toInt()
                val stok = it.getLong("stok")!!.toInt()
                val berat = it.getLong("berat")!!.toInt()
                val sold = it.getLong("sold")
                val selesai = it.getBoolean("selesai")
                val show = it.getBoolean("show")!!
                val promoId = it.getString("promoId").toString()

                mListener.draftProdukExist(id, nama, keterangan, harga, stok, berat, show)
                getPromo(promoId)
            }
        }
    }

    override fun simpan(produkId: String) {

        /*
        ambil data dari draft produk
         */
        Constants.PRODUK_DRAFT_DB.get().addOnSuccessListener {
            if (it.exists()) {
                val nama = it.getString("nama").toString()
                val thumbnail = it.getString("thumbnail")
                val keterangan = it.getString("keterangan").toString()
                val harga = it.getLong("harga")!!.toInt()
                val diskon = it.getLong("diskon")!!.toInt()
                val stok = it.getLong("stok")!!.toInt()
                val berat = it.getLong("berat")!!.toInt()
                val sold = it.getLong("sold")
                val selesai = it.getBoolean("selesai")
                val show = it.getBoolean("show")!!
                val promoId = it.getString("promoId")

                val data = mapOf(
                    "nama" to nama,
                    "thumbnail" to thumbnail,
                    "keterangan" to keterangan,
                    "harga" to harga,
                    "diskon" to diskon,
                    "stok" to stok,
                    "berat" to berat,
                    "sold" to sold,
                    "selesai" to selesai,
                    "show" to show,
                    "promoId" to promoId
                )

                /*
                update prdouk terkait dengan data yang ada di draft
                 */
                Constants.PRODUCT_DB.document(produkId).update(data).addOnSuccessListener {

                    /*
                    ambil data image dari produk terkait
                     */
                    Constants.PRODUCT_DB.document(produkId).collection("IMAGES").get()
                        .addOnSuccessListener { result ->
                            for (field in result) {
                                val id = field.id

                                /*
                                hapus data images yang ada di dalam produk terkait
                                 */
                                Constants.PRODUCT_DB.document(produkId).collection("IMAGES")
                                    .document(id).delete()
                            }

                            /*
                            ambil data image dari draft
                             */
                            Constants.PRODUK_DRAFT_DB.collection("IMAGES").get()
                                .addOnSuccessListener { result ->
                                    for (field in result) {
                                        val id = field.id
                                        val image = field.getString("image").toString()
                                        val nomor = field.getLong("nomor")!!.toInt()

                                        val data = mapOf(
                                            "image" to image,
                                            "nomor" to nomor
                                        )

                                        /*
                                        update data image produk terkait
                                         */
                                        Constants.PRODUCT_DB.document(produkId).collection("IMAGES")
                                            .document(id).set(data)
                                    }

                                    /*
                                    hapus draft
                                     */
                                    deleteDraft()
                                }
                        }


                }

            }
        }
    }

    override fun deleteDraft() {
        val data = mapOf(
            "produkId" to "",
            "nama" to "",
            "thumbnail" to "",
            "keterangan" to "",
            "harga" to 0,
            "diskon" to 0,
            "stok" to 0,
            "berat" to 0,
            "promoId" to "",
            "sold" to 0,
            "selesai" to false,
            "show" to false
        )
        Constants.PRODUK_DRAFT_DB.update(data).addOnSuccessListener {

        }
        /*
        ambil dan input data yang ingin di edit ke draft
        */
        Constants.PRODUK_DRAFT_DB.collection("IMAGES").get()
            .addOnSuccessListener { result ->
                for (field in result) {
                    val id = field.id

                    Constants.PRODUK_DRAFT_DB.collection("IMAGES").document(id).delete()
                }

                mListener.selesaiListener()
            }
    }

    override fun updateShow(show: Boolean, produkId: String) {
        Constants.PRODUK_DRAFT_DB.update("show", show).addOnCompleteListener {
            mListener.updateNamaListener()
        }
    }

    override fun updateNama(nama: String, produkId: String) {
        Constants.PRODUK_DRAFT_DB.update("nama", nama).addOnCompleteListener {
            mListener.updateNamaListener()
        }
    }

    override fun updateHarga(harga: Int, produkId: String) {
        Constants.PRODUK_DRAFT_DB.update("harga", harga).addOnCompleteListener {
            mListener.updateHargaListener()
        }
    }

    override fun updateStok(stok: Int, produkId: String) {
        Constants.PRODUK_DRAFT_DB.update("stok", stok).addOnCompleteListener {
            mListener.updateStokListener()
        }
    }

    override fun updateKeterangan(keterangan: String, produkId: String) {
        Constants.PRODUK_DRAFT_DB.update("keterangan", keterangan).addOnCompleteListener {
            mListener.updateKeteranganListener()
        }
    }

    override fun updateBerat(berat: Int, produkId: String) {
        Constants.PRODUK_DRAFT_DB.update("berat", berat).addOnCompleteListener {
            mListener.updateBeratListener()
        }
    }

//    override fun updateDiskon(diskon: Int, produkId: String) {
//        Constants.PRODUK_DRAFT_DB.update("diskon", diskon).addOnCompleteListener {
//            mListener.updateDiskonListener()
//        }
//    }

    override fun updatePromo(produkId: String, promoId: String, diskon: Int) {
        Constants.PRODUK_DRAFT_DB.update("promoId", promoId, "diskon", diskon)
            .addOnSuccessListener {
                getPromo(promoId)
            }
    }

    override fun deletePromo(produkId: String){
        Constants.PRODUK_DRAFT_DB.update("promoId", "", "diskon", 0)
            .addOnSuccessListener {
                mListener.deletePromoListener()
            }
    }

    fun getPromo(promoId: String){
        if (promoId != ""){
            Constants.PROMO_DB.document(promoId).get().addOnSuccessListener {
                val image = it.getString("imageThumbnail").toString()
                val nama = it.getString("judul").toString()
                val diskon = it.getLong("diskon")!!.toInt()
                mListener.updateDiskonListener(image, nama, diskon)
            }
        }
    }
}