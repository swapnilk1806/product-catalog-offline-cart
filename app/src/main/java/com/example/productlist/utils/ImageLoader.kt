
package com.example.productlist.utils

import android.graphics.BitmapFactory
import android.widget.ImageView
import com.example.productlist.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.net.HttpURLConnection
import java.net.URL

fun MainActivity.loadImage(
    imageUrl: String,
    imageView: ImageView
) {

    CoroutineScope(
        SupervisorJob() + Dispatchers.Main
    ).launch {

        try {

            val bitmap =
                withContext(Dispatchers.IO) {

                    val url =
                        URL(imageUrl)

                    val connection =
                        url.openConnection()
                                as HttpURLConnection

                    connection.requestMethod =
                        "GET"

                    connection.connectTimeout =
                        10000

                    connection.readTimeout =
                        10000

                    connection.setRequestProperty(
                        "Accept",
                        "image/*"
                    )

                    connection.connect()

                    if (
                        connection.responseCode !in
                        200..299
                    ) {

                        connection.disconnect()

                        throw Exception(
                            "Image request failed"
                        )
                    }

                    val input =
                        BufferedInputStream(
                            connection.inputStream
                        )

                    val bitmapResult =
                        BitmapFactory.decodeStream(
                            input
                        )

                    input.close()

                    connection.disconnect()

                    bitmapResult
                }

            if (bitmap != null) {

                imageView.setImageBitmap(
                    bitmap
                )
            }

        } catch (_: Exception) {

            // Image failed to load.
            // Keep the current ImageView unchanged.
        }
    }
}
