package com.munifahsan.biosheadmin

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.munifahsan.biosheadmin.databinding.ActivityMainBinding
import com.munifahsan.biosheadmin.ui.chat.ChatFragment
import com.munifahsan.biosheadmin.ui.pageHome.HomeFragment
import com.munifahsan.biosheadmin.ui.pageLainnya.LainyaFragment
import com.munifahsan.biosheadmin.ui.pageMitra.MitraFragment
import com.munifahsan.biosheadmin.ui.pagePenjualan.PenjualanFragment
import com.munifahsan.biosheadmin.ui.pageProduk.ProdukFragment
import com.munifahsan.biosheadmin.utils.CheckConection

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    lateinit var mainHandler: Handler
    var isConected = false

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

        findViewById<CardView>(R.id.closeStok).setOnClickListener {
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

    public fun openEditHarga() {
        (mBottomSheetEditHargaBehavior as BottomSheetBehavior<*>).state =
            BottomSheetBehavior.STATE_EXPANDED
        binding.blackBg.visibility = View.VISIBLE
    }

    public fun openEditStok() {
        (mBottomSheetEditStokBehavior as BottomSheetBehavior<*>).state =
            BottomSheetBehavior.STATE_EXPANDED
        binding.blackBg.visibility = View.VISIBLE
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

}