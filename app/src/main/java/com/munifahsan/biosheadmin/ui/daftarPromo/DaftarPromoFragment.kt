package com.munifahsan.biosheadmin.ui.daftarPromo

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.firestore.FirebaseFirestore
import com.munifahsan.biosheadmin.R
import com.munifahsan.biosheadmin.databinding.FragmentDaftarProdukBinding
import com.munifahsan.biosheadmin.databinding.FragmentDaftarPromoBinding
import com.munifahsan.biosheadmin.models.Promo
import com.munifahsan.biosheadmin.ui.daftarProduk.DaftarProdukFragment
import com.munifahsan.biosheadmin.ui.editPromo.EditPromoActivity
import com.munifahsan.biosheadmin.utils.Constants
import com.squareup.picasso.Picasso

class DaftarPromoFragment : Fragment() {
    private var _binding: FragmentDaftarPromoBinding? = null
    private val binding get() = _binding!!
    private var adapterPromo: PromoFirestoreRecyclerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDaftarPromoBinding.inflate(inflater, container, false)
        val view = binding.root
        // Inflate the layout for this fragment
        showPromoItem()
        return view
    }

    override fun onStart() {
        super.onStart()
        adapterPromo!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapterPromo!!.stopListening()
    }

    private fun showPromoItem() {
        binding.rvPromo.layoutManager = LinearLayoutManager(activity)
        val rootRef = FirebaseFirestore.getInstance()
        val query = rootRef.collection("PROMO").whereEqualTo("selesai", true)
        val options = FirestoreRecyclerOptions
            .Builder<Promo>()
            .setQuery(query, Promo::class.java)
            .build()
        adapterPromo = PromoFirestoreRecyclerAdapter(options)
        binding.rvPromo.adapter = adapterPromo
    }

    private inner class PromoViewHolder(private val view: View) :
        RecyclerView.ViewHolder(
            view
        ) {

        fun setImage(
            id: String,
            image: String,
            diskon: Int,
            show: Boolean
        ) {
            if (image != ""){
                Picasso.get().load(image).into(view.findViewById<ImageView>(R.id.imageView_promo))
            }
//            if (nomor == 0) {
//                view.findViewById<CardView>(R.id.utama).visibility = View.VISIBLE
//            } else {
//                view.findViewById<CardView>(R.id.utama).visibility = View.INVISIBLE
//            }

            view.findViewById<CardView>(R.id.cardPromo).setOnClickListener {
                val intent = Intent(activity, EditPromoActivity::class.java)
                intent.putExtra("PROMO_ID", id)
                startActivity(intent)
            }

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

            view.findViewById<CardView>(R.id.removeItem).setOnClickListener {
                showDeleteDialog(id)
            }

//            view.findViewById<CardView>(R.id.removeItem).setOnClickListener {
//                if (nomor == 0) {
//                    Constants.PRODUK_DRAFT_DB.update("thumbnail", "")
//                    Constants.PRODUK_DRAFT_DB.collection("IMAGES").document(id)
//                        .delete()
//                } else {
//                    Constants.PRODUK_DRAFT_DB.collection("IMAGES").document(id)
//                        .delete()
//                }
//            }
        }

    }

    private inner class PromoFirestoreRecyclerAdapter(options: FirestoreRecyclerOptions<Promo>) :
        FirestoreRecyclerAdapter<Promo, PromoViewHolder>(
            options
        ) {
        override fun onBindViewHolder(
            productViewHolder: PromoViewHolder,
            position: Int,
            model: Promo,
        ) {
            productViewHolder.setImage(
                model.id,
                model.imageThumbnail,
                model.diskon,
                model.show
            )

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromoViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_banner_promo,
                    parent,
                    false
                )
            return PromoViewHolder(view)
        }
    }

    private fun showDeleteDialog(promoId: String){
        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle("Hapus promo")
        builder.setMessage("Yakin ingin menghapus promo ini ?")
        builder.setPositiveButton("Ya") { _, _ ->
            Constants.PRODUCT_DB.whereEqualTo("promoId", promoId).get().addOnSuccessListener {
                for (field in it){
                    val produkId = field.id
                    Constants.PRODUCT_DB.document(produkId).update("promoId", "", "diskon", 0)
                }
                Constants.PROMO_DB.document(promoId).delete().addOnSuccessListener {
                    showMessage("Promo berhasil dihapus")
                }
            }
        }
        builder.setNegativeButton("Tidak") { dialog, _ ->
            dialog.cancel()
        }
        builder.show()
    }

    private fun showMessage(s: String) {
        Toast.makeText(requireActivity(), s, Toast.LENGTH_LONG).show()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DaftarPromoFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}