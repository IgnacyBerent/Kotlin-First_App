package com.example.mad_l3.custom_elements

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_l3.R
import com.example.mad_l3.firestore.GameData

class GameAdapter(private var gamesDataList: List<GameData>) :
    RecyclerView.Adapter<GameAdapter.MyGameHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyGameHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.game_item, parent, false)
        return MyGameHolder(itemView)
    }

    override fun getItemCount(): Int {
        return gamesDataList.size
    }

    override fun onBindViewHolder(holder: MyGameHolder, position: Int) {

        val currentGame = gamesDataList[position]
        holder.gameDate.text = "Game Date: ${currentGame.date}"
        holder.selNumbers.text = "Selected Numbers: " +
                "${currentGame.selNumb
                    .toString()
                    .replace("[", "")
                    .replace("]", "")}"
        holder.drawnNumbersTextView.text = "Drawn Numbers: " +
                "${currentGame.drawNumb
                    .toString()
                    .replace("[", "")
                    .replace("]", "")}"
        holder.winTextView.text = "Your win: ${currentGame.win}"
    }

    class MyGameHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val gameDate = itemView.findViewById<TextView>(R.id.dateTextView)
        val selNumbers = itemView.findViewById<TextView>(R.id.selNumbersTextView)
        val drawnNumbersTextView = itemView.findViewById<TextView>(R.id.drawnNumbersTextView)
        val winTextView = itemView.findViewById<TextView>(R.id.winTextView)


    }

}