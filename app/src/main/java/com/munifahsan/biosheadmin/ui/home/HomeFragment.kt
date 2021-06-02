package com.munifahsan.biosheadmin.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.munifahsan.biosheadmin.R
import com.munifahsan.biosheadmin.databinding.FragmentHomeBinding
import com.munifahsan.biosheadmin.ui.karyawan.KaryawanActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
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