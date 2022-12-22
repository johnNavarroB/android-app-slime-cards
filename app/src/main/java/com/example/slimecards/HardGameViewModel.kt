package com.example.slimecards

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
/**
 * This is the view model for the game activity in hard mode.
 */
class HardGameViewModel : ViewModel()
{
    private val cards = mutableListOf<Card>()
    private var viewMatrix = mutableListOf< Array<Any> >()

    private var running = true
    private var pauseOffSet : Long = 0
    private var base = 0.toLong()

    private var clickable : Boolean = true

    private var isPair = false
    private var pairCount : Int = 0

    private var score = 0.toLong()
    /**
     * Gets the current score.
     */
    fun getScore() : Long
    {
        return score
    }
    /**
     * Adds the score depending on the current time.
     * @param time The period of time passed since the beginning of the game until the card is clicked in milliseconds.
     */
    fun addScore( time : Long )
    {
        val seconds : Long = ( time + 1200 ) / 1000
        if ( seconds < 80 )
        {
            val difference = 80 - seconds
            score += difference * 100
        }
    }
    /**
     * Gets how many pairs have been revealed.
     */
    fun getPairValue() : Int
    {
        return pairCount
    }
    /**
     * Sets how many pairs have been revealed.
     * @param pairValue How many pairs have been revealed.
     */
    fun setPairValue( pairValue : Int )
    {
        this.pairCount = pairValue
    }
    /**
     * Increases how many pairs have been revealed by one.
     */
    fun increasePairCount()
    {
        pairCount ++
    }
    /**
     * Sets if the last pair of revealed cards matches or not.
     * @param state True if matches, false otherwise.
     */
    fun setIsPairState( state : Boolean )
    {
        isPair = state
    }
    /**
     * Modifies the view matrix.
     * @param viewMatrix The new view matrix.
     */
    fun setViewMatrix( viewMatrix : MutableList< Array<Any> > )
    {
        this.viewMatrix = viewMatrix
    }
    /**
     * Gets the view matrix.
     */
    fun getViewMatrix() : MutableList< Array<Any> >
    {
        return viewMatrix
    }
    /**
     * Modifies if the user can click the cards or not.
     * @param state True if the user can click the cards, false otherwise.
     */
    fun setClickableState( state : Boolean )
    {
        clickable = state
    }
    /**
     * Enables that the user can click the cards in a specific period of time.
     * @param milliseconds The period of time before the user can click the cards in milliseconds.
     */
    private fun enableClickIn( milliseconds : Long )
    {
        Handler( Looper.getMainLooper() ).postDelayed( { setClickableState( true ) }, milliseconds )
    }
    /**
     * If it's pair or there are less than two revealed cards, the user can click the cards in five hundred and fifty milliseconds.
     * But if there are two revealed cards and are not pair, the user can click the cards in one thousand eight hundred milliseconds,
     * so the cards have time to be animated and be turned down.
     */
    fun enableClick()
    {
        if ( getViewMatrix().size < 3 || isPair ) enableClickIn( 550 )
        else if ( getViewMatrix().size == 3 && !isPair ) enableClickIn( 2200 )
    }
    /**
     * Returns true if the game is not paused, the cards are clickable and
     * the card which its id matches with the one given through parameter it's turned down.
     * @param id The id from the card which must be turned down.
     */
    fun ableToTurnUp( id : Int ) : Boolean
    {
        return running() && clickable && !getCards()[id].turnedUp
    }
    /**
     * Returns true if the game is not paused, false otherwise.
     */
    fun running() : Boolean
    {
        return running
    }
    /**
     * Sets if the game is paused or not.
     * @param state True if the game is not paused, false otherwise.
     */
    fun setRunningStatus( state : Boolean )
    {
        this.running = state
    }
    /**
     * Updates the base, for the chronometer.
     * @param base The new base.
     */
    fun setBase( base : Long )
    {
        this.base = base
    }
    /**
     * Returns the current base, for the chronometer.
     */
    fun getBase() : Long
    {
        return base
    }
    /**
     * Gets the pause offset.
     */
    fun getPauseOffSet() : Long
    {
        return pauseOffSet
    }
    /**
     * Modifies the pause offset.
     * @param pauseOffSet The new pause offset.
     */
    fun setPauseOffSet( pauseOffSet : Long )
    {
        this.pauseOffSet = pauseOffSet
    }
    /**
     * Gets the cards.
     */
    fun getCards() : MutableList<Card>
    {
        return cards
    }
    /**
     * Initializes the cards, previously mixing each image resource identifier.
     */
    private fun initializeRandomCards()
    {
        val cardImages = arrayOf( R.drawable.card_fire, R.drawable.card_fire, R.drawable.card_fire, R.drawable.card_ice,
            R.drawable.card_ice, R.drawable.card_ice, R.drawable.card_feline, R.drawable.card_feline, R.drawable.card_feline )
        cardImages.shuffle()
        for ( i in 0 .. 8 ) getCards().add( Card(i, cardImages[i]) )
    }
    /**
     * Initializes the view model and the cards.
     */
    init
    {
        initializeRandomCards()
    }
}