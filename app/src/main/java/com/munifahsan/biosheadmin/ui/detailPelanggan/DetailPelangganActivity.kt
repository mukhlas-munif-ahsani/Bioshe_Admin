package com.munifahsan.biosheadmin.ui.detailPelanggan

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.munifahsan.biosheadmin.R
import com.munifahsan.biosheadmin.databinding.ActivityDetailPelangganBinding
import com.munifahsan.biosheadmin.ui.chatRoom.ChatRoomActivity
import com.munifahsan.biosheadmin.ui.detailDistributor.DetailDistributorActivity
import com.munifahsan.biosheadmin.ui.detailSales.DetailSalesActivity
import com.munifahsan.biosheadmin.ui.detailTransaksi.DetailTransaksiContract
import com.munifahsan.biosheadmin.ui.pilihDistributorPelanggan.PilihDistributorPelangganActivity
import com.munifahsan.biosheadmin.utils.Constants
import com.squareup.picasso.Picasso
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class DetailPelangganActivity : AppCompatActivity(), DetailPelangganContract.View {
    private lateinit var binding: ActivityDetailPelangganBinding
    private lateinit var mPres: DetailPelangganContract.Presenter

    private var FRIEND_ID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPelangganBinding.inflate(layoutInflater)
        val view = binding.root

        mPres = DetailPelangganPresenter(this)

        val intent = intent
        val id = intent.getStringExtra("PELANGGAN_ID").toString()
        mPres.getData(id)

        FRIEND_ID = id

        binding.backIcon.setOnClickListener {
            finish()
        }

        setContentView(view)
    }

    override fun showLoyalti(loyalti: String, id: String) {
        when (loyalti) {
            "SILVER" -> {
                changeNotifBarColor(this, R.color.silver)
                binding.relLoyalti.setBackgroundColor(ContextCompat.getColor(this, R.color.silver))
                binding.medalIcon.setImageDrawable(ContextCompat.getDrawable(this,
                    R.drawable.silver_ic))
                binding.relBackIcon.setBackgroundColor(ContextCompat.getColor(this, R.color.silver))
                binding.medalIcon.visibility = View.VISIBLE
            }

            "GOLD" -> {
                changeNotifBarColor(this, R.color.gold)
                binding.relLoyalti.setBackgroundColor(ContextCompat.getColor(this, R.color.gold))
                binding.medalIcon.setImageDrawable(ContextCompat.getDrawable(this,
                    R.drawable.gold_ic))
                binding.relBackIcon.setBackgroundColor(ContextCompat.getColor(this, R.color.gold))
                binding.medalIcon.visibility = View.VISIBLE
            }

            "PLATINUM" -> {
                changeNotifBarColor(this, R.color.platinum)
                binding.relLoyalti.setBackgroundColor(ContextCompat.getColor(this,
                    R.color.platinum))
                binding.medalIcon.setImageDrawable(ContextCompat.getDrawable(this,
                    R.drawable.platinum_ic))
                binding.relBackIcon.setBackgroundColor(ContextCompat.getColor(this,
                    R.color.platinum))
                binding.medalIcon.visibility = View.VISIBLE
            }

            "DIAMOND" -> {
                changeNotifBarColor(this, R.color.diamond)
                binding.relLoyalti.setBackgroundColor(ContextCompat.getColor(this, R.color.diamond))
                binding.medalIcon.setImageDrawable(ContextCompat.getDrawable(this,
                    R.drawable.diamond_ic))
                binding.relBackIcon.setBackgroundColor(ContextCompat.getColor(this,
                    R.color.diamond))
                binding.medalIcon.visibility = View.VISIBLE
            }
        }
    }

    override fun showfoto(fotoUrl: String) {
        Picasso.get()
            .load(fotoUrl)
            .placeholder(R.drawable.img_profile)
            .into(binding.pelangganFoto)
    }

    override fun showBioshePoints(points: String) {
        binding.points.text = points
        binding.points.visibility = View.VISIBLE
    }

    override fun showTotalBelanja(total: Int) {
        binding.belanja.text = rupiahFormat(total)
        binding.belanja.visibility = View.VISIBLE
    }

    override fun showTagihan(tagihan: Int) {
        binding.tagihan.text = rupiahFormat(tagihan)
        binding.tagihan.visibility = View.VISIBLE
    }

    override fun showSales(nama: String, id: String, photoUrl: String, a: String, b: String) {
        binding.namaSales.text = nama
        binding.alamatSales.text = a
        Picasso.get().load(photoUrl).into(binding.fotoSales)
        binding.relShowallSales.setOnClickListener {
            val intent = Intent(this, DetailSalesActivity::class.java)
            intent.putExtra("SALES_ID", id)
            startActivity(intent)
        }
    }

    override fun showDistributor(nama: String, id: String, photoUrl: String, a: String, b: String) {
        binding.namaDistributor.text = nama
        binding.alamatDistributor.text = a
        binding.relCardDistributor.visibility = View.VISIBLE
        binding.pilihDistributorText.visibility = View.INVISIBLE
        Picasso.get().load(photoUrl).into(binding.fotoDistributor)
        binding.relShowall.setOnClickListener {
            val intent = Intent(this, DetailDistributorActivity::class.java)
            intent.putExtra("DISTRIBUTOR_ID", id)
            startActivity(intent)
        }

        binding.relDistributor.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Konfirmasi")
            builder.setMessage("Anda akan mengganti Distributor untuk pelanggan ini.. ")
            builder.setPositiveButton("Ya") { _, _ ->
                val intent = Intent(this, PilihDistributorPelangganActivity::class.java)
                intent.putExtra("PELANGGAN_ID", FRIEND_ID)
                startActivity(intent)
            }
            builder.setNegativeButton("Batal") { dialog, _ ->
                dialog.cancel()
            }
            builder.show()  
        }
    }

    override fun hideDistributor() {
        binding.relCardDistributor.visibility = View.INVISIBLE
        binding.pilihDistributorText.visibility = View.VISIBLE
        binding.relDistributor.setOnClickListener {
            val intent = Intent(this, PilihDistributorPelangganActivity::class.java)
            intent.putExtra("PELANGGAN_ID", FRIEND_ID)
            startActivity(intent)
        }
    }

    override fun showEmail(email: String) {
        binding.pelangganEmail.visibility = View.VISIBLE
        binding.pelangganEmailShimmer.visibility = View.INVISIBLE
        binding.pelangganEmail.text = email
        binding.pelangganEmailBtn.setOnClickListener {

        }
    }

    override fun hideEmail() {
        binding.pelangganEmail.visibility = View.INVISIBLE
        binding.pelangganEmailShimmer.visibility = View.VISIBLE
    }

    override fun showNama(nama: String) {
        binding.pelangganNama.visibility = View.VISIBLE
        binding.pelangganNamaAtas.visibility = View.VISIBLE
        binding.pelangganNamaAtasShimmer.visibility = View.INVISIBLE
        binding.pelangganNamaShimmer.visibility = View.INVISIBLE
        binding.pelangganNama.text = nama
        binding.pelangganNamaAtas.text = nama
        binding.pelangganChatBtn.setOnClickListener {
            val intent = Intent(this, ChatRoomActivity::class.java)
            intent.putExtra("FRIEND_ID", FRIEND_ID)
            startActivity(intent)
        }
    }

    override fun hideNama() {
        binding.pelangganNama.visibility = View.INVISIBLE
        binding.pelangganNamaShimmer.visibility = View.VISIBLE
        binding.pelangganNamaAtas.visibility = View.INVISIBLE
        binding.pelangganNamaAtasShimmer.visibility = View.VISIBLE
    }

    override fun showNik(NIK: String) {
        binding.pelangganNIK.visibility = View.VISIBLE
        binding.pelangganNIKShimmer.visibility = View.INVISIBLE
        binding.pelangganNIK.text = NIK
        binding.pelangganNIKCopyBtn.setOnClickListener {

        }
    }

    override fun hideNik() {
        binding.pelangganNIK.visibility = View.INVISIBLE
        binding.pelangganNIKShimmer.visibility = View.VISIBLE
    }

    override fun showGender(gender: String) {
        binding.pelangganGender.visibility = View.VISIBLE
        binding.pelangganGenderShimmer.visibility = View.INVISIBLE
        binding.pelangganGender.text = gender
    }

    override fun hideGender() {
        binding.pelangganGender.visibility = View.VISIBLE
        binding.pelangganGenderShimmer.visibility = View.INVISIBLE
    }

    override fun showNohp(noHp: String) {
        binding.pelangganNohp.visibility = View.VISIBLE
        binding.pelangganNohpShimmer.visibility = View.INVISIBLE
        binding.pelangganNohp.text = noHp
        binding.pelangganTeleponBtn.setOnClickListener {

        }
    }

    override fun hideNohp() {
        binding.pelangganNohp.visibility = View.INVISIBLE
        binding.pelangganNohpShimmer.visibility = View.VISIBLE
    }

    override fun showNowa(noWa: String) {
        binding.pelangganNowa.visibility = View.VISIBLE
        binding.pelangganNowaShimmer.visibility = View.INVISIBLE
        binding.pelangganNowa.text = noWa
        binding.pelangganWhatsappBtn.setOnClickListener {

        }
    }

    override fun hideNowa() {
        binding.pelangganNowa.visibility = View.INVISIBLE
        binding.pelangganNowaShimmer.visibility = View.VISIBLE
    }

    override fun showAlamatRumah(alamat: String) {
        binding.pelangganAlamatRumah.visibility = View.VISIBLE
        binding.pelangganAlamatRumahShimmer.visibility = View.INVISIBLE
        binding.pelangganAlamatRumah.text = alamat
    }

    override fun hideAlamatRumah() {
        binding.pelangganAlamatRumah.visibility = View.INVISIBLE
        binding.pelangganAlamatRumahShimmer.visibility = View.VISIBLE
    }

    override fun showAhliWaris(ahliWaris: String) {
        binding.pelangganAhliWaris.visibility = View.VISIBLE
        binding.pelangganAhliWarisShimmer.visibility = View.INVISIBLE
        binding.pelangganAhliWaris.text = ahliWaris
    }

    override fun hideAhliWaris() {
        binding.pelangganAhliWaris.visibility = View.VISIBLE
        binding.pelangganAhliWarisShimmer.visibility = View.INVISIBLE
    }

    override fun showNamaOutlet(namaOutlet: String) {
        binding.pelangganNamaOutlet.visibility = View.VISIBLE
        binding.pelangganOutletAtas.visibility = View.VISIBLE
        binding.pelangganOutletAtasShimmer.visibility = View.INVISIBLE
        binding.pelangganNamaOutletShimmer.visibility = View.INVISIBLE
        binding.pelangganNamaOutlet.text = namaOutlet
        binding.pelangganOutletAtas.text = namaOutlet
    }

    override fun hideNamaOutlet() {
        binding.pelangganNamaOutlet.visibility = View.INVISIBLE
        binding.pelangganNamaOutletShimmer.visibility = View.VISIBLE
        binding.pelangganOutletAtas.visibility = View.INVISIBLE
        binding.pelangganOutletAtasShimmer.visibility = View.VISIBLE
    }

    override fun showAlamatOutlet(alamatOutlet: String) {
        binding.pelangganAlamatOutlet.visibility = View.VISIBLE
        binding.pelangganAlamatOutletShimmer.visibility = View.INVISIBLE
        binding.pelangganAlamatOutlet.text = alamatOutlet
    }

    override fun hideAlamatOutlet() {
        binding.pelangganAlamatOutlet.visibility = View.INVISIBLE
        binding.pelangganAlamatOutletShimmer.visibility = View.VISIBLE
    }

    private fun changeNotifBarColor(context: Context, color: Int) {
        /*
        Change status bar color
        */
        val window: Window = window

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(context, color)
    }

    private fun rupiahFormat(number: Int): String {
        val kursIndonesia = DecimalFormat.getCurrencyInstance() as DecimalFormat
        val formatRp = DecimalFormatSymbols()
        formatRp.currencySymbol = ""
        formatRp.monetaryDecimalSeparator = ','
        formatRp.groupingSeparator = '.'
        kursIndonesia.decimalFormatSymbols = formatRp
        val harga = kursIndonesia.format(number).toString()
        return harga.replace(",00", " ")
    }
}