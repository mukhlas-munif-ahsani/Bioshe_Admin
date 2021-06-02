package com.munifahsan.biosheadmin.ui.detailSales

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.munifahsan.biosheadmin.R
import com.munifahsan.biosheadmin.databinding.ActivityDetailSalesBinding
import com.squareup.picasso.Picasso

class DetailSalesActivity : AppCompatActivity(),DetailSalesContract.View {
    private lateinit var binding: ActivityDetailSalesBinding
    private lateinit var mPres: DetailSalesContract.Presenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailSalesBinding.inflate(layoutInflater)
        val view = binding.root

        mPres = DetailSalesPresenter(this)

        val intent = intent
        val orderId = intent.getStringExtra("SALES_ID").toString()
        mPres.getData(orderId)

        setContentView(view)
    }

    override fun showfoto(fotoUrl: String){
        Picasso.get()
            .load(fotoUrl)
            .placeholder(R.drawable.img_profile)
            .into(binding.foto)
    }

    override fun showEmail(email: String){
        binding.email.visibility = View.VISIBLE
        binding.emailShimmer.visibility = View.INVISIBLE
        binding.email.text = email
        binding.emailBtn.setOnClickListener {

        }
    }

    override fun hideEmail(){
        binding.email.visibility = View.INVISIBLE
        binding.emailShimmer.visibility = View.VISIBLE
    }

    override fun showNama(nama: String){
        binding.nama.visibility = View.VISIBLE
        binding.namaShimmer.visibility = View.INVISIBLE
        binding.nama.text = nama
        binding.chatBtn.setOnClickListener {

        }
    }

    override fun hideNama(){
        binding.nama.visibility = View.INVISIBLE
        binding.namaShimmer.visibility = View.VISIBLE
    }
}