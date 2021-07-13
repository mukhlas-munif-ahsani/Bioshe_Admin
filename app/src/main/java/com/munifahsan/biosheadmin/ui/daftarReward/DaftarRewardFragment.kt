package com.munifahsan.biosheadmin.ui.daftarReward

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.munifahsan.biosheadmin.R
import com.munifahsan.biosheadmin.databinding.FragmentDaftarProdukBinding
import com.munifahsan.biosheadmin.databinding.FragmentDaftarRewardBinding
import com.munifahsan.biosheadmin.models.Produk
import com.munifahsan.biosheadmin.models.Reward
import com.munifahsan.biosheadmin.ui.daftarProduk.DaftarProdukFragment
import com.munifahsan.biosheadmin.ui.editProduk.EditProdukActivity
import com.munifahsan.biosheadmin.ui.editReward.EditRewardActivity
import com.munifahsan.biosheadmin.utils.CheckConection
import com.munifahsan.biosheadmin.utils.Constants
import com.squareup.picasso.Picasso
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols


class DaftarRewardFragment : Fragment() {

    private var _binding : FragmentDaftarRewardBinding? = null
    private val binding get() = _binding!!

    private var adapter: RewardFirestoreRecyclerAdapter? = null

    var produkId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDaftarRewardBinding.inflate(inflater, container, false)
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
        binding.rvReward.layoutManager = LinearLayoutManager(
            activity
        )
        val rootRef = FirebaseFirestore.getInstance()
        val query = rootRef.collection("REWARD")
        val options = FirestoreRecyclerOptions
            .Builder<Reward>()
            .setQuery(query, Reward::class.java)
            .build()
        adapter = RewardFirestoreRecyclerAdapter(options)
        binding.rvReward.adapter = adapter
    }

    private inner class RewardViewHolder(private val view: View) :
        RecyclerView.ViewHolder(
            view
        ) {

        fun setProduct(
            itemId: String,
            points: Int,
            namaReward: String, thumbnailReward: String,
            show: Boolean, stok: Int
        ) {
            val rewardName = view.findViewById<TextView>(R.id.namaReward)
            val rewardPoint = view.findViewById<TextView>(R.id.pointReward)
            val image = view.findViewById<ImageView>(R.id.thumbnailImage)

            if (thumbnailReward != ""){
                Picasso.get()
                    .load(thumbnailReward)
                    .placeholder(R.drawable.black_transparent)
                    .into(image)
            }

            rewardName.text = namaReward
            rewardPoint.text = points.toString()

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

            view.findViewById<CardView>(R.id.rewardCard).setOnClickListener {
                if (CheckConection.isNetworkAvailable(activity!!)){
                    val inent = Intent(activity, EditRewardActivity::class.java)
                    inent.putExtra("REWARD_ID", itemId)
                    startActivity(inent)
                } else {
                    showMessage("Mohon periksa koneksi internet anda")
                }
            }

            view.findViewById<CardView>(R.id.removeItem).setOnClickListener {
                showDeleteDialog(itemId)
            }

        }

    }

    private inner class RewardFirestoreRecyclerAdapter(options: FirestoreRecyclerOptions<Reward>) :
        FirestoreRecyclerAdapter<Reward, RewardViewHolder>(
            options
        ) {
        override fun onBindViewHolder(
            rewardViewHolder: RewardViewHolder,
            position: Int,
            model: Reward
        ) {
            rewardViewHolder.setProduct(
                model.id,
                model.points,
                model.nama,
                model.thumbnail,
                model.show,
                model.stok
            )

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_reward,
                parent,
                false
            )
            return RewardViewHolder(view)
        }
    }

    private fun showDeleteDialog(rewardId: String){
        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle("Hapus Reward")
        builder.setMessage("Yakin ingin menghapus reward ini ?")
        builder.setPositiveButton("Ya") { _, _ ->
            Constants.REWARD_DB.document(rewardId).delete().addOnSuccessListener {
                showMessage("Reward berhasil dihapus")
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
            DaftarRewardFragment().apply {

            }
    }
}