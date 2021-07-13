package com.munifahsan.biosheadmin.ui.biodataDistributor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.munifahsan.biosheadmin.MainActivity
import com.munifahsan.biosheadmin.databinding.FragmentBiodataDistributorBinding
import com.munifahsan.biosheadmin.ui.detailDistributor.DetailDistributorActivity
import com.munifahsan.biosheadmin.ui.detailDistributor.DetailDistributorContract
import com.munifahsan.biosheadmin.ui.detailDistributor.DetailDistributorPresenter

class BiodataDistributorFragment : Fragment(), DetailDistributorContract.View {

    private var _binding: FragmentBiodataDistributorBinding? = null
    private val binding get() = _binding!!
    private lateinit var mPres: DetailDistributorContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentBiodataDistributorBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        mPres = DetailDistributorPresenter(this)
        val distributorId =  (activity as DetailDistributorActivity?)!!.getDistributorId()

        mPres.getData(distributorId)

        return view
    }

    override fun showfoto(fotoUrl: String) {
//        TODO("Not yet implemented")
    }

    override fun showEmail(email: String) {
        binding.email.visibility = View.VISIBLE
        binding.emailShimmer.visibility = View.INVISIBLE
        binding.email.text = email
        binding.emailBtn.setOnClickListener {

        }
    }

    override fun hideEmail() {
        binding.email.visibility = View.INVISIBLE
        binding.emailShimmer.visibility = View.VISIBLE
    }

    override fun showNama(nama: String) {
        binding.nama.visibility = View.VISIBLE
        binding.namaShimmer.visibility = View.INVISIBLE
        binding.nama.text = nama

        binding.chatBtn.setOnClickListener {

        }
    }

    override fun hideNama() {
        binding.nama.visibility = View.INVISIBLE
        binding.namaShimmer.visibility = View.VISIBLE
    }

    override fun showNik(nik: String){
        binding.nik.visibility = View.VISIBLE
        binding.nikShimmer.visibility = View.INVISIBLE
        binding.nik.text = nik
    }

    override fun hideNik(){
        binding.nik.visibility = View.INVISIBLE
        binding.nikShimmer.visibility = View.VISIBLE
    }

    override fun showDaerah(daerah: String){
        binding.daerah.visibility = View.VISIBLE
        binding.daerahShimmer.visibility = View.INVISIBLE
        binding.daerah.text = daerah
    }

    override fun hideDaerah(){
        binding.daerah.visibility = View.INVISIBLE
        binding.daerahShimmer.visibility = View.VISIBLE
    }

    override fun showAlamat(alamat: String){
        binding.alamat.visibility = View.VISIBLE
        binding.alamatShimmer.visibility = View.INVISIBLE
        binding.alamat.text = alamat
    }

    override fun hideAlamat(){
        binding.alamat.visibility = View.INVISIBLE
        binding.alamatShimmer.visibility = View.VISIBLE
    }

    override fun showNoHp(no: String){
        binding.noHp.visibility = View.VISIBLE
        binding.noHpShimmer.visibility = View.INVISIBLE
        binding.noHp.text = no
    }

    override fun hideNohp(){
        binding.noHp.visibility = View.INVISIBLE
        binding.noHpShimmer.visibility = View.VISIBLE
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BiodataDistributorFragment().apply {

            }
    }
}