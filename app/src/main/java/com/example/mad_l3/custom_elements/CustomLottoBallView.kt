package com.example.mad_l3.custom_elements

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.example.mad_l3.R

class CustomLottoBallView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {
    private val circleImageView: ImageView
    private val numberTextView: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.custom_lotto_ball_view, this)
        circleImageView = findViewById(R.id.circleImageView)
        numberTextView = findViewById(R.id.numberTextView)
    }

    fun setNumber(number: Int) {
        numberTextView.text = number.toString()
    }

    fun setCircleColor(color: Int) {
        circleImageView.setColorFilter(color)
    }

    fun getNumber(): String {
        return numberTextView.text.toString()
    }

    fun setTextColor(color: Int) {
        numberTextView.setTextColor(color)
    }

}
