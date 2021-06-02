package com.munifahsan.biosheadmin.ui.editProduk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.munifahsan.biosheadmin.R
import com.munifahsan.biosheadmin.databinding.ActivityEditProdukBinding
import com.munifahsan.biosheadmin.models.ProdukImage
import com.munifahsan.biosheadmin.models.Promo
import com.munifahsan.biosheadmin.ui.editProdukPilihFoto.EditProdukPilihFotoActivity
import com.munifahsan.biosheadmin.utils.Constants
import com.squareup.picasso.Picasso
import java.text.DecimalFormat
import java.text.NumberFormat

class EditProdukActivity : AppCompatActivity(), EditProdukContract.View {
    private lateinit var binding: ActivityEditProdukBinding
    private lateinit var mPres: EditProdukContract.Presenter

    private var adapterImage: ImageFirestoreRecyclerAdapter? = null
    private var adapterPromo: PromoFirestoreRecyclerAdapter? = null

    var produkId = ""
    private var current: String = ""
    var isEdited = false
    var isGambarProdukExist = true

    private var mBottomSheetBehavior: BottomSheetBehavior<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProdukBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mPres = EditProdukPresenter(this)

        produkId = intent.getStringExtra("PRODUK_ID").toString()
        mPres.getProduk(intent.getStringExtra("PRODUK_ID").toString())

        binding.contentProgress.visibility = View.VISIBLE
        binding.content.visibility = View.INVISIBLE

