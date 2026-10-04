package com.example.productlist

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import com.example.productlist.api.Api
import com.example.productlist.database.CartDatabase
import com.example.productlist.ui.createCartCard
import com.example.productlist.ui.createProductCard
import com.example.productlist.ui.showProductDetails
import com.example.productlist.utils.dp
import com.example.productlist.utils.formatPrice
import com.example.productlist.utils.styleButton
import com.example.productlist.utils.styleCard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.productlist.model.Product

class MainActivity : Activity() {

    private lateinit var root: LinearLayout

    lateinit var content: FrameLayout
        private set

    lateinit var database: CartDatabase
        private set

    private val scope =
        CoroutineScope(
            SupervisorJob() +
                    Dispatchers.Main
        )

    private val products =
        mutableListOf<Product>()

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(
            savedInstanceState
        )

        database =
            CartDatabase(this)

        buildMainLayout()

        showProductsScreen()
    }

    override fun onDestroy() {

        scope.cancel()

        database.close()

        super.onDestroy()
    }

    // =========================================================
    // MAIN LAYOUT
    // =========================================================

    private fun buildMainLayout() {

        root =
            LinearLayout(this)

        root.orientation =
            LinearLayout.VERTICAL

        root.setBackgroundColor(
            Color.rgb(
                248,
                249,
                250
            )
        )

        // HEADER

        val header =
            LinearLayout(this)

        header.orientation =
            LinearLayout.HORIZONTAL

        header.gravity =
            Gravity.CENTER_VERTICAL

        header.setPadding(
            dp(16),
            dp(10),
            dp(16),
            dp(10)
        )

        header.setBackgroundColor(
            Color.WHITE
        )

        val title =
            TextView(this)

        title.text =
            "Product Store"

        title.textSize = 24f

        title.setTypeface(
            null,
            Typeface.BOLD
        )

        title.setTextColor(
            Color.BLACK
        )

        title.gravity =
            Gravity.CENTER_VERTICAL

        header.addView(
            title,
            LinearLayout.LayoutParams(
                0,
                dp(55),
                1f
            )
        )

        val cartButton =
            Button(this)

        cartButton.text =
            "🛒  Cart"

        styleButton(cartButton)

        header.addView(
            cartButton,
            LinearLayout.LayoutParams(
                dp(110),
                dp(52)
            )
        )

        cartButton.setOnClickListener {
            showCartScreen()
        }

        root.addView(header)

        // CONTENT

        content =
            FrameLayout(this)

        root.addView(
            content,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        )

        setContentView(root)
    }

    // =========================================================
    // INTERNET
    // =========================================================

    private fun isInternetAvailable(): Boolean {

        val manager =
            getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager

        val network =
            manager.activeNetwork
                ?: return false

        val capabilities =
            manager.getNetworkCapabilities(
                network
            )
                ?: return false

        return capabilities.hasCapability(
            NetworkCapabilities.NET_CAPABILITY_INTERNET
        )
    }

    // =========================================================
    // PRODUCTS SCREEN
    // =========================================================

    fun showProductsScreen() {

        content.removeAllViews()

        val layout =
            LinearLayout(this)

        layout.orientation =
            LinearLayout.VERTICAL

        // SEARCH

        val searchLayout =
            LinearLayout(this)

        searchLayout.orientation =
            LinearLayout.HORIZONTAL

        searchLayout.gravity =
            Gravity.CENTER_VERTICAL

        searchLayout.setPadding(
            dp(12),
            dp(12),
            dp(12),
            dp(8)
        )

        val search =
            EditText(this)

        search.hint =
            "Search products..."

        search.textSize = 16f

        search.setSingleLine(true)

        search.setPadding(
            dp(14),
            0,
            dp(14),
            0
        )

        searchLayout.addView(
            search,
            LinearLayout.LayoutParams(
                0,
                dp(52),
                1f
            )
        )

        val searchButton =
            Button(this)

        searchButton.text =
            "Search"

        styleButton(searchButton)

        searchLayout.addView(
            searchButton,
            LinearLayout.LayoutParams(
                dp(105),
                dp(52)
            )
        )

        layout.addView(searchLayout)

        // PRODUCT CONTAINER

        val productContainer =
            FrameLayout(this)

        layout.addView(
            productContainer,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        )

        content.addView(layout)

        searchButton.setOnClickListener {

            val query =
                search.text
                    .toString()
                    .trim()

            if (query.isEmpty()) {

                loadProducts(
                    productContainer
                )

            } else {

                searchProducts(
                    query,
                    productContainer
                )
            }
        }

        search.setOnEditorActionListener { _, _, _ ->

            searchButton.performClick()

            true
        }

        loadProducts(
            productContainer
        )
    }

    // =========================================================
    // LOAD PRODUCTS
    // =========================================================

    private fun loadProducts(
        container: FrameLayout
    ) {

        showLoading(container)

        scope.launch {

            try {

                if (!isInternetAvailable()) {

                    throw Exception(
                        "No internet connection.\n" +
                                "Please connect to the internet and retry."
                    )
                }

                val result =
                    withContext(
                        Dispatchers.IO
                    ) {
                        Api.getProducts()
                    }

                products.clear()

                products.addAll(result)

                if (products.isEmpty()) {

                    showEmpty(
                        container,
                        "No products available."
                    )

                } else {

                    renderProductList(
                        container,
                        products
                    )
                }

            } catch (e: Exception) {

                showError(
                    container,
                    e.message
                        ?: "Unable to load products."
                ) {
                    loadProducts(container)
                }
            }
        }
    }

    // =========================================================
    // SEARCH
    // =========================================================

    private fun searchProducts(
        query: String,
        container: FrameLayout
    ) {

        showLoading(container)

        scope.launch {

            try {

                if (!isInternetAvailable()) {

                    throw Exception(
                        "No internet connection.\n" +
                                "Please connect to the internet and retry."
                    )
                }

                val result =
                    withContext(
                        Dispatchers.IO
                    ) {
                        Api.searchProducts(
                            query
                        )
                    }

                if (result.isEmpty()) {

                    showEmpty(
                        container,
                        "No products found for\n\"$query\""
                    )

                } else {

                    renderProductList(
                        container,
                        result
                    )
                }

            } catch (e: Exception) {

                showError(
                    container,
                    e.message
                        ?: "Search failed."
                ) {

                    searchProducts(
                        query,
                        container
                    )
                }
            }
        }
    }

    // =========================================================
    // PRODUCT LIST
    // =========================================================

    private fun renderProductList(
        container: FrameLayout,
        list: List<Product>
    ) {

        container.removeAllViews()

        if (list.isEmpty()) {

            showEmpty(
                container,
                "No products available."
            )

            return
        }

        val scroll =
            ScrollView(this)

        val listLayout =
            LinearLayout(this)

        listLayout.orientation =
            LinearLayout.VERTICAL

        listLayout.setPadding(
            dp(12),
            dp(6),
            dp(12),
            dp(30)
        )

        for (product in list) {

            val card =
                createProductCard(product)

            listLayout.addView(card)

            card.setOnClickListener {

                showProductDetails(
                    product
                )
            }
        }

        scroll.addView(listLayout)

        container.addView(scroll)
    }

    // =========================================================
    // CART SCREEN
    // =========================================================

    fun showCartScreen() {

        content.removeAllViews()

        val layout =
            LinearLayout(this)

        layout.orientation =
            LinearLayout.VERTICAL

        // HEADER

        val header =
            LinearLayout(this)

        header.orientation =
            LinearLayout.HORIZONTAL

        header.gravity =
            Gravity.CENTER_VERTICAL

        header.setPadding(
            dp(12),
            dp(10),
            dp(12),
            dp(10)
        )

        val back =
            Button(this)

        back.text =
            "← Products"

        styleButton(
            back,
            Color.rgb(90, 90, 90)
        )

        header.addView(
            back,
            LinearLayout.LayoutParams(
                dp(125),
                dp(50)
            )
        )

        back.setOnClickListener {
            showProductsScreen()
        }

        val title =
            TextView(this)

        title.text =
            "Shopping Cart"

        title.textSize = 23f

        title.setTypeface(
            null,
            Typeface.BOLD
        )

        title.setTextColor(
            Color.BLACK
        )

        title.setPadding(
            dp(15),
            0,
            0,
            0
        )

        header.addView(
            title,
            LinearLayout.LayoutParams(
                0,
                dp(50),
                1f
            )
        )

        layout.addView(header)

        // CART CONTAINER

        val cartContainer =
            FrameLayout(this)

        layout.addView(
            cartContainer,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        )

        content.addView(layout)

        renderCart(cartContainer)
    }

    // =========================================================
    // RENDER CART
    // =========================================================

    fun renderCart(
        container: FrameLayout
    ) {

        container.removeAllViews()

        val items =
            database.getCart()

        if (items.isEmpty()) {

            showEmpty(
                container,
                "🛒\n\nYour cart is empty"
            )

            return
        }

        val scroll =
            ScrollView(this)

        val layout =
            LinearLayout(this)

        layout.orientation =
            LinearLayout.VERTICAL

        layout.setPadding(
            dp(12),
            dp(8),
            dp(12),
            dp(30)
        )

        var totalItems = 0

        var totalPrice = 0.0

        for (item in items) {

            totalItems +=
                item.quantity

            totalPrice +=
                item.product.price *
                        item.quantity

            val card =
                createCartCard(
                    item,
                    container
                )

            layout.addView(card)
        }

        // TOTAL BOX

        val totalBox =
            LinearLayout(this)

        totalBox.orientation =
            LinearLayout.VERTICAL

        totalBox.setPadding(
            dp(18),
            dp(18),
            dp(18),
            dp(18)
        )

        styleCard(totalBox)

        val totalItemsText =
            TextView(this)

        totalItemsText.text =
            "Total Items: $totalItems"

        totalItemsText.textSize = 18f

        totalItemsText.setTypeface(
            null,
            Typeface.BOLD
        )

        totalBox.addView(
            totalItemsText
        )

        val totalPriceText =
            TextView(this)

        totalPriceText.text =
            "Total Price: ₹${
                formatPrice(totalPrice)
            }"

        totalPriceText.textSize = 23f

        totalPriceText.setTypeface(
            null,
            Typeface.BOLD
        )

        totalPriceText.setTextColor(
            Color.rgb(0, 125, 70)
        )

        totalPriceText.setPadding(
            0,
            dp(8),
            0,
            0
        )

        totalBox.addView(
            totalPriceText
        )

        val totalParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

        totalParams.setMargins(
            0,
            dp(5),
            0,
            dp(15)
        )

        layout.addView(
            totalBox,
            totalParams
        )

        scroll.addView(layout)

        container.addView(scroll)
    }

    // =========================================================
    // LOADING
    // =========================================================

    private fun showLoading(
        container: FrameLayout
    ) {

        container.removeAllViews()

        val layout =
            LinearLayout(this)

        layout.orientation =
            LinearLayout.VERTICAL

        layout.gravity =
            Gravity.CENTER

        val progress =
            ProgressBar(this)

        layout.addView(
            progress,
            LinearLayout.LayoutParams(
                dp(60),
                dp(60)
            )
        )

        val text =
            TextView(this)

        text.text =
            "Loading products..."

        text.textSize = 17f

        text.gravity =
            Gravity.CENTER

        text.setPadding(
            0,
            dp(15),
            0,
            0
        )

        layout.addView(text)

        container.addView(
            layout,
            FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
    }

    // =========================================================
    // EMPTY
    // =========================================================

    private fun showEmpty(
        container: FrameLayout,
        message: String
    ) {

        container.removeAllViews()

        val text =
            TextView(this)

        text.text =
            message

        text.textSize = 20f

        text.gravity =
            Gravity.CENTER

        text.setTextColor(
            Color.rgb(80, 80, 80)
        )

        text.setPadding(
            dp(30),
            dp(30),
            dp(30),
            dp(30)
        )

        container.addView(
            text,
            FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
    }

    // =========================================================
    // ERROR
    // =========================================================

    private fun showError(
        container: FrameLayout,
        message: String,
        retryAction: () -> Unit
    ) {

        container.removeAllViews()

        val layout =
            LinearLayout(this)

        layout.orientation =
            LinearLayout.VERTICAL

        layout.gravity =
            Gravity.CENTER

        layout.setPadding(
            dp(30),
            dp(30),
            dp(30),
            dp(30)
        )

        val error =
            TextView(this)

        error.text =
            "⚠️\n\n$message"

        error.textSize = 18f

        error.gravity =
            Gravity.CENTER

        error.setTextColor(
            Color.rgb(90, 90, 90)
        )

        layout.addView(error)

        val retry =
            Button(this)

        retry.text =
            "Retry"

        retry.textSize = 16f

        styleButton(retry)

        val retryParams =
            LinearLayout.LayoutParams(
                dp(130),
                dp(52)
            )

        retryParams.setMargins(
            0,
            dp(20),
            0,
            0
        )

        layout.addView(
            retry,
            retryParams
        )

        retry.setOnClickListener {
            retryAction()
        }

        container.addView(
            layout,
            FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
    }
}