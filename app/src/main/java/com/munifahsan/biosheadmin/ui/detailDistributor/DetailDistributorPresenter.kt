package com.munifahsan.biosheadmin.ui.detailDistributor

class DetailDistributorPresenter(private val mView: DetailDistributorContract.View): DetailDistributorContract.Presenter, DetailDistributorContract.Listener {
    val mRepo : DetailDistributorContract.Repository
    init {
        this.mRepo = DetailDistributorRepository(this)
    }


    override fun getData(salesId: String){
        mRepo.getData(salesId)

        mView.hideEmail()
        mView.hideNama()
        mView.hideNik()
        mView.hideDaerah()
        mView.hideAlamat()
        mView.hideNohp()
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

    override fun getNikListener(nik: String){
        if (nik == "" || nik == "null") {
            mView.showNik("-")
        } else {
            mView.showNik(nik)
        }
    }

    override fun getDaerahListener(daerah: String){
        if (daerah == "" || daerah== "null") {
            mView.showDaerah("-")
        } else {
            mView.showDaerah(daerah)
        }
    }

    override fun getAlamatListener(alamat: String){
        if (alamat == "" || alamat == "null") {
            mView.showAlamat("-")
        } else {
            mView.showAlamat(alamat)
        }
    }

    override fun getNoHpListener(noHp: String){
        if (noHp == "" || noHp == "null") {
            mView.showNoHp("-")
        } else {
            mView.showNoHp(noHp)
        }
    }
}