package com.munifahsan.biosheadmin

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.munifahsan.biosheadmin.databinding.ActivityMainBinding
import com.munifahsan.biosheadmin.ui.chat.ChatFragment
import com.munifahsan.biosheadmin.ui.home.HomeFragment
import com.munifahsan.biosheadmin.ui.lainnya.LainyaFragment
import com.munifahsan.biosheadmin.ui.penjualan.PenjualanFragment
import com.munifahsan.biosheadmin.ui.produk.ProdukFragment
import com.munifahsan.biosheadmin.utils.CheckConection

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    lateinit var mainHandler: Handler
    var isConected = false
    private val updateTextTask = object : Runnable {
        override fun run() {
            if (CheckConection.isNetworkAvailable(this@MainActivity)){
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
                    val fragment = ChatFragment.newInstance()
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
    }

    private fun changeNotifBarColor(context: Context, color: Int){
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

    private fun showMessage(s: String) {
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