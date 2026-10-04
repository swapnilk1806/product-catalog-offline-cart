package com.example.productlist.ui

import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.productlist.MainActivity
import com.example.productlist.model.Product
import com.example.productlist.utils.dp
import com.example.productlist.utils.loadImage
import com.example.productlist.utils.styleCard
import com.example.productlist.utils.formatPrice

fun MainActivity.createProductCard(
    product: Product
): LinearLayout {

    val card =
        LinearLayout(this)

    card.orientation =
        LinearLayout.HORIZONTAL

    card.gravity =
        Gravity.CENTER_VERTICAL

    card.setPadding(
        dp(12),
        dp(12),
        dp(12),
        dp(12)
    )

    styleCard(card)

    // IMAGE

    val image =
        ImageView(this)

    image.scaleType =
        ImageView.ScaleType.CENTER_CROP

    card.addView(
        image,
        LinearLayout.LayoutParams(
            dp(145),
            dp(145)
        )
    )

    loadImage(
        product.thumbnail,
        image
    )

    // INFO

    val info =
        LinearLayout(this)

    info.orientation =
        LinearLayout.VERTICAL

    info.gravity =
        Gravity.CENTER_VERTICAL

    info.setPadding(
        dp(14),
        0,
        dp(5),
        0
    )

    // TITLE

    val title =
        TextView(this)

    title.text =
        product.title

    title.textSize = 18f

    title.setTypeface(
        null,
        Typeface.BOLD
    )

    title.setTextColor(
        Color.BLACK
    )

    title.maxLines = 3

    info.addView(title)

    // PRICE

    val price =
        TextView(this)

    price.text =
        "₹${formatPrice(product.price)}"

    price.textSize = 19f

    price.setTypeface(
        null,
        Typeface.BOLD
    )

    price.setTextColor(
        Color.rgb(0, 125, 70)
    )

    price.setPadding(
        0,
        dp(8),
        0,
        dp(5)
    )

    info.addView(price)

    // RATING

    val rating =
        TextView(this)

    rating.text =
        "★ ${product.rating}"

    rating.textSize = 16f

    rating.setTypeface(
        null,
        Typeface.BOLD
    )

    rating.setTextColor(
        Color.rgb(245, 166, 35)
    )

    info.addView(rating)

    // STOCK

    val stock =
        TextView(this)

    stock.text =
        "Stock: ${product.stock}"

    stock.textSize = 14f

    stock.setTextColor(
        Color.DKGRAY
    )

    info.addView(stock)

    card.addView(
        info,
        LinearLayout.LayoutParams(
            0,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            1f
        )
    )

    val params =
        LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

    params.setMargins(
        0,
        0,
        0,
        dp(14)
    )

    card.layoutParams = params

    return card
}