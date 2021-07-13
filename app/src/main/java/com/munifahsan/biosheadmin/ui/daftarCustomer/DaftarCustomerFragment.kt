package com.munifahsan.biosheadmin.ui.daftarCustomer

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.munifahsan.biosheadmin.R
import com.munifahsan.biosheadmin.databinding.FragmentDaftarCustomerBinding
import com.munifahsan.biosheadmin.models.Sales
import com.munifahsan.biosheadmin.models.User
import com.munifahsan.biosheadmin.ui.chatRoom.ChatRoomActivity
import com.munifahsan.biosheadmin.ui.daftarSales.DaftarSalesFragment
import com.munifahsan.biosheadmin.ui.detailPelanggan.DetailPelangganActivity
import com.munifahsan.biosheadmin.ui.detailSales.DetailSalesActivity
import com.munifahsan.biosheadmin.utils.CheckConection
import com.munifahsan.biosheadmin.utils.Constants
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*


class DaftarCustomerFragment : Fragment() {

    private var _binding: FragmentDaftarCustomerBinding? = null
    private val binding get() = _binding!!

    lateinit var mAnimator: ValueAnimator
    private var adapter: DaftarFirestoreRecyclerAdapter? = null
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDaftarCustomerBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        showDaftar()

        // Inflate the layout for this fragment
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

    private fun showDaftar() {
        binding.rvCustomer.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        val rootRef = FirebaseFirestore.getInstance()
        val query = rootRef.collection("USERS").orderBy("nama", Query.Direction.ASCENDING)

        val options = FirestoreRecyclerOptions
            .Builder<User>()
            .setQuery(query, User::class.java)
            .build()
        adapter = DaftarFirestoreRecyclerAdapter(options)
        binding.rvCustomer.adapter = adapter
    }

    private inner class ProductViewHolder(private val view: View) :
        RecyclerView.ViewHolder(
            view
        ), ViewTreeObserver.OnPreDrawListener {

        var i: String = ""

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

            view.findViewById<RelativeLayout>(R.id.expandMenu).viewTreeObserver.addOnPreDrawListener(
                this)

            view.findViewById<TextView>(R.id.textView_peakMessage_chatList).text =
                "Assalaikum ya akhi ya ukhty.. assalamualaikum.."

            i = nama

            view.findViewById<CardView>(R.id.cardDistributor).setOnClickListener {
                if (view.findViewById<RelativeLayout>(R.id.expandMenu).visibility == View.GONE) {
                    expand()
                } else {
                    collapse()
                }
            }

            view.findViewById<RelativeLayout>(R.id.relShowall).setOnClickListener {
                if (CheckConection.isNetworkAvailable(activity!!)) {
                    val intent = Intent(activity, DetailPelangganActivity::class.java)
                    intent.putExtra("PELANGGAN_ID", id)
                    startActivity(intent)
                } else {
                    showMessage("Mohon periksa koneksi internet anda")
                }
            }

            view.findViewById<CardView>(R.id.cardTelepon).setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$noHp")
                startActivity(intent)
            }

            view.findViewById<CardView>(R.id.cardChat).setOnClickListener {
                val intent = Intent(activity, ChatRoomActivity::class.java)
                intent.putExtra("FRIEND_ID", id)
                startActivity(intent)
            }

