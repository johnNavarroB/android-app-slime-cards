package com.example.slimecards

import android.content.Intent
import androidx.lifecycle.ViewModel
import java.text.DecimalFormat
/**
 * This is the view model for the score activity.
 */
class ScoreViewModel : ViewModel()
{
    /**
     * Returns the score from an intent and gives it format.
     * @param intent The intent from which the score will be returned.
     */
    fun getScore( intent : Intent ) : String
    {
        val decimalFormat = DecimalFormat( "###,###" )
        val score = Integer.valueOf( intent.getStringExtra( "score" ).toString() )
        return "Score: " + decimalFormat.format( score ).replace( ',', '.' )
    }
    /**
     * Returns the difficulty from an intent.
     * @param intent The intent from which the difficulty will be returned.
     */
    fun getDifficulty( intent : Intent ) : String
    {
        return "Difficulty: " + intent.getStringExtra( "difficulty" ).toString()
    }
    /**
     * Formats milliseconds to get seconds and minutes, then returns it formatted.
     * @param time The milliseconds from which the formatted time will be returned.
     */
    private fun millisecondsToString( time : String ) : String
    {
        var seconds : Int = ( Integer.valueOf( time ) + 1200 ) / 1000
        val minutes = seconds / 60
        seconds -= ( minutes * 60 )
        return String.format( "%02d:%02d", minutes, seconds )
    }
    /**
     * Returns the time from an intent.
     * @param intent The intent from which the time will be returned.
     */
    fun getTime( intent : Intent ) : String
    {
        val timeString = intent.getStringExtra( "time" ).toString()
        return "Time: " + millisecondsToString( timeString )
    }
}