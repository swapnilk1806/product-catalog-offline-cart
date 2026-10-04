package com.example.productlist.ui

import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.productlist.MainActivity
import com.example.productlist.model.CartItem
import com.example.productlist.utils.dp
import com.example.productlist.utils.formatPrice
import com.example.productlist.utils.styleButton
import com.example.productlist.utils.styleCard

fun MainActivity.createCartCard(
    item: CartItem,
    container: FrameLayout
): LinearLayout {

    val card =
        LinearLayout(this)

    card.orientation =
        LinearLayout.VERTICAL

    card.setPadding(
        dp(15),
        dp(15),
        dp(15),
        dp(15)
    )

    styleCard(card)

    // TITLE

    val title =
        TextView(this)

    title.text =
        item.product.title

    title.textSize = 18f

    title.setTypeface(
        null,
        Typeface.BOLD
    )

    title.setTextColor(
        Color.BLACK
    )

    title.maxLines = 2

    card.addView(title)

    // PRICE

    val price =
        TextView(this)

    price.text =
        "₹${formatPrice(item.product.price)} each"

    price.textSize = 16f

    price.setTextColor(
        Color.DKGRAY
    )

    price.setPadding(
        0,
        dp(5),
        0,
        dp(10)
    )

    card.addView(price)

    // CONTROLS

    val controls =
        LinearLayout(this)

    controls.orientation =
        LinearLayout.HORIZONTAL

    controls.gravity =
        Gravity.CENTER_VERTICAL

    // MINUS

    val minus =
        Button(this)

    minus.text = "−"

    minus.textSize = 23f

    styleButton(
        minus,
        Color.rgb(80, 80, 80)
    )

    controls.addView(
        minus,
        LinearLayout.LayoutParams(
            dp(52),
            dp(52)
        )
    )

    // QUANTITY

    val quantity =
        TextView(this)

    quantity.text =
        item.quantity.toString()

    quantity.textSize = 20f

    quantity.gravity =
        Gravity.CENTER

    quantity.setTypeface(
        null,
        Typeface.BOLD
    )

    quantity.setTextColor(
        Color.BLACK
    )

    controls.addView(
        quantity,
        LinearLayout.LayoutParams(
            dp(55),
            dp(52)
        )
    )

    // PLUS

    val plus =
        Button(this)

    plus.text = "+"

    plus.textSize = 22f

    styleButton(plus)

    controls.addView(
        plus,
        LinearLayout.LayoutParams(
            dp(52),
            dp(52)
        )
    )

    // REMOVE

    val remove =
        Button(this)

    remove.text =
        "Remove"

    remove.textSize = 14f

    styleButton(
        remove,
        Color.rgb(220, 53, 69)
    )

    val removeParams =
        LinearLayout.LayoutParams(
            dp(105),
            dp(52)
        )

    removeParams.setMargins(
        dp(10),
        0,
        0,
        0
    )

    controls.addView(
        remove,
        removeParams
    )

    card.addView(controls)

    // SUBTOTAL

    val subtotal =
        TextView(this)

    subtotal.text =
        "Subtotal: ₹${
            formatPrice(
                item.product.price *
                        item.quantity
            )
        }"

    subtotal.textSize = 16f

    subtotal.setTypeface(
        null,
        Typeface.BOLD
    )

    subtotal.setTextColor(
        Color.rgb(0, 100, 60)
    )

    subtotal.setPadding(
        0,
        dp(10),
        0,
        0
    )

    card.addView(subtotal)

    // MINUS ACTION

    minus.setOnClickListener {

        if (item.quantity > 1) {

            database.updateQuantity(
                item.product.id,
                item.quantity - 1
            )

        } else {

            database.removeProduct(
                item.product.id
            )
        }

        renderCart(container)
    }

    // PLUS ACTION

    plus.setOnClickListener {

        if (
            item.quantity <
            item.product.stock
        ) {

            database.updateQuantity(
                item.product.id,
                item.quantity + 1
            )

            renderCart(container)

        } else {

            Toast.makeText(
                this,
                "Maximum stock reached",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // REMOVE ACTION

    remove.setOnClickListener {

        database.removeProduct(
            item.product.id
        )

        Toast.makeText(
            this,
            "Removed from cart",
            Toast.LENGTH_SHORT
        ).show()

        renderCart(container)
    }

    val params =
        LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

    params.setMargins(
        0,
        0,
        0,
        dp(12)
    )

    card.layoutParams = params

    return card
}