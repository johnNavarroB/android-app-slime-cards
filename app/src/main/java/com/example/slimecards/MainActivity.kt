package com.example.slimecards

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.*
import kotlin.concurrent.schedule
/**
 * Activity that shows a splash screen for two seconds.
 */
class MainActivity : AppCompatActivity()
{
    private var delay : Long = 2000
    /**
     * Loads the main menu after a specific amount of time.
     */
    private fun loadMainMenu()
    {
        val intent = Intent(this@MainActivity, MenuActivity::class.java)
        Timer("intent", false).schedule(delay) { startActivity(intent) }
    }
    /**
     * Starts the main menu activity after two seconds.
     */
    override fun onCreate( savedInstanceState : Bundle? )
    {
        super.onCreate( savedInstanceState )
        setContentView( R.layout.activity_main )
        loadMainMenu()
    }
}