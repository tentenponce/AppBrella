package com.tcorner.appbrella.util

import android.view.View
import android.view.ViewPropertyAnimator

class AnimateUtil {

    companion object {
        /**
         * helper method for the animator
         */
        fun viewPropertyStartCompat(animator: ViewPropertyAnimator) {
            animator.start()
        }

        /**
         * helper method to animate the heart view
         */
        fun animateFadeInFadeOut(firstImage: View, secondImage: View, on: Boolean) {
            firstImage.visibility = View.VISIBLE
            secondImage.visibility = View.VISIBLE

            viewPropertyStartCompat(secondImage.animate().scaleX((if (on) 0 else 1).toFloat()).scaleY((if (on) 0 else 1).toFloat()).alpha((if (on) 0 else 1).toFloat()))
            viewPropertyStartCompat(firstImage.animate().scaleX((if (on) 1 else 0).toFloat()).scaleY((if (on) 1 else 0).toFloat()).alpha((if (on) 1 else 0).toFloat()))
        }
    }
}