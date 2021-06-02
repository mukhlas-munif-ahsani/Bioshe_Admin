package com.munifahsan.biosheadmin.ui.editReward

class EditRewardPresenter(val mView: EditRewardContract.View): EditRewardContract.Presenter, EditRewardContract.Listener {
    private val mRepo: EditRewardContract.Repository
    init {
        mRepo = EditRewardRepository(this)
    }
}