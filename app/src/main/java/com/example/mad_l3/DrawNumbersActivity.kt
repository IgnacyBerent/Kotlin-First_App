package com.example.mad_l3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Button
import android.os.Handler
import android.graphics.Color
import android.view.View
import android.os.Looper
import com.example.mad_l3.custom_elements.CustomLottoBallView
import com.example.mad_l3.firestore.FireStoreClass
import com.example.mad_l3.project_functions.SnackbarHelper.showErrorSnackBar
import com.example.mad_l3.firestore.GameData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import kotlin.random.Random

class DrawNumbersActivity : AppCompatActivity() {

    private val colors = listOf(
        Color.BLUE,
        Color.CYAN,
        Color.parseColor("#7d2307"), //brown
        Color.GRAY,
        Color.LTGRAY,
        Color.MAGENTA,
        Color.YELLOW,
        Color.parseColor("#2bff9f"), //sea-green
        Color.RED,
        Color.GREEN,
    )
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var rootView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootView = findViewById(android.R.id.content)
        setContentView(R.layout.activity_third)

        val intent = intent
        val gameId = intent.getStringExtra("newGameId")

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val getNumbersButton = findViewById<Button>(R.id.getnumbers_button)
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

        var userNumbers: IntArray? = IntArray(6)

        db.collection("games")
            .document(gameId!!)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val dbData = documentSnapshot.toObject(GameData::class.java)
                    userNumbers = dbData?.selNumb?.toIntArray()
                    Log.i("DrawNumbersActivity", "Game data retrieved successfully")
                } else {
                    Log.e("DrawNumbersActivity", "No such document")
                }
            }
            .addOnFailureListener {
                Log.e("DrawNumbersActivity", "Error while getting game data")
            }

        var isTryAgain = false

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.max = 6
        val delayMillis = 1000L
        val handler = Handler(Looper.getMainLooper())

        getNumbersButton.setOnClickListener {

            // make all balls invisible
            for (ball in balls) {
                ball.visibility = View.INVISIBLE
            }

            getNumbersButton.isEnabled = false
            // creates list of numbers from 0 to 49, shuffles it and takes first 6 numbers
            // this way we get 6 random numbers without repetition
            val drawnNumbers = (0..49).shuffled().take(6)
            var matches = 0
            for (number in drawnNumbers) {
                if (userNumbers!!.contains(number)) {
                    matches += 1
                }
            }

            val winners = simPlayers(doubleArrayOf(7.2e-8,1.8e-5,0.00097,0.077))
            val winningAmount: Double = calculateWin(
                Random.nextDouble(0.0, 50e6),
                winners,
                matches
            )
            val winText: String
            val winColor: String
            if (winningAmount > 0) {
                winText = "You won: \n" +
                        " $winningAmount!"
                winColor = "green"
            } else {
                winText = "You lose! Try Again!"
                winColor = "red"
            }

            // sets number and random color for each ball
            for ((i, ball) in balls.withIndex()) {
                ball.setNumber(drawnNumbers[i])
                ball.setCircleColor(colors.random())
            }

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
                        if (ball.getNumber().toInt() in userNumbers!!) {
                            ball.setTextColor(Color.GREEN)
                        } else {
                            ball.setTextColor(Color.RED)
                        }

                        if (progressStatus == 6) {
                            showErrorSnackBar(rootView, winText, winColor)
                            getNumbersButton.text = "TRY AGAIN!"
                            getNumbersButton.isEnabled = true
                            if (!isTryAgain) {
                                isTryAgain = true
                                FireStoreClass().updateGame(gameId, winningAmount, drawnNumbers)
                            } else {
                                val newGame = GameData(
                                    selNumb = userNumbers?.toList(),
                                    drawNumb =  drawnNumbers,
                                    win = winningAmount
                                )
                                FireStoreClass().registerNewGame(
                                    newGame,
                                    auth.currentUser?.uid.toString()
                                )
                            }
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

    //simPlayers function simulate others players winnings based on probability array
    //which first element correspond to probability of win the lotto game, second element
    // correspond to probability of correctly selected 5 numbers and so on. By default theoretical\
    // number of players is set to 38 millions - but not everybody have to play.
    private fun simPlayers(probabilityArray: DoubleArray, numberOfPopulation: Int=38000000): IntArray {
        val numberOfPlayers = Random.nextInt(numberOfPopulation)
        val numberOfWins = IntArray(4)
        for ((iterator, probability) in probabilityArray.withIndex()) {
            var numberOfWinningPlayers = 0

            repeat(numberOfPlayers) {
                val randomValue = Random.nextDouble(0.0, 1.0)
                if (randomValue < probability) {
                    numberOfWinningPlayers++
                }
            }
            numberOfWins[iterator] = numberOfWinningPlayers
            numberOfWinningPlayers = 0
        }
        return numberOfWins
    }

    // calculateWin function calculate players win based on accumulation, number of others
    // winners and player's score.
    private fun calculateWin(cumulate: Double=0.0,
                             winners: IntArray
                             , score: Int = 0): Double {

        return when (score) {
            6 -> (cumulate * 0.44) / (winners[0] + 1)
            5 -> (cumulate * 0.08) / winners[1] + 1
            4 -> (cumulate * 0.48) / winners[2] + 1
            3 -> (cumulate * 0.48) / winners[3] + 1
            else -> 0.0
        }
    }

}