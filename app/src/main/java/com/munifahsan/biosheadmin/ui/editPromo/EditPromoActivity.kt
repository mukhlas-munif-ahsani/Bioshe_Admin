package com.munifahsan.biosheadmin.ui.editPromo

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.munifahsan.biosheadmin.databinding.ActivityEditPromoBinding
import com.munifahsan.biosheadmin.utils.FileUtil
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import id.zelory.compressor.loadBitmap
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.lang.Exception

class EditPromoActivity : AppCompatActivity(), EditPromoContract.View {
    private lateinit var mPres: EditPromoContract.Presenter
    private lateinit var binding: ActivityEditPromoBinding
    private val PICK_IMAGE_REQUEST = 1
    private var mImageUri: Uri? = null
    var promoId = ""

    private var actualImage: File? = null
    private var compressedImage: File? = null
    var isGambarPromoExist = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPromoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mPres = EditPromoPresenter(this)

        promoId = intent.getStringExtra("PROMO_ID").toString()
        mPres.getPromosi(promoId)
        binding.content.visibility = View.GONE
        binding.contentProgress.visibility = View.VISIBLE
        binding.selesaiBtn.isEnabled = false

        binding.cardPromo.setOnClickListener {
            openFileChooser()
        }

        binding.backIcon.setOnClickListener {
            finish()
        }

        binding.namaPromo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mPres.updateNama(promoId, text.toString())
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.diskonPromo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.diskonPromo.text!!.isNotEmpty()) {
                    mPres.updateDiskon(promoId, text.toString().toInt())
                } else {
                    mPres.updateDiskon(promoId, 0)
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.keteranganPromo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mPres.updateKeterangan(promoId, text.toString())
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.switchShow.setOnCheckedChangeListener { compoundButton, b ->
            if (isValidForm()) {
                mPres.updateShow(promoId, b)
            } else {
                compoundButton.isChecked = false
            }

        }

        binding.selesaiBtn.setOnClickListener {
            if (isValidForm()){
                mPres.updateSelesai(promoId)
                binding.content.visibility = View.GONE
                binding.contentProgress.visibility = View.VISIBLE
                binding.selesaiBtn.isEnabled = false
            } else {
                showMessage("Harap isi semua kolom")
            }
        }
    }

    override fun onDestroy() {
        if (promoId != ""){
            super.onDestroy()
            mPres.deleteDraft()
        } else {
            super.onDestroy()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data == null) {
                showMessage("Failed to open picture!")
                return
            }
            try {
                actualImage = FileUtil.from(this, data.data)?.also {
                    binding.imageViewPromo.setImageBitmap(loadBitmap(it))
                    binding.imageViewPromoGimik.visibility = View.GONE
                    binding.cardPromo.isEnabled = false
                    binding.uploadProgress.visibility = View.VISIBLE
                    //binding.textSize.text = String.format("Size : %s", getReadableFileSize(it.length()))
                    //clearImage()
                }
                customCompressImage()
            } catch (e: IOException) {
                showMessage("Failed to read picture data!")
                e.printStackTrace()
            }
        }

        if (resultCode == RESULT_CANCELED){

        }
    }

//    override fun promoId(promoId: String){
//        this.promoId = promoId
//        binding.namaProdukProgress.visibility = View.GONE
//        binding.diskonPromoProgress.visibility = View.GONE
//        binding.keteranganPromoProgress.visibility = View.GONE
//        //showMessage(promoId)
//    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            intent,
            PICK_IMAGE_REQUEST
        )
    }

    private fun customCompressImage() {
        actualImage?.let { imageFile ->
            lifecycleScope.launch {
                // Default compression with custom destination file
                /*compressedImage = Compressor.compress(this@MainActivity, imageFile) {
                    default()
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.also {
                        val file = File("${it.absolutePath}${File.separator}my_image.${imageFile.extension}")
                        destination(file)
                    }
                }*/

                // Full custom
                compressedImage = Compressor.compress(this@EditPromoActivity, imageFile) {
                    resolution(1280, 720)
                    quality(80)
                    format(Bitmap.CompressFormat.JPEG)
                    size(1_097_152) // 1 MB
                }
                setCompressedImage()
            }
        } ?: showMessage("Please choose an image!")
    }

    override fun showMessage(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }

    private fun setCompressedImage() {
        compressedImage?.let {
            binding.imageViewPromo.setImageBitmap(BitmapFactory.decodeFile(it.absolutePath))
            binding.imageViewPromoGimik.visibility = View.GONE
            binding.cardPromo.isEnabled = true

            mPres.updateImage(promoId, it.absoluteFile.toUri())
//            binding.textSize.text = String.format("Size : %s", getReadableFileSize(it.length()))
//            binding.upload.isEnabled = true
            //Toast.makeText(this, "Compressed image save in ${it.absoluteFile.toURI()}", Toast.LENGTH_LONG).show()
            Log.d("Compressor", "Compressed image save in " + it.path)
        }
    }

    override fun showImage(image: String){
        if (image != ""){
            isGambarPromoExist = true
            binding.imageViewPromoGimik.visibility = View.GONE
            binding.cardPromo.isEnabled = false
            binding.uploadProgress.visibility = View.GONE
            Picasso.get().load(image).into(binding.imageViewPromo, object : Callback {
                override fun onSuccess() {
                    binding.cardPromo.isEnabled = true
                }
                override fun onError(e: Exception?) {}
            })
        }else{
            isGambarPromoExist = false
        }

        binding.content.visibility = View.VISIBLE
        binding.contentProgress.visibility = View.GONE
        binding.selesaiBtn.isEnabled = true
    }

    override fun showNama(nama: String){
        binding.namaPromo.setText(nama)
        binding.namaProdukProgress.visibility = View.GONE
    }

    override fun showNamaProgress(){
        binding.namaProdukProgress.visibility = View.VISIBLE
    }

    override fun hideNamaProgress(){
        binding.namaProdukProgress.visibility = View.GONE
    }

    override fun showDiskon(diskon: String){
        binding.diskonPromo.setText(diskon)
        binding.diskonPromoProgress.visibility = View.GONE
    }

    override fun showDiskonProgress(){
        binding.diskonPromoProgress.visibility = View.VISIBLE
    }

    override fun hideDiskonProgress(){
        binding.diskonPromoProgress.visibility = View.GONE
    }

    override fun showKeterangan(ketereangan: String){
        binding.keteranganPromo.setText(ketereangan)
        binding.keteranganPromoProgress.visibility = View.GONE
    }

    override fun showKeteranganProgress(){
        binding.keteranganPromoProgress.visibility = View.VISIBLE
    }

    override fun hideKeteranganProgress(){
        binding.keteranganPromoProgress.visibility = View.GONE
    }

    override fun setShow(show: Boolean){
        binding.switchShow.isChecked = show
    }

    override fun showUploadImageProgress(progress: Int){
        binding.uploadProgress.progress = progress
    }

    private fun isValidForm(): Boolean {
        var isValid = true
        if (binding.namaPromo.text!!.isEmpty()) {
            isValid = false
            binding.namaPromoLay.error = "Nama promo tidak boleh kosong"
        }

        if (binding.diskonPromo.text!!.isEmpty()) {
            isValid = false
            binding.diskonPromoLay.error = "Diskon tidak boleh kosong"
        }

        if (binding.keteranganPromo.text!!.isEmpty()) {
            isValid = false
            binding.keteranganDiskonLay.error = "Stok produk tidak boleh kosong"
        }

        if (!isGambarPromoExist){
            isValid = false
            showMessage("Gambar harus dipilih")
        }

        return isValid
    }

    override fun selesai(){
        finish()
    }
}