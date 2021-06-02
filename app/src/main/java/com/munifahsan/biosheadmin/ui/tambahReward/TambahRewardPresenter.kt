package com.munifahsan.biosheadmin.ui.tambahReward

class TambahRewardPresenter(val mView: TambahRewardContract.View): TambahRewardContract.Listener, TambahRewardContract.Presenter {
    val mRepo: TambahRewardContract.Repository
    init {
        mRepo = TambahRewardRepository(this)
    }
}