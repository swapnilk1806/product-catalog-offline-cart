
package com.example.productlist.ui

import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import com.example.productlist.MainActivity
import com.example.productlist.model.Product
import com.example.productlist.utils.dp
import com.example.productlist.utils.formatPrice
import com.example.productlist.utils.loadImage
import com.example.productlist.utils.styleButton
import com.example.productlist.utils.styleCard

fun MainActivity.showProductDetails(
    product: Product
) {

    content.removeAllViews()

    val scroll = ScrollView(this)

    val layout = LinearLayout(this)

    layout.orientation = LinearLayout.VERTICAL

    layout.setPadding(
        dp(18),
        dp(16),
        dp(18),
        dp(30)
    )

    // =========================================================
    // BACK BUTTON
    // =========================================================

    val back = Button(this)

    back.text = "←  Back"

    styleButton(
        back,
        Color.rgb(90, 90, 90)
    )

    layout.addView(
        back,
        LinearLayout.LayoutParams(
            dp(110),
            dp(50)
        )
    )

    back.setOnClickListener {
        showProductsScreen()
    }

    // =========================================================
    // PRODUCT IMAGE
    // =========================================================

    val imageCard = FrameLayout(this)

    styleCard(imageCard)

    val image = ImageView(this)

    image.scaleType =
        ImageView.ScaleType.FIT_CENTER

    image.setPadding(
        dp(10),
        dp(10),
        dp(10),
        dp(10)
    )

    imageCard.addView(
        image,
        FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            dp(420)
        )
    )

    val imageCardParams =
        LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            dp(420)
        )

    imageCardParams.setMargins(
        0,
        dp(15),
        0,
        dp(10)
    )

    layout.addView(
        imageCard,
        imageCardParams
    )

    loadImage(
        product.thumbnail,
        image
    )

    // Click image → large image

    image.setOnClickListener {

        showFullScreenImage(
            product.thumbnail
        )
    }

    // =========================================================
    // IMAGE HINT
    // =========================================================

    val imageHint = TextView(this)

    imageHint.text =
        "Tap image to view larger"

    imageHint.textSize = 13f

    imageHint.gravity =
        Gravity.CENTER

    imageHint.setTextColor(
        Color.GRAY
    )

    layout.addView(imageHint)

    // =========================================================
    // TITLE
    // =========================================================

    val title = TextView(this)

    title.text =
        product.title

    title.textSize = 27f

    title.setTypeface(
        null,
        Typeface.BOLD
    )

    title.setTextColor(
        Color.BLACK
    )

    title.setPadding(
        0,
        dp(18),
        0,
        0
    )

    layout.addView(title)

    // =========================================================
    // PRICE
    // =========================================================

    val price = TextView(this)

    price.text =
        "₹${formatPrice(product.price)}"

    price.textSize = 25f

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
        dp(8)
    )

    layout.addView(price)

    // =========================================================
    // RATING
    // =========================================================

    val rating = TextView(this)

    rating.text =
        "★ ${product.rating} / 5"

    rating.textSize = 18f

    rating.setTypeface(
        null,
        Typeface.BOLD
    )

    rating.setTextColor(
        Color.rgb(245, 166, 35)
    )

    layout.addView(rating)

    // =========================================================
    // PRODUCT INFORMATION
    // =========================================================

    layout.addView(
        detailText(
            "Category",
            product.category
        )
    )

    layout.addView(
        detailText(
            "Brand",
            product.brand
        )
    )

    layout.addView(
        detailText(
            "Stock",
            product.stock.toString()
        )
    )

    // =========================================================
    // DESCRIPTION TITLE
    // =========================================================

    val descriptionTitle =
        TextView(this)

    descriptionTitle.text =
        "Description"

    descriptionTitle.textSize = 20f

    descriptionTitle.setTypeface(
        null,
        Typeface.BOLD
    )

    descriptionTitle.setPadding(
        0,
        dp(24),
        0,
        dp(8)
    )

    layout.addView(
        descriptionTitle
    )

    // =========================================================
    // DESCRIPTION
    // =========================================================

    val description =
        TextView(this)

    description.text =
        product.description

    description.textSize = 16f

    description.setTextColor(
        Color.DKGRAY
    )

    description.setLineSpacing(
        0f,
        1.15f
    )

    layout.addView(
        description
    )

    // =========================================================
    // ADD TO CART
    // =========================================================

    val addButton =
        Button(this)

    addButton.text =
        "🛒  Add to Cart"

    addButton.textSize = 17f

    styleButton(addButton)

    val addParams =
        LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            dp(56)
        )

    addParams.setMargins(
        0,
        dp(25),
        0,
        dp(10)
    )

    layout.addView(
        addButton,
        addParams
    )

    addButton.setOnClickListener {

        val added =
            database.addProduct(product)

        if (added) {

            Toast.makeText(
                this,
                "Added to cart",
                Toast.LENGTH_SHORT
            ).show()

        } else {

            Toast.makeText(
                this,
                "Maximum stock reached",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // =========================================================
    // VIEW CART
    // =========================================================

    val viewCart =
        Button(this)

    viewCart.text =
        "🛒  View Cart"

    viewCart.textSize = 17f

    styleButton(
        viewCart,
        Color.rgb(45, 45, 45)
    )

    layout.addView(
        viewCart,
        LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            dp(56)
        )
    )

    viewCart.setOnClickListener {

        showCartScreen()
    }

    // =========================================================
    // ADD TO SCREEN
    // =========================================================

    scroll.addView(layout)

    content.addView(scroll)
}


// =============================================================
// PRODUCT DETAIL ROW
// IMPORTANT:
// This is now an extension function of MainActivity.
// Therefore `this` and `dp()` are valid here.
// =============================================================

private fun MainActivity.detailText(
    label: String,
    value: String
): TextView {

    val text =
        TextView(this)

    text.text =
        "$label: $value"

    text.textSize = 16f

    text.setTextColor(
        Color.DKGRAY
    )

    text.setPadding(
        0,
        dp(8),
        0,
        0
    )

    return text
}
