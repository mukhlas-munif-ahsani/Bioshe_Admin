package com.munifahsan.biosheadmin.ui.produk

import android.content.Intent
import android.os.Bundle
import android.transition.Fade
import android.transition.Transition
import android.transition.TransitionManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.munifahsan.biosheadmin.databinding.FragmentProdukBinding
import com.munifahsan.biosheadmin.ui.daftarCustomer.DaftarCustomerFragment
import com.munifahsan.biosheadmin.ui.daftarDistributor.DaftarDistributorFragment
import com.munifahsan.biosheadmin.ui.daftarProduk.DaftarProdukFragment
import com.munifahsan.biosheadmin.ui.daftarPromo.DaftarPromoFragment
import com.munifahsan.biosheadmin.ui.daftarReward.DaftarRewardFragment
import com.munifahsan.biosheadmin.ui.daftarSales.DaftarSalesFragment
import com.munifahsan.biosheadmin.ui.tambahProduk.TambahProdukActivity
import com.munifahsan.biosheadmin.ui.tambahPromo.TambahPromoActivity
import com.munifahsan.biosheadmin.ui.tambahReward.TambahRewardActivity
import com.munifahsan.biosheadmin.utils.CheckConection
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems

class ProdukFragment : Fragment() {

    private var _binding: FragmentProdukBinding? = null
    private val binding get() = _binding!!
    private var pagePosition = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProdukBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.addIcon.setOnClickListener {
            val transition: Transition = Fade()
            transition.duration = 300
            transition.addTarget(binding.addItem)

            TransitionManager.beginDelayedTransition(view, transition)

            binding.addItem.visibility = View.VISIBLE
            binding.addItemBg.visibility = View.VISIBLE
        }

        binding.addItemBg.setOnClickListener {
            val transition: Transition = Fade()
            transition.duration = 300
            transition.addTarget(binding.addItem)

            TransitionManager.beginDelayedTransition(view, transition)

            binding.addItem.visibility = View.GONE
            binding.addItemBg.visibility = View.GONE
        }

        binding.addItem.setOnClickListener {
            if (CheckConection.isNetworkAvailable(activity!!)){
                when (pagePosition) {
                    0 ->{
                        startActivity(Intent(activity, TambahProdukActivity::class.java))
                    }
                    1 ->{
                        startActivity(Intent(activity, TambahPromoActivity::class.java))
                    }
                    2 ->{
                        startActivity(Intent(activity, TambahRewardActivity::class.java))
                    }
                }
            } else {
                showMessage("Mohon periksa koneksi internet anda")
            }
        }

        val adapter = FragmentPagerItemAdapter(
            childFragmentManager, FragmentPagerItems.with(activity)
                .add("PRODUK", DaftarProdukFragment::class.java)
                .add("PROMO", DaftarPromoFragment::class.java)
                .add("REWARD", DaftarRewardFragment::class.java)
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
                when (position) {
                    0 ->{
                        binding.toolbarTitle.text = "Daftar Produk"
                        binding.addItemTitle.text = "Tambah Produk"
                        pagePosition = position
                    }
                    1 ->{
                        binding.toolbarTitle.text = "Daftar Promo"
                        binding.addItemTitle.text = "Tambah Promo"
                        pagePosition = position
                    }
                    2 ->{
                        binding.toolbarTitle.text = "Daftar Reward"
                        binding.addItemTitle.text = "Tambah Reward"
                        pagePosition = position
                    }
                }
                //Toast.makeText(this@KaryawanActivity, "Position : $position", Toast.LENGTH_LONG).show()
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        return view
    }

    private fun showMessage(s: String) {
        Toast.makeText(activity, s, Toast.LENGTH_LONG).show()
    }

    companion object {
        @JvmStatic
        fun newInstance():
                ProdukFragment {
            val fragment = ProdukFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}