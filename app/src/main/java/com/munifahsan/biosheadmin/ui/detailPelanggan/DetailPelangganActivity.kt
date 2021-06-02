package com.munifahsan.biosheadmin.ui.detailPelanggan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.munifahsan.biosheadmin.R
import com.munifahsan.biosheadmin.databinding.ActivityDetailPelangganBinding
import com.munifahsan.biosheadmin.ui.detailTransaksi.DetailTransaksiContract
import com.squareup.picasso.Picasso

class DetailPelangganActivity : AppCompatActivity(), DetailPelangganContract.View {
    private lateinit var binding: ActivityDetailPelangganBinding
    private lateinit var mPres: DetailPelangganContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPelangganBinding.inflate(layoutInflater)
        val view = binding.root

        mPres = DetailPelangganPresenter(this)

        val intent = intent
        val orderId = intent.getStringExtra("PELANGGAN_ID").toString()
        mPres.getData(orderId)

        binding.backIcon.setOnClickListener {
            finish()
        }

        setContentView(view)
    }

    override fun showfoto(fotoUrl: String){
        Picasso.get()
            .load(fotoUrl)
            .placeholder(R.drawable.img_profile)
            .into(binding.pelangganFoto)
    }

    override fun showEmail(email: String){
        binding.pelangganEmail.visibility = View.VISIBLE
        binding.pelangganEmailShimmer.visibility = View.INVISIBLE
        binding.pelangganEmail.text = email
        binding.pelangganEmailBtn.setOnClickListener {

        }
    }

    override fun hideEmail(){
        binding.pelangganEmail.visibility = View.INVISIBLE
        binding.pelangganEmailShimmer.visibility = View.VISIBLE
    }

    override fun showNama(nama: String){
        binding.pelangganNama.visibility = View.VISIBLE
        binding.pelangganNamaShimmer.visibility = View.INVISIBLE
        binding.pelangganNama.text = nama
        binding.pelangganChatBtn.setOnClickListener {

        }
    }

    override fun hideNama(){
        binding.pelangganNama.visibility = View.INVISIBLE
        binding.pelangganNamaShimmer.visibility = View.VISIBLE
    }

    override fun showNik(NIK: String){
        binding.pelangganNIK.visibility = View.VISIBLE
        binding.pelangganNIKShimmer.visibility = View.INVISIBLE
        binding.pelangganNIK.text = NIK
        binding.pelangganNIKCopyBtn.setOnClickListener {

        }
    }

    override fun hideNik(){
        binding.pelangganNIK.visibility = View.INVISIBLE
        binding.pelangganNIKShimmer.visibility = View.VISIBLE
    }

    override fun showGender(gender: String){
        binding.pelangganGender.visibility = View.VISIBLE
        binding.pelangganGenderShimmer.visibility = View.INVISIBLE
        binding.pelangganGender.text = gender
    }

    override fun hideGender(){
        binding.pelangganGender.visibility = View.VISIBLE
        binding.pelangganGenderShimmer.visibility = View.INVISIBLE
    }

    override fun showNohp(noHp: String){
        binding.pelangganNohp.visibility = View.VISIBLE
        binding.pelangganNohpShimmer.visibility = View.INVISIBLE
        binding.pelangganNohp.text = noHp
        binding.pelangganTeleponBtn.setOnClickListener {

        }
    }

    override fun hideNohp(){
        binding.pelangganNohp.visibility = View.INVISIBLE
        binding.pelangganNohpShimmer.visibility = View.VISIBLE
    }

    override fun showNowa(noWa: String){
        binding.pelangganNowa.visibility = View.VISIBLE
        binding.pelangganNowaShimmer.visibility = View.INVISIBLE
        binding.pelangganNowa.text = noWa
        binding.pelangganWhatsappBtn.setOnClickListener {

        }
    }

    override fun hideNowa(){
        binding.pelangganNowa.visibility = View.INVISIBLE
        binding.pelangganNowaShimmer.visibility = View.VISIBLE
    }

    override fun showAlamatRumah(alamat: String){
        binding.pelangganAlamatRumah.visibility = View.VISIBLE
        binding.pelangganAlamatRumahShimmer.visibility = View.INVISIBLE
        binding.pelangganAlamatRumah.text = alamat
    }

    override fun hideAlamatRumah(){
        binding.pelangganAlamatRumah.visibility = View.INVISIBLE
        binding.pelangganAlamatRumahShimmer.visibility = View.VISIBLE
    }

    override fun showAhliWaris(ahliWaris: String){
        binding.pelangganAhliWaris.visibility = View.VISIBLE
        binding.pelangganAhliWarisShimmer.visibility = View.INVISIBLE
        binding.pelangganAhliWaris.text = ahliWaris
    }

    override fun hideAhliWaris(){
        binding.pelangganAhliWaris.visibility = View.VISIBLE
        binding.pelangganAhliWarisShimmer.visibility = View.INVISIBLE
    }

    override fun showNamaOutlet(namaOutlet: String){
        binding.pelangganNamaOutlet.visibility = View.VISIBLE
        binding.pelangganNamaOutletShimmer.visibility = View.INVISIBLE
        binding.pelangganNamaOutlet.text = namaOutlet
    }

    override fun hideNamaOutlet(){
        binding.pelangganNamaOutlet.visibility = View.INVISIBLE
        binding.pelangganNamaOutletShimmer.visibility = View.VISIBLE
    }

    override fun showAlamatOutlet(alamatOutlet: String){
        binding.pelangganAlamatOutlet.visibility = View.VISIBLE
        binding.pelangganAlamatOutletShimmer.visibility = View.INVISIBLE
        binding.pelangganAlamatOutlet.text = alamatOutlet
    }

    override fun hideAlamatOutlet(){
        binding.pelangganAlamatOutlet.visibility = View.INVISIBLE
        binding.pelangganAlamatOutletShimmer.visibility = View.VISIBLE
    }
}