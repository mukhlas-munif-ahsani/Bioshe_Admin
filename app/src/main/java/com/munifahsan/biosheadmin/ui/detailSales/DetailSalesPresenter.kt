package com.munifahsan.biosheadmin.ui.detailSales

class DetailSalesPresenter(private val mView: DetailSalesContract.View): DetailSalesContract.Presenter, DetailSalesContract.Listener {
    private val mRepo: DetailSalesContract.Repository
    init {
        this.mRepo = DetailSalesRepository(this)
    }

    override fun getData(salesId: String){
        mRepo.getData(salesId)

        mView.hideEmail()
        mView.hideNama()
    }

    override fun getFotoListener(foto: String) {
        if (foto != "" || foto.isNotEmpty()) {
            mView.showfoto(foto)
        }
    }

    override fun getEmailListener(email: String) {
        if (email == "" || email == "null") {
            mView.showEmail("-")
        } else {
            mView.showEmail(email)
        }
    }

    override fun getNamaListener(nama: String) {
        if (nama == "" || nama == "null") {
            mView.showNama("-")
        } else {
            mView.showNama(nama)
        }
    }
}