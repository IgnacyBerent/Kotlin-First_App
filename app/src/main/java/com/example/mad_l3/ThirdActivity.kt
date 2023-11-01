package com.example.mad_l3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.ProgressBar
import android.widget.Button
import android.os.Handler
import android.graphics.Color
import androidx.core.view.isVisible
import android.view.View
import android.os.Looper
import android.widget.Toast
import android.graphics.PorterDuff


import android.content.res.ColorStateList


class ThirdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        val intent = intent
        val user_numbers = intent.getStringExtra("NUMBERS")
        val getnumbers_button = findViewById<Button>(R.id.getnumbers_button)


        val numbers = (0..49).shuffled().take(6)

        val ball1 = findViewById<CustomLottoBallView>(R.id.customCircleView1)
        ball1.setNumber(numbers[0])
        val ball2 = findViewById<CustomLottoBallView>(R.id.customCircleView2)
        ball2.setNumber(numbers[1])
        val ball3 = findViewById<CustomLottoBallView>(R.id.customCircleView3)
        ball3.setNumber(numbers[2])
        val ball4 = findViewById<CustomLottoBallView>(R.id.customCircleView4)
        ball4.setNumber(numbers[3])
        val ball5 = findViewById<CustomLottoBallView>(R.id.customCircleView5)
        ball5.setNumber(numbers[4])
        val ball6 = findViewById<CustomLottoBallView>(R.id.customCircleView6)
        ball6.setNumber(numbers[5])

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.max = 6
        val delayMillis = 1000L
        val handler = Handler(Looper.getMainLooper())

        val balls = listOf(
            ball1,
            ball2,
            ball3,
            ball4,
            ball5,
            ball6,
        )

        val colors = listOf(
            Color.BLUE,
            Color.CYAN,
            Color.parseColor("#7d2307"), //brown
            Color.GRAY,
            Color.LTGRAY,
            Color.MAGENTA,
            Color.YELLOW,
            Color.parseColor("#2bff9f"), //seagreen
            Color.RED,
            Color.GREEN,
        )

        for (ball in balls) {
            //ustaw backgroundTint jako losowy kolor z listy colors
            ball.setCircleColor(colors.random())
        }

        getnumbers_button.setOnClickListener {
            getnumbers_button.isEnabled = false

            Thread {
                var progressStatus = 0
                while (progressStatus < 6) {
                    progressStatus += 1
                    handler.post {
                        progressBar.progress = progressStatus
                        val id = resources.getIdentifier(
                            "customCircleView$progressStatus",
                            "id",
                            packageName
                        )
                        val ball = findViewById<CustomLottoBallView>(id)
                        ball.visibility = View.VISIBLE
                        if (ball.getNumber() in user_numbers!!.split(" ")) {
                            ball.setTextColor(Color.GREEN)
                        } else {
                            ball.setTextColor(Color.RED)
                        }
                    }
                    try {
                        Thread.sleep(delayMillis)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }.start()
        }


    }
}