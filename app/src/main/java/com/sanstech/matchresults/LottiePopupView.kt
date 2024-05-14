package com.sanstech.matchresults

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.PopupWindow
import com.airbnb.lottie.LottieAnimationView

class LottiePopupView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val popupWindow: PopupWindow

    init {
        val animationView = LottieAnimationView(context)
        animationView.setAnimation(R.raw.loading)
        animationView.layoutParams = LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            Gravity.CENTER
        )
        animationView.playAnimation()
        addView(animationView)

        popupWindow = PopupWindow(
            this,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun show() {
        popupWindow.showAtLocation(this, Gravity.CENTER, 0, 0)
    }

    fun dismiss() {
        popupWindow.dismiss()
    }
}

