package com.munifahsan.biosheadmin.ui.detailTransaksi

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.munifahsan.biosheadmin.R
import com.munifahsan.biosheadmin.databinding.ActivityDetailTransaksiBinding
import com.munifahsan.biosheadmin.models.ProdukDetailTransaksi
import com.munifahsan.biosheadmin.ui.chatRoom.ChatRoomActivity
import com.munifahsan.biosheadmin.ui.detailDistributor.DetailDistributorActivity
import com.munifahsan.biosheadmin.ui.detailPelanggan.DetailPelangganActivity
import com.munifahsan.biosheadmin.ui.detailSales.DetailSalesActivity
import com.squareup.picasso.Picasso
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

class DetailTransaksiActivity : AppCompatActivity(), DetailTransaksiContract.View {
    private lateinit var binding: ActivityDetailTransaksiBinding
    private lateinit var mPres: DetailTransaksiContract.Presenter
    private var adapterCart: ProductFirestoreRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTransaksiBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mPres = DetailTransaksiPresenter(this)

        val intent = intent
        val orderId = intent.getStringExtra("ORDER_ID").toString()
        mPres.getData(orderId)

        showItemCart(orderId)

        binding.backIcon.setOnClickListener {
            finish()
        }

        mPres = DetailTransaksiPresenter(this)
    }

    override fun onStart() {
        super.onStart()
        adapterCart?.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapterCart?.stopListening()
    }

    private fun showItemCart(orderId: String) {
        binding.rvItem.layoutManager = LinearLayoutManager(
            this
        )
        val rootRef = FirebaseFirestore.getInstance()
        val query = rootRef.collection("ORDERS").document(orderId)
            .collection("PRODUCT")
        val options = FirestoreRecyclerOptions
            .Builder<ProdukDetailTransaksi>()
            .setQuery(query, ProdukDetailTransaksi::class.java)
            .build()
        adapterCart = ProductFirestoreRecyclerAdapter(options)
        binding.rvItem.adapter = adapterCart
        binding.rvItem.isNestedScrollingEnabled = false
    }

    private inner class ProductViewHolder(private val view: View) :
        RecyclerView.ViewHolder(
            view
        ) {

        fun setProduct(
            itemKeranjangId: String,
            productId: String, jumlahItem: Int,
            namaProduct: String, thumbnailProduct: String,
            hargaProduct: Int, disconProduct: Int, berat: Int
        ) {
            val productName = view.findViewById<TextView>(R.id.productName)
            val priceDiscon = view.findViewById<TextView>(R.id.priceDisconTxt)
            val disconTxt = view.findViewById<TextView>(R.id.disconTxt)
            val priceProduct = view.findViewById<TextView>(R.id.priceProduct)
            val linDiscon = view.findViewById<LinearLayout>(R.id.linDiscon)
            val image = view.findViewById<ImageView>(R.id.thumbnailImage)
            val jumlahItemTxt = view.findViewById<TextView>(R.id.jumlahItem)

            "${jumlahItem} Barang (${berat*jumlahItem} gr)".also { jumlahItemTxt.text = it }

            if (thumbnailProduct != "") {
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

        }
    }

    private inner class ProductFirestoreRecyclerAdapter(options: FirestoreRecyclerOptions<ProdukDetailTransaksi>) :
        FirestoreRecyclerAdapter<ProdukDetailTransaksi, ProductViewHolder>(
            options
        ) {
        override fun onBindViewHolder(
            productViewHolder: ProductViewHolder,
            position: Int,
            model: ProdukDetailTransaksi
        ) {
            productViewHolder.setProduct(
                model.id,
                model.productId,
                model.jumlahItem,
                model.nama,
                model.thumbnail,
                model.harga,
                model.diskon,
                model.berat
            )
            //productViewHolder.setJumlahClickableItem(keranjangModel.id, keranjangModel.jumlahItem)

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.item_detail_pesanan,
                parent,
                false
            )
            return ProductViewHolder(view)
        }
    }

    override fun showStatus(status: String){
        binding.status.visibility = View.VISIBLE
        binding.statusPesananShimmer.visibility = View.INVISIBLE
        binding.status.text = status
    }

    override fun hideStatus(){
        binding.status.visibility = View.INVISIBLE
        binding.statusPesananShimmer.visibility = View.VISIBLE
    }

    override fun showTanggal(tanggal: Date?){
        binding.tanggalPemesanan.visibility = View.VISIBLE
        binding.tanggalPemesananShimmer.visibility = View.INVISIBLE
        binding.tanggalPemesanan.text = getTimeDate(tanggal)
    }

    override fun hideTanggal(){
        binding.tanggalPemesananShimmer.visibility = View.VISIBLE
        binding.tanggalPemesanan.visibility = View.INVISIBLE
    }

    override fun showIdPesananBioshe(id: String){
        binding.idPemesanan.visibility = View.VISIBLE
        binding.idPesananShimmer.visibility = View.INVISIBLE
        "...${id.substring(15, id.length)}".also { binding.idPemesanan.text = it }

        binding.idPemesanan.setOnClickListener {
            val clipboardManager = this.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

            val clipData = ClipData.newPlainText("text", id)
            clipboardManager.setPrimaryClip(clipData)

            showMessage("ID Pesanan disalin ke clipboard")
        }

        binding.copyIcon.setOnClickListener {
            val clipboardManager = this.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

            val clipData = ClipData.newPlainText("text", id)
            clipboardManager.setPrimaryClip(clipData)

            showMessage("ID Pesanan disalin ke clipboard")
        }
    }

    override fun hideIdPesananBioshe(){
        binding.idPemesanan.visibility = View.INVISIBLE
        binding.idPesananShimmer.visibility = View.VISIBLE
    }

    override fun showIdPesananMidtrans(id: String){
        binding.idPemesananMidtrans.visibility = View.VISIBLE
        binding.idPesananMidtransShimmer.visibility = View.INVISIBLE
        binding.idPemesananMidtrans.text = id
    }

    override fun hideIdPesananMidtrans(){
        binding.idPemesananMidtrans.visibility = View.INVISIBLE
        binding.idPesananMidtransShimmer.visibility = View.VISIBLE
    }

    override fun showNamaPelanggan(nama: String, id: String){
        binding.namaPelanggan.visibility = View.VISIBLE
        binding.namaPelangganShimmer.visibility = View.INVISIBLE
        binding.namaPelanggan.text = nama

        binding.pelangganChatBtn.setOnClickListener {
            openChatRoom(id)
        }

        binding.relNamaPelanggan.setOnClickListener {
            openDetailPelaggan(id)
        }
    }

    override fun hideNamaPelanggan(){
        binding.namaPelanggan.visibility = View.VISIBLE
        binding.namaPelangganShimmer.visibility = View.INVISIBLE
    }

    override fun showSales(sales: String, id: String){
        binding.namaSales.visibility = View.VISIBLE
        binding.namaSalesShimmer.visibility = View.INVISIBLE
        binding.namaSales.text = sales

        binding.salesChatBtn.setOnClickListener {
            openChatRoom(id)
        }

        binding.relNamaSales.setOnClickListener {
            openDetailSales(id)
        }
    }

    override fun hideSales(){
        binding.namaSales.visibility = View.INVISIBLE
        binding.namaSalesShimmer.visibility = View.VISIBLE
    }

    override fun showDistributor(distributor: String, id: String){
        binding.namaDistributor.visibility = View.VISIBLE
        binding.namaDistributorShimmer.visibility = View.INVISIBLE
        binding.namaDistributor.text = distributor

        binding.distributorChatBtn.setOnClickListener {
            openChatRoom(id)
        }

        binding.relNamaDistributor.setOnClickListener {
            openDetailDistributor(id)
        }
    }

    override fun hideDistributor(){
        binding.namaDistributor.visibility = View.INVISIBLE
        binding.namaDistributorShimmer.visibility = View.VISIBLE
    }

    override fun showKurir(kurir: String){
        binding.kurirPengirim.visibility = View.VISIBLE
        binding.kurirShimmer.visibility = View.INVISIBLE
        binding.kurirPengirim.text = kurir
    }

    override fun hideKurir(){
        binding.kurirPengirim.visibility = View.INVISIBLE
        binding.kurirShimmer.visibility = View.VISIBLE
    }

    override fun showResi(resi: String){
        binding.noResi.visibility = View.VISIBLE
        binding.noResiShimmer.visibility = View.INVISIBLE
        binding.noResi.text = resi
    }

    override fun hideResi(){
        binding.noResi.visibility = View.INVISIBLE
        binding.noResiShimmer.visibility = View.VISIBLE
    }

    override fun showAlamat(nama: String, noHp: String, alamat: String) {
        binding.nama.visibility = View.VISIBLE
        binding.nomor.visibility = View.VISIBLE
        binding.alamatPengiriman.visibility = View.VISIBLE
        binding.alamatPengirimanShimmer.visibility = View.INVISIBLE

        binding.nama.text = nama
        binding.nomor.text = noHp
        binding.alamatPengiriman.text = alamat
    }

    override fun hideAlamat(){
        binding.nama.visibility = View.INVISIBLE
        binding.nomor.visibility = View.INVISIBLE
        binding.alamatPengiriman.visibility = View.INVISIBLE
        binding.alamatPengirimanShimmer.visibility = View.VISIBLE
    }

    override fun showMetodePembayaran(metode: String){
        binding.metodePembayaran.visibility = View.VISIBLE
        binding.metodePembayaranShimmer.visibility = View.INVISIBLE
        binding.metodePembayaran.text = metode
    }

    override fun hideMetodePembayaran(){
        binding.metodePembayaran.visibility = View.INVISIBLE
        binding.metodePembayaranShimmer.visibility = View.VISIBLE
    }

    override fun showTotalBarangHargaJumlah(jumlah: Int, harga: Int, berat: Int, ongkir: Int){
        binding.totalHarga.visibility = View.VISIBLE
        binding.totalHargaShimmer.visibility = View.INVISIBLE
        binding.totalBarang.visibility = View.VISIBLE
        binding.totalBerat.text = "Total Ongkos Kirim ($berat gr)"

        showTotalBayar(harga.plus(ongkir))

        binding.totalHarga.text = rupiahFormat(harga)
        binding.totalBarang.text = "Total Harga Barang ($jumlah)"
    }

    override fun hideTotalBarangHargaJumlah(){
        binding.totalBarang.text = "Total Harga"
        binding.totalHarga.visibility = View.INVISIBLE
        binding.totalHargaShimmer.visibility = View.VISIBLE
    }

    override fun showTotalOngkir(ongkir: Int){
        binding.totalOngkir.visibility = View.VISIBLE
        binding.totalOngkirShimmer.visibility = View.INVISIBLE
        if (ongkir == 0) {
            binding.totalOngkir.text = "Gratis"
        } else {
            binding.totalOngkir.text = rupiahFormat(ongkir)
        }
    }

    override fun hideTotalOngkir(){
        binding.totalOngkir.visibility = View.INVISIBLE
        binding.totalOngkirShimmer.visibility = View.VISIBLE
    }

    override fun showTotalBayar(bayar: Int){
        binding.totalBayar.visibility = View.VISIBLE
        binding.totalBayarShimmer.visibility = View.INVISIBLE
        binding.totalBayar.text = rupiahFormat(bayar)
    }

    override fun hideTotalBayar(){
        binding.totalBayar.visibility = View.INVISIBLE
        binding.totalBayarShimmer.visibility = View.VISIBLE
    }

    private fun getTimeDate(timestamp: Date?): String? {
        return try {
            //Date netDate = (timestamp);
            val sfd = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
            val tz = TimeZone.getTimeZone("Asia/Jakarta")
            //sfd.timeZone = tz

            //showMessage(tz.getDisplayName(false, TimeZone.SHORT, Locale.ENGLISH))

            sfd.format(timestamp)
        } catch (e: Exception) {
            "date"
        }
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

    override fun showMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    private fun openChatRoom(id: String) {
        val intent = Intent(this, ChatRoomActivity::class.java)
        intent.putExtra("FRIEND_ID", id)
        startActivity(intent)
    }

    private fun openDetailPelaggan(id: String){
        val intent = Intent(this, DetailPelangganActivity::class.java)
        intent.putExtra("PELANGGAN_ID", id)
        startActivity(intent)
    }

    private fun openDetailSales(id: String){
        val intent = Intent(this, DetailSalesActivity::class.java)
        intent.putExtra("SALES_ID", id)
        startActivity(intent)
    }

    private fun openDetailDistributor(id: String){
        val intent = Intent(this, DetailDistributorActivity::class.java)
        intent.putExtra("DISTRIBUTOR_ID", id)
        startActivity(intent)
    }
}