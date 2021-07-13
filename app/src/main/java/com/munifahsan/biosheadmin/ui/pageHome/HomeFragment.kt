package com.munifahsan.biosheadmin.ui.pageHome

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import com.munifahsan.biosheadmin.databinding.FragmentHomeBinding
import com.munifahsan.biosheadmin.ui.karyawan.KaryawanActivity


class HomeFragment : Fragment(){

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    lateinit var mAnimator: ValueAnimator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.karyawanBtn.setOnClickListener {
            val intent = Intent(activity, KaryawanActivity::class.java)
            startActivity(intent)
        }
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(): HomeFragment {
                val fragment = HomeFragment()
                val args = Bundle()
                fragment.arguments = args
                return fragment
            }
    }
}