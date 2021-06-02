package com.munifahsan.biosheadmin.ui.editReward

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.munifahsan.biosheadmin.databinding.ActivityEditRewardBinding

class EditRewardActivity : AppCompatActivity(), EditRewardContract.View {
    private lateinit var mPres: EditRewardContract.Presenter
    private lateinit var binding: ActivityEditRewardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditRewardBinding.inflate(layoutInflater)
        val view = binding.root

        mPres = EditRewardPresenter(this)

        setContentView(view)
    }
}