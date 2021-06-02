package com.munifahsan.biosheadmin.ui.detailSales

import com.munifahsan.biosheadmin.utils.Constants

class DetailSalesRepository(private val mListener: DetailSalesContract.Listener): DetailSalesContract.Repository {

    override fun getData(salesId: String){
        Constants.SALES_DB.document(salesId).get().addOnSuccessListener {
            mListener.getFotoListener(it.getString("photo_url").toString())
            mListener.getEmailListener(it.getString("email").toString())
            mListener.getNamaListener(it.getString("nama").toString())
        }
    }
}