        binding.backIcon.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            if (isEdited) {
                builder.setTitle("Yakin ingin keluar?")
                builder.setMessage("Datamu tidak tersimpan jika kamu kembali ke halaman sebelumnya.")
                builder.setPositiveButton("Ya") { _, _ ->
                    finish()
                }
                builder.setNegativeButton("Tidak") { dialog, _ ->
                    dialog.cancel()
                }
                builder.show()
            } else {
                finish()
            }
        }

        binding.tambahFotoCard.setOnClickListener {
            val intent = Intent(this, EditProdukPilihFotoActivity::class.java)
            intent.putExtra("PRODUK_ID", produkId)
            startActivity(intent)
        }

        binding.selesaiBtn.isEnabled = false

        binding.selesaiBtn.setOnClickListener {
            if (isGambarProdukExist) {
                if (isValidForm()) {
                    binding.contentProgress.visibility = View.VISIBLE
                    binding.content.visibility = View.INVISIBLE
                    mPres.simpan(produkId)
                } else {
                    showMessage("Harap isi semua kolom")
                }
            } else {
                binding.fotoKosong.visibility = View.VISIBLE
                showMessage("Gambar utama belum dipilih")
            }
        }

        binding.switchShow.setOnCheckedChangeListener { compoundButton, b ->
            if (isValidForm()) {
                mPres.updateShow(b, produkId)
            } else {
                compoundButton.isChecked = false
            }
        }

        binding.removePromo.setOnClickListener {
            mPres.deletePromo(produkId)
        }

        openClosePromosi()
        showPromoItem()
        input()
    }

    private fun openClosePromosi() {
        val bottomSheet = findViewById<FrameLayout>(R.id.bottom_sheet_pilih_promosi)
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        (mBottomSheetBehavior as BottomSheetBehavior<*>).isDraggable = false

        binding.bukaPromosi.setOnClickListener {
            (mBottomSheetBehavior as BottomSheetBehavior<*>).state =
                BottomSheetBehavior.STATE_EXPANDED
        }

        binding.pilihPromo.setOnClickListener {
            (mBottomSheetBehavior as BottomSheetBehavior<*>).state =
                BottomSheetBehavior.STATE_EXPANDED
        }

        findViewById<CardView>(R.id.close_pilih_promo_icon).setOnClickListener {
            (mBottomSheetBehavior as BottomSheetBehavior<*>).state =
                BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun input() {
        binding.namaProduk.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mPres.updateNama(text.toString(), produkId)

            }

            override fun afterTextChanged(p0: Editable?) {
                isEdited = true
            }
        })

        binding.hargaProduk.addTextChangedListener(object : TextWatcher {
            var dec: DecimalFormat = DecimalFormat("0.00")
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.hargaProduk.text!!.isNotEmpty()) {
                    if (text.toString().contains(".")){
                        mPres.updateHarga(text.toString().replace(".", "").toInt(), produkId)
                    }
                    if (text.toString().contains(",")){
                        mPres.updateHarga(text.toString().replace(",", "").toInt(), produkId)
                    }
                } else {
                    mPres.updateHarga(0, produkId)
                }

            }

            override fun afterTextChanged(s: Editable?) {
                binding.hargaProduk.removeTextChangedListener(this)

                try {
                    var originalString: String = s.toString()
                    if (originalString.contains(",")) {
                        originalString = originalString.replace(",".toRegex(), "")
                    }
                    val longval: Long = originalString.toLong()
                    val formatter = NumberFormat.getInstance() as DecimalFormat

                    formatter.applyPattern("#,###,###,###")
                    val formattedString = formatter.format(longval)//.replace(",", ".")

                    //setting text after format to EditText
                    binding.hargaProduk.setText(formattedString)
                    binding.hargaProduk.setSelection(binding.hargaProduk.text!!.length)
                } catch (nfe: NumberFormatException) {
                    nfe.printStackTrace()
                }

                binding.hargaProduk.addTextChangedListener(this)

                isEdited = true
            }
        })

        binding.stokProduk.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.stokProduk.text!!.isNotEmpty()) {
                    mPres.updateStok(text.toString().toInt(), produkId)
                } else {
                    mPres.updateStok(0, produkId)
                }

            }

            override fun afterTextChanged(p0: Editable?) {
                isEdited = true
            }
        })

        binding.deskripsiProduk.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mPres.updateKeterangan(text.toString(), produkId)

            }

            override fun afterTextChanged(p0: Editable?) {
                isEdited = true
            }
        })

        binding.berat.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.berat.text!!.isNotEmpty()) {
                    mPres.updateBerat(text.toString().toInt(), produkId)
                } else {
                    mPres.updateBerat(0, produkId)
                }

            }

            override fun afterTextChanged(p0: Editable?) {
                isEdited = true
            }
        })
    }

    override fun onBackPressed() {
        if (produkId != ""){
            if (mBottomSheetBehavior!!.state == BottomSheetBehavior.STATE_EXPANDED) {
                (mBottomSheetBehavior as BottomSheetBehavior<*>).state =
                    BottomSheetBehavior.STATE_COLLAPSED
            } else {
                val builder = AlertDialog.Builder(this)
                if (isEdited) {
                    builder.setTitle("Yakin ingin keluar?")
                    builder.setMessage("Datamu tidak tersimpan jika kamu kembali ke halaman sebelumnya.")
                    builder.setPositiveButton("Ya") { _, _ ->
                        super.onBackPressed()
                    }
                    builder.setNegativeButton("Tidak") { dialog, _ ->
                        dialog.cancel()
                    }
                    builder.show()
                } else {
                    super.onBackPressed()
                }
            }
        } else {
            super.onBackPressed()
        }
    }

    override fun onStart() {
        super.onStart()
        if (adapterImage != null) {
            adapterImage!!.startListening()
        }
        adapterPromo!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        if (produkId != ""){
            if (adapterImage != null && adapterPromo != null){
                adapterImage!!.stopListening()
                adapterPromo!!.stopListening()
            }
        }
    }

    override fun onDestroy() {
        if (produkId == ""){
            super.onDestroy()
        } else {
            super.onDestroy()
            mPres.deleteDraft()
        }
    }

    private fun showImageItem() {
        binding.rvImage.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.HORIZONTAL, false
        )
        val rootRef = FirebaseFirestore.getInstance()
        /*
        cek apakah gambar produk tidak kosong
         */
        rootRef.collection("DRAFT").document("PRODUK").collection("IMAGES")
            .whereEqualTo("nomor", 0).addSnapshotListener { value, error ->
                isGambarProdukExist = !value!!.isEmpty
                if (!value.isEmpty) {
                    binding.fotoKosong.visibility = View.GONE
                }
            }

        val query = rootRef.collection("DRAFT").document("PRODUK").collection("IMAGES")
            .orderBy("nomor", Query.Direction.ASCENDING)

        query.get().addOnSuccessListener {
            if (!it.isEmpty) {
                //binding.tambahFotoCard.visibility = View.GONE
                binding.rvImage.visibility = View.VISIBLE
            } else {
                //binding.tambahFotoCard.visibility = View.VISIBLE
                binding.rvImage.visibility = View.INVISIBLE
            }
        }

        val options = FirestoreRecyclerOptions
            .Builder<ProdukImage>()
            .setQuery(query, ProdukImage::class.java)
            .build()
        adapterImage = ImageFirestoreRecyclerAdapter(options)
        binding.rvImage.adapter = adapterImage
        ViewCompat.setNestedScrollingEnabled(binding.rvImage, false)
    }

    private inner class ImageViewHolder(private val view: View) :
        RecyclerView.ViewHolder(
            view
        ) {

        fun setImage(
            id: String,
            image: String,
            nomor: Int,
        ) {
            Picasso.get().load(image).into(view.findViewById<ImageView>(R.id.image))
            if (nomor == 0) {
                view.findViewById<CardView>(R.id.utama).visibility = View.VISIBLE
            } else {
                view.findViewById<CardView>(R.id.utama).visibility = View.INVISIBLE
            }

            view.findViewById<CardView>(R.id.removeItem).setOnClickListener {
                if (nomor == 0) {
                    Constants.PRODUK_DRAFT_DB.update("thumbnail", "")
                    Constants.PRODUK_DRAFT_DB.collection("IMAGES").document(id)
                        .delete()
                } else {
                    Constants.PRODUK_DRAFT_DB.collection("IMAGES").document(id)
                        .delete()
                }
            }
        }

    }

    private inner class ImageFirestoreRecyclerAdapter(options: FirestoreRecyclerOptions<ProdukImage>) :
        FirestoreRecyclerAdapter<ProdukImage, ImageViewHolder>(
            options
        ) {
        override fun onBindViewHolder(
            productViewHolder: ImageViewHolder,
            position: Int,
            model: ProdukImage,
        ) {
            productViewHolder.setImage(
                model.id,
                model.image,
                model.nomor
            )

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_image_tambah_produk,
                    parent,
                    false
                )
            return ImageViewHolder(view)
        }
    }

    private fun showPromoItem() {
        findViewById<RecyclerView>(R.id.rvPromo).layoutManager = LinearLayoutManager(this)
        val rootRef = FirebaseFirestore.getInstance()
        val query = rootRef.collection("PROMO").whereEqualTo("selesai", true).whereEqualTo("show", true)
        val options = FirestoreRecyclerOptions
            .Builder<Promo>()
            .setQuery(query, Promo::class.java)
            .build()
        adapterPromo = PromoFirestoreRecyclerAdapter(options)
        findViewById<RecyclerView>(R.id.rvPromo).adapter = adapterPromo
    }

    private inner class PromoViewHolder(private val view: View) :
        RecyclerView.ViewHolder(
            view
        ) {

        fun setImage(
            id: String,
            image: String,
            diskon: Int,
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
                (mBottomSheetBehavior as BottomSheetBehavior<*>).state =
                    BottomSheetBehavior.STATE_COLLAPSED

                mPres.updatePromo(produkId, id, diskon)
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
                model.diskon
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

    override fun produkId(produkId: String) {
        this.produkId = produkId
        binding.contentProgress.visibility = View.GONE
        binding.content.visibility = View.VISIBLE
        binding.selesaiBtn.isEnabled = true

        showImageItem()
        adapterImage!!.startListening()
    }

    override fun showNamaProgress() {
        binding.namaProdukProgress.visibility = View.VISIBLE
    }

    override fun hideNamaProgress() {
        binding.namaProdukProgress.visibility = View.INVISIBLE
    }

    override fun setNama(nama: String) {
        binding.namaProduk.setText(nama)
    }

    override fun showHargaProgress() {
        binding.hargaProdukProgress.visibility = View.VISIBLE
    }

    override fun hideHargaProgress() {
        binding.hargaProdukProgress.visibility = View.INVISIBLE
    }

    override fun setHarga(harga: String) {
        if (harga != "") {
            binding.hargaProduk.setText(harga)
        }
    }

    override fun showStokProgress() {
        binding.stokProdukProgress.visibility = View.VISIBLE
    }

    override fun hideStokProgress() {
        binding.stokProdukProgress.visibility = View.INVISIBLE
    }

    override fun setStok(stok: String) {
        if (stok != "") {
            binding.stokProduk.setText(stok)
        }
    }

    override fun showDeskripsiProgress() {
        binding.deskripsiProdukProgress.visibility = View.VISIBLE
    }

    override fun hideDeskripsiProgress() {
        binding.deskripsiProdukProgress.visibility = View.INVISIBLE
    }

    override fun setDeskripsi(keterangan: String) {
        binding.deskripsiProduk.setText(keterangan)
    }

    override fun showBeratProgress() {
        binding.beratProgress.visibility = View.VISIBLE
    }

    override fun hideBeratProgress() {
        binding.beratProgress.visibility = View.INVISIBLE
    }

    override fun setBerat(berat: String) {
        if (berat != "") {
            binding.berat.setText(berat)
        }
    }

    override fun setShow(show: Boolean) {
        binding.switchShow.isChecked = show
    }

    override fun showPromo(image: String, nama: String, diskon: Int){
        binding.relBukaPromosi.visibility = View.VISIBLE
        binding.pilihPromo.visibility = View.GONE
        Picasso.get().load(image).into(binding.promoImage)
        binding.namaPromo.text = nama
        binding.valuePromo.text = "Diskon $diskon%"
    }

    override fun hidePromo() {
        binding.relBukaPromosi.visibility = View.GONE
        binding.pilihPromo.visibility = View.VISIBLE
    }

    override fun selesai() {
        finish()
    }

    override fun showMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    private fun isValidForm(): Boolean {
        var isValid = true
        if (binding.namaProduk.text!!.isEmpty()) {
            isValid = false
            binding.namaProdukLay.error = "Nama produk tidak boleh kosong"
        }

        if (binding.hargaProduk.text!!.isEmpty()) {
            isValid = false
            binding.hargaProdukLay.error = "Harga produk tidak boleh kosong"
        }

        if (binding.stokProduk.text!!.isEmpty()) {
            isValid = false
            binding.stokProdukLay.error = "Stok produk tidak boleh kosong"
        }

        if (binding.deskripsiProduk.text!!.isEmpty()) {
            isValid = false
            binding.deskripsiProdukLay.error = "Deskripsi produk tidak boleh kosong"
        }

        if (binding.berat.text!!.isEmpty()) {
            isValid = false
            binding.beratLay.error = "Berat produk tidak boleh kosong"
        }
        return isValid
    }

}