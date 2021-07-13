package com.munifahsan.biosheadmin.ui.pageMitra

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.munifahsan.biosheadmin.R
import com.munifahsan.biosheadmin.databinding.FragmentMitraBinding
import com.munifahsan.biosheadmin.ui.daftarCustomer.DaftarCustomerFragment
import com.munifahsan.biosheadmin.ui.daftarDistributor.DaftarDistributorFragment
import com.munifahsan.biosheadmin.ui.daftarOrderDibayar.DaftarOrderDibayarFragment
import com.munifahsan.biosheadmin.ui.daftarOrderDikirim.DaftarOrderDikirimFragment
import com.munifahsan.biosheadmin.ui.daftarOrderDiproses.DaftarOrderDiprosesFragment
import com.munifahsan.biosheadmin.ui.daftarOrderPending.DaftarOrderPendingFragment
import com.munifahsan.biosheadmin.ui.daftarOrderSelesai.DaftarOrderSelesaiFragment
import com.munifahsan.biosheadmin.ui.daftarSales.DaftarSalesFragment
import com.munifahsan.biosheadmin.ui.pagePenjualan.PenjualanFragment
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems


class MitraFragment : Fragment() {

    private var _binding: FragmentMitraBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMitraBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        val adapter = FragmentPagerItemAdapter(
            childFragmentManager, FragmentPagerItems.with(activity)
                .add("SALES", DaftarSalesFragment::class.java)
                .add("DISTRIBUTOR", DaftarDistributorFragment::class.java)
                .add("CUSTOMER", DaftarCustomerFragment::class.java)
                .create()
        )

        binding.viewPager.adapter = adapter
        binding.viewPagerTab.setViewPager(binding.viewPager)

        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
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

        // Inflate the layout for this fragment
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance():
                MitraFragment {
            val fragment = MitraFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}