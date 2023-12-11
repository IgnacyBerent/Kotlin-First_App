package com.example.mad_l3.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.FieldValue
import android.util.Log

class FireStoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUserFS(userInfo: User){

        mFireStore.collection("users")
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener{
                Log.i("FireStoreClass", "User document created")
                // create userGames document
                val userGames = UserGames(userInfo.id)
                mFireStore.collection("usersGames")
                    .document(userInfo.id)
                    .set(userGames, SetOptions.merge())
                    .addOnSuccessListener{
                        Log.i("FireStoreClass", "UserGames document created")
                    }
                    .addOnFailureListener{
                        Log.e("FireStoreClass", "Error while adding usersGames during registering user")
                    }
            }
            .addOnFailureListener{
                Log.e("FireStoreClass", "Error while registering user")
            }
    }

    fun registerNewGame(gameInfo: GameData, userId: String){

        mFireStore.collection("games")
            .document(gameInfo.id)
            .set(gameInfo, SetOptions.merge())
            .addOnSuccessListener{
                Log.i("FireStoreClass", "Game registered")
                mFireStore.collection("usersGames")
                    .document(userId)
                    .update("gamesId", FieldValue.arrayUnion(gameInfo.id))
                    .addOnSuccessListener{
                        Log.i("FireStoreClass", "Game added to userGames")
                    }
                    .addOnFailureListener{
                        Log.e("FireStoreClass", "Error while adding game to userGames")
                    }
            }
            .addOnFailureListener{
                Log.e("FireStoreClass", "Error while registering game")
            }

    }

    fun updateGame(gameId: String, winningAmount: Double, drawnNumbers: List<Int>){

        val updates = mapOf(
            "win" to winningAmount,
            "drawNumb" to drawnNumbers.toList()
        )
        mFireStore.collection("games")
            .document(gameId)
            .update(updates)
            .addOnSuccessListener{
                Log.i("FireStoreClass", "Game updated")
            }
            .addOnFailureListener{
                Log.e("FireStoreClass", "Error while updating game")
            }

    }


}