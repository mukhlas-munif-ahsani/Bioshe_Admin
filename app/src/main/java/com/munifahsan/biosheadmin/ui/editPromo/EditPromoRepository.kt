package com.munifahsan.biosheadmin.ui.editPromo

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.munifahsan.biosheadmin.utils.Constants

class EditPromoRepository(val mListener: EditPromoContract.Listener) :
    EditPromoContract.Repository {

    override fun getPromosi(promoId: String) {
        /*
        ambil data produk yang ingin di edit ke draft
         */
        Constants.PROMO_DB.document(promoId).get().addOnSuccessListener {
            if (it.exists()) {
                val id = it.id
                val nama = it.getString("judul").toString()
                val thumbnail = it.getString("imageThumbnail")
                val keterangan = it.getString("keterangan").toString()
                val diskon = it.getLong("diskon")!!.toInt()
                val show = it.getBoolean("show")!!
                val selesai = it.getBoolean("selesai")!!

                createDraftProduk(id,
                    nama,
                    thumbnail,
                    keterangan,
                    diskon,
                    selesai,
                    show)

            }
        }
    }

    private fun createDraftProduk(
        id: String,
        nama: String,
        thumbnail: String?,
        keterangan: String,
        diskon: Int,
        selesai: Boolean,
        show: Boolean,
    ) {
        val data = mapOf(
            "promoId" to id,
            "judul" to nama,
            "imageThumbnail" to thumbnail,
            "keterangan" to keterangan,
            "diskon" to diskon,
            "selesai" to selesai,
            "show" to show
        )
        Constants.PROMO_DRAFT_DB.set(data).addOnSuccessListener {
            Log.d("EditProdukRepository", "EditProdukRepository : masuk")

            Constants.PROMO_DRAFT_DB.get().addOnSuccessListener {
                val id = it.getString("promoId").toString()
                val nama = it.getString("judul").toString()
                val thumbnail = it.getString("imageThumbnail")
                val keterangan = it.getString("keterangan").toString()
                val diskon = it.getLong("diskon")!!.toInt()
                val selesai = it.getBoolean("selesai")
                val show = it.getBoolean("show")!!

                mListener.draftPromoExist(id, nama, thumbnail, keterangan, diskon, show)
            }
        }
    }

    override fun simpan(promoId: String) {

        /*
        ambil data dari draft produk
         */
        Constants.PROMO_DRAFT_DB.get().addOnSuccessListener {
            if (it.exists()) {
                val judul = it.getString("judul").toString()
                val thumbnail = it.getString("imageThumbnail")
                val keterangan = it.getString("keterangan").toString()
                val diskon = it.getLong("diskon")!!.toInt()
                val selesai = it.getBoolean("selesai")
                val show = it.getBoolean("show")!!

                val data = mapOf(
                    "judul" to judul,
                    "imageThumbnail" to thumbnail,
                    "keterangan" to keterangan,
                    "diskon" to diskon,
                    "selesai" to selesai,
                    "show" to show,
                )

                /*
                update prdouk terkait dengan data yang ada di draft
                 */
                Constants.PROMO_DB.document(promoId).update(data).addOnSuccessListener {

                    Constants.PRODUCT_DB.whereEqualTo("promoId", promoId).get()
                        .addOnSuccessListener { queryResult ->
                            for (field in queryResult) {
                                val produkId = field.id
                                Constants.PRODUCT_DB.document(produkId).update("diskon", diskon)
                            }
                            mListener.selesai()
                        }
                }
            }
        }
    }

    override fun deleteDraft() {
        val data = mapOf(
            "promoId" to "",
            "judul" to "",
            "imageThumbnail" to "",
            "keterangan" to "",
            "diskon" to "",
            "selesai" to false,
            "show" to false
        )
        Constants.PROMO_DRAFT_DB.update(data).addOnSuccessListener {

        }
    }

    override fun updateImage(promoId: String, imageUri: Uri) {

        val fileReference: StorageReference =
            FirebaseStorage.getInstance().reference.child("promoImages/" + Constants.CURRENT_USER_ID + "/" + imageUri.lastPathSegment)
        fileReference
            .putFile(imageUri)
            .addOnSuccessListener {

                fileReference.downloadUrl.addOnSuccessListener { url ->
                    Constants.PROMO_DRAFT_DB.update("imageThumbnail", url.toString())
                        .addOnCompleteListener {
                            mListener.updateImageListener(url.toString())
                        }
                }

            }
            .addOnProgressListener { task ->
                val progress = (100.0 * task.bytesTransferred / task.totalByteCount).toInt()
                mListener.uploadImageProgressListener(progress)
            }
            .addOnFailureListener {

            }
    }

    override fun updateShow(show: Boolean, produkId: String) {
        Constants.PROMO_DRAFT_DB.update("show", show).addOnCompleteListener {
//            mListener.updateNamaListener()
        }
    }

    override fun updateNama(nama: String, produkId: String) {
        Constants.PROMO_DRAFT_DB.update("judul", nama).addOnCompleteListener {
            mListener.updateNamaListener()
        }
    }

    override fun updateDiskon(diskon: Int, produkId: String) {
        Constants.PROMO_DRAFT_DB.update("diskon", diskon).addOnCompleteListener {
            mListener.updateDiskonListener()
        }
    }

    override fun updateKeterangan(keterangan: String, produkId: String) {
        Constants.PROMO_DRAFT_DB.update("keterangan", keterangan).addOnCompleteListener {
            mListener.updateKeteranganListener()
        }
    }


}