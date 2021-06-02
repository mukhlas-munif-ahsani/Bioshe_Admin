package com.munifahsan.biosheadmin.ui.penjualan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.munifahsan.biosheadmin.R
import com.munifahsan.biosheadmin.databinding.FragmentPenjualanBinding
import com.munifahsan.biosheadmin.ui.daftarOrderDibayar.DaftarOrderDibayarFragment
import com.munifahsan.biosheadmin.ui.daftarOrderDikirim.DaftarOrderDikirimFragment
import com.munifahsan.biosheadmin.ui.daftarOrderDiproses.DaftarOrderDiprosesFragment
import com.munifahsan.biosheadmin.ui.daftarOrderPending.DaftarOrderPendingFragment
import com.munifahsan.biosheadmin.ui.daftarOrderSelesai.DaftarOrderSelesaiFragment
import com.munifahsan.biosheadmin.ui.daftarProduk.DaftarProdukFragment
import com.munifahsan.biosheadmin.ui.daftarPromo.DaftarPromoFragment
import com.munifahsan.biosheadmin.ui.daftarReward.DaftarRewardFragment
import com.munifahsan.biosheadmin.ui.home.HomeFragment
import com.munifahsan.biosheadmin.ui.produk.ProdukFragment
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems

class PenjualanFragment : Fragment() {

    private var _binding: FragmentPenjualanBinding? = null
    private val binding get() = _binding!!
    private var pagePosition = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPenjualanBinding.inflate(inflater, container, false)
        val view = binding.root


        val adapter = FragmentPagerItemAdapter(
            childFragmentManager, FragmentPagerItems.with(activity)
                .add("PENDING", DaftarOrderPendingFragment::class.java)
                .add("DIBAYAR", DaftarOrderDibayarFragment::class.java)
                .add("DIPROSES", DaftarOrderDiprosesFragment::class.java)
                .add("DIKIRIM", DaftarOrderDikirimFragment::class.java)
                .add("SELESAI", DaftarOrderSelesaiFragment::class.java)
                .create()
        )

        binding.viewPager.adapter = adapter

        binding.viewPagerTab.setViewPager(binding.viewPager)

        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
//                when (position) {
//                    0 ->{
//                        binding.toolbarTitle.text = "Daftar Produk"
//                        binding.addItemTitle.text = "Tambah Produk"
//                        pagePosition = position
//                    }
//                    1 ->{
//                        binding.toolbarTitle.text = "Daftar Promo"
//                        binding.addItemTitle.text = "Tambah Promo"
//                        pagePosition = position
//                    }
//                    2 ->{
//                        binding.toolbarTitle.text = "Daftar Reward"
//                        binding.addItemTitle.text = "Tambah Reward"
//                        pagePosition = position
//                    }
//                }
                //Toast.makeText(this@KaryawanActivity, "Position : $position", Toast.LENGTH_LONG).show()
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })


        return view
    }

    companion object {
        @JvmStatic
        fun newInstance():
                PenjualanFragment {
            val fragment = PenjualanFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}