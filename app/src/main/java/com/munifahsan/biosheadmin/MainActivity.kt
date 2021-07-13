package com.munifahsan.biosheadmin

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.munifahsan.biosheadmin.databinding.ActivityMainBinding
import com.munifahsan.biosheadmin.ui.chat.ChatFragment
import com.munifahsan.biosheadmin.ui.pageHome.HomeFragment
import com.munifahsan.biosheadmin.ui.pageLainnya.LainyaFragment
import com.munifahsan.biosheadmin.ui.pageMitra.MitraFragment
import com.munifahsan.biosheadmin.ui.pagePenjualan.PenjualanFragment
import com.munifahsan.biosheadmin.ui.pageProduk.ProdukFragment
import com.munifahsan.biosheadmin.utils.CheckConection
import com.munifahsan.biosheadmin.utils.Constants
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    lateinit var mainHandler: Handler
    var isConected = false

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    private var mBottomSheetEditHargaBehavior: BottomSheetBehavior<*>? = null
    private var mBottomSheetEditStokBehavior: BottomSheetBehavior<*>? = null

    private val updateTextTask = object : Runnable {
        override fun run() {
            if (CheckConection.isNetworkAvailable(this@MainActivity)) {
                binding.ofline.visibility = View.GONE
                isConected = true
                changeNotifBarColor(this@MainActivity, R.color.biru_dasar)
            } else {
                binding.ofline.visibility = View.VISIBLE
                isConected = false
                changeNotifBarColor(this@MainActivity, R.color.black)
            }
            mainHandler.postDelayed(this, 2000)
        }
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.itemHome -> {
                    val fragment = HomeFragment.newInstance()
                    addFragment(fragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.itemProduk -> {
                    val fragment = ProdukFragment.newInstance()
                    addFragment(fragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.itemChat -> {
                    val fragment = MitraFragment.newInstance()
                    addFragment(fragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.itemPenjualan -> {
                    val fragment = PenjualanFragment.newInstance()
                    addFragment(fragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.itemLiannya -> {
                    val fragment = LainyaFragment.newInstance()
                    addFragment(fragment)
                    return@OnNavigationItemSelectedListener true
                }
            }

            false
        }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bot_nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.bottomNavigationView.setOnNavigationItemSelectedListener(
            mOnNavigationItemSelectedListener
        )

        mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(updateTextTask)

        val fragment = HomeFragment.newInstance()
        addFragment(fragment)

        //Bottom sheet edit harga
        val bottomSheetEditHarga = findViewById<FrameLayout>(R.id.editHargaBottomSheet)
        mBottomSheetEditHargaBehavior = BottomSheetBehavior.from(bottomSheetEditHarga)
        (mBottomSheetEditHargaBehavior as BottomSheetBehavior<*>).isDraggable = false

        findViewById<CardView>(R.id.close).setOnClickListener {
            (mBottomSheetEditHargaBehavior as BottomSheetBehavior<*>).state =
                BottomSheetBehavior.STATE_COLLAPSED
            binding.blackBg.visibility = View.GONE
        }

        //Bottom sheet edit harga
        val bottomSheetEditStok = findViewById<FrameLayout>(R.id.editStokBottomSheet)
        mBottomSheetEditStokBehavior = BottomSheetBehavior.from(bottomSheetEditStok)
        (mBottomSheetEditStokBehavior as BottomSheetBehavior<*>).isDraggable = false

        findViewById<CardView>(R.id.close).setOnClickListener {
            (mBottomSheetEditStokBehavior as BottomSheetBehavior<*>).state =
                BottomSheetBehavior.STATE_COLLAPSED
            binding.blackBg.visibility = View.GONE
        }

        binding.blackBg.setOnClickListener {
            (mBottomSheetEditStokBehavior as BottomSheetBehavior<*>).state =
                BottomSheetBehavior.STATE_COLLAPSED
            (mBottomSheetEditHargaBehavior as BottomSheetBehavior<*>).state =
                BottomSheetBehavior.STATE_COLLAPSED
            binding.blackBg.visibility = View.GONE
        }
    }

    public fun openEditHarga(idProduk: String, namaProduk: String, harga: Int) {
        (mBottomSheetEditHargaBehavior as BottomSheetBehavior<*>).state =
            BottomSheetBehavior.STATE_EXPANDED
        binding.blackBg.visibility = View.VISIBLE

        val nama = findViewById<TextView>(R.id.namaProduk)
        nama.text = namaProduk

        val hargaProduk = findViewById<TextView>(R.id.hargaProduk)
        hargaProduk.text = rupiahFormat(harga)

        val simpan = findViewById<CardView>(R.id.simpan)
        simpan.setOnClickListener {
            hideKeyboard()

            if (hargaProduk.text!!.isNotEmpty()) {
                if (hargaProduk.text.toString().contains(".")) {
                    Constants.PRODUCT_DB.document(idProduk)
                        .update("harga",
                            hargaProduk.text.toString().replace(".", "").replace(" ", "").toInt())
                        .addOnSuccessListener {
                            (mBottomSheetEditHargaBehavior as BottomSheetBehavior<*>).state =
                                BottomSheetBehavior.STATE_COLLAPSED
                            binding.blackBg.visibility = View.INVISIBLE

                            showMessage("Harga berhasil dirubah")
                        }
                }
                if (hargaProduk.text.toString().contains(",")) {
                    Constants.PRODUCT_DB.document(idProduk)
                        .update("harga",
                            hargaProduk.text.toString().replace(",", "").replace(" ", "").toInt())
                        .addOnSuccessListener {
                            (mBottomSheetEditHargaBehavior as BottomSheetBehavior<*>).state =
                                BottomSheetBehavior.STATE_COLLAPSED
                            binding.blackBg.visibility = View.INVISIBLE

                            showMessage("Harga berhasil dirubah")
                        }
                }
            } else {
                //mPres.updateHarga(0, produkId)
            }
        }
    }

    public fun openEditStok(idProduk: String, namaProduk: String, jumlahStok: Int) {
        (mBottomSheetEditStokBehavior as BottomSheetBehavior<*>).state =
            BottomSheetBehavior.STATE_EXPANDED
        binding.blackBg.visibility = View.VISIBLE

        val nama = findViewById<TextView>(R.id.namaProduk)
        nama.text = namaProduk

        val increase = findViewById<CardView>(R.id.add)
        val decrease = findViewById<CardView>(R.id.remove)
        val simpan = findViewById<CardView>(R.id.simpanStok)
        val jmlStok = findViewById<TextInputEditText>(R.id.jumlahStok)
        jmlStok.setText(jumlahStok.toString())

        increase.setOnClickListener {
            val jml = jmlStok.text.toString()
            val jmlInt = jml.toInt().plus(1)
            jmlStok.setText(jmlInt.toString())
        }

        decrease.setOnClickListener {
            val jml = jmlStok.text.toString()
            if (jml.toInt() > jumlahStok) {
                val jmlInt = jml.toInt().minus(1)
                jmlStok.setText(jmlInt.toString())
            }
        }

        simpan.setOnClickListener {
            Constants.PRODUCT_DB.document(idProduk).update("stok", jmlStok.text.toString().toInt())
                .addOnSuccessListener {
                    (mBottomSheetEditStokBehavior as BottomSheetBehavior<*>).state =
                        BottomSheetBehavior.STATE_COLLAPSED
                    binding.blackBg.visibility = View.INVISIBLE
                    showMessage("Stok berhasil dirubah")
                }
        }
    }


    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onBackPressed() {
        if ((mBottomSheetEditHargaBehavior as BottomSheetBehavior<*>).state ==
            BottomSheetBehavior.STATE_EXPANDED
        ) {
            (mBottomSheetEditHargaBehavior as BottomSheetBehavior<*>).state =
                BottomSheetBehavior.STATE_COLLAPSED
            binding.blackBg.visibility = View.GONE
        } else if ((mBottomSheetEditStokBehavior as BottomSheetBehavior<*>).state ==
            BottomSheetBehavior.STATE_EXPANDED
        ) {
            (mBottomSheetEditStokBehavior as BottomSheetBehavior<*>).state =
                BottomSheetBehavior.STATE_COLLAPSED
            binding.blackBg.visibility = View.GONE
        } else {
            super.onBackPressed()
        }
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

    public fun showMessage(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.mtrl_bottom_sheet_slide_in,
                R.anim.mtrl_bottom_sheet_slide_out
            )
            .replace(R.id.fl_container, fragment, fragment.javaClass.simpleName)
            .commit()
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