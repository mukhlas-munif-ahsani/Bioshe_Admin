package com.munifahsan.biosheadmin.ui.detailDistributor

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.munifahsan.biosheadmin.R
import com.munifahsan.biosheadmin.databinding.ActivityDetailDistributorBinding
import com.munifahsan.biosheadmin.ui.biodataDistributor.BiodataDistributorFragment
import com.munifahsan.biosheadmin.ui.daftarPermintaanDistributor.DaftarPermintaanDistributorFragment
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import com.squareup.picasso.Picasso


class DetailDistributorActivity : AppCompatActivity(), DetailDistributorContract.View {
    private lateinit var binding: ActivityDetailDistributorBinding
    private lateinit var mPres: DetailDistributorContract.Presenter
    var distributorId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailDistributorBinding.inflate(layoutInflater)
        val view = binding.root

        mPres = DetailDistributorPresenter(this)

        val intent = intent
        distributorId = intent.getStringExtra("DISTRIBUTOR_ID").toString()
        mPres.getData(distributorId)

        binding.backIcon.setOnClickListener {
            finish()
        }

        val adapter = FragmentPagerItemAdapter(
            supportFragmentManager, FragmentPagerItems.with(this)
                .add("BIODATA", BiodataDistributorFragment::class.java)
                .add("PERMINTAAN", DaftarPermintaanDistributorFragment::class.java)
                .create()
        )

        binding.viewPager.adapter = adapter
        binding.viewPagerTab.setViewPager(binding.viewPager)

        setContentView(view)
    }

    @JvmName("getDistributorId1")
    public fun getDistributorId(): String {
        return distributorId
    }

    override fun showfoto(fotoUrl: String) {
        Picasso.get()
            .load(fotoUrl)
            .placeholder(R.drawable.img_profile)
            .into(binding.foto)
    }

    override fun showEmail(email: String) {
    }

    override fun hideEmail() {
    }

    override fun showNama(nama: String) {
        binding.distributorNamaAtas.visibility = View.VISIBLE
        binding.distributorNamaAtasShimmer.visibility = View.INVISIBLE
        binding.distributorNamaAtas.text = nama
    }

    override fun hideNama() {
        binding.distributorNamaAtas.visibility = View.INVISIBLE
        binding.distributorNamaAtasShimmer.visibility = View.VISIBLE
    }

    override fun showNik(nik: String) {

    }

    override fun hideNik() {

    }

    override fun showDaerah(daerah: String) {

    }

    override fun hideDaerah() {

    }

    override fun showAlamat(alamat: String) {

    }

    override fun hideAlamat() {

    }

    override fun showNoHp(no: String) {

    }

    override fun hideNohp() {

    }
}