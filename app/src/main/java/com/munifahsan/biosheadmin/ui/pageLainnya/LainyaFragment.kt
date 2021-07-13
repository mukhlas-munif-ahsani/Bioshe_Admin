package com.munifahsan.biosheadmin.ui.pageLainnya

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.munifahsan.biosheadmin.R

class LainyaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lainya, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance():
            LainyaFragment {
                val fragment = LainyaFragment()
                val args = Bundle()
                fragment.arguments = args
                return fragment
            }
    }
}