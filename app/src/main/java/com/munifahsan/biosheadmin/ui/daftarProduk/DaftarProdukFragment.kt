package com.munifahsan.biosheadmin.ui.daftarProduk

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.munifahsan.biosheadmin.R
import com.munifahsan.biosheadmin.databinding.FragmentDaftarProdukBinding
import com.munifahsan.biosheadmin.models.Produk
import com.munifahsan.biosheadmin.ui.editProduk.EditProdukActivity
import com.munifahsan.biosheadmin.utils.CheckConection
import com.munifahsan.biosheadmin.utils.Constants
import com.squareup.picasso.Picasso
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class DaftarProdukFragment : Fragment() {

    private var _binding : FragmentDaftarProdukBinding? = null
    private val binding get() = _binding!!

    private var adapter: DaftarProdukFragment.ProductFirestoreRecyclerAdapter? = null

    var produkId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDaftarProdukBinding.inflate(inflater, container, false)
        val view = binding.root

        showItem()

        return view
    }

    override fun onStart() {
        super.onStart()
        adapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter!!.stopListening()
    }

    private fun showItem() {
        binding.rvProduk.layoutManager = LinearLayoutManager(
            activity
        )
        val rootRef = FirebaseFirestore.getInstance()
        val query = rootRef.collection("PRODUCT").whereEqualTo("selesai", true)
        val options = FirestoreRecyclerOptions
            .Builder<Produk>()
            .setQuery(query, Produk::class.java)
            .build()
        adapter = ProductFirestoreRecyclerAdapter(options)
        binding.rvProduk.adapter = adapter
    }

    private inner class ProductViewHolder(private val view: View) :
        RecyclerView.ViewHolder(
            view
        ) {

        fun setProduct(
            itemId: String,
            promoId: String,
            namaProduct: String, thumbnailProduct: String,
            hargaProduct: Int, disconProduct: Int, show: Boolean, stok: Int
        ) {
            val productName = view.findViewById<TextView>(R.id.productName)
            val priceDiscon = view.findViewById<TextView>(R.id.priceDisconTxt)
            val disconTxt = view.findViewById<TextView>(R.id.disconTxt)
            val priceProduct = view.findViewById<TextView>(R.id.priceProduct)
            val linDiscon = view.findViewById<LinearLayout>(R.id.linDiscon)
            val image = view.findViewById<ImageView>(R.id.thumbnailImage)

            if (thumbnailProduct != ""){
                Picasso.get()
                    .load(thumbnailProduct)
                    .placeholder(R.drawable.black_transparent)
                    .into(image)
            }

            productName.text = namaProduct
            disconTxt.text = disconProduct.toString()
            priceDiscon.text = rupiahFormat(hargaProduct)
            priceDiscon.paintFlags = priceDiscon.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            if (disconProduct == 0) {
                linDiscon.visibility = View.INVISIBLE
            } else {
                linDiscon.visibility = View.VISIBLE
            }

            val disconNum: Int = disconProduct
            val disconHarga: Int = hargaProduct * disconNum / 100
            val harga = hargaProduct - disconHarga
            priceProduct.text = rupiahFormat(harga)

            if (show){
                view.findViewById<CardView>(R.id.statusShowCard).visibility = View.VISIBLE
                view.findViewById<CardView>(R.id.statusShowCard).setCardBackgroundColor(Color.parseColor("#D7EDFE"))
                view.findViewById<TextView>(R.id.textStatus).setTextColor(Color.parseColor("#118EEA"))
                view.findViewById<TextView>(R.id.textStatus).text = "Aktif"
            } else {
                view.findViewById<CardView>(R.id.statusShowCard).visibility = View.VISIBLE
                view.findViewById<CardView>(R.id.statusShowCard).setCardBackgroundColor(Color.parseColor("#B8CCDC"))
                view.findViewById<TextView>(R.id.textStatus).setTextColor(Color.parseColor("#FF000000"))
                view.findViewById<TextView>(R.id.textStatus).text = "Nonaktif"
            }

            view.findViewById<CardView>(R.id.produkCard).setOnClickListener {
                if (CheckConection.isNetworkAvailable(activity!!)){
                    val inent = Intent(activity, EditProdukActivity::class.java)
                    inent.putExtra("PRODUK_ID", itemId)
                    startActivity(inent)
                } else {
                    showMessage("Mohon periksa koneksi internet anda")
                }
            }

            view.findViewById<TextView>(R.id.stokProduct).text = "Stok : $stok"

            view.findViewById<CardView>(R.id.removeItem).setOnClickListener {
                showDeleteDialog(itemId)
            }

//            if (promoId != ""){
//                Constants.PROMO_DB.document(promoId).get().addOnSuccessListener {
//                    if (it.exists()){
//                        Constants.PROMO_DB.document(promoId).addSnapshotListener { value, error ->
//                            val diskon = value!!.getLong("diskon")!!.toInt()
//                            Constants.PRODUCT_DB.document(produkId).update("diskon", diskon)
//                        }
//                    }
//                }
//            }
        }

    }

    private inner class ProductFirestoreRecyclerAdapter(options: FirestoreRecyclerOptions<Produk>) :
        FirestoreRecyclerAdapter<Produk, ProductViewHolder>(
            options
        ) {
        override fun onBindViewHolder(
            productViewHolder: ProductViewHolder,
            position: Int,
            model: Produk
        ) {
            productViewHolder.setProduct(
                model.id,
                model.promoId,
                model.nama,
                model.thumbnail,
                model.harga,
                model.diskon,
                model.show,
                model.stok
            )

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_produk,
                parent,
                false
            )
            return ProductViewHolder(view)
        }
    }

    private fun showDeleteDialog(produkId: String){
        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle("Hapus produk")
        builder.setMessage("Yakin ingin menghapus produk ini ?")
        builder.setPositiveButton("Ya") { _, _ ->
           Constants.PRODUCT_DB.document(produkId).delete().addOnSuccessListener {
               showMessage("Produk berhasil dihapus")
           }
        }
        builder.setNegativeButton("Tidak") { dialog, _ ->
            dialog.cancel()
        }
        builder.show()
    }

    private fun rupiahFormat(number: Int): String {
        val kursIndonesia = DecimalFormat.getCurrencyInstance() as DecimalFormat
        val formatRp = DecimalFormatSymbols()
        formatRp.currencySymbol = "Rp "
        formatRp.monetaryDecimalSeparator = ','
        formatRp.groupingSeparator = '.'
        kursIndonesia.decimalFormatSymbols = formatRp
        val harga = kursIndonesia.format(number).toString()
        return harga.replace(",00", " ")
    }

    private fun showMessage(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DaftarProdukFragment().apply {

            }
    }
}