package com.example.mad_l3.custom_elements

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_l3.R
import com.example.mad_l3.firestore.GameData
import com.example.mad_l3.firestore.FireStoreClass
import com.example.mad_l3.fragments.ui.GameInfoFragment

class GameAdapter(private var uid: String , private var gamesDataList: List<GameData>) :
    RecyclerView.Adapter<GameAdapter.MyGameHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyGameHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.game_item_2, parent, false)
        return MyGameHolder(itemView, gamesDataList)
    }

    override fun getItemCount(): Int {
        return gamesDataList.size
    }

    override fun onBindViewHolder(holder: MyGameHolder, position: Int) {

        val currentGame = gamesDataList[position]
        holder.gameDate.text = "Game Date: ${currentGame.date}"

        holder.deleteButton.setOnClickListener {
            FireStoreClass().deleteGame(uid ,currentGame.id)
        }
    }

    class MyGameHolder(itemView: View, private val gamesDataList: List<GameData>) : RecyclerView.ViewHolder(itemView) {

        val gameDate = itemView.findViewById<TextView>(R.id.dateTextView)
        val deleteButton = itemView.findViewById<TextView>(R.id.deleteButton)
        init {
            itemView.setOnClickListener { view: View ->
                val position: Int = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val gameData = gamesDataList[position]
                    val gameInfoFragment = GameInfoFragment()
                    val bundle = Bundle()
                    bundle.putString("gameDate", gameData.date)
                    bundle.putString("gameSelNumbers", gameData.selNumb.toString())
                    bundle.putString("gameDrawnNumbers", gameData.drawNumb.toString())
                    bundle.putString("gameWin", gameData.win.toString())
                    gameInfoFragment.arguments = bundle

                    val activity: AppCompatActivity = view.context as AppCompatActivity
                    activity.supportFragmentManager.beginTransaction().replace(R.id.statisticView, gameInfoFragment).commit()
                }
            }
        }
    }

}