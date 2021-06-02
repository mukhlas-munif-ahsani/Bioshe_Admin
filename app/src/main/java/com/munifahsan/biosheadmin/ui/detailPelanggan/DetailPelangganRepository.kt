package com.munifahsan.biosheadmin.ui.detailPelanggan

import com.munifahsan.biosheadmin.utils.Constants

class DetailPelangganRepository (private val mListener: DetailPelangganContract.Listener): DetailPelangganContract.Repository{

    override fun getData(pelangganId: String){
        Constants.USERS_DB.document(pelangganId).get().addOnSuccessListener {
            if (it.exists()){
                mListener.getFotoListener(it.getString("photo_url").toString())
                mListener.getEmailListener(it.getString("email").toString())
                mListener.getNamaListener(it.getString("nama").toString())
                mListener.getNikListener(it.getString("nik").toString())
                mListener.getGenderListener(it.getString("gender").toString())
                mListener.getNohpListener(it.getString("noHp").toString())
                mListener.getNowaListener(it.getString("noWa").toString())
                mListener.getAlamatRumahListener(it.getString("alamatRumah").toString())
                mListener.getAhliWarisListener(it.getString("ahliWaris").toString())
                mListener.getNamaOutletListener(it.getString("namaOutlet").toString())
                mListener.getAlamatOutletListener(it.getString("alamatOutlet").toString())
            }
        }
    }
}