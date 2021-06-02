package com.munifahsan.biosheadmin.ui.detailPelanggan

class DetailPelangganPresenter(val mView: DetailPelangganContract.View) :
    DetailPelangganContract.Presenter, DetailPelangganContract.Listener {
    private val mRepo: DetailPelangganContract.Repository

    init {
        this.mRepo = DetailPelangganRepository(this)
    }

    override fun getData(pelangganId: String) {
        mRepo.getData(pelangganId)

        mView.hideEmail()
        mView.hideNama()
        mView.hideNik()
        mView.hideGender()
        mView.hideNohp()
        mView.hideNowa()
        mView.hideAlamatRumah()
        mView.hideAhliWaris()
        mView.hideNamaOutlet()
        mView.hideAlamatOutlet()
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

    override fun getNikListener(nik: String) {
        if (nik == "" || nik == "null") {
            mView.showNik("-")
        } else {
            mView.showNik(nik)
        }
    }

    override fun getGenderListener(gender: String) {
        if (gender == "" || gender == "null") {
            mView.showGender("-")
        } else {
            mView.showGender(gender)
        }
    }

    override fun getNohpListener(noHp: String) {
        if (noHp == "" || noHp == "null") {
            mView.showNohp("-")
        } else {
            mView.showNohp(noHp)
        }
    }

    override fun getNowaListener(noWa: String) {
        if (noWa == "" || noWa == "null") {
            mView.showNowa("-")
        } else {
            mView.showNowa(noWa)
        }
    }

    override fun getAlamatRumahListener(alamat: String) {
        if (alamat == "" || alamat == "null") {
            mView.showAlamatRumah("-")
        } else {
            mView.showAlamatRumah(alamat)
        }
    }

    override fun getAhliWarisListener(ahli: String) {
        if (ahli == "" || ahli == "null") {
            mView.showAhliWaris("-")
        } else {
            mView.showAhliWaris(ahli)
        }
    }

    override fun getNamaOutletListener(nama: String) {
        if (nama == "" || nama == "null") {
            mView.showNamaOutlet("-")
        } else {
            mView.showNamaOutlet(nama)
        }
    }

    override fun getAlamatOutletListener(alamat: String) {
        if (alamat == "" || alamat == "null") {
            mView.showAlamatOutlet("-")
        } else {
            mView.showAlamatOutlet(alamat)
        }
    }

}