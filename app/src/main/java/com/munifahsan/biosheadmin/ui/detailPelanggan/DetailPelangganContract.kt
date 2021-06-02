package com.munifahsan.biosheadmin.ui.detailPelanggan

interface DetailPelangganContract {
    interface View{
        fun showfoto(fotoUrl: String)
        fun showEmail(email: String)
        fun showNama(nama: String)
        fun hideEmail()
        fun hideNama()
        fun showNik(NIK: String)
        fun hideNik()
        fun showGender(gender: String)
        fun hideGender()
        fun showNohp(noHp: String)
        fun showNowa(noWa: String)
        fun hideNohp()
        fun showAlamatRumah(alamat: String)
        fun hideNowa()
        fun hideAlamatRumah()
        fun showAhliWaris(ahliWaris: String)
        fun hideAhliWaris()
        fun showNamaOutlet(namaOutlet: String)
        fun hideNamaOutlet()
        fun showAlamatOutlet(alamatOutlet: String)
        fun hideAlamatOutlet()
    }
    interface Presenter{

        fun getData(pelangganId: String)
    }
    interface Repository{

        fun getData(pelangganId: String)
    }
    interface Listener{

        fun getEmailListener(email: String)
        fun getNamaListener(nama: String)
        fun getFotoListener(foto: String)
        fun getNikListener(nik: String)
        fun getGenderListener(gender: String)
        fun getNohpListener(noHp: String)
        fun getNowaListener(noWa: String)
        fun getAlamatRumahListener(alamat: String)
        fun getAhliWarisListener(ahli: String)
        fun getNamaOutletListener(nama: String)
        fun getAlamatOutletListener(alamat: String)
    }
}
