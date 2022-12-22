package com.example.slimecards

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
/**
 * Activity that shows to the user the total time, score and difficulty of the game.
 */
class ScoreActivity : AppCompatActivity()
{
    private lateinit var viewModel : ScoreViewModel

    private lateinit var menuButton : Button
    private lateinit var replayButton : Button

    private lateinit var time : TextView
    private lateinit var difficulty : TextView
    private lateinit var score : TextView
    /**
     * Initializes the score text view and updates its content.
     */
    private fun initializeScore()
    {
        score = findViewById( R.id.finalScore )
        score.text = viewModel.getScore( intent )
    }
    /**
     * Initializes the difficulty text view and updates its content.
     */
    private fun initializeDifficulty()
    {
        difficulty = findViewById( R.id.scoreDifficulty )
        difficulty.text = viewModel.getDifficulty( intent )
    }
    /**
     * Initializes the time text view and updates its content.
     */
    private fun initializeTime()
    {
        time = findViewById( R.id.scoreTime )
        time.text = viewModel.getTime( intent )
    }
    /**
     * Initializes the intents of each game mode, the replay button and a
     * click listener that allows the user to replay the last game mode played.
     */
    private fun initializeReplayButton()
    {
        val difficulty = intent.getStringExtra( "difficulty" ).toString()
        val easy = Intent( this, EasyGameActivity::class.java )
        val hard = Intent( this, HardGameActivity::class.java )
        replayButton = findViewById( R.id.replayButton )
        replayButton.setOnClickListener {
            if ( difficulty == "easy" ) startActivity( easy )
            else startActivity( hard )
        }
    }
    /**
     * Initializes the menu button and sets a click listener that allows the user to return to the main menu.
     */
    private fun initializeMenuButton()
    {
        menuButton = findViewById( R.id.returnToMenuButton )
        menuButton.setOnClickListener {
            val menu = Intent( this, MenuActivity::class.java )
            startActivity( menu )
        }
    }
    /**
     * Initializes the view model.
     */
    private fun initializeViewModel()
    {
        viewModel = ViewModelProvider( this ).get( ScoreViewModel::class.java )
    }
    /**
     * Initializes the view model, menu button, replay button, time, difficulty and score.
     */
    override fun onCreate( savedInstanceState : Bundle? )
    {
        super.onCreate( savedInstanceState )
        setContentView( R.layout.activity_score )

        initializeViewModel()

        initializeMenuButton()

        initializeReplayButton()

        initializeTime()

        initializeDifficulty()

        initializeScore()
    }
}