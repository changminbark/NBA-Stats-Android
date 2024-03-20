package com.example.nbastatsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.nbastatsapp.databinding.ActivityMainBinding
import okhttp3.Headers

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var playerName = ""
    var fieldPercent = ""
    var playerPoints = ""
    var numGames = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener { retrievePlayerStats() }
    }

    private fun retrievePlayerStats() {
        playerName = binding.inputText.text.toString().replace(" ", "%20")
        Log.d("player name", playerName)
        Log.d("URL", "https://nba-stats-db.herokuapp.com/api/playerdata/name/$playerName")

        val client = AsyncHttpClient()
        client["https://nba-stats-db.herokuapp.com/api/playerdata/name/$playerName", object : JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Headers,
                json: JsonHttpResponseHandler.JSON
            ) {
                Log.d("Stats", "response successful$json")
                fieldPercent = json.jsonObject.getJSONArray("results").getJSONObject(0).getString("field_percent")
                playerPoints = json.jsonObject.getJSONArray("results").getJSONObject(0).getString("PTS")
                numGames = json.jsonObject.getJSONArray("results").getJSONObject(0).getString("games")
                Log.d("Stats", fieldPercent)

                updateText(fieldPercent, playerPoints, numGames)
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Stats", errorResponse)
            }
        }]
    }

    private fun updateText(fP: String, pP: String, nG: String) {
        binding.fieldPercentText.text = fP
        binding.playerPointsText.text = pP
        binding.numGamesText.text = nG
    }
}
