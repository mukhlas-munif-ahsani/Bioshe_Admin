package com.munifahsan.biosheadmin.ui.tambahPromo

import android.net.Uri
import android.view.View
import androidx.core.net.toUri
import com.google.firebase.firestore.FieldValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.munifahsan.biosheadmin.models.Promo
import com.munifahsan.biosheadmin.utils.Constants

class TambahPromoRepository(val mListener: TambahPromoContract.Listener): TambahPromoContract.Repository {

    override fun createPromo(){
        Constants.ADMIN_DB.document(Constants.CURRENT_USER_ID.toString()).get()
            .addOnSuccessListener {
                if (it.exists()) {
                    val nomorProduk = it.getLong("nomorPromo")

                    createPromoSecondStep(nomorProduk!!.toInt())
                }
            }
    }

    private fun createPromoSecondStep(nomorPromo: Int) {
        val promo = Promo("", "", 0, "", show = false, selesai = false, keterangan = "")
        Constants.PROMO_DB.document(Constants.CURRENT_USER_ID.toString() + nomorPromo.toString()).get().addOnSuccessListener {
            if (it.exists()){
                val id = Constants.CURRENT_USER_ID.toString() + nomorPromo.toString()
                val image = it.getString("imageThumbnail").toString()
                val judul = it.getString("judul").toString()
                val diskon = it.getLong("diskon")!!.toInt()
                val show = it.getBoolean("show")
                val keterangan = it.getString("keterangan").toString()

                mListener.draftPromoExist(Constants.CURRENT_USER_ID.toString() + nomorPromo.toString(), image, judul, keterangan, diskon, show)

            } else {
                Constants.PROMO_DB.document(Constants.CURRENT_USER_ID.toString() + nomorPromo.toString())
                    .set(promo).addOnCompleteListener {
                        mListener.createPromoListener(Constants.CURRENT_USER_ID.toString() + nomorPromo.toString())
                    }
            }
        }
    }

    override fun updateSelesai(promoId: String){
        Constants.ADMIN_DB.document(Constants.CURRENT_USER_ID.toString()).update("nomorPromo", FieldValue.increment(1))
        Constants.PROMO_DB.document(promoId).update("selesai", true).addOnCompleteListener {
            mListener.selesaiListener()
        }
    }


    override fun updateImage(promoId: String, imageUri: Uri){

        val fileReference: StorageReference =
            FirebaseStorage.getInstance().reference.child("promoImages/" + Constants.CURRENT_USER_ID + "/" + imageUri.lastPathSegment)
        fileReference
            .putFile(imageUri)
            .addOnSuccessListener {

                fileReference.downloadUrl.addOnSuccessListener {url->
                    Constants.PROMO_DB.document(promoId).update("imageThumbnail", url.toString()).addOnCompleteListener {
                        mListener.updateImageListener(url.toString())
                    }
                }

            }
            .addOnProgressListener { task->
               val progress = (100.0 * task.bytesTransferred / task.totalByteCount).toInt()
                mListener.uploadImageProgressListener(progress)
            }
            .addOnFailureListener {

            }
    }

    override fun updateJudul(promoId: String, judul: String){
        Constants.PROMO_DB.document(promoId).update("judul", judul).addOnCompleteListener {
            mListener.updateNamaListener()
        }
    }

    override fun updateKetarangan(promoId: String, kertarangan: String){
        Constants.PROMO_DB.document(promoId).update("keterangan", kertarangan).addOnCompleteListener {
            mListener.updateKeranganListener()
        }
    }

    override fun updateDiskon(promoId: String, diskon: Int){
        Constants.PROMO_DB.document(promoId).update("diskon", diskon).addOnCompleteListener {
            mListener.updateDiskonListener()
        }
    }

    override fun updateShow(promoId: String, show: Boolean){
        Constants.PROMO_DB.document(promoId).update("show", show).addOnCompleteListener {
            //mListener.updateShowListener()
        }
    }
}