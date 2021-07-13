package com.munifahsan.biosheadmin.ui.detailDistributor

interface DetailDistributorContract {
    interface View{
        fun showfoto(fotoUrl: String)
        fun showEmail(email: String)
        fun hideEmail()
        fun showNama(nama: String)
        fun hideNama()
        fun showNik(nik: String)
        fun hideNik()
        fun showDaerah(daerah: String)
        fun hideDaerah()
        fun showAlamat(alamat: String)
        fun hideAlamat()
        fun showNoHp(no: String)
        fun hideNohp()
    }
    interface Presenter{
        fun getData(salesId: String)
    }
    interface Repository{
        fun getData(salesId: String)
    }
    interface Listener{
        fun getFotoListener(foto: String)
        fun getEmailListener(email: String)
        fun getNamaListener(nama: String)
        fun getAlamatListener(alamat: String)
        fun getNoHpListener(noHp: String)
        fun getDaerahListener(daerah: String)
        fun getNikListener(nik: String)
    }
}