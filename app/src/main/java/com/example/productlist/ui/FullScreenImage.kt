package com.example.productlist.ui

import android.app.Dialog
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import com.example.productlist.MainActivity
import com.example.productlist.utils.dp
import com.example.productlist.utils.loadImage

fun MainActivity.showFullScreenImage(
    imageUrl: String
) {

    val dialog = Dialog(this)

    dialog.window?.setBackgroundDrawableResource(
        android.R.color.transparent
    )

    val frame = FrameLayout(this)

    frame.setBackgroundColor(
        Color.BLACK
    )

    val image = ImageView(this)

    image.scaleType =
        ImageView.ScaleType.FIT_CENTER

    frame.addView(
        image,
        FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    )

    val close = Button(this)

    close.text = "✕"
    close.textSize = 22f

    close.setTextColor(Color.WHITE)

    close.setBackgroundColor(
        Color.TRANSPARENT
    )

    val closeParams =
        FrameLayout.LayoutParams(
            dp(60),
            dp(60)
        )

    closeParams.gravity =
        Gravity.TOP or Gravity.END

    frame.addView(
        close,
        closeParams
    )

    close.setOnClickListener {
        dialog.dismiss()
    }

    image.setOnClickListener {
        dialog.dismiss()
    }

    dialog.setContentView(frame)

    dialog.show()

    dialog.window?.setLayout(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )

    loadImage(
        imageUrl,
        image
    )
}