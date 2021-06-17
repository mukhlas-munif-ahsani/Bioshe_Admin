package com.munifahsan.biosheadmin.ui.pageHome

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import com.munifahsan.biosheadmin.databinding.FragmentHomeBinding
import com.munifahsan.biosheadmin.ui.karyawan.KaryawanActivity


class HomeFragment : Fragment(), ViewTreeObserver.OnPreDrawListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    lateinit var mAnimator: ValueAnimator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.expandable.viewTreeObserver.addOnPreDrawListener(this)

        binding.header.setOnClickListener {
            if (binding.expandable.visibility == View.GONE){
                expand();
            }else{
                collapse();
            }
        }

        binding.karyawanBtn.setOnClickListener {
            val intent = Intent(activity, KaryawanActivity::class.java)
            startActivity(intent)
        }
        return view
    }

    private fun expand() {
        //set Visible
        binding.expandable.visibility = View.VISIBLE

        /* Remove and used in preDrawListener
		final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		mLinearLayout.measure(widthSpec, heightSpec);
		mAnimator = slideAnimator(0, mLinearLayout.getMeasuredHeight());
		*/
        mAnimator.start()
    }

    private fun collapse() {
        val finalHeight: Int = binding.expandable.height
        val mAnimator: ValueAnimator = slideAnimator(finalHeight, 0)
        mAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationEnd(animator: Animator) {
                //Height=0, but it set visibility to GONE
                binding.expandable.visibility = View.GONE
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
            val layoutParams: ViewGroup.LayoutParams = binding.expandable.layoutParams
            layoutParams.height = value
            binding.expandable.layoutParams = layoutParams
        }
        return animator
    }

    companion object {
        @JvmStatic
        fun newInstance(): HomeFragment {
                val fragment = HomeFragment()
                val args = Bundle()
                fragment.arguments = args
                return fragment
            }
    }

    override fun onPreDraw(): Boolean {
        binding.expandable.viewTreeObserver.removeOnPreDrawListener(this)
        binding.expandable.visibility = View.GONE

        val widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        binding.expandable.measure(widthSpec, heightSpec)

        mAnimator = slideAnimator(0, binding.expandable.measuredHeight)
        return true
    }
}