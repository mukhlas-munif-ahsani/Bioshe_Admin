package com.munifahsan.biosheadmin.ui.pilihDistributorPelanggan

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.munifahsan.biosheadmin.R
import com.munifahsan.biosheadmin.databinding.ActivityPilihDistributorPelangganBinding
import com.munifahsan.biosheadmin.models.Distributor
import com.munifahsan.biosheadmin.models.Sales
import com.munifahsan.biosheadmin.ui.chatRoom.ChatRoomActivity
import com.munifahsan.biosheadmin.ui.daftarSales.DaftarSalesFragment
import com.munifahsan.biosheadmin.ui.detailDistributor.DetailDistributorActivity
import com.munifahsan.biosheadmin.ui.detailSales.DetailSalesActivity
import com.munifahsan.biosheadmin.utils.CheckConection
import com.munifahsan.biosheadmin.utils.Constants
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class PilihDistributorPelangganActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPilihDistributorPelangganBinding

    lateinit var mAnimator: ValueAnimator
    private var adapter: DaftarFirestoreRecyclerAdapter? = null
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    var pelangganId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPilihDistributorPelangganBinding.inflate(layoutInflater)
        val view = binding.root

        val intent = intent
        pelangganId = intent.getStringExtra("PELANGGAN_ID").toString()

//        mPres.getData(id)
        showDaftar()

        setContentView(view)
    }

    override fun onStart() {
        super.onStart()
        adapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter!!.stopListening()
    }

    private fun showDaftar() {
        binding.rvDaftar.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        val rootRef = FirebaseFirestore.getInstance()
        val query = rootRef.collection("DISTRIBUTOR").orderBy("nama", Query.Direction.ASCENDING)

        val options = FirestoreRecyclerOptions
            .Builder<Distributor>()
            .setQuery(query, Distributor::class.java)
            .build()
        adapter = DaftarFirestoreRecyclerAdapter(options)
        binding.rvDaftar.adapter = adapter
    }

    private inner class ProductViewHolder(private val view: View) :
        RecyclerView.ViewHolder(
            view
        ) {

        fun setContentDistributor(
            id: String,
            nama: String,
            fotoUrl: String,
            alamat: String,
            noHp: String,
        ) {
            val namaTxt = view.findViewById<TextView>(R.id.namaDistributor)
            val fotoView = view.findViewById<ImageView>(R.id.foto)
            val alamatView = view.findViewById<TextView>(R.id.alamat)

            namaTxt.text = nama
            Picasso.get()
                .load(fotoUrl)
                .placeholder(R.drawable.img_profile)
                .into(fotoView)
            alamatView.text = alamat

            view.findViewById<RelativeLayout>(R.id.relShowall).setOnClickListener {
                if (CheckConection.isNetworkAvailable(this@PilihDistributorPelangganActivity)) {
                    val intent = Intent(this@PilihDistributorPelangganActivity, DetailDistributorActivity::class.java)
                    intent.putExtra("DISTRIBUTOR_ID", id)
                    startActivity(intent)
                } else {
                    showMessage("Mohon periksa koneksi internet anda")
                }
            }

            view.findViewById<CardView>(R.id.cardDistributor).setOnClickListener {
                if (CheckConection.isNetworkAvailable(this@PilihDistributorPelangganActivity)) {
//                    val intent = Intent(this@PilihDistributorPelangganActivity, DetailDistributorActivity::class.java)
//                    intent.putExtra("DISTRIBUTOR_ID", id)
//                    startActivity(intent)
                    if (pelangganId != ""){
                        val builder = AlertDialog.Builder(this@PilihDistributorPelangganActivity)
                        builder.setTitle("Konfirmasi")
                        builder.setMessage("Anda akan memilih Distributor ini untuk pelanggan.. ")
                        builder.setPositiveButton("Ya") { _, _ ->
                            showMessage("Mohon tunggu...")
                            Constants.USERS_DB.document(pelangganId).update("distributorId", id).addOnSuccessListener {
                                finish()
                            }
                        }
                        builder.setNegativeButton("Batal") { dialog, _ ->
                            dialog.cancel()
                        }
                        builder.show()

                    }
                } else {
                    showMessage("Mohon periksa koneksi internet anda")
                }
            }
        }
    }

    private fun showMessage(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }

    private inner class DaftarFirestoreRecyclerAdapter(options: FirestoreRecyclerOptions<Distributor>) :
        FirestoreRecyclerAdapter<Distributor, ProductViewHolder>(
            options
        ) {
        override fun onBindViewHolder(
            productViewHolder: ProductViewHolder,
            position: Int,
            model: Distributor,
        ) {
            productViewHolder.setContentDistributor(model.id,
                model.nama,
                model.photo_url,
                model.alamat,
                model.noHp)

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.item_pilih_distributor,
                parent,
                false
            )
            return ProductViewHolder(view)
        }
    }

    private fun getTimeDateFirebase(timestamp: Date?): String? {
        return try {
            //Date netDate = (timestamp);
            val sfd = SimpleDateFormat("dd/MM/yy, hh:mm a", Locale.getDefault())
            sfd.format(timestamp)
        } catch (e: Exception) {
            "date"
        }
    }

    private fun getTimeDateFirebase2(timestamp: Date?): String? {
        return try {
            //Date netDate = (timestamp);
            val sfd = SimpleDateFormat("hh:mm a", Locale.getDefault())
            sfd.format(timestamp)
        } catch (e: Exception) {
            "date"
        }
    }

    private fun getTimeDateNow1(timestamp: Date?): String? {
        return try {
            //Date netDate = (timestamp);
            val sfd = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
            sfd.format(timestamp)
        } catch (e: Exception) {
            "date"
        }
    }

    private fun getTimeDateNow2(): String? {
        return try {
            //Date netDate = (timestamp);
            val sfd = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
            sfd.format(Date())
        } catch (e: Exception) {
            "date"
        }
    }

    private fun getTimeDate(timestamp: Date?): String? {
        return try {
            //Date netDate = (timestamp);
            val sfd = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
            sfd.format(timestamp)
        } catch (e: Exception) {
            "date"
        }
    }
}