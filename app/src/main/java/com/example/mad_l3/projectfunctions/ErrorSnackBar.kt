package com.example.mad_l3.projectfunctions

import android.graphics.Color
import com.google.android.material.snackbar.Snackbar
import android.view.View
import java.util.Locale

object SnackbarHelper {
    private val colorMap = mapOf(
        "red" to "#FF0000",
        "green" to "#00FF00",
        "blue" to "#0000FF"
        // Add more colors as needed
    )

    fun showErrorSnackBar(view: View, message: String, colorName: String = "red") {
        val colorHex = colorMap[colorName.lowercase()]
        if (colorHex != null) {
            val upperColor = colorHex.uppercase(Locale.getDefault())
            Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setBackgroundTint(Color.parseColor(upperColor))
                .setTextColor(Color.WHITE)
                .show()
        } else {
            error("Unknown color name: $colorName")
        }
    }
}
