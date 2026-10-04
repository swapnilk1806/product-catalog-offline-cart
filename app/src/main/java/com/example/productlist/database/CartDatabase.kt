package com.example.productlist.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.productlist.model.CartItem
import com.example.productlist.model.Product

class CartDatabase(
    context: Context
) : SQLiteOpenHelper(
    context,
    "product_store.db",
    null,
    1
) {

    override fun onCreate(
        db: SQLiteDatabase
    ) {

        db.execSQL(
            """
            CREATE TABLE cart (
                id INTEGER PRIMARY KEY,
                title TEXT NOT NULL,
                description TEXT,
                price REAL NOT NULL,
                rating REAL,
                category TEXT,
                brand TEXT,
                stock INTEGER,
                thumbnail TEXT,
                quantity INTEGER NOT NULL
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {

        db.execSQL(
            "DROP TABLE IF EXISTS cart"
        )

        onCreate(db)
    }

    fun addProduct(
        product: Product
    ): Boolean {

        val db = writableDatabase

        val cursor = db.rawQuery(
            "SELECT quantity FROM cart WHERE id = ?",
            arrayOf(
                product.id.toString()
            )
        )

        if (cursor.moveToFirst()) {

            val oldQuantity =
                cursor.getInt(0)

            cursor.close()

            if (oldQuantity >= product.stock) {
                return false
            }

            updateQuantity(
                product.id,
                oldQuantity + 1
            )

            return true
        }

        cursor.close()

        val values = ContentValues()

        values.put(
            "id",
            product.id
        )

        values.put(
            "title",
            product.title
        )

        values.put(
            "description",
            product.description
        )

        values.put(
            "price",
            product.price
        )

        values.put(
            "rating",
            product.rating
        )

        values.put(
            "category",
            product.category
        )

        values.put(
            "brand",
            product.brand
        )

        values.put(
            "stock",
            product.stock
        )

        values.put(
            "thumbnail",
            product.thumbnail
        )

        values.put(
            "quantity",
            1
        )

        db.insert(
            "cart",
            null,
            values
        )

        return true
    }

    fun updateQuantity(
        productId: Int,
        quantity: Int
    ) {

        if (quantity <= 0) {
            removeProduct(productId)
            return
        }

        val values = ContentValues()

        values.put(
            "quantity",
            quantity
        )

        writableDatabase.update(
            "cart",
            values,
            "id = ?",
            arrayOf(
                productId.toString()
            )
        )
    }

    fun removeProduct(
        productId: Int
    ) {

        writableDatabase.delete(
            "cart",
            "id = ?",
            arrayOf(
                productId.toString()
            )
        )
    }

    fun getCart(): List<CartItem> {

        val result =
            mutableListOf<CartItem>()

        val cursor =
            readableDatabase.rawQuery(
                "SELECT * FROM cart ORDER BY id DESC",
                null
            )

        while (cursor.moveToNext()) {

            val product = Product(

                id = cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                        "id"
                    )
                ),

                title = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        "title"
                    )
                ),

                description = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        "description"
                    )
                ),

                price = cursor.getDouble(
                    cursor.getColumnIndexOrThrow(
                        "price"
                    )
                ),

                rating = cursor.getDouble(
                    cursor.getColumnIndexOrThrow(
                        "rating"
                    )
                ),

                category = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        "category"
                    )
                ),

                brand = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        "brand"
                    )
                ),

                stock = cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                        "stock"
                    )
                ),

                thumbnail = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        "thumbnail"
                    )
                )
            )

            val quantity =
                cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                        "quantity"
                    )
                )

            result.add(
                CartItem(
                    product,
                    quantity
                )
            )
        }

        cursor.close()

        return result
    }
}