            setContentChat(id)
        }

        fun setContentChat(id: String) {
            val time = view.findViewById<TextView>(R.id.date)
            val numPeak = view.findViewById<TextView>(R.id.textView_notifNumPeak)
            val cardNumPeak = view.findViewById<CardView>(R.id.cardView_notifNumPeak)
            val cardChat = view.findViewById<CardView>(R.id.cardChat)
            val cardTelp = view.findViewById<CardView>(R.id.cardTelepon)
            val chatIcon = view.findViewById<ImageView>(R.id.chatIcon)

            Constants.CHAT_DB.document(id + auth.currentUser!!.uid).get()
                .addOnSuccessListener { doc1 ->
                    if (doc1.exists()) {

                        val layoutParams =
                            chatIcon.layoutParams as RelativeLayout.LayoutParams
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START)
                        chatIcon.layoutParams = layoutParams

                        //showing chat indicator and peak message
                        setIndicator(id + auth.currentUser!!.uid,
                            doc1.getString("peakMessage").toString())

                        //showing chat date
                        time.visibility = View.VISIBLE
                        if (getTimeDateNow1(doc1.getTimestamp("updated")!!
                                .toDate()) == getTimeDateNow2()
                        ) {
                            time.text =
                                getTimeDateFirebase2(doc1.getTimestamp("updated")!!.toDate())
                        } else {
                            time.text = getTimeDateFirebase(doc1.getTimestamp("updated")!!.toDate())
                        }

                        //showing number of unread chat
                        Constants.CHAT_DB.document(id + auth.currentUser!!.uid).collection("CHAT")
                            .whereEqualTo("sender", id)
                            .whereEqualTo("seen", false).addSnapshotListener { value, error ->
                                if (value!!.size() == 0) {
                                    cardNumPeak.visibility = View.INVISIBLE
                                } else {
                                    cardNumPeak.visibility = View.VISIBLE
                                    numPeak.text = value.size().toString()
                                }
                            }

                    } else {
                        Constants.CHAT_DB.document(auth.currentUser!!.uid + id).get()
                            .addOnSuccessListener { doc2 ->
                                if (doc2.exists()) {

                                    val layoutParams =
                                        chatIcon.layoutParams as RelativeLayout.LayoutParams
                                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START)
                                    chatIcon.layoutParams = layoutParams

                                    //showing chat indicator and peak message
                                    setIndicator(auth.currentUser!!.uid + id,
                                        doc2.getString("peakMessage").toString())

                                    //showing chat date
                                    time.visibility = View.VISIBLE
                                    if (getTimeDateNow1(doc2.getTimestamp("updated")!!
                                            .toDate()) == getTimeDateNow2()
                                    ) {
                                        time.text =
                                            getTimeDateFirebase2(doc2.getTimestamp("updated")!!
                                                .toDate())
                                    } else {
                                        time.text =
                                            getTimeDateFirebase(doc2.getTimestamp("updated")!!
                                                .toDate())
                                    }

                                    //showing number of unread chat
                                    Constants.CHAT_DB.document(auth.currentUser!!.uid + id)
                                        .collection("CHAT").whereEqualTo("sender", id)
                                        .whereEqualTo("seen", false)
                                        .addSnapshotListener { value, error ->
                                            if (value!!.size() == 0) {
                                                cardNumPeak.visibility = View.INVISIBLE
                                            } else {
                                                cardNumPeak.visibility = View.VISIBLE
                                                numPeak.text = value.size().toString()
                                            }
                                        }
                                } else {

                                    val layoutParams =
                                        chatIcon.layoutParams as RelativeLayout.LayoutParams
                                    layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
                                    chatIcon.layoutParams = layoutParams

                                }
                            }
                    }
                }
        }

        fun setIndicator(id: String, idMessage: String) {
            val peakMessages = view.findViewById<TextView>(R.id.textView_peakMessage_chatList)
            val indicator = view.findViewById<CardView>(R.id.indicator)
            Constants.CHAT_DB.document(id).collection("CHAT").document(idMessage)
                .addSnapshotListener { value, error ->
                    if (value != null) {
                        peakMessages.visibility = View.VISIBLE
                        peakMessages.text = value.getString("message")
                        if (value.getString("sender").equals(auth.currentUser!!.uid)) {
                            indicator.visibility = View.VISIBLE
                        } else {
                            indicator.visibility = View.GONE
                        }

                        if (value.getBoolean("seen") == true) {
                            indicator.setCardBackgroundColor(Color.parseColor("#118EEA"))
                        } else {
                            indicator.setCardBackgroundColor(Color.parseColor("#B8CCDC"))
                        }
                    }
                }
        }

        private fun expand() {
            //set Visible
            val finalHeight: Int = view.findViewById<RelativeLayout>(R.id.expandMenu).height
            val mAnimator: ValueAnimator = slideAnimator(0, 180)
            mAnimator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationEnd(animator: Animator) {
                    //Height=0, but it set visibility to GONE
                }

                override fun onAnimationStart(animator: Animator) {
                    view.findViewById<RelativeLayout>(R.id.expandMenu).visibility = View.VISIBLE
                }

                override fun onAnimationCancel(animator: Animator) {}
                override fun onAnimationRepeat(animator: Animator) {}
            })

            mAnimator.start()
        }

        private fun collapse() {
            val finalHeight: Int = view.findViewById<RelativeLayout>(R.id.expandMenu).height
            val mAnimator: ValueAnimator = slideAnimator(finalHeight, 0)
            mAnimator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationEnd(animator: Animator) {
                    //Height=0, but it set visibility to GONE
                    view.findViewById<RelativeLayout>(R.id.expandMenu).visibility = View.GONE
                }

                override fun onAnimationStart(animator: Animator) {}
                override fun onAnimationCancel(animator: Animator) {}
                override fun onAnimationRepeat(animator: Animator) {}
            })
            mAnimator.start()
        }

        private fun slideAnimator(start: Int, end: Int): ValueAnimator {
            val animator: ValueAnimator = ValueAnimator.ofInt(start, end)
            animator.addUpdateListener { valueAnimator -> //Update Height
                val value: Int = valueAnimator.animatedValue as Int
                val layoutParams: ViewGroup.LayoutParams =
                    view.findViewById<RelativeLayout>(R.id.expandMenu).layoutParams
                layoutParams.height = value
                view.findViewById<RelativeLayout>(R.id.expandMenu).layoutParams = layoutParams
            }
            return animator
        }

        override fun onPreDraw(): Boolean {
            view.findViewById<RelativeLayout>(R.id.expandMenu).viewTreeObserver.removeOnPreDrawListener(
                this)
            if (i == "Faito") {
                view.findViewById<RelativeLayout>(R.id.expandMenu).visibility = View.VISIBLE
            } else {
                view.findViewById<RelativeLayout>(R.id.expandMenu).visibility = View.GONE
            }

            val widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            view.findViewById<RelativeLayout>(R.id.expandMenu).measure(widthSpec, heightSpec)

            mAnimator =
                slideAnimator(0, view.findViewById<RelativeLayout>(R.id.expandMenu).measuredHeight)
            return true
        }

    }

    private fun showMessage(s: String) {
        Toast.makeText(activity, s, Toast.LENGTH_LONG).show()
    }

    private inner class DaftarFirestoreRecyclerAdapter(options: FirestoreRecyclerOptions<User>) :
        FirestoreRecyclerAdapter<User, ProductViewHolder>(
            options
        ) {
        override fun onBindViewHolder(
            productViewHolder: ProductViewHolder,
            position: Int,
            model: User,
        ) {
            productViewHolder.setContentDistributor(model.id,
                model.nama,
                model.photo_url,
                model.namaOutlet,
                model.noHp)

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.item_customer,
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

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DaftarCustomerFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}