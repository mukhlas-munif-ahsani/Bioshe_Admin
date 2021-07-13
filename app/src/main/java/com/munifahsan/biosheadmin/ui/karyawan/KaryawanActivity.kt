package com.munifahsan.biosheadmin.ui.karyawan

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.munifahsan.biosheadmin.databinding.ActivityKaryawanBinding
import com.munifahsan.biosheadmin.ui.daftarCustomer.DaftarCustomerFragment
import com.munifahsan.biosheadmin.ui.daftarDistributor.DaftarDistributorFragment
import com.munifahsan.biosheadmin.ui.daftarSales.DaftarSalesFragment
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems

class KaryawanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKaryawanBinding

    val adapter = FragmentPagerItemAdapter(
        supportFragmentManager, FragmentPagerItems.with(this)
            .add("SALES", DaftarSalesFragment::class.java)
            .add("DISTRIBUTOR", DaftarDistributorFragment::class.java)
            .add("CUSTOMER", DaftarCustomerFragment::class.java)
            .create()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKaryawanBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.viewPager.adapter = adapter
        binding.viewPagerTab.setViewPager(binding.viewPager)

        binding.viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int) {
                when (position){
                    0 -> binding.toolbarTitle.text = "Daftar Sales"
                    1 -> binding.toolbarTitle.text = "Daftar Distributor"
                    2 -> binding.toolbarTitle.text = "Daftar Customer"
                }
                //Toast.makeText(this@KaryawanActivity, "Position : $position", Toast.LENGTH_LONG).show()
            }
            override fun onPageScrollStateChanged(state: Int) {}
        })

        binding.backIcon.setOnClickListener {
            finish()
        }
    }
}