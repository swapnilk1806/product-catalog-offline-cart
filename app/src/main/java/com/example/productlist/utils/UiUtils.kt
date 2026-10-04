package com.example.productlist.utils

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.widget.Button
import java.util.Locale

fun Activity.dp(value: Int): Int {
    return (
            value * resources.displayMetrics.density
            ).toInt()
}

fun Activity.styleButton(
    button: Button,
    color: Int = Color.rgb(33, 150, 243)
) {

    button.minHeight = 0
    button.minimumHeight = 0

    button.setPadding(
        dp(10),
        0,
        dp(10),
        0
    )

    button.gravity = Gravity.CENTER

    button.textSize = 16f

    button.setTypeface(
        null,
        Typeface.BOLD
    )

    button.isAllCaps = false

    button.setTextColor(Color.WHITE)

    val drawable =
        GradientDrawable()

    drawable.setColor(color)

    drawable.cornerRadius =
        dp(12).toFloat()

    button.background = drawable
}

fun Activity.styleCard(
    view: View
) {

    val drawable =
        GradientDrawable()

    drawable.setColor(Color.WHITE)

    drawable.cornerRadius =
        dp(14).toFloat()

    drawable.setStroke(
        dp(1),
        Color.rgb(225, 225, 225)
    )

    view.background = drawable

    view.elevation =
        dp(3).toFloat()
}

fun formatPrice(
    price: Double
): String {

    return String.format(
        Locale.US,
        "%.2f",
        price
    )
}