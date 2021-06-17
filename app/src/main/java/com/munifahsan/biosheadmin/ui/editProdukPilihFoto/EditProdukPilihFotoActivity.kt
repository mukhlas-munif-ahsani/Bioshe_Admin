package com.munifahsan.biosheadmin.ui.editProdukPilihFoto

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.munifahsan.biosheadmin.databinding.ActivityEditProdukPilihFotoBinding
import com.munifahsan.biosheadmin.utils.Constants
import com.munifahsan.biosheadmin.utils.FileUtil
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import id.zelory.compressor.loadBitmap
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.DecimalFormat
import kotlin.math.log10
import kotlin.math.pow

class EditProdukPilihFotoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProdukPilihFotoBinding

    private val PICK_IMAGE_REQUEST = 1
    private var mImageUri: Uri? = null
    var produkId = ""

    private var actualImage: File? = null
    private var compressedImage: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProdukPilihFotoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        produkId = intent.getStringExtra("PRODUK_ID").toString()
        //showMessage(produkId)

        binding.upload.setOnClickListener {
            compressedImage?.let {
                val imageUri = it.absoluteFile.toUri()
                val fileReference: StorageReference =
                    FirebaseStorage.getInstance().reference.child("produkImages/" + Constants.CURRENT_USER_ID + "/" + imageUri.lastPathSegment)
                fileReference
                    .putFile(imageUri)
                    .addOnSuccessListener {
                        showMessage("Success")
                        binding.uploadProgress.visibility = View.INVISIBLE

                        downloadUrl(fileReference)
                    }
                    .addOnProgressListener { task ->
                        binding.uploadProgress.progress =
                            (100.0 * task.bytesTransferred / task.totalByteCount).toInt()
                        binding.uploadProgress.visibility = View.VISIBLE
                    }
                    .addOnFailureListener {
                        showMessage("gagal")
                        binding.uploadProgress.visibility = View.INVISIBLE
                    }
            }
        }
        openFileChooser()
        binding.upload.isEnabled = false
    }

    private fun downloadUrl(fileReference: StorageReference) {
        fileReference.downloadUrl.addOnSuccessListener {
            postImage(it)
        }
    }

    private fun postImage(imageUri: Uri) {

        Constants.PRODUK_DRAFT_DB.collection("IMAGES").whereEqualTo("nomor", 0).get()
            .addOnSuccessListener {
                if (it.isEmpty) {
                    val data = mapOf(
                        "image" to imageUri.toString(),
                        "nomor" to 0
                    )
                    Constants.PRODUK_DRAFT_DB.collection("IMAGES").document().set(data)
                        .addOnCompleteListener {
                            finish()
                        }
                    Constants.PRODUK_DRAFT_DB.update("thumbnail", imageUri.toString())
                } else {
                    val data = mapOf(
                        "image" to imageUri.toString(),
                        "nomor" to 1
                    )
                    Constants.PRODUK_DRAFT_DB.collection("IMAGES").document().set(data)
                        .addOnCompleteListener {
                            finish()
                        }
                }
            }

    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            intent,
            PICK_IMAGE_REQUEST
        )
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
                    binding.image.setImageBitmap(loadBitmap(it))
                    binding.textSize.text =
                        String.format("Size : %s", getReadableFileSize(it.length()))
                    //clearImage()
                }
                customCompressImage()
            } catch (e: IOException) {
                showMessage("Failed to read picture data!")
                e.printStackTrace()
            }
        }

        if (resultCode == RESULT_CANCELED) {
            finish()
        }
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
                compressedImage = Compressor.compress(this@EditProdukPilihFotoActivity, imageFile) {
                    resolution(1280, 720)
                    quality(80)
                    format(Bitmap.CompressFormat.JPEG)
                    size(1_097_152) // 2 MB
                }
                setCompressedImage()
            }
        } ?: showMessage("Please choose an image!")
    }

    private fun setCompressedImage() {
        compressedImage?.let {
            binding.image.setImageBitmap(BitmapFactory.decodeFile(it.absolutePath))
            binding.textSize.text = String.format("Size : %s", getReadableFileSize(it.length()))
            binding.upload.isEnabled = true
            //Toast.makeText(this, "Compressed image save in ${it.absoluteFile.toURI()}", Toast.LENGTH_LONG).show()
            Log.d("Compressor", "Compressed image save in " + it.path)
        }
    }

    private fun showMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    private fun getReadableFileSize(size: Long): String {
        if (size <= 0) {
            return "0"
        }
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#").format(size / 1024.0.pow(digitGroups.toDouble())) + " " + units[digitGroups]
    }
}