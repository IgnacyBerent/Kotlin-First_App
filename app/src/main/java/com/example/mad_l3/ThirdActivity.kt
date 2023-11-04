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

        // creates list of numbers from 0 to 49, shuffles it and takes first 6 numbers
        // this way we get 6 random numbers without repetition
        val numbers = (0..49).shuffled().take(6)

        // I have created my own custom view - CustomLottoBallView
        val ball1 = findViewById<CustomLottoBallView>(R.id.customCircleView1)
        val ball2 = findViewById<CustomLottoBallView>(R.id.customCircleView2)
        val ball3 = findViewById<CustomLottoBallView>(R.id.customCircleView3)
        val ball4 = findViewById<CustomLottoBallView>(R.id.customCircleView4)
        val ball5 = findViewById<CustomLottoBallView>(R.id.customCircleView5)
        val ball6 = findViewById<CustomLottoBallView>(R.id.customCircleView6)

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

        // sets number and random color for each ball
        for ((i, ball) in balls.withIndex()) {
            ball.setNumber(numbers[i])
            ball.setCircleColor(colors.random())
        }

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.max = 6
        val delayMillis = 1000L
        val handler = Handler(Looper.getMainLooper())

        var matches = 0
        for (number in numbers) {
            if (user_numbers!!.split(" ").contains(number.toString())) {
                matches += 1
            }
        }

        fun calculateWinningAmount(matches: Int): Int {
            return when (matches) {
                3 -> 24
                4 -> 100
                5 -> 10000
                6 -> 1000000
                else -> 0
            }
        }

        val winText = findViewById<TextView>(R.id.winText)
        val winningAmount = calculateWinningAmount(matches)
        if (winningAmount > 0) {
            winText.text = "WYGRAŁEŚ: $winningAmount!"
        } else {
            winText.text = "Spróbuj ponownie!"
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
                        // If a ball number matches a guess number,
                        // number on the ball is colored green, otherwise red
                        if (ball.getNumber() in user_numbers!!.split(" ")) {
                            ball.setTextColor(Color.GREEN)
                        } else {
                            ball.setTextColor(Color.RED)
                        }

                        if (progressStatus == 6) {
                            winText.visibility = View.VISIBLE
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