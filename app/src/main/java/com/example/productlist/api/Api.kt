package com.example.productlist.api

import com.example.productlist.model.Product
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

object Api {

    private const val BASE_URL = "https://dummyjson.com"

    fun getProducts(): List<Product> {

        val response = request(
            "$BASE_URL/products?limit=100"
        )

        return parseProducts(response)
    }

    fun searchProducts(query: String): List<Product> {

        val encoded = URLEncoder.encode(
            query,
            "UTF-8"
        )

        val response = request(
            "$BASE_URL/products/search?q=$encoded"
        )

        return parseProducts(response)
    }

    private fun request(
        urlString: String
    ): String {

        val url = URL(urlString)

        val connection =
            url.openConnection() as HttpURLConnection

        connection.requestMethod = "GET"

        connection.connectTimeout = 10000
        connection.readTimeout = 10000

        connection.setRequestProperty(
            "Accept",
            "application/json"
        )

        connection.connect()

        val status = connection.responseCode

        if (status !in 200..299) {
            connection.disconnect()

            throw Exception(
                "Server error: HTTP $status"
            )
        }

        val stream =
            BufferedInputStream(
                connection.inputStream
            )

        val output = ByteArrayOutputStream()

        val buffer = ByteArray(4096)

        var bytesRead: Int

        while (
            stream.read(buffer).also {
                bytesRead = it
            } != -1
        ) {
            output.write(
                buffer,
                0,
                bytesRead
            )
        }

        stream.close()
        connection.disconnect()

        return output.toString("UTF-8")
    }

    private fun parseProducts(
        json: String
    ): List<Product> {

        val result = mutableListOf<Product>()

        val root = JSONObject(json)

        val array =
            root.optJSONArray("products")
                ?: JSONArray()

        for (i in 0 until array.length()) {

            val item =
                array.getJSONObject(i)

            result.add(
                Product(

                    id = item.optInt("id"),

                    title = item.optString(
                        "title"
                    ),

                    description = item.optString(
                        "description"
                    ),

                    price = item.optDouble(
                        "price"
                    ),

                    rating = item.optDouble(
                        "rating"
                    ),

                    category = item.optString(
                        "category"
                    ),

                    brand = item.optString(
                        "brand",
                        "Unknown"
                    ),

                    stock = item.optInt(
                        "stock"
                    ),

                    thumbnail = item.optString(
                        "thumbnail"
                    )
                )
            )
        }

        return result
    }
}