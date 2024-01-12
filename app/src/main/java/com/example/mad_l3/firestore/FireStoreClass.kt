package com.example.mad_l3.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import android.util.Log
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class FireStoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUserFS(userInfo: User){

        mFireStore.collection("users")
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener{
                Log.i("FireStoreClass", "User document created")
                // create userGames document
            }
            .addOnFailureListener{
                Log.e("FireStoreClass", "Error while registering user")
            }
    }

    fun registerNewGame(gameInfo: GameData, userId: String){

        mFireStore.collection("userGames")
            .document(userId)
            .collection("games")
            .document(gameInfo.id)
            .set(gameInfo, SetOptions.merge())
            .addOnSuccessListener{
                Log.i("FireStoreClass", "Game registered")
            }
            .addOnFailureListener{
                Log.e("FireStoreClass", "Error while registering game")
            }

    }

    fun updateGame(userId: String, gameId: String, winningAmount: Double, drawnNumbers: List<Int>){

        val updates = mapOf(
            "win" to winningAmount,
            "drawNumb" to drawnNumbers.toList()
        )
        mFireStore.collection("userGames")
            .document(userId)
            .collection("games")
            .document(gameId)
            .update(updates)
            .addOnSuccessListener{
                Log.i("FireStoreClass", "Game updated")
            }
            .addOnFailureListener{
                Log.e("FireStoreClass", "Error while updating game")
            }
    }

    fun deleteGame(userId: String, gameId: String){

        mFireStore.collection("userGames")
            .document(userId)
            .collection("games")
            .document(gameId)
            .delete()
            .addOnSuccessListener{
                Log.i("FireStoreClass", "Game deleted")
            }
            .addOnFailureListener{
                Log.e("FireStoreClass", "Error while deleting game")
            }
    }

    fun getSelectedNumbers(userId: String, gameId: String): Deferred<List<Int>?> {
        return GlobalScope.async {
            val db = FirebaseFirestore.getInstance()
            val document = db.collection("userGames").document(userId).collection("games").document(gameId)
                .get()
                .await()

            if (document != null) {
                val gameData = document.toObject<GameData>()
                val selectedNumbers = gameData?.selNumb
                Log.i("SelectedNumbers", "Selected numbers: $selectedNumbers")
                selectedNumbers
            } else {
                Log.e("SelectedNumbers", "No such document")
                null
            }
        }

    fun getGames(userId: String, callback: (List<GameData>) -> Unit){

        mFireStore.collection("userGames")
            .document(userId)
            .collection("games")
            .get()
            .addOnSuccessListener { result ->
                val gamesDataList = mutableListOf<GameData>()
                for (document in result) {
                    val gameData = document.toObject(GameData::class.java)
                    gamesDataList.add(gameData)
                }
                callback(gamesDataList)
            }
            .addOnFailureListener { exception ->
                Log.e("FireStoreClass", "Error getting documents: ", exception)
            }
    }



}