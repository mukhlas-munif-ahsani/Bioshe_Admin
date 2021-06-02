package com.munifahsan.biosheadmin.ui.tambahReward

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.munifahsan.biosheadmin.databinding.ActivityTambahRewardBinding

class TambahRewardActivity : AppCompatActivity(), TambahRewardContract.View {
    private lateinit var mPres: TambahRewardContract.Presenter
    private lateinit var binding: ActivityTambahRewardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahRewardBinding.inflate(layoutInflater)
        val view = binding.root

        mPres = TambahRewardPresenter(this)

        setContentView(view)
    }

    override fun showMessage(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}