package com.munifahsan.biosheadmin.ui.daftarDistributor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.munifahsan.biosheadmin.R
import com.munifahsan.biosheadmin.databinding.FragmentDaftarDistributorBinding

class DaftarDistributorFragment : Fragment() {

    private var _binding: FragmentDaftarDistributorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDaftarDistributorBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        return view
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DaftarDistributorFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}