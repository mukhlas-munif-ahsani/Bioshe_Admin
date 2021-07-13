package com.munifahsan.biosheadmin.ui.detailDistributor

import com.munifahsan.biosheadmin.utils.Constants

class DetailDistributorRepository(val mListener: DetailDistributorContract.Listener): DetailDistributorContract.Repository {
    override fun getData(distributorId: String){
        Constants.DISTRIBUTOR_DB.document(distributorId).get().addOnSuccessListener {
            mListener.getFotoListener(it.getString("photo_url").toString())
            mListener.getEmailListener(it.getString("email").toString())
            mListener.getNamaListener(it.getString("nama").toString())
            mListener.getNikListener(it.getString("nik").toString())
            mListener.getDaerahListener(it.getString("daerah").toString())
            mListener.getAlamatListener(it.getString("alamat").toString())
            mListener.getNoHpListener(it.getString("noHp").toString())
        }
    }
}