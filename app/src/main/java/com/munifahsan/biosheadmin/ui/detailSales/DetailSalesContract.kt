package com.munifahsan.biosheadmin.ui.detailSales

interface DetailSalesContract {
    interface View{
        fun showfoto(fotoUrl: String)
        fun showEmail(email: String)
        fun hideEmail()
        fun showNama(nama: String)
        fun hideNama()
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
    }
}