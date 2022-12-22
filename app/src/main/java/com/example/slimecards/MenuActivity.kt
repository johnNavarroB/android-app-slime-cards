package com.example.slimecards

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
/**
 * Activity that allows the user to choose a game mode, show a help window and launch a game.
 */
class MenuActivity : AppCompatActivity()
{
    private lateinit var spinner : Spinner
    private lateinit var playButton : Button
    private lateinit var helpButton : Button
    private lateinit var helpOkButton : Button
    /**
     * Initializes the help button and sets a click listener that shows a help window.
     */
    private fun loadHelpButton()
    {
        helpButton = findViewById( R.id.helpButton )
        helpButton.setOnClickListener {
            val helpView = LayoutInflater.from( this ).inflate( R.layout.activity_help, null )
            helpOkButton = helpView.findViewById( R.id.helpOkButton )
            val alertDialogBuilder = AlertDialog.Builder( this ).setView( helpView )
            val alertDialog = alertDialogBuilder.show()
            helpOkButton.setOnClickListener {
                alertDialog.dismiss()
            }
        }
    }
    /**
     * Initializes an spinner from an array of String and a style.
     */
    private fun loadSpinner()
    {
        spinner = findViewById( R.id.difficultySpinner )
        spinner.adapter = ArrayAdapter.createFromResource( this, R.array.difficulty, R.layout.style_spinner )
    }
    /**
     * Initializes the intents of each game mode, the spinner, the play button and
     * sets a click listener that launches the selected game when the user presses the play button.
     */
    private fun loadPlayButton()
    {
        val easyGame = Intent( this, EasyGameActivity::class.java )
        val mediumGame = Intent( this, MediumGameActivity::class.java )
        val hardGame = Intent( this, HardGameActivity::class.java )
        playButton = findViewById( R.id.playButton )
        loadSpinner()
        playButton.setOnClickListener {
            when ( spinner.selectedItemPosition )
            {
                0 -> startActivity( easyGame )
                1 -> startActivity( mediumGame )
                else -> startActivity( hardGame )
            }
        }
    }
    /**
     * Loads the play button, the spinner and the help button.
     */
    override fun onCreate( savedInstanceState : Bundle? )
    {
        super.onCreate( savedInstanceState )
        setContentView( R.layout.activity_menu )

        loadPlayButton()

        loadHelpButton()
    }
}